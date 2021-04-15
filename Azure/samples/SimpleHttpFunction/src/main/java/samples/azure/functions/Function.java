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

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

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
            // Parse query parameter
            String userName = getParam(request,"username");
            String userPriv = getParam(request,"userpriv");

            if (userName == null || userPriv == null) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("The required parameters have not been specified").build();
            } 

            StringBuilder str = new StringBuilder();
            str.append("<p>This is a GET/POST funtion call<br>");
            str.append("User name is '"+userName+"' <br>");
            str.append("User privelege is '"+userPriv+"' <br></p>");
            userName = null;
            userPriv = null;
            return request.createResponseBuilder(HttpStatus.OK).body(str.toString()).build();
        } catch(Exception e) {
          context.getLogger().severe("Trapped exception "+e.toString());
        }
        return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                  .body("The function errored").build();
    }
}
