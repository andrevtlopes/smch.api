#!/bin/bash

set -e

GROUP_ID=unioeste
ARTIFACT_ID=smch-api
VERSION=0.0.1
REMOTE_USER=root
REMOTE_ADDR=167.99.83.189
FILE_NAME=${GROUP_ID}-${ARTIFACT_ID}-${VERSION}
IMAGE_NAME=${GROUP_ID}/${ARTIFACT_ID}:${VERSION}

mvn clean package -B -DskipTests
docker build -f Dockerfile -t ${IMAGE_NAME} .
docker save ${IMAGE_NAME} -o ~/${FILE_NAME}.tar
scp ~/${FILE_NAME}.tar ${REMOTE_USER}@${REMOTE_ADDR}:~
