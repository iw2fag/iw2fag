#!/usr/bin/env bash

function config_properties_file(){
    "${SKELETON_HOME}"/utils/add_or_replace_property.sh "${SKELETON_HOME}"/conf/db.properties db.hibernate.connection.url "jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}"
}

function wait_db(){
    "${SKELETON_HOME}"/utils/wait-for-it.sh ${DB_HOST}:${DB_PORT} --timeout=60 --strict -- echo "db is up"
}
#

config_properties_file

wait_db

if [ $? -ne 0 ] ; then
    echo "Init service failed."
    exit -1
fi