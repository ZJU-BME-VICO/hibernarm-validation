package edu.zju.bme.hibernarm.service;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

@WebService(endpointInterface = "edu.zju.bme.hibernarm.service.AQLExecuteParameterized")
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class AQLExecuteParameterizedImpl implements AQLExecuteParameterized {

	public AQLExecuteParameterizedImpl() {
	}
	
	@Override
	public List<String> select(String aql, Map<String, Object> parameters) {

		return AQLExecuteSingleton.INSTANCE.select(aql, parameters);

	}
	
//	@Override
	public List<String> select(String aql, String archetypeId, Map<String, Object> parameters) {

		return AQLExecuteSingleton.INSTANCE.select(aql, archetypeId, parameters);

	}
	
	@Override
	public int delete(String aql, Map<String, Object> parameters) {

		return AQLExecuteSingleton.INSTANCE.delete(aql, parameters);

	}

	@Override
	public int update(String aql, Map<String, Object> parameters) {

		return AQLExecuteSingleton.INSTANCE.update(aql, parameters);

	}

}
