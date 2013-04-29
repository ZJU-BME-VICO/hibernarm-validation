
package adr.ws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

@WebService(endpointInterface = "adr.ws.AQLExecute")
public class AQLExecuteImpl implements AQLExecute {
	private static String endpointUrl = "http://localhost:8098";
	
    public List<String> execute(String aql) {
    	List<String> dadlResults = new ArrayList<String>();
    	return dadlResults;
    }
}

