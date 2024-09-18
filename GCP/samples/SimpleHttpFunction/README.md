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

    % mvn clean function:run \
        -Drun.functionTarget=samples.gcp.functions.HttpMethod

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
        --source=target \
        --allow-unauthenticated 
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
    status: ACTIVE
    timeout: 60s
    updateTime: '2021-04-08T14:03:48.689Z'
    versionId: '1'

If the role requires a binding role, then

    gcloud functions add-iam-policy-binding simplehttpfunction \
        --region=us-central1 --member=allUsers \
        --role=roles/cloudfunctions.invoker

If any errors occur during deployment, then you can debug them with...

    % gcloud functions logs read
    
You can then invoke it via...

    % curl "https://us-central1-investdemo-300915.cloudfunctions.net/simplehttpfunction/?username=testuserfred&userpriv=normal"
    <p>This is a GET funtion call<br>User name is 'testuserfred' <br>User privelege is 'normal' <br></p>

Cleaning Up
-----------
You can clean the function up using...

    % mvn clean
    % gcloud functions delete simplehttpfunction


