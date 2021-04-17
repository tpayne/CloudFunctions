MySQLHttpFunction Sample
========================

This sample shows how to use Azure Cloud Functions to process a MySQL HTTP call that supports JSON, GET and POST requests.

Dependencies
------------
Before you attempt this example, please ensure you have done the following: -
- You have installed Maven (mvn), MySQL client (mysql) and the Azure client (az)
- You have logged into a (Unix) terminal window that will allow you to do deployments to a valid Azure account

Running Sample
--------------
To run the sample, please do the following...

First, install the MySQL server on Azure...

    % ./dbSetup.sh

Then, run the function...

    % mvn clean package azure-functions:run -DskipTests=true

Then in another console window, do...

    % curl "http://localhost:7071/api/MySQLHttpFunction/?host=testdbmysql-server.mysql.database.azure.com&dbname=testdbmysql&username=mysqladmin@testdbmysql-server&passwd=Ermd32_-1a2b3c"
    <p><br>Clients<br><ol><li>0 NAME00 COMPANY00</li><li>1 NAME01 COMPANY01</li><li>2 NAME02 COMPANY02</li><li>3 NAME03 COMPANY03</li><li>4 NAME04 COMPANY04</li></ol><br></p>
    % curl -X POST -d host=testdbmysql-server.mysql.database.azure.com \
          -d dbname=testdbmysql -d username=mysqladmin@testdbmysql-server \
          -d passwd=Ermd32_-1a2b3c http://localhost:7071/api/MySQLHttpFunction/
    <p><br>Clients<br><ol><li>0 NAME00 COMPANY00</li><li>1 NAME01 COMPANY01</li><li>2 NAME02 COMPANY02</li><li>3 NAME03 COMPANY03</li><li>4 NAME04 COMPANY04</li></ol><br></p>    
    % curl -H "Content-Type: application/json" \
        --request POST \
        --data \
        '{"host":"testdbmysql-server.mysql.database.azure.com","dbname":"testdbmysql","username":"mysqladmin@testdbmysql-server","passwd":"Ermd32_-1a2b3c"}' \ 
      "http://localhost:7071/api/MySQLHttpFunction/"
    <p><br>Clients<br><ol><li>0 NAME00 COMPANY00</li><li>1 NAME01 COMPANY01</li><li>2 NAME02 COMPANY02</li><li>3 NAME03 COMPANY03</li><li>4 NAME04 COMPANY04</li></ol><br></p>

The function will parse the parameters, connect to the DB and then display the results with HTTP wrapping.

Other functions, like a PUT will be rejected and not do anything.

This function also supports converting output to JSON format. To do this, please add the parameter 
`jsontxt=true` to the appropriate CURL call. This will return the data in JSON, rather than HTML
format.

    % curl -X POST -d host=testdbmysql-server.mysql.database.azure.com \
          -d dbname=testdbmysql -d username=mysqladmin@testdbmysql-server \
          -d jsontxt=true \
          -d passwd=Ermd32_-1a2b3c http://localhost:7071/api/MySQLHttpFunction/
    {"0-NAME00":{"clientId":0,"clientName":"NAME00","clientCompany":"COMPANY00"},
    "1-NAME01":{"clientId":1,"clientName":"NAME01","clientCompany":"COMPANY01"},
    "2-NAME02":{"clientId":2,"clientName":"NAME02","clientCompany":"COMPANY02"},
    "3-NAME03":{"clientId":3,"clientName":"NAME03","clientCompany":"COMPANY03"},
    "4-NAME04":{"clientId":4,"clientName":"NAME04","clientCompany":"COMPANY04"}}

Deploying the Function to Azure
-------------------------------
If you wish to deploy the function to Azure, you can use the following...

    % mvn clean package azure-functions:deploy -DskipTests=true
    
You can then invoke it via...

    % curl "https://mysqlhttpfunctions-20210414130559868.azurewebsites.net/api/mysqlhttpfunction/?host=testdbmysql-server.mysql.database.azure.com&dbname=testdbmysql&username=mysqladmin@testdbmysql-server&passwd=Ermd32_-1a2b3c"
    <p><br>Clients<br><ol><li>0 NAME00 COMPANY00</li><li>1 NAME01 COMPANY01</li><li>2 NAME02 COMPANY02</li><li>3 NAME03 COMPANY03</li><li>4 NAME04 COMPANY04</li></ol><br></p>
    % curl -X POST -d host=testdbmysql-server.mysql.database.azure.com \
          -d dbname=testdbmysql -d username=mysqladmin@testdbmysql-server \
          -d passwd=Ermd32_-1a2b3c https://mysqlhttpfunctions-20210414130559868.azurewebsites.net/api/mysqlhttpfunction/
    <p><br>Clients<br><ol><li>0 NAME00 COMPANY00</li><li>1 NAME01 COMPANY01</li><li>2 NAME02 COMPANY02</li><li>3 NAME03 COMPANY03</li><li>4 NAME04 COMPANY04</li></ol><br></p>
    % curl -H "Content-Type: application/json" \
        --request POST \
        --data \
        '{"host":"testdbmysql-server.mysql.database.azure.com","dbname":"testdbmysql","username":"mysqladmin@testdbmysql-server","passwd":"Ermd32_-1a2b3c"}' \ 
        "https://mysqlhttpfunctions-20210414130559868.azurewebsites.net/api/mysqlhttpfunction/"
    <p><br>Clients<br><ol><li>0 NAME00 COMPANY00</li><li>1 NAME01 COMPANY01</li><li>2 NAME02 COMPANY02</li><li>3 NAME03 COMPANY03</li><li>4 NAME04 COMPANY04</li></ol><br></p>

If you get an error like "ERROR", then you will probably need to add a firewall rule to the MySQL server which allows the function IP address to connect.
You can do this via the console or using the `az mysql server firewall-rule create ...` command. See `dbSetup.sh` for an example.

To view errors or logs of the function, you can do...

    % func azure functionapp logstream mysqlhttpfunctions-20210414130559868
    
Cleaning Up
-----------
You can clean the function up using...

    % mvn clean
    % az functionapp delete -n "mysqlhttpfunctions-20210414130559868" -g "java-functions-group"
    % ./dbSetup.sh -c
    
Issues
------
- On Mac, the JAVA_HOME needs to be faked for the azure-functions:run to work properly - known issue?
- The password used here is just random junk and bears no relevance to any I use
- The unit tests work on the basis that the target system does not exist. Therefore if the system does exist, then the unit tests will fail. This is not an issue, you just need to skip the unit tests if you have created a default database system using the provided script.

