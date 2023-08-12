# Python Azure Function Example

This repo demonstrates how to use Python Azure functions within a container app.

The repo provides the following: -

* A Dockerfile to build the Python sample
* A HTTP Trigger Python sample that works with the GitHub API
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


