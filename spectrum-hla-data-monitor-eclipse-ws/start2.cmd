echo off

set RTI_LIB_HOME=D:\rtis_D39\lib\win64_vc++-14.0
set JAVA_HOME=C:\Program Files\jdk-20

set PATH=%PATH%;%RTI_LIB_HOME%;%JAVA_HOME%\bin
set RTI_RID_FILE=D:\CDR\Configuration_Files\2.2.0-aviation\NTF_aviation.rid

REM Add the required jars to classpath
set CP=.\lib\outline.jar;.\lib\gson-1.7.jar;.\lib\container_classes.jar;.\lib\encoding_classes.jar;.\dist\spectrum-hla-radio-monitor.jar;D:\rtis_D39\lib\win64_vc++-14.0\rtis1516e.jar

java -cp %CP% orbisoftware.hla_tools.spectrum_hla_monitor.MainApplication %*

