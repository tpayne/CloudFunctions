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

    % mvn clean function:run \
        -Drun.functionTarget=samples.gcp.functions.SendHttpRequest

Then in another console window, do...

    % curl "http://localhost:8080/"
    <p>This is a GET function call<br><br>Received code '400' from url 'https://www.yahoo.com'</p>
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
        --source=target \
        --allow-unauthenticated 
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

If the role requires a binding role, then

    gcloud functions add-iam-policy-binding sendhttprequest \
        --region=us-central1 --member=allUsers \
        --role=roles/cloudfunctions.invoker

If any errors occur during deployment, then you can debug them with...

    % gcloud functions logs read
    
You can then invoke it via...

    % curl "https://us-central1-investdemo-300915.cloudfunctions.net/sendhttprequest"
    <p>This is a GET function call<br><br>Received code '400' from url 'https://www.yahoo.com'</p>
    % curl -X POST -d targetURL=https://www.google.com https://us-central1-investdemo-300915.cloudfunctions.net/sendhttprequest
    <p>This is a POST function call<br><li>Field: 'targetURL' -> Value: 'https://www.google.com'</li></ul><br></p><br>Received code '200' from url 'https://www.google.com'</p>

API Gateway Deployment
----------------------
If you want to put the function behind an API gateway, then please do the following.

Enable the API gateway services (if needed)

    % gcloud services enable apigateway.googleapis.com
    % gcloud services enable servicemanagement.googleapis.com
    % gcloud services enable servicecontrol.googleapis.com
    % gcloud services list

Create your API definition

    % gcloud api-gateway apis create sendhttprequest \
        --project=$(gcloud config get-value project)
    % gcloud api-gateway apis describe sendhttprequest
    createTime: '2021-04-13T13:20:19.745554655Z'
    displayName: sendhttprequest
    managedService: sendhttprequest-0lc5wdz0ordgs.apigateway.investdemo-300915.cloud.goog
    name: projects/investdemo-300915/locations/global/apis/sendhttprequest
    state: ACTIVE
    updateTime: '2021-04-13T13:21:58.456459123Z'

Open the file `sendhttprequest.yaml` and edit values as appropriate (like the `x-google-backend`)

    % nano sendhttprequest.yaml

Check you have a service account you can use for the API gateway. If not, then create one using the 

    % gcloud iam service-accounts list
    % # Create an account via...
    % gcloud iam service-accounts sendhttprequest \
                add-iam-policy-binding \
                --member user:<USER_EMAIL> \
                -role roles/iam.serviceAccountUser

Grant them access to the API services

    % gcloud api-gateway api-configs create sendhttprequest \
        --api=sendhttprequest --openapi-spec=sendhttprequest.yaml \
        --project=$(gcloud config get-value project) \
        --backend-auth-service-account=SERVICE_ACCOUNT_EMAIL
    % gcloud api-gateway api-configs describe sendhttprequest \
        --api=sendhttprequest  --project=$(gcloud config get-value project) 
    createTime: '2021-04-13T13:52:43.323424596Z'
    displayName: sendhttprequest
    gatewayServiceAccount: projects/-/serviceAccounts/test-142@investdemo-300915.iam.gserviceaccount.com
    name: projects/127131513455/locations/global/apis/sendhttprequest/configs/sendhttprequest
    serviceConfigId: sendhttprequest-0jbz7zq3qxy8h
    state: ACTIVE
    updateTime: '2021-04-13T13:55:02.947184753Z'

Create the API gateway

    % gcloud api-gateway gateways create httpgateway \
        --api=sendhttprequest --api-config=sendhttprequest \
        --location=us-central1 \
        --project=$(gcloud config get-value project)
    % gcloud api-gateway gateways describe httpgateway \
        --location=us-central1 \
        --project=$(gcloud config get-value project)
    apiConfig: projects/127131513455/locations/global/apis/sendhttprequest/configs/sendhttprequest
    createTime: '2021-04-13T14:12:43.780099885Z'
    defaultHostname: httpgateway-1meitjlb.uc.gateway.dev
    displayName: httpgateway
    name: projects/investdemo-300915/locations/us-central1/gateways/httpgateway
    state: ACTIVE
    updateTime: '2021-04-13T14:14:22.877756197Z'

Testing the call

    % curl -X POST -d targetURL=https://www.google.com https://httpgateway-1meitjlb.uc.gateway.dev/sendhttprequestpost/ 
    <p>This is a POST function call<br><li>Field: 'targetURL' -> Value: 'https://www.google.com'</li></ul><br></p><br>Received code '200' from url 'https://www.google.com'</p>
    % curl https://httpgateway-1meitjlb.uc.gateway.dev/sendhttprequestget/
    <p>This is a GET function call<br><br>Received code '400' from url 'https://www.yahoo.com'</p

Cleaning Up
-----------
You can clean the function up using...

    % gcloud api-gateway gateways delete httpgateway \
        --location=us-central1 \
        --project=$(gcloud config get-value project)
    % gcloud api-gateway api-configs delete sendhttprequest \
        --api=sendhttprequest  --project=$(gcloud config get-value project) 
    % gcloud api-gateway apis delete sendhttprequest
    
    % gcloud services disable apigateway.googleapis.com --force && \
        gcloud services disable servicemanagement.googleapis.com --force && \
        gcloud services disable servicecontrol.googleapis.com --force

    % mvn clean
    % gcloud functions delete sendhttprequest

Notes
-----
You can get details on the various API gateway and OpenAPI spec(s) here...
- https://cloud.google.com/api-gateway/docs/about-api-gateway
- https://swagger.io/docs/specification/2-0/describing-parameters/
- https://cloud.google.com/api-gateway/docs/configure-dev-env#configuring_a_service_account
- https://cloud.google.com/api-gateway/docs/quickstart
- https://cloud.google.com/api-gateway/docs/get-started-cloud-functions

