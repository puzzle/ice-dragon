#!/bin/bash

echo "You are currently on project:"
echo "-------------"
oc project --short
echo "-------------"

echo "Are you sure you want to apply all resources? Press <Enter> to do so. Or press <Ctrl>+<C> to abort"
read DUMMY

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}" )" && pwd)"

TEMPLATE="${DIR}/template.yml"
CONFIG="${DIR}/dev.yml"

cat ${TEMPLATE} \
  | oc process -f - --param-file ${CONFIG} \
  | oc apply -f -
