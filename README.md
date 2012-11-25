appdynamics-apache-plugin
=========================

Plugin for the AppDynamics machine agent, which allows you to harvest mod_status data and feed it into metrics.


Compiling
=========
Because the plugin uses the libraries shipped with the AppDynamics machine agent, there is no maven build to set up the development.
What you need to to is to extract a current "MachineAgent.zip" into the folder "MachineAgent" (tested with 3.4 - 3.6).
The .classpath file for Eclipse will already have the relevant libraries on import.

Usage
=====
To use the plugin you need to include the monitor as described in the monitor.xml. For further information read the AppDynamics documentation.
To configure which Apache is monitored, provide a host and port as task argument.
