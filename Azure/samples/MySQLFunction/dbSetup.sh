#!/bin/sh

command=`basename $0`
direct=`dirname $0`
trap 'stty echo; echo "${command} aborted"; exit' 1 2 3 15
#
CWD=`pwd`

dbname="testdbmysql"
server="testdbmysql-server"
resourcegrp=""
location="northeurope"

userName="mysqladmin"
passwd="Ermd32_-1a2b3c"

schema="populate.sql"
sqlfile="data.sql"
doClean=0
verbose=0
tmpFile="/tmp/dblog$$.log"

chkEnv()
{
(az --help && mysql --help) > /dev/null 2>&1
if [ $? -gt 0 ]; then
    echo "Error: Required exes have not been located"
    return 1
fi
return 0
}

rmFile()
{
(/bin/rm -f "$1") > /dev/null 2>&1
return 0
}

#
# Usage 
#
usage()
{
#

getDefaults

while [ $# -ne 0 ] ; do
        case $1 in
             -db | --database) dbname=$2
                 shift 2;;
             -m | --server) server=$2
                 shift 2;;   
             -s | --schema) schema=$2
                 shift 2;;        
             -f | --sql-file) sqlfile=$2
                 shift 2;;  
             -c | --clean-up) doClean=1
                 shift;;                   
             -v | --verbose) verbose=1
                 shift;;                   
             -g | --resource-group) resourcegrp=$2
                 shift 2;;  
             -u | --user) userName=$2
                 shift 2;;  
             -p | --pwd) passwd=$2
                 shift 2;;  
             --debug) set -xv ; shift;;
             -?*) show_usage ; break;;
             --) shift ; break;;
             -|*) break;;
        esac
done

if [ "x${resourcegrp}" = "x" ]; then
    echo "Error: Resource group must be specified"  
    return 1
fi

if [ ! -f "${sqlfile}" -o ! -f "${schema}" ]; then
    echo "Error: Referenced SQL file(s) were not found"  
    return 1
fi

rmFile "${tmpFile}"

return 0
}

getDefaults()
{
resourcegrp="`az config get defaults.group --only-show-errors | grep value | gawk '{ print $2; }'`"
#userName="`az account show --query 'user.name'`"
resourcegrp=`echo ${resourcegrp} | sed 's|"||g'`
#userName=`echo ${userName} | sed 's|"||g'`
}

dropDb()
{
rmFile "${tmpFile}"
echo "${command}: Dropping everything..."
(az mysql db delete --name "$1" -g "$2" --server-name "$3" --yes) > "${tmpFile}" 2>&1 
(az mysql server delete -g "$2" -n "$3" --yes) >> "${tmpFile}" 2>&1

if [ ${verbose} -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
fi

return 0
}

createServer()
{
rmFile "${tmpFile}"
echo "${command}: Creating a server..."
(az mysql server create -l "${location}" \
	-n "$1" -g "$2" \
	-u "$3" -p "$4" \
    --sku-name GP_Gen5_2) > "${tmpFile}" 2>&1 

if [ $? -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
    return 1
fi

if [ ${verbose} -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
fi

return 0
}

addIpAddress()
{
rmFile "${tmpFile}"
echo "${command}: Setup firewall-rule for server..."
#externalIp=`curl -s http://checkip.dyndns.org/ | sed 's/[a-zA-Z<>/ :]//g' | sed 's|\\r||g'`
externalIp="`dig +short myip.opendns.com @resolver1.opendns.com`"

(az mysql server firewall-rule create -g "$2" \
    --server "$1" --name AllowMyIP \
    --start-ip-address "${externalIp}" \
    --end-ip-address "${externalIp}") > "${tmpFile}" 2>&1 

if [ $? -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
    return 1
fi

if [ ${verbose} -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
fi


return 0
}

createdb()
{
rmFile "${tmpFile}"
echo "${command}: Creating a database..."
(az mysql db create -n "$1" \
    -g "$2" \
    -s "$3") > "${tmpFile}" 2>&1 

if [ $? -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
    return 1
fi

if [ ${verbose} -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
fi

return 0
}

createSchema()
{
rmFile "${tmpFile}"
echo "${command}: Defining schema..."

(mysql --database="$1" -h "${2}.mysql.database.azure.com" \
    -u "${3}@${2}" --password="$4" < "$5") > "${tmpFile}" 2>&1

if [ $? -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
    return 1
fi

if [ ${verbose} -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
fi

return 0
}


populatedb()
{
rmFile "${tmpFile}"
echo "${command}: Populating database..."

(mysql --database="$1" -h "${2}.mysql.database.azure.com" \
    -u "${3}@${2}" --password="$4" < "$5") > "${tmpFile}" 2>&1

if [ $? -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
    return 1
fi

if [ ${verbose} -gt 0 ]; then
    cat "${tmpFile}"
    rmFile "${tmpFile}"
fi

return 0
}

chkEnv
if [ $? -gt 0 ]; then
    exit 1
fi

usage $*
if [ $? -gt 0 ]; then
    exit 1
fi

dropDb "${dbname}" "${resourcegrp}" "${server}"

if [ $doClean -gt 0 ]; then
    exit 0
fi

createServer "${server}" "${resourcegrp}" "${userName}" "${passwd}"
if [ $? -gt 0 ]; then
    exit 1
fi

addIpAddress "${server}" "${resourcegrp}" "${userName}" "${passwd}"
if [ $? -gt 0 ]; then
    exit 1
fi

createdb "${dbname}" "${resourcegrp}" "${server}"
if [ $? -gt 0 ]; then
    exit 1
fi

createSchema "${dbname}" "${server}" "${userName}" "${passwd}" "${schema}"
if [ $? -gt 0 ]; then
    exit 1
fi

populatedb "${dbname}" "${server}" "${userName}" "${passwd}" "${sqlfile}"
if [ $? -gt 0 ]; then
    exit 1
fi

rmFile "${tmpFile}"

exit 0
