SendHttpRequest Sample
======================

This sample shows how to use GCP Cloud Functions to process a simple HTTP relay request (HTTP request).

The function call will support a GET request and a POST request.

Dependencies
------------
Before you attempt this example, please ensure you have done the following: -
- You have installed Maven (mvn) and the GCP client (gcloud)
- You have logged into a (Unix) terminal window that will allow you to do deployments to a valid GCP account

Running Sample
--------------
To run the sample, please do the following...

    % mvn clean function:run -Drun.functionTarget=samples.gcp.functions.SendHttpRequest

Then in another console window, do...

    % curl "http://localhost:8080/"
    <p>This is a GET function call<br><br>Received code '400' from url 'http://www.yahoo.com'</p>
    % curl -X POST -d targetURL=https://www.google.com localhost:8080
    <p>This is a POST function call<br><li>Field: 'targetURL' -> Value: 'https://www.google.com'</li></ul><br></p><br>Received code '200' from url 'https://www.google.com'</p>

Other functions, like a PUT will be rejected.

    % curl -X PUT localhost:8080
    <p>This call is not supported<br></p>

Deploying the Function to GCP
-----------------------------
If you wish to deploy the function to GCP, you can use the following...

    % mvn clean package
    % gcloud functions deploy sendhttprequest \
        --entry-point=samples.gcp.functions.SendHttpRequest \
        --runtime=java11 \
        --trigger-http \
        --source=target
    Deploying function (may take a while - up to 2 minutes)...done.                                                
    availableMemoryMb: 256
    buildId: 0dfc1254-837c-471c-89fa-8833ae94da98
    entryPoint: samples.gcp.functions.SendHttpRequest
    httpsTrigger:
      securityLevel: SECURE_OPTIONAL
      url: https://us-central1-investdemo-300915.cloudfunctions.net/sendhttprequest
    ingressSettings: ALLOW_ALL
    labels:
      deployment-tool: cli-gcloud
    name: projects/investdemo-300915/locations/us-central1/functions/sendhttprequest
    runtime: java11
    status: ACTIVE
    timeout: 60s
    updateTime: '2021-04-09T12:38:46.416Z'
    versionId: '1'

If any errors occur during deployment, then you can debug them with...

    % gcloud functions logs read
    
You can then invoke it via...

    % curl "https://us-central1-investdemo-300915.cloudfunctions.net/sendhttprequest"
    <p>This is a GET function call<br><br>Received code '400' from url 'http://www.yahoo.com'</p>
    % curl -X POST -d targetURL=https://www.google.com https://us-central1-investdemo-300915.cloudfunctions.net/sendhttprequest
    <p>This is a POST function call<br><li>Field: 'targetURL' -> Value: 'https://www.google.com'</li></ul><br></p><br>Received code '200' from url 'https://www.google.com'</p>

Cleaning Up
-----------
You can clean the function up using...

    % mvn clean
    % gcloud functions delete sendhttprequest


