# To enable ssh & remote debugging on app service change the base image to the one below
# FROM mcr.microsoft.com/azure-functions/node:4-node18-appservice
FROM mcr.microsoft.com/azure-functions/node:4-node18

ARG SITE_HOME=/home/site/wwwroot

ENV AzureWebJobsScriptRoot=${SITE_HOME} \
    AzureFunctionsJobHost__Logging__Console__IsEnabled=true \
    AzureWebJobsFeatureFlags=EnableWorkerIndexing \
    AzureWebJobsStorage=false

RUN mkdir -p ${SITE_HOME}/
COPY host.json ${SITE_HOME}/
COPY package.json ${SITE_HOME}/
COPY ./github/ ${SITE_HOME}/github/
COPY ./funcversion/ ${SITE_HOME}/funcversion/

WORKDIR ${SITE_HOME}/ 
RUN npm install