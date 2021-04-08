SimpleHttpFunction Sample
=========================

This sample shows how to use GCP Cloud Functions to process a simple HTTP call.

The function call will support a GET request with two parameters.

Dependencies
------------
Before you attempt this example, please ensure you have done the following: -
- You have installed Maven (mvn) and the GCP client (gcloud)
- You have logged into a (Unix) terminal window that will allow you to do deployments to a valid GCP account

Running Sample
--------------
To run the sample, please do the following...

    % mvn clean function:run -Drun.functionTarget=samples.gcp.functions.HttpMethod

Then in another console window, do...

    % curl "http://localhost:8080/?username=testuserfred&userpriv=normal-user"
    <p>This is a GET funtion call<br>User name is 'testuserfred' <br>User privelege is 'normal-user' <br></p>

The function will parse the parameters "username" and "userpriv" then display their values with HTTP wrapping.

Other functions, like a PUT or POST will be rejected.

    % curl -X PUT localhost:8080
    <p>This is a PUT funtion call which is not allowed<br></p>
    % curl -X POST -d value=value localhost:8080
    <p>This call is not supported<br></p>

Deploying the Function to GCP
-----------------------------
If you wish to deploy the function to GCP, you can use the following...

    % mvn clean package
    % gcloud functions deploy simplehttpfunction \
        --entry-point=samples.gcp.functions.HttpMethod \
        --runtime=java11 \
        --trigger-http \
        --source=target
    Deploying function (may take a while - up to 2 minutes)...⠹                                                  
    For Cloud Build Stackdriver Logs, visit: https://console.cloud.google.com/logs/viewer?project=investdemo-300915&advancedFilter=resource.type%3Dbuild%0Aresource.labels.build_id%3D12f816ee-8eac-4201-9d0d-c085b224ef3d%0AlogName%3Dprojects%2Finvestdemo-300915%2Flogs%2Fcloudbuild
    Deploying function (may take a while - up to 2 minutes)...done.                                              
    availableMemoryMb: 256
    buildId: 12f816ee-8eac-4201-9d0d-c085b224ef3d
    entryPoint: samples.gcp.functions.HttpMethod
    httpsTrigger:
      securityLevel: SECURE_OPTIONAL
      url: https://us-central1-investdemo-300915.cloudfunctions.net/simplehttpfunction
    ingressSettings: ALLOW_ALL
    labels:
      deployment-tool: cli-gcloud
    name: projects/investdemo-300915/locations/us-central1/functions/simplehttpfunction
    runtime: java11
    serviceAccountEmail: investdemo-300915@appspot.gserviceaccount.com
    sourceUploadUrl: https://storage.googleapis.com/gcf-upload-us-central1-e5e3084a-5b2f-4b02-b063-ce7668f0beb1/d0e400a3-7071-4340-b1fe-80abfde58fa3.zip?GoogleAccessId=service-127131513455@gcf-admin-robot.iam.gserviceaccount.com&Expires=1617892352&Signature=ZikZYAq%2BldK%2FAS1lSe%2Fs51JjiaVRvqYC1uad87nh8xrLJNn8kdB5WZKa8b%2F1XjyGZucnxmGFV%2BndliiLJzoxbdsy7RTjKhOVSfB2QMxIQxNOpbMY5VoJQIHW6CzjEB%2FDq0MjTohrJ6wpu8DIQmugbcEfjaBDflTTrwg%2Bk6z36DFSUg03zWMZjhi9h0VDWB3AdeyTSgdzTyEOqMhISJYOwazf8%2FYKdwWQEO7STcouWEM3W9pSp7NT9JyXKcK7p1aINrrsiWT64iJwk9n3OV7iwyCXK0fB10E%2FV23y3V0jqLrDOUIhIawCAchJNi6RfTMQQbjX1u0UDs%2B%2BbFqvSMFGAA%3D%3D
    status: ACTIVE
    timeout: 60s
    updateTime: '2021-04-08T14:03:48.689Z'
    versionId: '1'

You can then invoke it via...

    % curl "https://us-central1-investdemo-300915.cloudfunctions.net/simplehttpfunction/?username=testuserfred&userpriv=normal"
    <p>This is a GET funtion call<br>User name is 'testuserfred' <br>User privelege is 'normal' <br></p>

Cleaning Up
-----------
You can clean the function up using...

    % mvn clean
    % gcloud functions delete simplehttpfunction


