"""
This is an Azure function that works with the GH API to list
public repos.

It responds to /api/repo/list
"""
# importing the requests library
import requests
import jwt
import traceback
import logging
import json
import azure.functions as func

app = func.FunctionApp()

@app.function_name(name="httpGitHub")

@app.route(route="repo/list",auth_level=func.AuthLevel.ANONYMOUS) # HTTP Trigger
def httpGitHub(req: func.HttpRequest) -> func.HttpResponse:
    logging.info("Python HTTP trigger function processed a request.")
    # userName = req.route_params.get("username")
    auth = req.headers.get("Authorization", None)
    auth_token = ""

    if auth:
        auth_token = auth.split()[1]

    userName = req.params.get("username")
    if not userName:
        try:
            req_body = req.get_json()
        except ValueError:
            pass
        else:
            userName = req_body.get("username")

    if not userName:
        return func.HttpResponse("Error: Username not specified", status_code=400)
    else:
        try:
            gitHubUrl = "https://api.github.com/users/" + userName + "/repos" 
            if not auth_token:
                gitHubUrl += "?visibility=public"
            else:
                gitHubUrl += "?visibility=private"

            logging.info("Running request on "+gitHubUrl)
            if auth_token:
                headers = {'Authorization': f'{auth_token}'}
                r = requests.get(url=gitHubUrl,headers=headers)
            else:
                r = requests.get(url=gitHubUrl)
            data = r.json()
            strData = json.dumps(data,indent=2)
            return func.HttpResponse(strData, status_code=200)
        except Exception as e:
            logging.error(traceback.format_exc())
            return func.HttpResponse("Service error", status_code=500)

@app.route(route="version", auth_level=func.AuthLevel.ANONYMOUS)
def version(req: func.HttpRequest) -> func.HttpResponse:
    logging.info("Processing version request")
    return func.HttpResponse(f"Version 1.0")
