#!/bin/bash

# create release folder for jfill
# release folder is a zipped folder with version number in the name
# upload this zipped folder as a release in github

mvn clean package
cd target 
jfill_version=$(./jfill -v | grep -oP "([0-9.])+")
release_name="jfill-${jfill_version}"
mkdir $release_name
upx_target="${release_name}/jfill"
echo $upx_target
upx -o $upx_target jfill
zipped_file="${release_name}.zip"
zip -r $zipped_file $release_name
echo "${release_name} is ready"

