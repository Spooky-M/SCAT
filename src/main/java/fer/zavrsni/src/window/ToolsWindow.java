package fer.zavrsni.src.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;


public class ToolsWindow {
    private JPanel toolsWindowContent;
    private JCheckBox toolsCheckBox;
    private JLabel name;
    private Map<String, Integer> tools = new HashMap<>();

    private static String toolsNames = "spotbugs_3.1.0_RC7, spotbugs_3.1.12, spotbugs_4.0.0_beta1, " +
            "spotbugs_4.0.0_beta2, spotbugs_4.0.0_beta3, spotbugs_4.0.0_beta4, pmd, cpd, graudit, checkstyle";


    public ToolsWindow(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        int i = 0;
        for(String tool:toolsNames.split(",")) {
            tools.put(tool.trim(), i++);
        }
        createComponents(toolWindow);
    }

    private void createComponents(ToolWindow toolWindow) {
        toolsWindowContent = new JPanel();
        name = new JLabel("SCAT");

        toolsCheckBox = new JCheckBox();
        for(String tool : tools.keySet()) {
            toolsCheckBox.add(new JLabel(tool));
            toolsCheckBox.setMnemonic(tools.get(tool));
            toolsCheckBox.setSelected(true);
        }

        toolsWindowContent.add(name);
        toolsWindowContent.add(toolsCheckBox);
        //BoxLayout neki error
        toolsWindowContent.setLayout(new BoxLayout(toolsWindowContent, BoxLayout.Y_AXIS));
        toolsWindowContent.setVisible(true);
    }

    public JPanel getContent() {
        return toolsWindowContent;
    }



}