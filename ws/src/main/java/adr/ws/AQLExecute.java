package adr.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface AQLExecute {
	
    @WebMethod
    @WebResult Boolean reconfigure();
	
    @WebMethod
    @WebResult String registerArchetype( 
    		@WebParam String archetypeId,  
    		@WebParam String archetype);
	
    @WebMethod
    @WebResult String registerArm( 
    		@WebParam String archetypeId,  
    		@WebParam String arm);
	
    @WebMethod
    @WebResult List<String> select(
    		@WebParam String aql, 
    		@WebParam String archetypeId, 
    		@WebParam List<String> parameters);

    @WebMethod
    @WebResult List<String> insert(
    		@WebParam String aql, 
    		@WebParam List<String> parameters);
    
}

