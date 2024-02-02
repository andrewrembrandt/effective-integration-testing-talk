#!/usr/bin/env bash

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"

mkdir -p "$dir/resources/postgres-data"

export userid=$(id -u $USER)
export groupid=$(id -g $USER)

dc_cfg="$dir/resources/postgresql.yml"

if [ -z $(docker-compose -f "$dc_cfg" ps -q postgres) ] || [ -z $(docker ps -q --no-trunc | grep $(docker-compose -f "$dc_cfg" ps -q postgres)) ]; then
  docker-compose -f "$dc_cfg" up -d
else
  echo "postgresql container already running"
fi
