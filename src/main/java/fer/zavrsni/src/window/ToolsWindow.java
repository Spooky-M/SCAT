package fer.zavrsni.src.window;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import fer.zavrsni.src.analysis.ProjectAnalysis;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fer.zavrsni.src.utils.Util;


public class ToolsWindow {

    private JPanel toolsWindowContent;
    private JLabel header;
    private JButton readmeLink;
    private JTextField packageChooser;
    private JButton analyseButton;
    private List<JCheckBox> boxes;

    private final Map<String, Integer> tools = new HashMap<>();

    private static final int GRID_ROWS = 20;
    private static final int GRID_COLUMNS = 1;
    private static final int GRID_H_GAP = 30;
    private static final int GRID_V_GAP = 10;

    public ToolsWindow(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        int i = 0;
        for(String tool: Util.TOOLS_NAMES) {
            tools.put(tool.trim(), i++);
        }
        createComponents(project, toolWindow);
    }

    private void createComponents(Project project, ToolWindow toolWindow) {
        JComponent component = toolWindow.getComponent();
        toolsWindowContent = new JPanel();
        toolsWindowContent.setSize(component.getWidth(), component.getHeight());

        header = new JLabel("<HTML><strong>" + Util.SHORT_README + "</strong></HTML>", JLabel.CENTER);
        header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toolsWindowContent.add(header);

        readmeLink = new JButton();
        readmeLink.setText("README link");
        readmeLink.addActionListener(e -> {
            try {
                BrowserUtil.browse(new URI("https://github.com/Spooky-M/SCAT"));
            } catch (URISyntaxException uriSyntaxException) {
                JOptionPane.showMessageDialog(null,
                        "Something went wrong while accessing URI. " +
                                "Please contact developers for further information and questions.",
                        "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        toolsWindowContent.add(readmeLink);

        boxes = new ArrayList<>();
        for(String tool : tools.keySet()) {
            JCheckBox toolCheckBox = new JCheckBox(tool, false);
            boxes.add(toolCheckBox);
            toolCheckBox.setMnemonic(tools.get(tool));
            toolsWindowContent.add(toolCheckBox);
        }

        packageChooser = new JTextField("Enter package name");
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
                pa = new ProjectAnalysis(project, selectedBoxes, packageChooser.getText());
                pa.executeAnalysis();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Something went wrong while running script. " +
                                "Please contact developers for further information and questions.",
                        "Error", JOptionPane.INFORMATION_MESSAGE);
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