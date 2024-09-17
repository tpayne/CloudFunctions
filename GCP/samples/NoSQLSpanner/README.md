NoSQLSpanner Sample
===================

This sample shows how to use GCP Cloud Functions to process a simple NoSQL DDL.

The function call will support a GET request and a POST request.

Dependencies
------------
Before you attempt this example, please ensure you have done the following: -
- You have installed Maven (mvn) and the GCP client (gcloud)
- You have logged into a (Unix) terminal window that will allow you to do deployments to a valid GCP account

Running Sample
--------------
To run the sample, please do the following.

First, we need to create the database. This is done using a shell script which will create and populate a Cloud
Spanner database with data.

    % ./dbsetup.sh 

Then run the cloud function.

    % mvn clean function:run -Drun.functionTarget=samples.gcp.functions.CloudSpannerHttp

Then in another console window, do...

    % curl "http://localhost:8080/?instanceName=testdb&dbName=testdb"
    <p>This is a GET function call<br><ul><li>Instance = 'testdb' </li><li>Database = 'testdb' </li></ul><br></p><br>Clients:
    Error querying database: UNAUTHENTICATED: com.google.api.gax.rpc.UnauthenticatedException: io.grpc.StatusRuntimeException: UNAUTHENTICATED: Request is missing required authentication credential. Expected OAuth 2 access token, login cookie or other valid authentication credential. See https://developers.google.com/identity/sign-in/web/devconsole-project.    
    % curl -X POST -d instanceName=testdb -d dbName=testdb localhost:8080
    <p>This is a POST function call<br><ul><li>Instance = 'testdb' </li><li>Database = 'testdb' </li></ul><br></p><br>Clients:
    Error querying database: UNAUTHENTICATED: com.google.api.gax.rpc.UnauthenticatedException: io.grpc.StatusRuntimeException: UNAUTHENTICATED: Request is missing required authentication credential. Expected OAuth 2 access token, login cookie or other valid authentication credential. See https://developers.google.com/identity/sign-in/web/devconsole-project.

Other functions, like a PUT will be rejected.

    % curl -X PUT localhost:8080
    <p>This call is not supported<br></p>

All the above functions will FAIL because of authentication errors. To get the function to work properly it needs to be deployed
to GCP.

Deploying the Function to GCP
-----------------------------
If you wish to deploy the function to GCP, you can use the following...

    % mvn clean package
    % gcloud functions deploy cloudspanner \
        --entry-point=samples.gcp.functions.CloudSpannerHttp \
        --runtime=java11 \
        --trigger-http \
        --source=target 
    Deploying function (may take a while - up to 2 minutes)...done.                                                
    availableMemoryMb: 256
    buildId: 34f79f46-9d00-4c2c-9ebc-77640d0c3a84
    entryPoint: samples.gcp.functions.CloudSpannerHttp
    httpsTrigger:
      securityLevel: SECURE_OPTIONAL
      url: https://us-central1-investdemo-300915.cloudfunctions.net/cloudspanner
    ingressSettings: ALLOW_ALL
    labels:
      deployment-tool: cli-gcloud
    name: projects/investdemo-300915/locations/us-central1/functions/cloudspanner
    runtime: java11
    status: ACTIVE
    timeout: 60s
    updateTime: '2021-04-09T12:38:46.416Z'
    versionId: '1'

If the role requires a binding role, then

    gcloud functions add-iam-policy-binding cloudspanner \
        --region=us-central1 --member=allUsers \
        --role=roles/cloudfunctions.invoker

If any errors occur during deployment, then you can debug them with...

    % gcloud functions logs read
    
You can then invoke it via...

    % curl "https://us-central1-investdemo-300915.cloudfunctions.net/cloudspanner/?instanceName=testdb&dbName=testdb" \
            -H "Authorization: bearer $(gcloud auth print-identity-token)"
    <p>This is a GET function call<br><ul><li>Instance = 'testdb' </li><li>Database = 'testdb' </li></ul><br></p><br>Clients:

    <br>0 NAME00 COMPANY00

    <br>1 NAME01 COMPANY01

    <br>2 NAME02 COMPANY02

    <br>3 NAME03 COMPANY03

    <br>4 NAME04 COMPANY04    
    % curl -X POST -d instanceName=testdb -d dbName=testdb  https://us-central1-investdemo-300915.cloudfunctions.net/cloudspanner/ \
            -H "Authorization: bearer $(gcloud auth print-identity-token)"  
    <p>This is a POST function call<br><ul><li>Instance = 'testdb' </li><li>Database = 'testdb' </li></ul><br></p><br>Clients:

    <br>0 NAME00 COMPANY00

    <br>1 NAME01 COMPANY01

    <br>2 NAME02 COMPANY02

    <br>3 NAME03 COMPANY03

    <br>4 NAME04 COMPANY04

Cleaning Up
-----------
You can clean the function up using...

    % mvn clean
    % gcloud functions delete cloudspanner
    % ./dbsetup.sh --clean-up

Notes
-----
Obviously, this function can have a lot done to it to tidy up the output, but that is left to the user to do.

For more information on authentication and Spanner, please see...
- https://cloud.google.com/functions/docs/securing/authenticating
- https://developers.google.com/identity/sign-in/web/sign-in
- https://cloud.google.com/sdk/gcloud/reference/spanner/databases/execute-sql
- https://cloud.google.com/spanner/docs/modify-gcloud
- https://cloud.google.com/sdk/gcloud/reference/spanner/databases/ddl/update

