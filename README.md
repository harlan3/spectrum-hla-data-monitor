# Spectrum HLA Data Monitor
The Spectrum HLA Data Monitor is a graphical tool used to publish and receive HLA 1.3 and HLA-1516e content over the federation. 
Samples defined as HLA objects and interactions instantiations can be modified and injected through the graphical user interface.
The project also includes a bulk data producer (the solarsystem project), which can generate HLA objects and interactions for the Spectrum HLA Data Monitor.

# Building the toolset

## Checkout from github
git clone https://github.com/harlan3/spectrum-hla-data-monitor.git

## Compiling and Running the HLA 1.3 projects on Windows
in a command window type the following:  
cd hla13\solarsystem-eclipse-ws  
tweak build.xml to point to the correct rtis13.jar on your system.  
ant clean  
ant  
tweak start.cmd to point to the correct RTI_LIB_HOME, JAVA_HOME, RTI_RID_FILE, and rtis13.jar.
