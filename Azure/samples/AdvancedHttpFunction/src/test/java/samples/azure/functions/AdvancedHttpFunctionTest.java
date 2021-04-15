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
public class AdvancedHttpFunctionTest {
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

        queryParams.put("username", "testname");
        queryParams.put("userpriv", "testpriv");
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
        final HttpResponseMessage ret = new AdvancedHttpFunction().run(req, context);

        // Verify
        assertEquals(ret.getStatus(), HttpStatus.OK);
        assertTrue(ret.getBody().toString()
            .contains("<p>This is a GET/POST TEXT funtion call<br>User name is 'testname' <br>User privelege is 'testpriv' <br></p>"));    
    }

    @Test
    public void testHttpTriggerJson() throws Exception {
        // Setup
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

        final Map<String, String> queryParams = new HashMap<>();
        final Map<String, String> headers = new HashMap<>();

        queryParams.put("username", "testname");
        queryParams.put("userpriv", "testpriv");
        doReturn(queryParams).when(req).getQueryParameters();

        headers.put("Content-Type", "application/json");
        doReturn(headers).when(req).getHeaders();

        final LinkedHashMap<String, String> queryBody = new LinkedHashMap<>();
        queryBody.put("username", "testname");
        queryBody.put("userpriv", "testpriv");

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
        final HttpResponseMessage ret = new AdvancedHttpFunction().run(req, context);

        // Verify
        assertEquals(ret.getStatus(), HttpStatus.OK);
        assertTrue(ret.getBody().toString()
            .contains("<p>This is a GET/POST JSON funtion call<br>User name is 'testname' <br>User privelege is 'testpriv' <br></p>"));    
    }
}
