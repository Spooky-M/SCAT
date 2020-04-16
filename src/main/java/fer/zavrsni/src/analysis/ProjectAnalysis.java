package fer.zavrsni.src.analysis;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ProjectAnalysis {

    private Project project;
    private List<String> tools;

    public ProjectAnalysis(@NotNull Project project, List<String> tools) {
        this.project = project;
        this.tools = tools;
    }

    public void executeAnalysis() throws IOException {
        StringBuilder toolsArg = new StringBuilder();
        tools.forEach(toolsArg::append);

        ProjectRootManager projectManager = ProjectRootManager.getInstance(project);
        projectManager.getContentRootsFromAllModules();
        Sdk projectSdk = projectManager.getProjectSdk();
        if(Objects.isNull(projectSdk)){
         //TODO ispiši grešku
        }

        String classpath = projectSdk.getHomePath();
        String pack = "";
        String root = project.getBasePath();
        String script = "";

        String arg = "python3 " + script + " -t " + toolsArg.toString() + " -c " + classpath + " -p " + pack + " " + root;
        String[] args = new String[] {"/bin/bash", "-c", arg};
        Process proc = new ProcessBuilder(args).start();
        if(proc.exitValue() != 0) {
            //TODO ispiši grešku
        }

    }

}
