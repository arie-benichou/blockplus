#!/bin/sh

./scripts/CLEANFORME.sh;
./scripts/BUILDME.sh;
./scripts/RUNME.sh & \
(sleep 10 && ./scripts/VIEWME.sh);