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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;

import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;

import java.time.Duration;

public class SendHttpRequest implements HttpFunction {

  private static final Logger logger = Logger.getLogger(SendHttpRequest.class.getName());

  // Create a target to send the message to...
  private static HttpClient target =
      HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();

  // This is the entry point for the function...
  @Override
  public void service(HttpRequest request, HttpResponse response)
      throws IOException, InterruptedException {

    boolean doCall = true;
    BufferedWriter writer = response.getWriter();
    StringBuilder str = new StringBuilder();

    StringBuilder targetURL = new StringBuilder();

    // This function will only support POST methods...
    try {
      switch (request.getMethod()) {
        case "POST":
          response.setStatusCode(HttpURLConnection.HTTP_OK);
          {
            str.append("<p>This is a POST function call<br>");
            request.getQueryParameters().forEach(
                (param, values) -> {
                  String value = values.get(0);
                  if (param.equals("targetURL")) {
                    targetURL.append(value);
                    str.append("<li>Field: '"+param+"' -> Value: '"+value+"'</li>");
                  }
                });
            str.append("</ul><br></p>");
          }
          break;
        case "PUT":
          response.setStatusCode(HttpURLConnection.HTTP_BAD_METHOD);
          str.append("<p>This call is not supported<br></p>");
          doCall = false;
          break;
        case "GET":
          response.setStatusCode(HttpURLConnection.HTTP_OK);
          str.append("<p>This is a GET function call<br>");
          break;
        default:
          break;
      }

      if (doCall) {
        String url = (targetURL.length()==0) ? "http://www.yahoo.com" : targetURL.toString();
        var getRequest = java.net.http.HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        var getResponse = target.send(getRequest, BodyHandlers.ofString());
        str.append("<br>Received code '"+getResponse.statusCode()+"' from url '"+url+"'</p>");
      }
    } catch(Exception e) {
      logger.severe("Trapped exception "+e.toString());
    }
    logger.info(str.toString());
    writer.write(str.toString());      
    return;
  }
}
