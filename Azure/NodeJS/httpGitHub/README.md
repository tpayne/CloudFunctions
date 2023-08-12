# NodeJS Azure Function Example

This repo demonstrates how to use NodeJS Azure functions within a container app.

The repo provides the following: -

* A Dockerfile to build the NodeJS sample
* A HTTP Trigger NodeJS sample that works with the GitHub API
* Associated documentation

## Pre-requists
To run the functions, you will need to have the following

* Access to Azure with the ability to deploy functions

## Running the sample locally
To build and run the sample locally, you need to do the following

Build the application and run it using Docker.

```console
docker build --platform linux/amd64 . \
  -t azfuncpytest:v1 && \
docker run --rm -t -p 3000:80 -t azfuncpytest:v1
```

Once the Docker image is running, you can invoke the functions provided by it using commands.

The `api/repo/list?username=<username>` will dump all the repos owned by a user (public).

```console
curl http://localhost:3000/api/repo/list?username=tpayne
curl http://localhost:3000/api/version
```

## Deploying the sample to Azure
To deploy the sample to Azure, you have various options

* Use the Azure Portal
* Use Terraform
* Use Azure CLI

This repo provides a sample Terraform module for deploying the app along with other required resources. This can be found in the `Terraform/` directory.

## Notes
The following are useful references when developing functions

* https://learn.microsoft.com/en-us/azure/azure-functions/functions-reference-node?tabs=javascript%2Cwindows%2Cazure-cli&pivots=nodejs-model-v3
* https://learn.microsoft.com/en-us/azure/azure-functions/functions-reference-node?source=recommendations&tabs=javascript%2Cwindows%2Cazure-cli&pivots=nodejs-model-v3
* https://learn.microsoft.com/en-us/training/modules/create-serverless-logic-with-azure-functions/1-introduction
* https://github.com/Azure/azure-functions-core-tools/issues/3275
* https://learn.microsoft.com/en-us/azure/azure-functions/functions-how-to-custom-container?tabs=core-tools%2Cacr%2Cazure-cli&pivots=azure-functions#create-supporting-azure-resources-for-your-function
* https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=macos%2Cportal%2Cv2%2Cbash&pivots=programming-language-csharp
* https://learn.microsoft.com/en-us/azure/azure-functions/functions-bindings-http-webhook-trigger?tabs=python-v2%2Cin-process%2Cfunctionsv2&pivots=programming-language-javascript
* https://github.com/yvele/azure-function-express
* https://learn.microsoft.com/en-us/samples/azure-samples/js-e2e-azure-function-graphql-hello/graphql-typescript-hello-world/
* https://learn.microsoft.com/en-us/samples/browse/?languages=javascript%2Cnodejs&products=azure-functions
* https://learn.microsoft.com/en-us/azure/developer/javascript/how-to/develop-serverless-apps?tabs=v3-js
* https://learn.microsoft.com/en-us/samples/browse/?products=azure-functions&languages=python
* https://learn.microsoft.com/en-us/azure/azure-functions/functions-how-to-custom-container?tabs=core-tools%2Cacr%2Cazure-cli&pivots=azure-functions

