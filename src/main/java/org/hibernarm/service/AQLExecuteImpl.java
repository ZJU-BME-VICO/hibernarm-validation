package org.hibernarm.service;

import java.util.List;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

@WebService(endpointInterface = "org.hibernarm.service.AQLExecute")
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
	public int reconfigure() {

		return AQLExecuteSingleton.INSTANCE.reconfigure();

	}

	@Override
	public int registerArchetype(String archetypeId, String archetype, String arm) {

		return AQLExecuteSingleton.INSTANCE.registerArchetype(archetypeId, archetype, arm);

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

}
