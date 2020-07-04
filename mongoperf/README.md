# Mongo Perf

The goal is to test the performance on search for collection with large objects

For test purpose, the mongodb database will run on docker with limited memory :
docker run --name mongoperf --memory=1g -p 27018:27017 -d mongo:bionic
