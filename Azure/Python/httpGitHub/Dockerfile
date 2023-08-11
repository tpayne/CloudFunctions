# To enable ssh & remote debugging on app service change the base image to the one below
# FROM mcr.microsoft.com/azure-functions/python:4-python3.9-appservice
FROM mcr.microsoft.com/azure-functions/python:4-python3.10

ARG SITE_HOME=/home/site/wwwroot

ENV AzureWebJobsScriptRoot=${SITE_HOME} \
    AzureFunctionsJobHost__Logging__Console__IsEnabled=true \
    AzureWebJobsFeatureFlags=EnableWorkerIndexing \
    AzureWebJobsStorage=UseDevelopmentStorage=true

COPY requirements.txt /

RUN mkdir -p ${SITE_HOME}
RUN pip install -r /requirements.txt

COPY host.json ${SITE_HOME}/
COPY *.py ${SITE_HOME}/