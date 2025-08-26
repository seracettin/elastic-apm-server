# Elastic APM Server Setup

This repository contains a complete Elastic APM (Application Performance Monitoring) setup with Elasticsearch, Kibana, and APM Server using Docker Compose, along with a Spring Boot test application for demonstration.

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Java Agent Configuration](#java-agent-configuration)
- [Spring Boot Test Application](#spring-boot-test-application)
- [Monitoring Endpoints](#monitoring-endpoints)
- [Troubleshooting](#troubleshooting)

## 🔧 Prerequisites

- Docker and Docker Compose installed
- Java 8+ for running the test application
- Maven for building the Spring Boot application

## 🚀 Quick Start

1. **Start the Elastic Stack:**
   ```bash
   cd apm-server
   docker-compose up -d
   ```

2. **Verify services are running:**
   - Elasticsearch: http://localhost:9200 (elastic/25ELstkP*)
   - Kibana: http://localhost:5601 (elastic/25ELstkP*)
   - APM Server: http://localhost:8200

3. **Build and run the test application:**
   ```bash
   cd "java app"
   mvn clean package
   java [JVM_OPTIONS] -jar target/apm-test-app-1.0.0.jar
   ```

## ☕ Java Agent Configuration

### Download the Elastic APM Java Agent

Download the latest Elastic APM Java agent from:
https://github.com/elastic/apm-agent-java/releases/download/v1.45.0/elastic-apm-agent-1.45.0.jar

### JVM Options for APM Monitoring

Add these JVM options when starting your Java application:

```bash
-javaagent:D:\Doc\atmosware\to-dropboz\java\java-agent\ElasticAPM\jar\elastic-apm-agent-1.45.0.jar
-Delastic.apm.service_name=ems-order
-Delastic.apm.server_url=http://localhost:8200
-Delastic.apm.secret_token=
-Delastic.apm.application_packages=com.example.apmtest
-Delastic.apm.use_path_as_transaction_name=false
-Delastic.apm.span_frames_min_duration=0ms
-Delastic.apm.trace_methods=com.example.apmtest.service.*#*
-Delastic.apm.transaction_name_strategy=framework,method_path
```

### Configuration Explanation

| Parameter | Description |
|-----------|-------------|
| `javaagent` | Path to the Elastic APM Java agent JAR file |
| `service_name` | Name of your service as it appears in APM |
| `server_url` | URL of the APM Server (default: http://localhost:8200) |
| `secret_token` | Authentication token (empty for development) |
| `application_packages` | Your application's package names for better performance |
| `use_path_as_transaction_name` | Use HTTP paths as transaction names |
| `span_frames_min_duration` | Minimum duration to capture stack traces |
| `trace_methods` | Specific methods to trace automatically |
| `transaction_name_strategy` | How to name transactions |

## 🌟 Spring Boot Test Application

The included Spring Boot application (`java app/`) provides endpoints to test APM monitoring:

### Available Endpoints

1. **Fast Endpoint** - `/api/fast`
   - Quick response endpoint for baseline performance testing
   - Returns immediately with minimal processing

2. **Slow Endpoint** - `/api/slow`
   - Simulates slow operations with configurable delays
   - Useful for testing performance monitoring and alerts

### Running the Test Application

#### If there isn't maven,  add maven first
- Download Maven from: https://maven.apache.org/download.cgi
- Download the Binary zip archive (apache-maven-3.9.x-bin.zip)
- Extract it to a folder like C:\Program Files\Apache\maven
- Add the bin directory to your PATH environment variable

```bash
cd "java app"

# Build the application
mvn clean package

# Run with APM monitoring (update the agent path)
java -javaagent:path/to/elastic-apm-agent-1.45.0.jar \
     -Delastic.apm.service_name=apm-test-app \
     -Delastic.apm.server_url=http://localhost:8200 \
     -Delastic.apm.application_packages=com.example.apmtest \
     -jar target/apm-test-app-1.0.0.jar
```


### Testing the Endpoints

```bash
# Test fast endpoint
curl http://localhost:8080/api/fast

# Test slow endpoint
curl http://localhost:8080/api/slow
```

## 📊 Monitoring Endpoints

Once your application is running with APM monitoring:

1. **Access Kibana:** http://localhost:5601
2. **Login:** elastic / 25ELstkP*
3. **Navigate to APM:** Menu → Observability → APM
4. **View your service:** Look for "apm-test-app" or your configured service name

You'll be able to see:
- Transaction traces
- Error rates
- Response times
- Service dependencies
- Database queries
- External service calls

## 🔍 Troubleshooting

### Common Issues

1. **Services not starting:**
   ```bash
   docker-compose logs [service-name]
   ```

2. **APM data not appearing:**
   - Verify APM agent path is correct
   - Check application logs for APM connection errors
   - Ensure APM Server is accessible at http://localhost:8200

3. **Memory issues:**
   - Elasticsearch requires at least 2GB RAM
   - Increase Docker memory limits if needed

### Useful Commands

```bash
# Check service health
docker-compose ps

# View logs
docker-compose logs apm-server
docker-compose logs elasticsearch

# Reset everything
docker-compose down -v
docker-compose up -d
```

## 📚 Additional Resources

- [Elastic APM Java Agent Documentation](https://www.elastic.co/guide/en/apm/agent/java/current/index.html)
- [APM Server Configuration](https://www.elastic.co/guide/en/apm/server/current/configuring-howto-apm-server.html)
- [Kibana APM UI](https://www.elastic.co/guide/en/kibana/current/xpack-apm.html)

## 🏷️ Version Information

- Elasticsearch: 8.13.0
- Kibana: 8.13.0
- APM Server: 8.13.0
- Elastic APM Java Agent: 1.45.0
- Spring Boot: 3.2.0
