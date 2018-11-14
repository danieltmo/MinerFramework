package agent;

import framework.CollectorFramework;
import framework.CollectorFrameworkException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Copyright 2018 Daniel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 * @author Daniel
 */
public class MainAgent extends Agent {

    private AID[] sortAgents;
    private CollectorFramework framework;
   

    @Override
    protected void setup() {
        System.out.println("Main-agent UP");
        addBehaviour(new TickerBehaviour(this, 2000) {
            @Override
            protected void onTick() {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("sort-agent");
                template.addServices(sd);

                framework = (CollectorFramework) getArguments()[0];

                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    sortAgents = new AID[result.length];
                    if (sortAgents.length == 0) {
                        System.out.println("None sort-agent found, execution will follow the inserted order");
                        framework.run();
                        myAgent.doDelete();
                    } else {
                        System.out.println("Found the following sort agents:");
                        for (int i = 0; i < result.length; ++i) {
                            sortAgents[i] = result[i].getName();
                            System.out.println(sortAgents[i].getName());
                        }

                        myAgent.addBehaviour(new RequestPerformer());
                    }
                } catch (Exception fe) {
                    fe.printStackTrace();
                }
            }
        });
    }

    private class RequestPerformer extends Behaviour {

        private MessageTemplate mt; // The template to receive replies
        private int step = 0;
        private boolean done;

        @Override
        public void action() {
            switch (step) {
                case 0:
                    // Send the msg to all agents
                    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                    for (int i = 0; i < sortAgents.length; ++i) {
                        cfp.addReceiver(sortAgents[i]);
                    }

                    cfp.setContent(framework.parse.toJson(framework.getTools()));
                    cfp.setConversationId("sort-tool");
                    cfp.setReplyWith("cfp" + System.currentTimeMillis());
                    System.out.println("MainAgent: Asking for sort the tools");
                    System.out.println(cfp.toString());// Unique value
                    myAgent.send(cfp);
                    // Prepare the template to get proposals
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("sort-tool"),
                            MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                    step = 1;
                    break;
                case 1:
                    // Receive all proposals/refusals from sort agents
                    ACLMessage reply = myAgent.receive(mt);
                    if(reply == null){
                        block();
                        return;
                    }
                    if ( reply.getPerformative() == ACLMessage.REFUSE) {
                        System.out.println("Unable to process the tools in that order");
                        done = true;
                        myAgent.doSuspend();

                    } else {
                        // Reply received
                        if (reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                            System.out.println("Able to process the tools");
                            String jsonList = reply.getContent();
                            try {
                                framework.run();
                            } catch (CollectorFrameworkException ex) {
                                Logger.getLogger(MainAgent.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            done = true;
                            myAgent.doSuspend();
                        }
                    }
                    break;

            }
        }

        @Override
        public boolean done() {
            return done;
        }

    }

}
