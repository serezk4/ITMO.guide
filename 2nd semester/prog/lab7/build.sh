#!/bin/bash

target_dir="./deploy"
log_file="build.log"
PARAM_FILE="build.conf"
LOG_FILE="$log_file"

current_time=$(date +"%H-%M-%S")
zip_dir="deploy#zip"
deploy_archive_name=$zip_dir"/lab7_$current_time.zip"
code_archive_name=$zip_dir"/lab7_code_$current_time.zip"

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

if [ -f "$LOG_FILE" ]; then
  rm -f "$LOG_FILE"
  touch "$LOG_FILE"
fi

if [ -d "$zip_dir" ]; then
    rm -rf "$zip_dir"
fi

if [ ! -d "$zip_dir" ]; then
    mkdir -p "$zip_dir"
fi

echo "Creating the code archive..."
zip -r "$code_archive_name" . >> "$log_file" 2>&1 -x "*.idea*" -x "*.DS_Store*" -x "*.zip"
echo "Code archive created: $code_archive_name"

if [ ! -d "$target_dir" ]; then
    mkdir -p "$target_dir"
fi

echo "Cleaning up the target directory and archive..."
rm -rf "$target_dir"
mkdir -p "$target_dir"

echo "Building the project..."
gradle clean build >> log_file 2>&1

echo "Copying the files..."
find . \( -name 'lab7-1.0-SNAPSHOT.jar' \) -exec cp {} "$target_dir" \;
cp scripts/* ./deploy/
cp sql/* ./deploy/
echo "Files copied to $target_dir"

echo "Creating the archive..."
zip -r "$deploy_archive_name" "$target_dir" >> "$log_file" 2>&1
echo "Deploy archive created: $deploy_archive_name"

echo "Cleaning up..."
gradle clean >> log_file 2>&1
echo "Done, log file: $log_file"

echo "get username & password: https://se.ifmo.ru/passwd/"
read_param "Enter helios username" "${USERNAME:-}" USERNAME
read_param "Enter helios password" "${PASSWORD:-}" PASSWORD
read_param "Enter server" "${SERVER:-helios.cs.ifmo.ru}" SERVER
read_param "Enter port" "${PORT:-2222}" PORT
REMOTE_DIR="~/deploy"

echo "USERNAME=$USERNAME" > "$PARAM_FILE"
echo "PASSWORD=$PASSWORD" >> "$PARAM_FILE"
echo "SERVER=$SERVER" >> "$PARAM_FILE"
echo "PORT=$PORT" >> "$PARAM_FILE"

LOCAL_DEPLOY_DIR="./deploy"
#
#echo ""
#echo "Installing Gradle and building project..."
#(brew install gradle || exit 1 ) >> "$LOG_FILE" 2>&1
#echo "Maven installed"
#(brew install sshpass || exit 1 ) >> "$LOG_FILE" 2>&1
#echo "sshpass installed"

echo ""
echo "Connecting to the server and removing old deployment..."
sshpass -p $PASSWORD ssh -p $PORT $USERNAME@$SERVER "rm -rf $REMOTE_DIR && mkdir -p $REMOTE_DIR" || exit 1

echo ""
echo "Copying new files to the server..."
sshpass -p $PASSWORD scp -P $PORT -r $LOCAL_DEPLOY_DIR/* $USERNAME@$SERVER:$REMOTE_DIR || exit 1

echo ""
echo "Deployment completed successfully!"
echo "To run the project, connect to the server and run the following command:"
echo "cd $REMOTE_DIR && ./server.sh"
echo "cd $REMOTE_DIR && ./client.sh"

echo "script finished, check $LOG_FILE for details"