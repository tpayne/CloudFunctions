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
    Deploying function (may take a while - up to 2 minutes)...⠹                                                  
    For Cloud Build Stackdriver Logs, visit: https://console.cloud.google.com/logs/viewer?project=investdemo-300915&advancedFilter=resource.type%3Dbuild%0Aresource.labels.build_id%3D999a237f-de4c-4569-b38f-69471ef804cc%0AlogName%3Dprojects%2Finvestdemo-300915%2Flogs%2Fcloudbuild
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
    serviceAccountEmail: investdemo-300915@appspot.gserviceaccount.com
    sourceUploadUrl: https://storage.googleapis.com/gcf-upload-us-central1-e5e3084a-5b2f-4b02-b063-ce7668f0beb1/df74b0ba-2c0b-4a08-8e8c-e996fbb3ba5e.zip?GoogleAccessId=service-127131513455@gcf-admin-robot.iam.gserviceaccount.com&Expires=1617905469&Signature=iYv1exjLkTrt3ifbVtUjjkCccsIbAmFRJTK99XsJhbuTlyQvp%2BtrmdRg5a9RHMipSYHAb%2B3OFAwH9QbOLniXVJpHo7HbPsHMEaJDR48lCk5mWTd8qOYZjoNAdHx7vh4qMhTiqWoaA7SMYP5Un7oLeY4t2s32%2F1JiU4Ilo%2BW4tms8bs04zcu%2F3GGxhj%2ByFfKqK2yEFHVwh1FNXXqh%2Bk%2FgqBEr7N53F4A2xezKO7IPeNjPzzhq4xFX19cAjldr%2BZTE7mwB7eixEDX6cb3nXgl%2F0C1%2FVMkPsoUdMYPHyc%2Bmk07hZWCkBMvambHT4m9z%2BPRrKA29ottSGM1y%2BUMqf2oOzA%3D%3D
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


