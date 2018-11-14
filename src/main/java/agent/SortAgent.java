package agent;

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
import com.google.gson.Gson;
import framework.CollectorFramework;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import tool.PersistenceTool;
import util.Parse;

/**
 *
 * @author Daniel
 */
public class SortAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println("Sort-Agent UP");
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("sort-agent");
        sd.setName("sort-agent");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Add the behaviour serving queries from main agents
        addBehaviour(new RequestsServer());

    }

    // Put agent clean-up operations here
    @Override
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    private class RequestsServer extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                System.out.println("SortAgent: Received the mesage");
                System.out.println(msg.toString());
                // CFP Message received. Process it
                String toolsString = msg.getContent();
                boolean possibleSequence = processList(toolsString);

                ACLMessage reply = msg.createReply();
                if (possibleSequence) {
                    //Possible sequence
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setContent("avaliable");
                } else {
                    // Impossible sequence
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }

        private boolean processList(String toolsString) {
            try {   
                
                ArrayList<PersistenceTool> tools = ((CollectorFramework)myAgent.getArguments()[0]).getTools();
                ArrayList<String> adquiredList = new ArrayList<>();

                for (PersistenceTool pe : tools) {
                    for (String needed : pe.getTool().requiredProperties()) {
                        if (!adquiredList.contains(needed)) {
                            return false;
                        }
                    }
                    adquiredList.addAll(pe.getTool().offeredProperties());
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return false;
            }
        }
    }

}
