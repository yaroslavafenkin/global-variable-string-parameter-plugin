Global Variable String Parameter Plugin
=======

This plugin is intended to provide a parameter with support for global node properties via $VARIABLE or ${VARIABLE}

Installation
=======
Add global-variable-string-parameter.hpi to jenkins/plugins and restart.  In your job, choose Add Parameter and select Global Variable String Parameter.  Use $VARIABLE or ${VARIABLE} to substitute global node parameters. 

Example
=======
Create a global variable
![createglobalvariable](https://github.com/pmaccamp/global-variable-string-parameter/raw/master/images/createglobalvariable.PNG "createglobalvariable")
Create a global variable string parameter in a job
![createparameter](https://github.com/pmaccamp/global-variable-string-parameter/raw/master/images/createparameter.PNG "createparameter")
Reference the global variable in the default value
![parametervalue](https://github.com/pmaccamp/global-variable-string-parameter/raw/master/images/parametervalue.PNG "parametervalue")
Add a simple echo statement to demonstrate the substitution
![echoparameter](https://github.com/pmaccamp/global-variable-string-parameter/raw/master/images/echoparameter.PNG "echoparameter")
The variable remains unsubstituted on the Build Now page
![parameterbuildnow](https://github.com/pmaccamp/global-variable-string-parameter/raw/master/images/parameterbuildnow.PNG "parameterbuildnow")
But is correctly substituted in the console output
![echoparameter](https://github.com/pmaccamp/global-variable-string-parameter/raw/master/images/echoparameter.PNG "echoparameter")
The variable can also be changed from the build now screen to be incorrect
![buildnow2](https://github.com/pmaccamp/global-variable-string-parameter/raw/master/images/buildnow2.PNG "buildnow2")
And no longer substitutes
![console2](https://github.com/pmaccamp/global-variable-string-parameter/raw/master/images/console2.PNG "console2")
