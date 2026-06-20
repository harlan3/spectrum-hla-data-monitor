# Spectrum HLA Data Monitor
The Spectrum HLA Data Monitor is a graphical tool used to publish and receive HLA-1.3 and HLA-1516e content over the federation. 
Samples defined as HLA objects and interactions instantiations can be modified and injected through the graphical user interface.
The project also includes a bulk data producer (the solarsystem project), which can generate HLA objects and interactions for the Spectrum HLA Data Monitor.

# Building the toolset

## Checkout from github
git clone https://github.com/harlan3/spectrum-hla-data-monitor.git

## Compiling and Running the HLA-1.3 or HLA-1516e solar system project in Windows or Linux


in a following command window, type the following depending on whether you are either using HLA-1.3 or HLA-1516e modes:  
cd hla13\solarsystem-eclipse-ws (for HLA-1.3)
cd hla1516e\solarsystem-eclipse-ws (for HLA-1516e)
tweak build.xml to point to the correct rtis13.jar or rtis1516e.jar on your system depending on whether you are either using HLA-1.3 or HLA-1516e modes.  
ant clean  
ant  
tweak start.cmd (Windows) or start.sh (Linux) to point to the correct RTI_LIB_HOME, JAVA_HOME, RTI_RID_FILE, and rtis13.jar or rtis1516e.jar.
start.cmd -f test123 (Windows)
start.sh -f test123 (Linux)
---
## Compiling and Running the HLA-1.3 or HLA-1516e spectrum-hla-eclipse-ws on Windows or Linux

in a following command window, type the following depending on whether you are either using HLA-1.3 or HLA-1516e modes:    
cd hla13\spectrum-hla-eclipse-ws (for HLA-1.3)
cd hla1516e\spectrum-hla-eclipse-ws (for HLA-1516e)
