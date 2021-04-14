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
        String query = request.getQueryParameters().get(paramName);
	String value = new String(request.getBody().orElse(query));
	String searchStr = paramName+"=";
	if (value.contains(searchStr)) {
            int loc = value.indexOf(searchStr)+searchStr.length();
            value = value.substring(loc,value.length());
	    if (value.indexOf('&')>0) {
                value = value.substring(0,value.indexOf('&'));
	    }
	}

	return value;
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

        // Parse query parameter
        final String userName = getParam(request,"username");
        final String userPriv = getParam(request,"userpriv");

        StringBuilder str = new StringBuilder();
        str.append("<p>This is a GET/POST funtion call<br>");
        str.append("User name is '"+userName+"' <br>");
        str.append("User privelege is '"+userPriv+"' <br></p>");

        if (userName == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("The required parameters have not been specified").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body(str.toString()).build();
        }
    }
}
