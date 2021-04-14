CloudFunctions Examples Repo
============================

This repo contains various examples of Cloud Functions.

Status
------
````
Cloud Functions Status: Ready for use
````
Build CI/Testing Status
-----------------------
The following indicates the CI and coverage status.

[![Build Status](https://travis-ci.org/tpayne/CloudFunctions.svg?branch=main)](https://travis-ci.org/tpayne/CloudFunctions)

Azure Cloud Function Examples
-----------------------------
The examples contained in this repo run on Maven and Azure. They are based in the following directories.

>| Project | Description | 
>| ------- | ----------- |
>| [samples/Azure/SimpleHttpFunction/](https://github.com/tpayne/CloudFunctions/tree/main/Azure/samples/SimpleHttpFunction) | This sample will use Maven to build simple HTTP functions |

GCP Cloud Function Examples
---------------------------
The examples contained in this repo run on Maven and GCP. They are based in the following directories.

>| Project | Description | 
>| ------- | ----------- |
>| [samples/GCP/AdvancedHttpFunction/](https://github.com/tpayne/CloudFunctions/tree/main/GCP/samples/AdvancedHttpFunction) | This sample will use Maven to build advance HTTP functions |
>| [samples/GCP/NoSQLSpanner/](https://github.com/tpayne/CloudFunctions/tree/main/GCP/samples/NoSQLSpanner) | This sample will use Maven to build NoSQL query functions using Spanner |
>| [samples/GCP/SimpleHttpFunction/](https://github.com/tpayne/CloudFunctions/tree/main/GCP/samples/SimpleHttpFunction) | This sample will use Maven to build simple HTTP functions |
>| [samples/GCP/SendHttpRequest/](https://github.com/tpayne/CloudFunctions/tree/main/GCP/samples/SendHttpRequest) | This sample will use Maven to build HTTP relay functions. `This also includes an example on how to deploy an API Gateway` |

Notes
-----
The following notes may be useful.

`Azure`
- https://docs.microsoft.com/en-us/azure/azure-functions/create-first-function-cli-java?tabs=bash%2Cazure-cli%2Cbrowser
- https://docs.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=macos%2Ccsharp%2Cbash#v2
- https://docs.microsoft.com/en-us/java/api/com.microsoft.azure.functions?view=azure-java-stable

`Google Cloud Platform`
- https://cloud.google.com/functions/
- https://cloud.google.com/functions/docs/quickstarts
- https://cloud.google.com/functions/docs/how-to
- https://cloud.google.com/functions/docs/tutorials
- https://cloud.google.com/functions/docs/apis
- https://cloud.google.com/functions/docs/monitoring/logging#viewing_logs
