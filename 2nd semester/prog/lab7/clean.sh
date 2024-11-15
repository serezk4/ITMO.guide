#!/bin/zsh

target_dir="./deploy"
log_file="build.log"
zip_dir="deploy#zip"

echo -n "Removing all zip files... "
(rm -f *.zip) 1>/dev/null 2>&1
rm -rf $zip_dir
echo "Done!"
echo -n "Removing all deploy files... "
rm -rf "$target_dir"
echo "Done!"
echo -n "Removing log file... "
rm -f "$log_file"
rm -f log_file
echo "Done!"
echo -n "Cleaning the project... "
gradle clean >> log_file 2>&1
echo "Done!"