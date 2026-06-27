echo off

set RTI_LIB_HOME=C:\rtis_D39\lib\win64_vc++-14.0
set JAVA_HOME=C:\Program Files\jdk-23.0.1

set PATH=%PATH%;%RTI_LIB_HOME%;%JAVA_HOME%\bin;
set RTI_RID_FILE=C:\CDR\Data\default.rid

REM Add the required jars to classpath
set CP=.\dist\star_field.jar;.\lib\hla13_fom_objs.jar;C:\rtis_D39\lib\win64_vc++-14.0\rtis13.jar

for /L %%i in (1,1,5) do (
	start "" java -cp %CP% orbisoftware.hlatools.spectrumhlamonitor.starfielddemo.MainApplication %*%%i
	REM echo %*%%i
)