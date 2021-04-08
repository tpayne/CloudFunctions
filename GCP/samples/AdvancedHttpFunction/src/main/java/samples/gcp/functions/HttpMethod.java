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

package samples.gcp.functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.HttpURLConnection;
import java.net.HttpURLConnection;

import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.Optional;

import java.util.logging.Logger;

public class HttpMethod implements HttpFunction {

  private static final Logger logger = Logger.getLogger(HttpMethod.class.getName());
  private static final Gson gson = new Gson();

  // This is a simple helper function for getting request params...
  private String getParam(HttpRequest request, String paramName) {
    String value = null;

    Optional<String> param = request.getFirstQueryParameter(paramName);
    if (param.isPresent()) {
      value = param.get();
    }      
    return value;
  }

  // This is a simple function for getting json params...
  private String getParam(JsonObject json, String paramName) {
    String value = null;

    if (json.has(paramName)) {
      value = json.get(paramName).getAsString();
    }
    return value;
  }

  // This is the entry point for the function...
  @Override
  public void service(HttpRequest request, HttpResponse response)
      throws IOException {

    String contentType = request.getContentType().orElse("");
    logger.info("contentType = "+contentType);
    
    // Process the request based on the type of content...
    switch (contentType) {
      case "application/json":
        JsonObject body = gson.fromJson(request.getReader(), JsonObject.class);
        processRequest(body,request,response);
        break;
      default:
        processRequest(request,response);
    }
    return;
  }

  private void processRequest(HttpRequest request, HttpResponse response)
      throws IOException {
    
    BufferedWriter writer = response.getWriter();
    StringBuilder str = new StringBuilder();

    // This function will only support GET methods...
    switch (request.getMethod()) {
      case "GET":
        response.setStatusCode(HttpURLConnection.HTTP_OK);

        String userName = getParam(request,"username");
        String userPriv = getParam(request,"userpriv");
        
        str.append("<p>This is a GET funtion call<br>");
        str.append("User name is '"+userName+"' <br>");
        str.append("User privelege is '"+userPriv+"' <br></p>");
        logger.info(str.toString());
        writer.write(str.toString());
        break;
      case "POST":
        response.setStatusCode(HttpURLConnection.HTTP_OK);
        {
          str.append("<p>This is a POST function call<br>");
          str.append("<ul>");          
          request.getQueryParameters().forEach(
              (param, values) -> {
                String value = values.get(0);
                str.append("<li>Field: '"+param+"' -> Value: '"+value+"'</li>");
              });
          str.append("</ul><br></p>");
          logger.info(str.toString());
          writer.write(str.toString());
        }
        break;
      default:
        response.setStatusCode(HttpURLConnection.HTTP_BAD_METHOD);
        str.append("<p>This call is not supported<br></p>");
        logger.info(str.toString());
        writer.write(str.toString());
        break;
    }
    return;
  }

  private void processRequest(JsonObject body, HttpRequest request, HttpResponse response)
      throws IOException {
    
    BufferedWriter writer = response.getWriter();
    StringBuilder str = new StringBuilder();

    // This function will only support POST methods...
    switch (request.getMethod()) {
      case "POST":
        response.setStatusCode(HttpURLConnection.HTTP_OK);

        String userName = getParam(body,"username");
        String userPriv = getParam(body,"userpriv");
        
        str.append("<p>This is a POST funtion call<br>");
        str.append("User name is '"+userName+"' <br>");
        str.append("User privelege is '"+userPriv+"' <br></p>");
        logger.info(str.toString());
        writer.write(str.toString());
        break;
      default:
        response.setStatusCode(HttpURLConnection.HTTP_BAD_METHOD);
        str.append("<p>This call is not supported<br></p>");
        logger.info(str.toString());
        writer.write(str.toString());
        break;
    }
    return;
  }
}
