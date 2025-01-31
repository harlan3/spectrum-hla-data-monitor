
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/rtis_D39/lib/x86_64_g++-12.2
export RTI_RID_FILE=/home/harlan/spectrum-hla-radio-monitor/spectrum-eclipse-ws/NTF_aviation.rid

# Add the required jars to classpath
CP=lib/outline.jar:lib/gson-1.7.jar:lib/container_classes.jar:lib/encoding_classes.jar:dist/spectrum-hla-radio-monitor.jar:/usr/local/rtis_D39/lib/x86_64_g++-12.2/rtis1516e.jar

java -cp $CP orbisoftware.hla_tools.spectrum_hla_monitor.MainApplication $@