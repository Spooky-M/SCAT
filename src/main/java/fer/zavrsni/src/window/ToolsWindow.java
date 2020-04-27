package fer.zavrsni.src.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import fer.zavrsni.src.analysis.ProjectAnalysis;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ToolsWindow {
    private JPanel toolsWindowContent;
    private JLabel header;
    private JTextField packageChooser;
    private JTextField scriptField;
    private JButton analyseButton;
    private List<JCheckBox> boxes;

    private Map<String, Integer> tools = new HashMap<>();

    private static final int GRID_ROWS = 20;
    private static final int GRID_COLUMNS = 1;
    private static final int GRID_H_GAP = 30;
    private static final int GRID_V_GAP = 10;

    private static final String[] TOOLS_NAMES = {"spotbugs_3.1.0_RC7", "spotbugs_3.1.12", "spotbugs_4.0.0_beta1",
        "spotbugs_4.0.0_beta2", "spotbugs_4.0.0_beta3", "spotbugs_4.0.0_beta4", "pmd", "cpd", "graudit", "checkstyle"};
    private static final String SHORT_README = "Select tools, enter which package you want to analyse, " +
            "and click \"analyse\" button to start.";

    public ToolsWindow(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        int i = 0;
        for(String tool: TOOLS_NAMES) {
            tools.put(tool.trim(), i++);
        }
        createComponents(project, toolWindow);
    }

    private void createComponents(Project project, ToolWindow toolWindow) {
        JComponent component = toolWindow.getComponent();
        toolsWindowContent = new JPanel();
        toolsWindowContent.setSize(component.getWidth(), component.getHeight());

        header = new JLabel("<HTML><strong>" + SHORT_README + "</strong></HTML>", JLabel.CENTER);
        header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toolsWindowContent.add(header);

        boxes = new ArrayList<>();
        for(String tool : tools.keySet()) {
            JCheckBox toolCheckBox = new JCheckBox(tool, false);
            boxes.add(toolCheckBox);
            toolCheckBox.setMnemonic(tools.get(tool));
            toolsWindowContent.add(toolCheckBox);
        }

        packageChooser = new JTextField("jakhar.aseem.diva");
//        packageChooser = new JTextField("Enter package which you want to analyse (eg. \"com.intellij.openapi\")");
        toolsWindowContent.add(packageChooser);

        analyseButton = new JButton("Analyse");
        analyseButton.addActionListener(e -> {
            analyseButton.setEnabled(false);
            List<String> selectedBoxes = new ArrayList<>();
            for(JCheckBox checkbox : boxes) {
                if(checkbox.isSelected()) {
                    selectedBoxes.add(checkbox.getText());
                }
            }

            ProjectAnalysis pa;
            try {
                pa = new ProjectAnalysis(project, selectedBoxes, packageChooser.getText(),
                        packageChooser);
                pa.executeAnalysis();
            } catch (IOException ioException) {
                ioException.getCause();
            }

            analyseButton.setEnabled(true);
        });
        toolsWindowContent.add(analyseButton);

        toolsWindowContent.setLayout(new GridLayout(GRID_ROWS, GRID_COLUMNS, GRID_H_GAP, GRID_V_GAP));
        toolsWindowContent.setVisible(true);
    }

    public JPanel getContent() {
        return toolsWindowContent;
    }



}