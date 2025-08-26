#!/bin/bash

echo "========================================"
echo "Starting APM Test Application with Elastic APM Agent"
echo "========================================"

# Set the path to your Elastic APM agent JAR file
APM_AGENT_PATH="../jar-dir/elastic-apm-agent-1.45.0.jar"

# Check if the agent file exists
if [ ! -f "$APM_AGENT_PATH" ]; then
    echo "ERROR: APM agent not found at: $APM_AGENT_PATH"
    echo "Please download the agent from: https://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/1.45.0/elastic-apm-agent-1.45.0.jar"
    echo "And update the APM_AGENT_PATH variable in this script"
    exit 1
fi

echo "Building the application..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "ERROR: Build failed"
    exit 1
fi

echo "Starting application with APM monitoring..."
echo "APM Agent: $APM_AGENT_PATH"
echo "Service Name: apm-test-app"
echo "APM Server URL: http://localhost:8200"
echo "========================================"

java -javaagent:"$APM_AGENT_PATH" \
     -Delastic.apm.service_name=amp-test-app \
     -Delastic.apm.server_url=http://localhost:8200 \
     -Delastic.apm.secret_token= \
     -Delastic.apm.application_packages=com.example.apmtest \
     -Delastic.apm.use_path_as_transaction_name=false \
     -Delastic.apm.span_frames_min_duration=0ms \
     -Delastic.apm.trace_methods=com.example.apmtest.service.*#* \
     -Delastic.apm.transaction_name_strategy=framework,method_path \
     -jar target/apm-test-app-1.0.0.jar
