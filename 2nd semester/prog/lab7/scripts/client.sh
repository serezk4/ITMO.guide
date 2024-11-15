#!/bin/sh

port_file=~/port

if [ -f "$port_file" ]; then
    echo "Файл с портом существует, берём порт оттуда."
    port=$(cat "$port_file")
else
    echo "Файл с портом не найден, создаём новый."
    port=$(jot -r 1 1024 65535)
    echo "$port" > "$port_file"
    echo "Сгенерирован и сохранён порт: $port"
fi

echo "selected port: $port"
export server_port="$port"

sleep 2
clear

export JAVA_VERSION=21

java -jar lab7-1.0-SNAPSHOT.jar client