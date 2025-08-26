#!/bin/bash

echo "========================================"
echo "Testing APM Application Endpoints"
echo "========================================"

BASE_URL="http://localhost:8080/api"

echo "Testing Health endpoint..."
curl -s -X GET "$BASE_URL/health" | jq '.' 2>/dev/null || curl -s -X GET "$BASE_URL/health"

echo ""
echo "Testing Fast endpoint..."
curl -s -X GET "$BASE_URL/fast" | jq '.' 2>/dev/null || curl -s -X GET "$BASE_URL/fast"

echo ""
echo "Testing Slow endpoint (2 second delay)..."
curl -s -X GET "$BASE_URL/slow?delayMs=2000" | jq '.' 2>/dev/null || curl -s -X GET "$BASE_URL/slow?delayMs=2000"

echo ""
echo "Testing Database endpoint (GET)..."
curl -s -X GET "$BASE_URL/database?search=Order" | jq '.' 2>/dev/null || curl -s -X GET "$BASE_URL/database?search=Order"

echo ""
echo "Testing Database endpoint (POST)..."
curl -s -X POST "$BASE_URL/database" -H "Content-Type: application/json" -d '{"searchTerm":"User"}' | jq '.' 2>/dev/null || curl -s -X POST "$BASE_URL/database" -H "Content-Type: application/json" -d '{"searchTerm":"User"}'

echo ""
echo "Testing External API endpoint..."
curl -s -X GET "$BASE_URL/external" | jq '.' 2>/dev/null || curl -s -X GET "$BASE_URL/external"

echo ""
echo "Testing Slow endpoint with error simulation..."
curl -s -X GET "$BASE_URL/slow?delayMs=1000&simulateError=true" | jq '.' 2>/dev/null || curl -s -X GET "$BASE_URL/slow?delayMs=1000&simulateError=true"

echo ""
echo "========================================"
echo "All endpoint tests completed!"
echo "Check Kibana APM UI at: http://localhost:5601"
echo "========================================"

