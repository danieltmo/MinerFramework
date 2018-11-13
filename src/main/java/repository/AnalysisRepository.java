/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.io.File;
import org.repodriller.scm.GitRemoteRepository;
import org.repodriller.scm.GitRepository;
import org.repodriller.scm.SCM;
import org.repodriller.scm.SCMRepository;

/**
 *
 * @author Daniel
 */
public class AnalysisRepository extends SCMRepository {

    private String url;
    private Source source;
    private String projectName;

    public static AnalysisRepository buildRepository(String url, Source source, String projectName) {
        String projectPath = url;

        if (source.equals(Source.GIT)) {
            projectPath = "./temp/" + projectName;
            if (!checkIfProjectAlreadyDownloaded(projectPath)) {
                GitRemoteRepository.hostedOn(url).inTempDir(projectPath).buildAsSCMRepository();
            }
        }
        SCMRepository scmr = GitRepository.singleProject(projectPath);
        return new AnalysisRepository(url, source, projectName, scmr.getScm(),
                scmr.getOrigin(), scmr.getPath(), scmr.getHeadCommit(), scmr.getFirstCommit());
    }

    public AnalysisRepository(String url, Source source, String projectName,
            SCM scm, String origin, String path, String headCommit, String firstCommit) {
        super(scm, origin, path, headCommit, firstCommit);
        this.url = url;
        this.source = source;
        this.projectName = projectName;
    }

    private static boolean checkIfProjectAlreadyDownloaded(String path) {
        File checkPath = new File(path);
        return checkPath.isDirectory();
    }

    public String getUrl() {
        return url;
    }

    public Source getSource() {
        return source;
    }

    public String getProjectName() {
        return projectName;
    }

}
