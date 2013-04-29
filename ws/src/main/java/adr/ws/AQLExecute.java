package adr.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface AQLExecute {
    @WebMethod
    @WebResult List<String> execute(@WebParam String aql);
}

