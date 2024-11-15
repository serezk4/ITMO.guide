#!/bin/sh

port_file=~/port
PARAM_FILE="params.conf"

read_param() {
  local prompt=$1
  local default_value=$2
  local var_name=$3

  if [ -n "$default_value" ]; then
    read -p "$prompt ($default_value): " input
    input=${input:-$default_value}
  else
    read -p "$prompt: " input
  fi

  eval $var_name="'$input'"
}

if [ -f "$PARAM_FILE" ]; then
  source "$PARAM_FILE"
fi

if [ -f "$HOME/.pgpass" ]; then
  PG_PASS_LINE=$(grep '^*:*:*' "$HOME/.pgpass")
  PG_USER_FROM_PGPASS=$(echo $PG_PASS_LINE | cut -d: -f4)
  PG_PASSWORD_FROM_PGPASS=$(echo $PG_PASS_LINE | cut -d: -f5)
fi

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

DB_HOST=pg
DB_PORT=5432
DB_NAME=studs
DB_USER=$PG_USER_FROM_PGPASS
DB_PASSWORD=$PG_PASSWORD_FROM_PGPASS

echo "DB_HOST=$DB_HOST" > "$PARAM_FILE"
# shellcheck disable=SC2129
echo "DB_PORT=$DB_PORT" >> "$PARAM_FILE"
echo "DB_NAME=$DB_NAME" >> "$PARAM_FILE"
echo "DB_USER=$DB_USER" >> "$PARAM_FILE"
echo "DB_PASSWORD=$DB_PASSWORD" >> "$PARAM_FILE"

PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f create.sql 2>/dev/null

if [ $? -ne 0 ]; then
  echo "Error while executing SQL script."
else
  echo "SQL script executed successfully."
fi

export DB_HOST
export DB_PORT
export DB_NAME
export DB_USER
export DB_PASSWORD
export JAVA_VERSION=21

sleep 2
clear

java -jar lab7-1.0-SNAPSHOT.jar