**********************************************
 Find out more about tools:
    spotbugs: https://spotbugs.readthedocs.io/en/latest/
    pmd and cpd: https://pmd.github.io/
    graudit: https://github.com/wireghoul/graudit/
    checkstyle: https://checkstyle.org/
**********************************************
usage: python3 script.py [-t] [options] <file>
    
    Must be run from directory which contains script.py. The script usually runs up to several minutes. Spotbugs is a
    slow tool, it's not recommended to choose more than 2 versions of spotbugs in the same run.
    
    Available tools (run with -t), must be comma separated without any spaces:
        spotbugs_3.1.0_RC7
        spotbugs_3.1.12
        spotbugs_4.0.0_beta1
        spotbugs_4.0.0_beta2
        spotbugs_4.0.0_beta3
        spotbugs_4.0.0_beta4
        spotbugs (runs the newest version)
        pmd
        cpd
        graudit
        checkstyle


    Following options should be provided when running specific tools while analysing a project:
        spotbugs (all versions):
            -c <file>
            -p <package>
            
        pmd:
            -c <file>
        
        cpd: none
        
        graudit: none
        
        checkstyle:
            -p <package>
        
        -c <file>
            Auxiliary classpath from file.
            Files that are necessary for deeper security analysis.
            Each location (path) should be in a new row.
            The best practise is usually this:
                1. Update Android Studio and run your project to compile all the necessary files
                2. Find the installation folder of Android Studio and .gradle folder
                3. Put those two locations in text file and name it "auxclasspathFromFile"
            
            These are necessary for tools (Spotbugs and PMD) to analyse your project correctly.
            See:
                https://spotbugs.readthedocs.io/en/latest/running.html#project-configuration-options
                https://pmd.github.io/latest/pmd_userdocs_installation.html
        -p <package>
            Package for analysis. Must be specified for some tools because they analyze all the files in project,
            including android.* package.
            
    File can be a single Java file, or a root project folder of Android Studio application.
    
    However if you're analysing a Java file rather than a whole project, you do not need to provide package argument.
    
    examples of running the script:
        python3 script.py -t spotbugs,pmd,cpd,checkstyle,graudit -c /home/user/Documents/auxclasspathFromFile -p my.package.* /home/user/Documents/project/
        python3 script.py -t pmd,cpd,checkstyle -c /home/user/Documents/auxclasspathFromFile /home/user/Documents/project/app/src/main/java/my/package/ClassForAnalysis.java
        
    auxClasspathFromFile example:
        /snap/android-studio/81/android-studio/
        /home/user/.gradle/
        
    For help run the script with -h option, and for list of all available tools run with -l.
    
    20.1.2020.
    FER (Fakultet elektrotehnike i računarstva), Zagreb
    Antonio Špoljar, mentor Stjepan Groš