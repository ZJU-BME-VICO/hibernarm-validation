package adr.ws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.Transformers;
import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.DADLParser;
import org.openehr.am.parser.ParseException;
import org.openehr.build.RMObjectBuildingException;
import org.openehr.rm.binding.DADLBinding;
import org.openehr.rm.binding.DADLBindingException;

@WebService(endpointInterface = "adr.ws.AQLExecute")
public class AQLExecuteImpl implements AQLExecute {

	private static Configuration cfg;
	private static SessionFactory sessionFactory;

	private static Map<String, String> archetypes;
	private static Map<String, String> arms;

	public AQLExecuteImpl() {
		if (cfg == null) {
			cfg = new Configuration();
			cfg.configure();

			sessionFactory = cfg.buildSessionFactory();
		}

		if (archetypes == null) {
			archetypes = new HashMap<String, String>();
		}

		if (arms == null) {
			arms = new HashMap<String, String>();
		}
	}

	@Override
	@WebMethod
	@WebResult
	public Boolean reconfigure() {
		try {
			for (String key : arms.keySet()) {
				InputStream is = new ByteArrayInputStream(arms.get(key)
						.getBytes("UTF-8"));
				cfg.addInputStream(is);
			}

			for (String key : archetypes.keySet()) {
				InputStream is = new ByteArrayInputStream(archetypes.get(key)
						.getBytes("UTF-8"));
				cfg.addArchetype(is);
			}
		} catch (Exception e) {
			return false;
		}

		sessionFactory.close();
		sessionFactory = cfg.buildSessionFactory();

		return true;
	}

	@Override
	@WebMethod
	@WebResult
	public String registerArchetype(@WebParam String archetypeId,
			@WebParam String archetype) {
		return archetypes.put(archetypeId, archetype);
	}

	@Override
	@WebMethod
	@WebResult
	public String registerArm(@WebParam String archetypeId, @WebParam String arm) {
		return arms.put(archetypeId, arm);
	}

	@Override
	@WebMethod
	@WebResult
	public List<String> select(@WebParam String aql,
			@WebParam String archetypeId) throws Exception {

		return select(aql, archetypeId, null);

	}

//	@Override
//	@WebMethod
	@WebResult
	public List<String> select(@WebParam String aql,
			@WebParam String archetypeId,
			@WebParam Map<String, Object> parameters) throws Exception {

		Session s = sessionFactory.openSession();
		Transaction txn = s.beginTransaction();

		Query q = s.createAQLQuery(aql);
		passParameters(q, parameters);
		List results = q.setResultTransformer(
				Transformers.aliasToArchetype(archetypeId)).listAQL();

		s.flush();
		txn.commit();
		s.close();

		List<String> dadlResults = new ArrayList<String>();
		DADLBinding binding = new DADLBinding();
		for (Object obj : results) {
			dadlResults.add(binding.toDADLString(obj));
		}

		return dadlResults;

	}

	@Override
	@WebMethod
	@WebResult
	public void insert(@WebParam List<String> dadls)
			throws UnsupportedEncodingException, ParseException,
			DADLBindingException, RMObjectBuildingException {

		List<Object> objects = new ArrayList<Object>();

		for (String dadl : dadls) {
			InputStream is = new ByteArrayInputStream(dadl.getBytes("UTF-8"));
			DADLParser parser = new DADLParser(is);
			ContentObject contentObj = parser.parse();
			DADLBinding binding = new DADLBinding();
			Object bp = binding.bind(contentObj);
			objects.add(bp);
		}

		Session s = sessionFactory.openSession();
		Transaction txn = s.beginTransaction();

		for (Object object : objects) {
			s.save(object);
		}

		s.flush();
		txn.commit();
		s.close();

	}

	@Override
	@WebMethod
	@WebResult
	public int delete(@WebParam String aql) {

		return delete(aql, null);

	}

//	@Override
//	@WebMethod
	@WebResult
	public int delete(@WebParam String aql,
			@WebParam Map<String, Object> parameters) {

		return executeUpdate(aql, parameters);

	}

	@Override
	@WebMethod
	@WebResult
	public int update(@WebParam String aql) {

		return update(aql, null);
	}

//	@Override
//	@WebMethod
	@WebResult
	public int update(@WebParam String aql,
			@WebParam Map<String, Object> parameters) {

		return executeUpdate(aql, parameters);
	}

	protected int executeUpdate(String aql, Map<String, Object> parameters) {

		Session s = sessionFactory.openSession();
		Transaction txn = s.beginTransaction();

		Query q = s.createAQLQuery(aql);
		passParameters(q, parameters);
		int ret = q.executeUpdateAQL();

		s.flush();
		txn.commit();
		s.close();

		return ret;

	}

	protected void passParameters(Query q, Map<String, Object> parameters) {

		if (parameters != null) {
			for (String paraName : parameters.keySet()) {
				q.setParameter(paraName, parameters.get(paraName));
			}
		}

	}

}
