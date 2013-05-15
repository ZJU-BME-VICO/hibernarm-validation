package adr.ws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.Transformers;
import org.openehr.rm.binding.DADLBinding;

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
	public
	Boolean reconfigure() {
		try {
			for (String key : arms.keySet()) {
				InputStream is = new ByteArrayInputStream(arms.get(key).getBytes("UTF-8"));
				cfg.addInputStream(is);
			}

			for (String key : archetypes.keySet()) {
				InputStream is = new ByteArrayInputStream(archetypes.get(key).getBytes("UTF-8"));
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
			@WebParam String archetypeId, @WebParam List<String> parameters) {

		Session s = sessionFactory.openSession();
		Transaction txn = s.beginTransaction();

		List<String> dadlResults = new ArrayList<String>();
		List results = s
				.createAQLQuery(aql)
				.setResultTransformer(
						Transformers.aliasToArchetype(archetypeId)).listAQL();

		DADLBinding binding = new DADLBinding();
		for (Object obj : results) {
			StringBuilder sb = new StringBuilder();
			try {
				for (String str : binding.toDADL(obj)) {
					sb.append(str);
					sb.append("\n");
				}
			} catch (Exception e) {
				continue;
			}
			dadlResults.add(sb.toString());
		}

		s.flush();
		txn.commit();
		s.close();

		return dadlResults;

	}

	@Override
	@WebMethod
	@WebResult
	public List<String> insert(@WebParam String aql,
			@WebParam List<String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
