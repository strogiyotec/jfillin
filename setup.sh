#!/bin/bash

# create release folder for jfill
# release folder is a zipped folder with version number in the name
# upload this zipped folder as a release in github

mvn clean package
echo "Native image compiled"
cd target 
jfill_version=$(./jfill -v | grep -oP "([0-9.])+")
release_name="jfill-${jfill_version}"
mkdir $release_name
echo "Start upx compression"
upx_target="${release_name}/jfill"
upx -o $upx_target jfill
echo "upx file ${upx_target} is ready"
zipped_file="${release_name}.zip"
zip -r $zipped_file $release_name
echo "${zipped_file} release is ready"

