AdvancedHttpFunction Sample
===========================

This sample shows how to use Azure Cloud Functions to process a complex HTTP call that supports JSON, GET and POST requests.

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

    % curl "http://localhost:7071/api/HttpMethod/?username=testuserfred&userpriv=normal-user"
    <p>This is a GET/POST TEXT funtion call<br>User name is 'testuserfred' <br>User privelege is 'normal-user' <br></p>
    % curl -X POST -d username=testuserfred -d userpriv=normal-user http://localhost:7071/api/HttpMethod/
    <p>This is a GET/POST TEXT funtion call<br>User name is 'testuserfred' <br>User privelege is 'normal-user' <br></p>
    % curl -H "Content-Type: application/json" \
        --request POST \
        --data '{"username":"testuserfred","userpriv":"delta-system"}' "http://localhost:7071/api/HttpMethod/"
    <p>This is a GET/POST JSON funtion call<br>User name is 'testuserfred' <br>User privelege is 'delta-system' <br></p>
       
The function will parse the parameters "username" and "userpriv" then display their values with HTTP wrapping.

Other functions, like a PUT will be rejected and not do anything.

Deploying the Function to Azure
-------------------------------
If you wish to deploy the function to Azure, you can use the following...

    % mvn clean package azure-functions:deploy
    
You can then invoke it via...

    % curl "https://advancedhttpfunction-20210414130559868.azurewebsites.net/api/httpmethod/?username=testuserfred&userpriv=normal"
    <p>This is a GET/POST TEXT funtion call<br>User name is 'testuserfred' <br>User privelege is 'normal' <br></p>
    % curl -X POST -d username=testuserfred -d userpriv=normal-user \
        https://advancedhttpfunction-20210414130559868.azurewebsites.net/api/httpmethod/
    <p>This is a GET/POST TEXT funtion call<br>User name is 'testuserfred' <br>User privelege is 'normal-user' <br></p>
    % curl -H "Content-Type: application/json" \
        --request POST \
        --data '{"username":"testuserfred","userpriv":"delta-system"}' \
        "https://advancedhttpfunction-20210414130559868.azurewebsites.net/api/httpmethod/"
    <p>This is a GET/POST JSON funtion call<br>User name is 'testuserfred' <br>User privelege is 'delta-system' <br></p>

To view errors or logs of the function, you can do...

    % func azure functionapp logstream advancedhttpfunction-20210414130559868
    
Cleaning Up
-----------
You can clean the function up using...

    % mvn clean
    % az functionapp delete -n "advancedhttpfunction-20210414130559868" -g "java-functions-group"

Issues
------
- On Mac, the JAVA_HOME needs to be faked for the azure-functions:run to work properly - known issue?

