# Spectrum HLA Data Monitor
The Spectrum HLA Data Monitor is a graphical tool used to publish and receive HLA-1.3 and HLA-1516e content over the federation. 
Sample data which consists of the payload of HLA objects and interactions can be modified and injected through the graphical user interface. The project also includes a bulk data producer (the solarsystem project), which can generate HLA objects and interactions that are viewable by the Spectrum HLA Data Monitor.

# Building the toolset
## Checkout from github
git clone https://github.com/harlan3/spectrum-hla-data-monitor.git

## Compiling the HLA-1.3 or HLA-1516e starfield project in Windows or Linux
in a command window, type the following depending on whether you are either using either HLA-1.3 or HLA-1516e mode:  
1. - cd spectrum-hla-data-monitor\hla13\starfield-eclipse-ws (for HLA-1.3)   
   - cd spectrum-hla-data-monitor\hla1516e\starfield-eclipse-ws (for HLA-1516e)   
1. tweak build.xml to point to the correct rtis13.jar or rtis1516e.jar depending on whether you are using HLA-1.3 or HLA-1516e modes.  
1. ant clean  
1. ant  
1. tweak start.cmd (Windows) or start.sh (Linux) to point to the correct RTI_LIB_HOME, JAVA_HOME, RTI_RID_FILE, and rtis13.jar or rtis1516e.jar.

## Compiling the HLA-1.3 or HLA-1516e solarsystem project in Windows or Linux
in a command window, type the following depending on whether you are either using either HLA-1.3 or HLA-1516e mode:  
1. - cd spectrum-hla-data-monitor\hla13\solarsystem-eclipse-ws (for HLA-1.3)   
   - cd spectrum-hla-data-monitor\hla1516e\solarsystem-eclipse-ws (for HLA-1516e)   
1. tweak build.xml to point to the correct rtis13.jar or rtis1516e.jar depending on whether you are using HLA-1.3 or HLA-1516e modes.  
1. ant clean  
1. ant  
1. tweak start.cmd (Windows) or start.sh (Linux) to point to the correct RTI_LIB_HOME, JAVA_HOME, RTI_RID_FILE, and rtis13.jar or rtis1516e.jar.  

## Running the HLA-1.3 or HLA-1516e starfield project in Windows or Linux
start.cmd -f starfield (Windows)  
start.sh -f starfield (Linux)  

## Running the HLA-1.3 or HLA-1516e solarsystem project in Windows or Linux
start.cmd -f solarsystem (Windows)  
start.sh -f solarsystem (Linux)  

---

## Compiling the HLA-1.3 or HLA-1516e spectrum-hla-data-monitor on Windows or Linux

in a following command window, type the following depending on whether you are either using HLA-1.3 or HLA-1516e mode:    
1. - cd spectrum-hla-data-monitor\hla13\spectrum-hla-eclipse-ws (for HLA-1.3)
   - cd spectrum-hla-data-monitor\hla1516e\spectrum-hla-eclipse-ws (for HLA-1516e)
1. tweak build.xml to point to the correct rtis13.jar or rtis1516e.jar depending on whether you are using HLA-1.3 or HLA-1516e modes.  
1. ant clean  
1. ant  
1. tweak start.cmd (Windows) or start.sh (Linux) to point to the correct RTI_LIB_HOME, JAVA_HOME, RTI_RID_FILE, and rtis13.jar or rtis1516e.jar.  

## Running the HLA-1.3 or HLA-1516e spectrum-hla-data-monitor project in Windows or Linux
start.cmd -f datamonitor (Windows)  
start.sh -f datamonitor (Linux)  
