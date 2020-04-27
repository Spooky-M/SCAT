from typing import Set

with open("README.md", "r") as r:
    HELP: str = r.read()
HELP_ARG = "-h"
LIST_ARG = "-l"
TOOLS_ARG = "-t"
CLASSPATH_ARG = "-c"
PACKAGE_ARG = "-p"

CHECKSTYLE: str = "checkstyle"
GRAUDIT: str = "graudit"
PMD: str = "pmd"
CPD: str = "cpd"
SPOTBUGS: str = "spotbugs"
SPOTBUGS_3_1_0_RC7: str = "spotbugs_3.1.0_RC7"
SPOTBUGS_3_1_12: str = "spotbugs_3.1.12"
SPOTBUGS_4_0_0_BETA1: str = "spotbugs_4.0.0_beta1"
SPOTBUGS_4_0_0_BETA2: str = "spotbugs_4.0.0_beta2"
SPOTBUGS_4_0_0_BETA3: str = "spotbugs_4.0.0_beta3"
SPOTBUGS_4_0_0_BETA4: str = "spotbugs_4.0.0_beta4"

ALL_TOOLS: Set[str] = {CHECKSTYLE, GRAUDIT, PMD, CPD, SPOTBUGS, SPOTBUGS_3_1_0_RC7, SPOTBUGS_3_1_12,
                       SPOTBUGS_4_0_0_BETA1, SPOTBUGS_4_0_0_BETA2, SPOTBUGS_4_0_0_BETA3, SPOTBUGS_4_0_0_BETA4}
C_TOOLS: Set[str] = {SPOTBUGS_3_1_0_RC7, SPOTBUGS_3_1_12, SPOTBUGS_4_0_0_BETA1, SPOTBUGS_4_0_0_BETA2,
                     SPOTBUGS_4_0_0_BETA3, SPOTBUGS_4_0_0_BETA4, SPOTBUGS, PMD}

P_TOOLS: Set[str] = {SPOTBUGS_3_1_0_RC7, SPOTBUGS_3_1_12, SPOTBUGS_4_0_0_BETA1, SPOTBUGS_4_0_0_BETA2,
                     SPOTBUGS_4_0_0_BETA3, SPOTBUGS_4_0_0_BETA4, SPOTBUGS, CHECKSTYLE}

CHECKSTYLE_REPORT_PATH: str = "./reports/" + CHECKSTYLE
GRAUDIT_REPORT_PATH: str = "./reports/" + GRAUDIT
PMD_REPORT_PATH: str = "./reports/" + PMD
CPD_REPORT_PATH: str = "./reports/" + CPD
SPOTBUGS_3_1_0_RC7_REPORT_PATH: str = "./reports/" + SPOTBUGS_3_1_0_RC7
SPOTBUGS_3_1_12_REPORT_PATH: str = "./reports/" + SPOTBUGS_3_1_12
SPOTBUGS_4_0_0_BETA1_REPORT_PATH: str = "./reports/" + SPOTBUGS_4_0_0_BETA1
SPOTBUGS_4_0_0_BETA2_REPORT_PATH: str = "./reports/" + SPOTBUGS_4_0_0_BETA2
SPOTBUGS_4_0_0_BETA3_REPORT_PATH: str = "./reports/" + SPOTBUGS_4_0_0_BETA3
SPOTBUGS_4_0_0_BETA4_REPORT_PATH: str = "./reports/" + SPOTBUGS_4_0_0_BETA4
