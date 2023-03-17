#!/bin/bash

docker run -d \
  --name postgres-jdbc-component \
  -p 5432:5432 \
  -e POSTGRES_DB=jdbc-component \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=password \
  postgres:latest
