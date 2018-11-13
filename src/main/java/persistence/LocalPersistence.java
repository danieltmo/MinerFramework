/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import repository.AnalysisRepository;

/**
 *
 * @author Daniel
 */
public class LocalPersistence implements PersistenceMechanism {

    private AnalysisRepository repository;
    private String relativePath;
    private String commitPath;

    public LocalPersistence(AnalysisRepository repository) {
        this.repository = repository;
        createProjectFolder();
    }

    private void createProjectFolder() {
        relativePath = "./projects/" + repository.getProjectName();
        File projectPath = new File(relativePath);
        projectPath.mkdirs();
    }

    private void createCommitFolder(String commit) {
        commitPath = relativePath + "/" + commit;
        File projectPath = new File(relativePath + "/" + commit);
        projectPath.mkdir();
    }

    @Override
    public void write(Object... os) {
        Commit commit = (Commit) os[0];

        createCommitFolder(commit.getHash());

        //write code
        File f = new File(commitPath + "/code");
        f.mkdir();
        commit.getModifications().forEach((m) -> {
            String fileName = m.getFileName().substring(m.getFileName().lastIndexOf("/"));
            write(f.getAbsolutePath(),fileName,m.getSourceCode());
        });
    }

    private void write(String path, String fileName, String content) {
        File f = new File(path + "/" + fileName);
        try {
            FileWriter writer = new FileWriter(f);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(LocalPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() {

    }

}
