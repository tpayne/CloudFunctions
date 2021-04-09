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
import java.io.PrintWriter;

import java.net.HttpURLConnection;

import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.Optional;

public class HttpMethod implements HttpFunction {

  // This is a simple helper function for getting request params...
  private String getParam(HttpRequest request, String paramName) {
    String value = null;

    Optional<String> param = request.getFirstQueryParameter(paramName);
    if (param.isPresent()) {
      value = param.get();
    }      
    return value;
  }

  // This is the entry point for the function...
  @Override
  public void service(HttpRequest request, HttpResponse response)
      throws IOException {

    BufferedWriter writer = response.getWriter();

    String contentType = request.getContentType().orElse("");

    // This function will only support GET methods...
    switch (request.getMethod()) {
      case "GET":
        StringBuilder str = new StringBuilder();

        response.setStatusCode(HttpURLConnection.HTTP_OK);

        String userName = getParam(request,"username");
        String userPriv = getParam(request,"userpriv");
        
        str.append("<p>This is a GET funtion call<br>");
        str.append("User name is '"+userName+"' <br>");
        str.append("User privelege is '"+userPriv+"' <br></p>");
        writer.write(str.toString());
        break;
      case "PUT":
        response.setStatusCode(HttpURLConnection.HTTP_FORBIDDEN);
        writer.write("<p>This is a PUT funtion call which is not allowed<br></p>");
        break;
      default:
        response.setStatusCode(HttpURLConnection.HTTP_BAD_METHOD);
        writer.write("<p>This call is not supported<br></p>");
        break;
    }
    return;
  }
}
