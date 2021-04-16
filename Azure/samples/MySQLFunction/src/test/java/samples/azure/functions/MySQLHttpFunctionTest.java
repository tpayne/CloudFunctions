package samples.azure.functions;

import com.microsoft.azure.functions.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


/**
 * Unit test for Function class.
 */
public class MySQLHttpFunctionTest {
    /**
     * Unit test for HttpTriggerJava method.
     */
    @Test
    public void testHttpTriggerText() throws Exception {
        // Setup
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

        final Map<String, String> queryParams = new HashMap<>();
        final Map<String, String> headers = new HashMap<>();

        queryParams.put("host", "testdbmysql-server.mysql.database.azure.com");
        queryParams.put("dbname", "testdbmysql");
        queryParams.put("username", "mysqladmin@testdbmysql-server");
        queryParams.put("passwd", "Ermd32_-1a2b3c"); // This is just random junk...
        doReturn(queryParams).when(req).getQueryParameters();

        headers.put("Content-Type", "text/plain");
        doReturn(headers).when(req).getHeaders();

        final Optional<String> queryBody = Optional.empty();
        doReturn(queryBody).when(req).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        // Invoke
        final HttpResponseMessage ret = new MySQLHttpFunction().run(req, context);

        // Verify
        assertEquals(ret.getStatus(), HttpStatus.OK);
        assertTrue(ret.getBody().toString().contains("ERROR")); 
        //assertTrue(ret.getBody().toString()
        //    .contains("<p><br>Clients<br><ol><li>0 NAME00 COMPANY00</li>"));    
    }

    @Test
    public void testHttpTriggerJson() throws Exception {
        // Setup
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

        final Map<String, String> queryParams = new HashMap<>();
        final Map<String, String> headers = new HashMap<>();

        queryParams.put("host", "testdbmysql-server.mysql.database.azure.com");
        queryParams.put("dbname", "testdbmysql");
        queryParams.put("username", "mysqladmin@testdbmysql-server");
        queryParams.put("passwd", "Ermd32_-1a2b3c"); // This is just random junk...
        doReturn(queryParams).when(req).getQueryParameters();

        headers.put("Content-Type", "application/json");
        doReturn(headers).when(req).getHeaders();

        final LinkedHashMap<String, String> queryBody = new LinkedHashMap<>();
        queryBody.put("host", "testdbmysql-server.mysql.database.azure.com");
        queryBody.put("dbname", "testdbmysql");
        queryBody.put("username", "mysqladmin@testdbmysql-server");
        queryBody.put("passwd", "Ermd32_-1a2b3c");

        doReturn(queryBody).when(req).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        // Invoke
        final HttpResponseMessage ret = new MySQLHttpFunction().run(req, context);

        // Verify
        assertEquals(ret.getStatus(), HttpStatus.OK);
        assertTrue(ret.getBody().toString().contains("ERROR")); 
        //assertTrue(ret.getBody().toString()
        //    .contains("<p><br>Clients<br><ol><li>0 NAME00 COMPANY00</li>"));    
    }
}
