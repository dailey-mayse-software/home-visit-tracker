#!/bin/bash

sqlite3 development.db < rbac.ddl
sqlite3 development.db < application.ddl
sqlite3 development.db < init.sql
