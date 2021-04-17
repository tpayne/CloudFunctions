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

public class ClientInfo {
    
    private int clientId;
    private String clientName;
    private String clientCompany;

    public int getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientCompany() {
        return clientCompany;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientCompany(String clientCompany) {
        this.clientCompany = clientCompany;
    }    

    public ClientInfo(final int clientId, final String clientName, final String clientCompany) {
        setClientId(clientId);
        setClientName(clientName);
        setClientCompany(clientCompany);
    }

    @Override
    public String toString() {
        return "\"ClientInfo [clientId "+ clientId + ", clientName = " + clientName + ", clientCompany = " + clientCompany + "]\"";
    }
}