#!/bin/bash
#docker run -d -p 6379:6379 -v /Users/roberto/work/microservices/docker/redis/redis.conf:/usr/local/etc/redis/redis.conf --name redis redis:latest redis-server /usr/local/etc/redis/redis.conf
docker run -d -p 6379:6379 -v /Users/roberto/work/microservices/docker/redis/redis.conf:/usr/local/etc/redis/redis.conf --name redis redis:latest redis-server
