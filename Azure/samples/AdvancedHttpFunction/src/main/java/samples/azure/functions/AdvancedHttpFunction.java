/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package samples.azure.functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Azure Functions with HTTP Trigger.
 */
public class AdvancedHttpFunction {

    // This function will return JSON text from a request...
    private String getTextFromJSon(HttpRequestMessage<Optional<String>> request) {
        return getParam(request,"text");
    }

    // Get parameter value...
    private String getParam(HttpRequestMessage<Optional<String>> request, final String paramName) {
        if (request == null) {
            return null;
        }

        String query = null;
        String value = null;

        try {
            if (!request.getQueryParameters().isEmpty()) {
                query = request.getQueryParameters().get(paramName);
                value = new String(request.getBody().orElse(query));
            } else {
                value = new String(request.getBody().orElse(query));
            }

            // We need to massage the string being returned to get rid of POST/GET differences in string 
            // construction, so the results are the same
            String searchStr = paramName+"=";
            if (value.contains(searchStr)) {
                int loc = value.indexOf(searchStr)+searchStr.length();
                value = value.substring(loc,value.length());
                if (value.indexOf('&')>0) {
                    value = value.substring(0,value.indexOf('&'));
                }
            }
            return value;
        } catch(Exception e) {
            return null;
        }
    }

    // Process a standard Http request...
    private HttpResponseMessage processRequest(HttpRequestMessage<Optional<String>> request,
                                               final ExecutionContext context) {
        try {
            // Parse query parameter            
            String userName = getParam(request,"username");
            String userPriv = getParam(request,"userpriv");

            if (userName == null || userPriv == null) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("The required parameters have not been specified").build();
            } 

            StringBuilder str = new StringBuilder();
            str.append("<p>This is a GET/POST TEXT funtion call<br>");
            str.append("User name is '"+userName+"' <br>");
            str.append("User privelege is '"+userPriv+"' <br></p>");
            userName = null;
            userPriv = null;
            return request.createResponseBuilder(HttpStatus.OK).body(str.toString()).build();
        } catch(Exception e) {
          context.getLogger().severe("Trapped text exception "+e.toString());
        }
        return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                  .body("The function errored").build();
    }

    // Process a JSON request...
    private HttpResponseMessage processRequest(HttpRequestMessage request,
                                               LinkedHashMap body,
                                               final ExecutionContext context) {
        try {
            // Parse query parameter            
            String userName = (String)body.get("username");
            String userPriv = (String)body.get("userpriv");

            if (userName == null || userPriv == null) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("The required parameters have not been specified").build();
            } 

            StringBuilder str = new StringBuilder();
            str.append("<p>This is a GET/POST JSON funtion call<br>");
            str.append("User name is '"+userName+"' <br>");
            str.append("User privelege is '"+userPriv+"' <br></p>");
            userName = null;
            userPriv = null;
            return request.createResponseBuilder(HttpStatus.OK).body(str.toString()).build();
        } catch(Exception e) {
          context.getLogger().severe("Trapped JSON exception "+e.toString());
        }
        return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                  .body("The function errored").build();
    }

    /**
     * This function listens at endpoint "/api/HttpMethod". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpMethod
     * 2. curl "{your host}/api/HttpMethod?name=HTTP%20Query"
     */
    @FunctionName("HttpMethod")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        try {
            Map<String,String> headers = request.getHeaders();
            Object body = request.getBody();

            // Find out what request content I am...
            String contentType = (String)headers.get("Content-Type");
            if (contentType == null) {
                contentType = (String)headers.get("content-type");
            }
            if (contentType == null) {
                contentType = "text/plain";
            }

            // Process the request based on content...
            switch (contentType) {
                case "application/json":
                    String jsonText = getTextFromJSon(request);
                    if (jsonText != null) {
                        // Convert JSON text to Map...
                        LinkedHashMap<String,String> map = new Gson().fromJson(jsonText, LinkedHashMap.class);
                        return processRequest(request,map,context);
                    } else {
                        // Process default...
                        return processRequest(request,(LinkedHashMap)body,context);
                    }
                case "text/plain":
                case "text/plain; charset=utf-8":
                case "application/x-www-form-urlencoded":
                    return processRequest(request,context);
                default:
                    break;
            }           
        } catch(Exception e) {
          context.getLogger().severe("Trapped main exception "+e.toString()); 
          e.printStackTrace();           
        }
        return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                  .body("The function errored").build();
    }
}
