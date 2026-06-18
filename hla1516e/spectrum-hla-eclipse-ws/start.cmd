echo off

set RTI_LIB_HOME=C:\rtis_D39\lib\win64_vc++-14.0
set JAVA_HOME=C:\Program Files\jdk-23.0.1

set PATH=%PATH%;%RTI_LIB_HOME%;%JAVA_HOME%\bin;
set RTI_RID_FILE=C:\CDR\Data\default.rid

REM Add the required jars to classpath
set CP=.\dist\spectrum-hla-data-monitor.jar;.\lib\outline.jar;.\lib\gson-1.7.jar;.\lib\container_classes.jar;.\lib\encoding_classes.jar;C:\rtis_D39\lib\win64_vc++-14.0\rtis1516e.jar

java -cp %CP% orbisoftware.hla_tools.spectrum_hla_monitor.MainApplication %*
