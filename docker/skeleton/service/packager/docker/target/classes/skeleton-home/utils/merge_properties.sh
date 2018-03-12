#!/bin/bash

# copy and replace configuration files that are not properties files
shopt -s extglob
cp -f ${SKELETON_HOME}/conf-docker/!(*.properties) ${SKELETON_HOME}/conf
shopt -u extglob

# merge configuration files that are properties files
shopt -s nullglob
for i in ${SKELETON_HOME}/conf-docker/*.properties; do
    r=$(echo $i | sed -e 's/\/conf-docker\//\/conf\//g')

    echo "Merge properties from "$i" to "$r

    keys=($(awk -F= '{print $1}' $i))

    for key in "${keys[@]}"; do
        echo 'Remove key '${key}' from file '$i
        sed -i '/^'"${key}"'=/d' $r
    done

    echo "" >> $r
    echo "" >> $r
    cat $i >> $r
    echo "" >> $r

    # The following command doesn't work in some cases for unknown reason
    # awk -F= '!a[$1]++' $i $r 2> /dev/null | tee $r

done
shopt -u nullglob
