
export DYLD_LIBRARY_PATH=/usr/local/rtis_D39/lib/macintel64_clang-14.0:$DYLD_LIBRARY_PATH
export RTI_RID_FILE=/Users/harlan/spectrum-hla-radio-monitor/spectrum-eclipse-ws/NTF_aviation.rid

# Add the required jars to classpath
CP=lib/outline.jar:lib/gson-1.7.jar:lib/container_classes.jar:lib/encoding_classes.jar:dist/spectrum-hla-radio-monitor.jar:/usr/local/rtis_D39/lib/macintel64_clang-14.0/rtis1516e.jar

java -Djava.library.path=/usr/local/rtis_D39/lib/macintel64_clang-14.0 -cp $CP orbisoftware.hla_tools.spectrum_hla_monitor.MainApplication $@