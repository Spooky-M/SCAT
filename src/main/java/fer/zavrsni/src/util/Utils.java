package fer.zavrsni.src.util;

public class Utils {
    public static final String[] TOOLS_NAMES = {"spotbugs_3.1.0_RC7", "spotbugs_3.1.12", "spotbugs_4.0.0_beta1",
            "spotbugs_4.0.0_beta2", "spotbugs_4.0.0_beta3", "spotbugs_4.0.0_beta4", "pmd", "cpd", "graudit", "checkstyle"};
    public static final String SHORT_README = "Select tools, enter which package you want to analyse, " +
            "and click \"analyse\" button to start.";
    public static final String LONG_README = "SCAT (Static Code Analysis Tools) plugin provides a single interface " +
            "for combining several open source tools which perform security, coding style and other checks on Java " +
            "static code in Android Studio projects. For it to work properly, it is necessary to clone a script " +
            "(called PPProjekt) to your Android Studio project's root. For example, if there's a project named " +
            "Application in home/userx/Documents folder, PPProjekt folder should be placed like this: " +
            "\"home/userx/Documents/Application/PPProjekt\". Script can also be run from command line, " +
            "and its documentation is also available on Github. In short, this plugin provides an interface " +
            "for easier selection of desired tools and runs the Python script. The Python script runs " +
            "the selected tools, and a html report called simply \"report.html\" is rendered in script's root directory, " +
            "on location: PPProjekt/report.html. After the analysis, which runs in background, is finished, " +
            "the popup opens with appropriate message, specifying the absolute path of generated report.\n\n" +
            "Open source code tools used: spotbugs, pmd/cpd, checkstyle, graudit.\n\n" +
            "Antonio Špoljar, 8.5.2020.";
}
