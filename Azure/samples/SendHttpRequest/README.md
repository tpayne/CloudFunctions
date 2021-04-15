SendHttpRequest Sample
======================

This sample shows how to use Azure Cloud Functions to process a simple HTTP relay request (HTTP request).

The function call will support a GET request and a POST request.

Dependencies
------------
Before you attempt this example, please ensure you have done the following: -
- You have installed Maven (mvn) and the Azure client (az)
- You have logged into a (Unix) terminal window that will allow you to do deployments to a valid Azure account

Running Sample
--------------
To run the sample, please do the following...

    % mvn clean package azure-functions:run

Then in another console window, do...

    % curl "http://localhost:7071/api/SendHttpRequest/?targetURL=http://www.yahoo.com"
    <p>This is a GET/POST funtion call<br><br>Received code '400' from url 'http://www.yahoo.com'</p>
    % curl -X POST -d targetURL=https://www.google.com http://localhost:7071/api/SendHttpRequest
    <p>This is a GET/POST funtion call<br><br>Received code '200' from url 'https://www.google.com'</p>

Other functions, like a PUT will be rejected.

Deploying the Function to Azure
-------------------------------
If you wish to deploy the function to Azure, you can use the following...

    % mvn clean package azure-functions:deploy
        
You can then invoke it via...

    % curl "https://sendhttprequest-20210414130559868.azurewebsites.net/api/sendhttprequest/?targetURL=http://www.yahoo.com"
    <p>This is a GET/POST funtion call<br><br>Received code '400' from url 'http://www.yahoo.com'</p>
    % curl -X POST -d targetURL=https://www.google.com \
        https://sendhttprequest-20210414130559868.azurewebsites.net/api/sendhttprequest/
    <p>This is a GET/POST funtion call<br><br>Received code '200' from url 'https://www.google.com'</p>
    
To view errors or logs of the function, you can do...

    % func azure functionapp logstream sendhttprequest-20210414130559868

Cleaning Up
-----------
You can clean the function up using...

    % mvn clean
    % az functionapp delete -n "sendhttprequest-20210414130559868" -g "java-functions-group"

Issues
------
- On Mac, the JAVA_HOME needs to be faked for the azure-functions:run to work properly - known issue?


