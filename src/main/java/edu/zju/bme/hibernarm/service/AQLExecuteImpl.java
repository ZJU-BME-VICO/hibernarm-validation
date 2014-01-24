package edu.zju.bme.hibernarm.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

@WebService(endpointInterface = "edu.zju.bme.hibernarm.service.AQLExecute")
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class AQLExecuteImpl implements AQLExecute {

	public AQLExecuteImpl() {
	}

	@Override
	public int start() {

		return AQLExecuteSingleton.INSTANCE.start();

	}

	@Override
	public int stop() {

		return AQLExecuteSingleton.INSTANCE.stop();

	}

	@Override
	public boolean getServiceStatus() {

		return AQLExecuteSingleton.INSTANCE.getServiceStatus();

	}

	@Override
	public int reconfigure(Collection<String> archetypes, Collection<String> arms) {

		return AQLExecuteSingleton.INSTANCE.reconfigure(archetypes, arms);

	}

	@Override
	public List<String> select(String aql) {

		return AQLExecuteSingleton.INSTANCE.select(aql);

	}

//	@Override
	public List<String> select(String aql, String archetypeId) {

		return AQLExecuteSingleton.INSTANCE.select(aql, archetypeId);

	}

	@Override
	public int insert(String[] dadls) {

		return AQLExecuteSingleton.INSTANCE.insert(dadls);

	}

	@Override
	public int delete(String aql) {

		return AQLExecuteSingleton.INSTANCE.delete(aql);

	}

	@Override
	public int update(String aql) {

		return AQLExecuteSingleton.INSTANCE.update(aql);

	}

	@Override
	public List<String> getSQL(String aql) {

		return AQLExecuteSingleton.INSTANCE.getSQL(aql);

	}

	@Override
	public Set<String> getArchetypeIds() {

		return AQLExecuteSingleton.INSTANCE.getArchetypeIds();
		
	}

	@Override
	public String getArchetypeString(String archetypeId) {

		return AQLExecuteSingleton.INSTANCE.getArchetypeString(archetypeId);
		
	}

}
