#!/bin/sh
./scripts/CLEANME.sh;
./scripts/BUILDME.sh;
./scripts/RUNME.sh & \
(sleep 3 && ./scripts/VIEWME.sh);