#!/bin/sh

command=$(basename $0)
direct=$(dirname $0)
trap 'stty echo; echo "${command} aborted"; exit' 1 2 3 15
#
CWD=$(pwd)

dbname="testdb"
instance="testdb"
schema="populate.sql"
sqlfile="data.sql"
doClean=0
tmpFile="/tmp/tmp$$.log"

#
# Usage
#
usage() {
  #

  while [ $# -ne 0 ]; do
    case $1 in
    -db | --database)
      dbname=$2
      shift 2
      ;;
    -i | --instance)
      instance=$2
      shift 2
      ;;
    -s | --schema)
      schema=$2
      shift 2
      ;;
    -f | --sql-file)
      sqlfile=$2
      shift 2
      ;;
    -c | --clean-up)
      doClean=1
      shift
      ;;
    --debug)
      set -xv
      shift
      ;;
    -?*)
      show_usage
      break
      ;;
    --)
      shift
      break
      ;;
    - | *) break ;;
    esac
  done

  return 0
}

disableapi() {
  (gcloud services disable spanner.googleapis.com) \
    >"${tmpFile}" 2>&1

  if [ $? -gt 0 ]; then
    echo "Error: Disable API failed"
    cat "${tmpFile}"
    return 1
  fi
  return 0
}

enableapi() {
  echo "${command}: Enable Cloud Spanner API..."
  (gcloud services enable spanner.googleapis.com) \
    >"${tmpFile}" 2>&1

  if [ $? -gt 0 ]; then
    echo "Error: Enable API failed"
    cat "${tmpFile}"
    return 1
  fi
  sleep 30
  return 0
}

dropDb() {
  echo "${command}: Dropping everything..."
  (gcloud spanner instances delete $1 -q) >"${tmpFile}" 2>&1
  return 0
}

createinstance() {
  echo "${command}: Creating an instance..."
  (gcloud spanner instances create $1 \
    --config=regional-us-central1 \
    --description="Test Instance" \
    --nodes=1 -q) >"${tmpFile}" 2>&1

  if [ $? -gt 0 ]; then
    echo "Error: Instance creation failed"
    cat "${tmpFile}"
    return 1
  fi

  return 0
}

createdb() {
  echo "${command}: Creating a database..."
  (gcloud spanner databases create $1 \
    --instance=$2 \
    --ddl-file=$3 -q) >"${tmpFile}" 2>&1

  if [ $? -gt 0 ]; then
    echo "Error: Database creation failed"
    cat "${tmpFile}"
    return 1
  fi

  return 0
}

populatedb() {
  echo "${command}: Populating database..."

  while read sql; do
    if [ "${sql}" != "" ]; then
      (gcloud spanner databases execute-sql $1 --instance=$2 \
        --sql="${sql}") >"${tmpFile}" 2>&1
      if [ $? -gt 0 ]; then
        echo "Error: Database population failed"
        cat "${tmpFile}"
        return 1
      fi
    fi
  done <"$3"

  return 0
}

usage $*

enableapi
if [ $? -gt 0 ]; then
  exit 1
fi

dropDb "${instance}"

if [ $doClean -gt 0 ]; then
  disableapi
  exit 0
fi

createinstance "${instance}"
if [ $? -gt 0 ]; then
  disableapi
  exit 1
fi

createdb "${dbname}" "${instance}" "${schema}"
if [ $? -gt 0 ]; then
  disableapi
  exit 1
fi

populatedb "${dbname}" "${instance}" "${sqlfile}"
if [ $? -gt 0 ]; then
  disableapi
  exit 1
fi

disableapi
exit 0
