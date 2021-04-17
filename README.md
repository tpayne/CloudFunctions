CloudFunctions Examples Repo
============================

This repo contains various examples of Cloud Functions. These functions are NOT intended for production use, but rather to show how to construct them.

For things like getting return data and passing security related information, these functions are not good examples to use.

For passing security related information, it is best to use [`Authentication header`](https://en.wikipedia.org/wiki/Basic_access_authentication) and HTTPS. A example implementation can be found [here](https://stackoverflow.com/questions/3283234/http-basic-authentication-in-java-using-httpclient) and [here](https://www.digizol.com/2012/06/http-basic-authentication-java-decode.html).

For getting return data, it is best to convert the information to JSON and then return a JSON string to the caller for processing into objects. More information can be found [here](https://www.javatpoint.com/convert-java-object-to-json#:~:text=These%20are%20the%20following%20steps%20to%20convert%20a,class%20to%20convert%20the%20Java%20object%20to%20JSON.).

Implementation examples of these are however beyond the scope of these demos and are left to the reader to source.

Status
------
````
Cloud Functions Status: Ready for use
````
Build CI/Testing Status
-----------------------
The following indicates the CI and coverage status.

[![Build Status](https://travis-ci.org/tpayne/CloudFunctions.svg?branch=main)](https://travis-ci.org/tpayne/CloudFunctions)
[![Java CI with Maven](https://github.com/tpayne/CloudFunctions/actions/workflows/maven.yml/badge.svg)](https://github.com/tpayne/CloudFunctions/actions/workflows/maven.yml)

Azure Cloud Function Examples
-----------------------------
The examples contained in this repo run on Maven and Azure. They are based in the following directories.

>| Project | Description | 
>| ------- | ----------- |
>| [samples/Azure/AdvancedHttpFunction/](https://github.com/tpayne/CloudFunctions/tree/main/Azure/samples/AdvancedHttpFunction) | This sample will use Maven to build advance HTTP functions |
>| [samples/Azure/MySQLFunction/](https://github.com/tpayne/CloudFunctions/tree/main/Azure/samples/MySQLFunction) | This sample will use Maven to build MySQL HTTP functions |
>| [samples/Azure/SendHttpRequest/](https://github.com/tpayne/CloudFunctions/tree/main/Azure/samples/SendHttpRequest) | This sample will use Maven to build HTTP relay functions |
>| [samples/Azure/SimpleHttpFunction/](https://github.com/tpayne/CloudFunctions/tree/main/Azure/samples/SimpleHttpFunction) | This sample will use Maven to build simple HTTP functions |

GCP Cloud Function Examples
---------------------------
The examples contained in this repo run on Maven and GCP. They are based in the following directories.

>| Project | Description | 
>| ------- | ----------- |
>| [samples/GCP/AdvancedHttpFunction/](https://github.com/tpayne/CloudFunctions/tree/main/GCP/samples/AdvancedHttpFunction) | This sample will use Maven to build advance HTTP functions |
>| [samples/GCP/NoSQLSpanner/](https://github.com/tpayne/CloudFunctions/tree/main/GCP/samples/NoSQLSpanner) | This sample will use Maven to build NoSQL query functions using Spanner |
>| [samples/GCP/SendHttpRequest/](https://github.com/tpayne/CloudFunctions/tree/main/GCP/samples/SendHttpRequest) | This sample will use Maven to build HTTP relay functions. `This also includes an example on how to deploy an API Gateway` |
>| [samples/GCP/SimpleHttpFunction/](https://github.com/tpayne/CloudFunctions/tree/main/GCP/samples/SimpleHttpFunction) | This sample will use Maven to build simple HTTP functions |

Notes
-----
The following notes may be useful.

`Azure`
- https://docs.microsoft.com/en-us/azure/azure-functions/create-first-function-cli-java?tabs=bash%2Cazure-cli%2Cbrowser
- https://docs.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=macos%2Ccsharp%2Cbash#v2
- https://docs.microsoft.com/en-us/java/api/com.microsoft.azure.functions?view=azure-java-stable
- https://docs.microsoft.com/en-us/azure/azure-functions/
- https://docs.microsoft.com/en-us/azure/azure-functions/functions-reference-java?tabs=bash%2Cconsumption

`Google Cloud Platform`
- https://cloud.google.com/functions/
- https://cloud.google.com/functions/docs/quickstarts
- https://cloud.google.com/functions/docs/how-to
- https://cloud.google.com/functions/docs/tutorials
- https://cloud.google.com/functions/docs/apis
- https://cloud.google.com/functions/docs/monitoring/logging#viewing_logs

Issues
------
- On Mac Azure needs the JAVA_HOME faked when running functions locally
- Object data is being returned as pseudo HTML constructed in the cloud functions. This is NOT the way to do it for real. You should convert the object data to JSON and then return this string to the calling client. However, this is beyond the scope of these demos. Although, I may add one to show how to do it in the future.
