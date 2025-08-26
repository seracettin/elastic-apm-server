@echo off
echo ========================================
echo Starting APM Test Application with Elastic APM Agent
echo ========================================

REM Set the path to your Elastic APM agent JAR file
set APM_AGENT_PATH=..\jar-dir\elastic-apm-agent-1.45.0.jar

REM Check if the agent file exists
if not exist "%APM_AGENT_PATH%" (
    echo ERROR: APM agent not found at: %APM_AGENT_PATH%
    echo Please download the agent from: https://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/1.45.0/elastic-apm-agent-1.45.0.jar
    echo And update the APM_AGENT_PATH variable in this script
    pause
    exit /b 1
)

echo Building the application...
call mvn clean package -DskipTests

if %ERRORLEVEL% neq 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)

echo Starting application with APM monitoring...
echo APM Agent: %APM_AGENT_PATH%
echo Service Name: apm-test-app
echo APM Server URL: http://localhost:8200
echo ========================================

java -javaagent:"%APM_AGENT_PATH%" ^
     -Delastic.apm.service_name=apm-test-app ^
     -Delastic.apm.server_url=http://localhost:8200 ^
     -Delastic.apm.secret_token= ^
     -Delastic.apm.application_packages=com.example.apmtest ^
     -Delastic.apm.use_path_as_transaction_name=false ^
     -Delastic.apm.span_frames_min_duration=0ms ^
     -Delastic.apm.trace_methods=com.example.apmtest.service.*#* ^
     -Delastic.apm.transaction_name_strategy=framework,method_path ^
     -jar target/apm-test-app-1.0.0.jar

pause
