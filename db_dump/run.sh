#!/usr/bin/env bash
echo `date`
cd `dirname $0`

set -e;
set -a; . ../.env; set +a;

## REFRESH_DATE should be kept 2012-11-01 for the first import
export DATABASE=md
export import_start_date=2012-11-01
export PUBLIC_PATH=/opt/opencontracting-moldova/public

#cd /opt/opencontracting-moldova/db_dump

cd etender2mongoscripts

#python pulldata.py
#python dumpdata.py
#python pulltenderitems.py
#python dumptenderitems.py
cd ..
#mongo localhost:27017/$DATABASE mongojsscripts/map_to_ocds.js
#mongo localhost:27017/$DATABASE mongojsscripts/change_contracts_date.js
set +e;
#mongo localhost:27017/$DATABASE mongojsscripts/rename.js --eval "var DATABASE='$DATABASE'"
#mongo localhost:27017/$DATABASE mongojsscripts/change_contracts_date.js

echo creating csv files
sh ./createCsv.sh

cd mongojsscripts
#
#mongo localhost:27017/$DATABASE change_type_of_ocds_release_date.js
echo generating json files
sh ./generate_json_file.sh

cd ..

cd linkage

#python blacklist_script.py
#python contractors_script.py
#python courtcases_script.py
#python ocds_script.py

cd ..
#assesment of data

cd assessmentscripts

#mongo localhost:27017/$DATABASE prepare.js
#mongo localhost:27017/$DATABASE --quiet summarize.js > $PUBLIC_PATH/csv/assessment.csv

cd ..

cd company/mongoscripts
#mongo localhost:27017/$DATABASE run_for_etenders.js
cd ../../

sed -i.bak '/REFRESH_DATE/d' ././../.env
echo "REFRESH_DATE=$(date +%F)" >> ././../.env
echo `date`
