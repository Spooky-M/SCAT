# SCAT - Source Code Analysis Tool Plugin
SCAT (Static Code Analysis Tools) plugin provides a single interface for combining several open source tools which 
perform security, coding style and other checks on Java static code in Android Studio projects. For it to work properly,
it is necessary to clone a Python project (called Script) containing a script to your Android Studio project's root.
For example, if there's a project named Application in home/userx/Documents folder, Script folder should be placed 
like this: home/userx/Documents/Application/Script. Script can also be run from command line, and its documentation 
is available on Github. The other requirement is installed and configured Python 3, so the script can be run.
In short, this plugin provides an interface for easier selection of desired tools and runs the Python script.
The Python script runs the selected tools, and a html report called simply "report.html" is rendered in script's
root directory, on location: Script/report.html. After the analysis, which runs in background, is finished, the popup
opens with appropriate message, specifying the path to generated report.

Open source code tools used: spotbugs, pmd/cpd, checkstyle, graudit.

Antonio Špoljar, 8th of May, 2020.