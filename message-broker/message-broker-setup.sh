#!/bin/bash

INSTALLATION_PATH="/opt/obelisk/message-broker"
SOURCE_PATH="https://github.com/BurrowStudios/Obelisk/message-broker"

mkdir -p $INSTALLATION_PATH
cd $INSTALLATION_PATH || exit 1

wget $SOURCE_PATH/compose.yml
wget $SOURCE_PATH/rabbitmq.conf

docker --version >/dev/null || exit 2

docker compose up -d