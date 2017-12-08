#!/usr/bin/env bash
mvn -f ./nb-backend/ clean
npm install -g yarn
yarn install --cwd ./nb-frontend/
yarn add global @angular/cli --cwd ./nb-frontend/
yarn add global codecov --cwd ./nb-frontend/
