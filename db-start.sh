#!/bin/sh
pg_ctl -D pg stop
sudo service postgresql stop
sudo chmod a+w /var/run/postgresql/
postgres -D pg &
