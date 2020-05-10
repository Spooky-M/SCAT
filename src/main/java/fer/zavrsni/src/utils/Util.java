package fer.zavrsni.src.utils;

/**
Constants and utilities
 */
public class Util {
    /**
    Available tools
     */
    public static final String[] TOOLS_NAMES = {"spotbugs_3.1.0_RC7", "spotbugs_3.1.12", "spotbugs_4.0.0_beta1",
            "spotbugs_4.0.0_beta2", "spotbugs_4.0.0_beta3", "spotbugs_4.0.0_beta4", "pmd", "cpd", "graudit", "checkstyle"};

    /**
    Short readme, to be displayed in the header of window
     */
    public static final String SHORT_README = "Select tools, enter which package you want to analyse, " +
            "and click \"analyse\" button to start.";

    /**
    Timeout for background process which runs the script
     */
    public static final int TIMEOUT = 1000 * 60 * 10;
}
