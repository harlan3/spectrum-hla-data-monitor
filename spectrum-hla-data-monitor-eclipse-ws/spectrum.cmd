echo off

REM Add the required jars to classpath
set CP=.\lib\spectrum.jar;.\lib\outline.jar;.\lib\oric_rdsm.jar;.\lib\gson-1.7.jar

REM Add DDS idl generated data classes/jars here
set CP=%CP%;.\lib\DDSSolarSystem.jar;.\lib\container_classes.jar;.\lib\test_class.jar

java -cp %CP% orbisoftware.hlatools.spectrumhlamonitor.SpectrumHLAMonitor

