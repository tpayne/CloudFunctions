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


import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpRequest.HttpPart;
import com.google.cloud.functions.HttpResponse;
import com.google.common.testing.TestLogHandler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.google.api.client.http.HttpStatusCodes;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import java.net.HttpURLConnection;

import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class CloudSpannerHttpTest {
  @Mock private HttpRequest request;
  @Mock private HttpResponse response;

  private BufferedWriter writerOut;
  private StringWriter responseOut;

  private static final Logger logger = Logger.getLogger(CloudSpannerHttp.class.getName());
  private static final TestLogHandler logHandler = new TestLogHandler();

  @BeforeClass
  public static void setUp() {
    logger.addHandler(logHandler);
  }

  @Before
  public void beforeTest() throws IOException {
    MockitoAnnotations.initMocks(this);

    responseOut = new StringWriter();
    writerOut = new BufferedWriter(responseOut);
    when(response.getWriter()).thenReturn(writerOut);
  }

  @Test
  public void CloudSpannerHttpGet() throws IOException, InterruptedException {
    when(request.getMethod()).thenReturn("GET");
    when(request.getParts()).thenReturn(Map.of());
    List<String> db = Arrays.asList("testdb");

    Map<String, List<String>> queryParams = new HashMap<String, List<String>>(0);
    queryParams.put("instanceName",db);
    queryParams.put("dbName",db);

    when(request.getQueryParameters()).thenReturn(queryParams);
    when(request.getFirstQueryParameter("instanceName")).thenReturn(Optional.of("testdb"));
    when(request.getFirstQueryParameter("dbName")).thenReturn(Optional.of("testdb"));

    try {
      new CloudSpannerHttp().service(request, response);
    } catch(Exception e) {
    }
    
    ImmutableList.Builder<String> logMessages = new ImmutableList.Builder<>();
    for (LogRecord record : logHandler.getStoredLogRecords()) {
      logMessages.add(record.getMessage());
    }

    writerOut.flush();

    org.junit.Assert.assertTrue("Expected to get GET function call text",
                                logMessages.build().toString().contains("<p>This is a GET function call<br>"));
    //org.junit.Assert.assertTrue("Expected to UNAUTHENTICATED GET function call text",
    //                            responseOut.toString().contains("UNAUTHENTICATED"));
    org.junit.Assert.assertTrue("Expected to get GET function call text",
                                responseOut.toString().contains("<p>This is a GET function call<br>"));
    verify(response, times(1)).setStatusCode(HttpURLConnection.HTTP_OK);
  }

  @Test
  public void CloudSpannerHttpPost() throws IOException, InterruptedException {
    when(request.getMethod()).thenReturn("POST");
    when(request.getParts()).thenReturn(Map.of());
    List<String> db = Arrays.asList("testdb");

    Map<String, List<String>> queryParams = new HashMap<String, List<String>>(0);
    queryParams.put("instanceName",db);
    queryParams.put("dbName",db);

    when(request.getQueryParameters()).thenReturn(queryParams);
    when(request.getFirstQueryParameter("instanceName")).thenReturn(Optional.of("testdb"));
    when(request.getFirstQueryParameter("dbName")).thenReturn(Optional.of("testdb"));

    try {
      new CloudSpannerHttp().service(request, response);
    } catch(Exception e) {
    }

    writerOut.flush();
    //org.junit.Assert.assertTrue("Expected to UNAUTHENTICATED POST function call text",
    //                            responseOut.toString().contains("UNAUTHENTICATED"));
    org.junit.Assert.assertTrue("Expected to get POST function call text",
                                responseOut.toString().contains("<p>This is a POST function call<br>"));
    verify(response, times(1)).setStatusCode(HttpURLConnection.HTTP_OK);
  }
}
