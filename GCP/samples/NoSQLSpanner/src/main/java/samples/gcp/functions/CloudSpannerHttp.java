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

import com.google.api.client.http.HttpStatusCodes;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.LazySpannerInitializer;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;

import com.google.common.annotations.VisibleForTesting;

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

public class CloudSpannerHttp implements HttpFunction {

  private static final Logger logger = Logger.getLogger(CloudSpannerHttp.class.getName());

  // The LazySpannerInitializer instance is shared across all instances of the HelloSpanner class.
  // It will create a Spanner instance the first time one is requested, and continue to return that
  // instance for all subsequent requests.
  private static final LazySpannerInitializer SPANNER_INITIALIZER = new LazySpannerInitializer();

  // This is a simple helper function for getting request params...
  private String getParam(final HttpRequest request, final String paramName) {
    String value = null;

    Optional<String> param = request.getFirstQueryParameter(paramName);
    if (param.isPresent()) {
      value = param.get();
    }      
    return value;
  }

  @VisibleForTesting
  DatabaseClient getClient(final String instanceName, final String dbName) 
    throws Throwable {
    
    DatabaseId databaseId =
      DatabaseId.of(
          SpannerOptions.getDefaultProjectId(),
          instanceName,
          dbName);
    return SPANNER_INITIALIZER.get().getDatabaseClient(databaseId);
  }

  private void 
  processQuery(final String instanceName,
                       final String dbName,
                       final HttpResponse response,
                       final PrintWriter writer) 
    throws Exception {

    try {
      DatabaseClient client = getClient(instanceName,dbName);
      try (ResultSet rs =
          client
              .singleUse()
              .executeQuery(Statement.of("SELECT client_id, client_name, client_company FROM client_data"))) {
        writer.printf("<br>Clients:%n");
        while (rs.next()) {
          writer.printf(
              "\n<br>%d %s %s%n",
              rs.getLong("client_id"), rs.getString("client_name"), rs.getString("client_company"));
        }
      } catch (SpannerException e) {
        logger.severe("Spanner SQL failed "+e.getMessage());
        writer.printf("Error querying database: %s", e.getMessage());
        response.setStatusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR, e.getMessage());
        return;
      }
    } catch (Throwable t) {
      logger.severe("Spanner example failed "+t.getMessage());
      writer.printf("Error setting up Spanner: %s", t.getMessage());
      response.setStatusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR, t.getMessage());
    }
    return;
  }      

  // This is the entry point for the function...
  @Override
  public void service(HttpRequest request, HttpResponse response)
      throws IOException, InterruptedException, Exception {

    boolean doCall = true;
    PrintWriter writer = new PrintWriter(response.getWriter());
    StringBuilder str = new StringBuilder();

    String instanceName = "";
    String dbName = "";

    // This function will only support POST methods...
    try {
      switch (request.getMethod()) {
        case "POST":
          response.setStatusCode(HttpURLConnection.HTTP_OK);
          {
            str.append("<p>This is a POST function call<br><ul>");
            instanceName = getParam(request,"instanceName");
            dbName = getParam(request,"dbName");
          }
          break;
        case "PUT":
          response.setStatusCode(HttpURLConnection.HTTP_BAD_METHOD);
          str.append("<p>This call is not supported<br></p>");
          doCall = false;
          break;
        case "GET":
          response.setStatusCode(HttpURLConnection.HTTP_OK);
          {
            str.append("<p>This is a GET function call<br><ul>");
            instanceName = getParam(request,"instanceName");
            dbName = getParam(request,"dbName");
          }          
          break;
        default:
          break;
      }

      if (instanceName != null) {
        if (!instanceName.isEmpty()) {
          str.append("<li>Instance = '"+instanceName+"' </li>");        
        }
      }
      if (dbName != null) {
        if (!dbName.isEmpty()) {
          str.append("<li>Database = '"+dbName+"' </li>");        
        }
      }

      str.append("</ul><br></p>");
      logger.info(str.toString());
      writer.write(str.toString());      

      if (doCall) {
        processQuery(instanceName,dbName,response,writer);
      }
    } catch(Exception e) {
      writer.write("Trapped general exception ");
      e.printStackTrace(writer);
      logger.severe("Trapped general exception "+e.getMessage());
    }
    return;
  }
}
