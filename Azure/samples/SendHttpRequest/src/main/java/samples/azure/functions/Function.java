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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;

import java.time.Duration;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

  private static final Logger logger = Logger.getLogger(Function.class.getName());

  // Create a target to send the message to...
  private static HttpClient target =
      HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();

  // Get parameter value...
  private String getParam(HttpRequestMessage<Optional<String>> request, final String paramName) {
    if (request == null) {
      return null;
    }

    try {
      String query = null;
      String value = null;

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

  /**
   * This function listens at endpoint "/api/SendHttpRequest". Two ways to invoke it using "curl" command in bash:
   * 1. curl -d "HTTP Body" {your host}/api/SendHttpRequest
   * 2. curl "{your host}/api/SendHttpRequest?name=HTTP%20Query"
   */
  @FunctionName("SendHttpRequest")
  public HttpResponseMessage run(
    @HttpTrigger(
        name = "req",
        methods = {HttpMethod.GET, HttpMethod.POST},
        authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context) {

    context.getLogger().info("Java HTTP trigger processed a request.");

    try {
      // Parse query parameter
      String targetURL = getParam(request,"targetURL");

      StringBuilder str = new StringBuilder();
      str.append("<p>This is a GET/POST funtion call<br>");

      if (targetURL == null) {
        return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                  .body("The required parameters have not been specified").build();
      } 

      String url = (targetURL.length()==0) ? "https://www.yahoo.com" : targetURL.toString();
      HttpRequest getRequest = java.net.http.HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
      HttpResponse<String> getResponse = target.send(getRequest, BodyHandlers.ofString());
      str.append("<br>Received code '"+getResponse.statusCode()+"' from url '"+url+"'</p>");
      targetURL = null;
      context.getLogger().info(str.toString());
      return request.createResponseBuilder(HttpStatus.OK).body(str.toString()).build();

    } catch(Exception e) {
      context.getLogger().severe("Trapped exception "+e.toString());
    }
    return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                  .body("The function errored").build();
  }
}

