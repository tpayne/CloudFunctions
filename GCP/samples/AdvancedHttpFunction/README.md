AdvancedHttpFunction Sample
===========================

This sample shows how to use GCP Cloud Functions to process a complex HTTP call that supports JSON, GET and POST requests.

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
    % curl -X POST -d key1=value1 -d key2=value2 localhost:8080
    <p>This is a POST function call<br><ul><li>Field: 'key1' -> Value: 'value1'</li><li>Field: 'key2' -> Value: 'value2'</li></ul><br></p>
    % curl -H "Content-Type: application/json" \
        --request POST \
        --data '{"username":"testuserfred","userpriv":"delta-system"}' localhost:8080
    <p>This is a POST funtion call<br>User name is 'testuserfred' <br>User privelege is 'delta-system' <br></p>

Other functions, like a PUT will be rejected.

    % curl -X PUT localhost:8080
    <p>This call is not supported<br></p>

Deploying the Function to GCP
-----------------------------
If you wish to deploy the function to GCP, you can use the following...

    % mvn clean package
    % gcloud functions deploy advancedhttpfunction \
        --entry-point=samples.gcp.functions.HttpMethod \
        --runtime=java11 \
        --trigger-http \
        --source=target
    Deploying function (may take a while - up to 2 minutes)...done.                                              
    availableMemoryMb: 256
    buildId: 999a237f-de4c-4569-b38f-69471ef804cc
    entryPoint: samples.gcp.functions.HttpMethod
    httpsTrigger:
      securityLevel: SECURE_OPTIONAL
      url: https://us-central1-investdemo-300915.cloudfunctions.net/advancedhttpfunction
    ingressSettings: ALLOW_ALL
    labels:
      deployment-tool: cli-gcloud
    name: projects/investdemo-300915/locations/us-central1/functions/advancedhttpfunction
    runtime: java11
    status: ACTIVE
    timeout: 60s
    updateTime: '2021-04-08T17:43:03.855Z'
    versionId: '4'

If any errors occur during deployment, then you can debug them with...

    % gcloud functions logs read

You can then invoke it via...

    % curl "https://us-central1-investdemo-300915.cloudfunctions.net/advancedhttpfunction/?username=testuserfred&userpriv=normal"
    <p>This is a GET funtion call<br>User name is 'testuserfred' <br>User privelege is 'normal' <br></p>
    % curl -X POST -d key1=value1 -d key2=value2 https://us-central1-investdemo-300915.cloudfunctions.net/advancedhttpfunction/
    <p>This is a POST function call<br><ul><li>Field: 'key1' -> Value: 'value1'</li><li>Field: 'key2' -> Value: 'value2'</li></ul><br></p>
    % curl -H "Content-Type: application/json" \
        --request POST \
        --data '{"username":"testuserfred","userpriv":"delta-system"}' \
        https://us-central1-investdemo-300915.cloudfunctions.net/advancedhttpfunction/
    <p>This is a POST funtion call<br>User name is 'testuserfred' <br>User privelege is 'delta-system' <br></p>

Cleaning Up
-----------
You can clean the function up using...

    % mvn clean
    % gcloud functions delete advancedhttpfunction


