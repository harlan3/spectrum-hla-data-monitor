#!/bin/bash

export JAVA_CMD=/usr/local/jdk-22/bin/java
export JAVA_HOME=/usr/local/jdk-22
export RTI_LIB_HOME=/rtis_D39/lib/x86_64_g++-11.2

export RTI_RID_FILE=/CDR/Data/default.rid

# VM options
export VM_OPTIONS="-Djava.library.path=$RTI_LIB_HOME"

export PATH=$JAVA_HOME/bin:$RTI_LIB_HOME

# Add the required jars to classpath
export CP="./dist/solar_system.jar:./lib/hla1516_fom_objs.jar:/rtis_D39/lib/x86_64_g++-11.2/rtis1516e.jar"

$JAVA_CMD $VM_OPTIONS -cp $CP orbisoftware.hlatools.spectrumhlamonitor.solarsystemdemo.MainApplication "$@"
