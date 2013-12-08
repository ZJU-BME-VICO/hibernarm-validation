package org.hibernarm.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;
import org.hibernate.transform.Transformers;
import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.DADLParser;
import org.openehr.am.parser.ParseException;
import org.openehr.build.RMObjectBuildingException;
import org.openehr.rm.binding.DADLBinding;
import org.openehr.rm.binding.DADLBindingException;
import org.openehr.rm.common.archetyped.Locatable;

@WebService(endpointInterface = "org.hibernarm.service.AQLExecute")
public class AQLExecuteImpl implements AQLExecute {

	private static Logger logger = Logger.getLogger(AQLExecuteImpl.class.getName());

	private static Configuration cfg;
	private static SessionFactory sessionFactory;

	private static Map<String, String> archetypes;
	private static Map<String, String> arms;

	private static boolean serviceStatus = false;

	public AQLExecuteImpl() {

		try {
			if (archetypes == null) {
				archetypes = new HashMap<String, String>();
			}

			if (arms == null) {
				arms = new HashMap<String, String>();
			}

			stop();
			
			reconfigure();
			
			start();			
		} catch (Exception e) {
			logger.error("AQLExecuteImpl", e);
			stop();
		}

	}

	@Override
	@WebMethod
	@WebResult
	public int start() {
		
		logger.info("start");

		serviceStatus = true;
		return 0;

	}

	@Override
	@WebMethod
	@WebResult
	public int stop() {
		
		logger.info("stop");

		serviceStatus = false;
		return 0;

	}

	@Override
	@WebMethod
	@WebResult
	public boolean getServiceStatus() {
		
		logger.info("getServiceStatus: " + serviceStatus);

		return serviceStatus;

	}

	@Override
	@WebMethod
	@WebResult
	public int reconfigure() {
		
		logger.info("reconfigure");

		try {
			if (getServiceStatus()) {
				return -1;
			}

			if (sessionFactory != null) {
				sessionFactory.close();
			}
			
			cfg = new Configuration();
			cfg.configure();

			for (String key : arms.keySet()) {
				InputStream is = new ByteArrayInputStream(arms.get(key).getBytes("UTF-8"));
				cfg.addInputStream(is);
			}

			for (String key : archetypes.keySet()) {
				InputStream is = new ByteArrayInputStream(archetypes.get(key).getBytes("UTF-8"));
				cfg.addArchetype(is);
			}
			
			StandardServiceRegistry serviceRegistry = 
					new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();

			sessionFactory = cfg.buildSessionFactory(serviceRegistry);

			return 0;
		} catch (Exception e) {
			logger.error("reconfigure", e);
			return -2;
		}

	}

	@Override
	@WebMethod
	@WebResult
	public int registerArchetype(@WebParam String archetypeId,
			@WebParam String archetype, @WebParam String arm) {
		
		logger.info("registerArchetype");
		
		try {
			if (getServiceStatus()) {
				return -1;
			}
			
			logger.info(archetypeId);
			logger.info(archetype);
			logger.info(arm);

			archetypes.put(archetypeId, archetype);
			arms.put(archetypeId, arm);

			return 0;			
		} catch (Exception e) {
			logger.error("registerArchetype", e);
			return -2;
		}

	}

	@Override
	@WebMethod
	@WebResult
	public List<String> select(@WebParam String aql) throws Exception {

		return select(aql, null, null);

	}

	// @Override
	// @WebMethod
	@WebResult
	public List<String> select(@WebParam String aql,
			@WebParam String archetypeId) throws Exception {

		return select(aql, archetypeId, null);

	}

	// @Override
	// @WebMethod
	@WebResult
	public List<String> select(@WebParam String aql,
			@WebParam Map<String, Object> parameters) throws Exception {

		return select(aql, null, parameters);

	}

	// @Override
	// @WebMethod
	@WebResult
	public List<String> select(@WebParam String aql,
			@WebParam String archetypeId,
			@WebParam Map<String, Object> parameters) throws Exception {
		
		logger.info("select");
		
		logger.info(aql);
		
		try {
			if (!getServiceStatus()) {
				return null;
			}

			Session s = sessionFactory.openSession();
			Transaction txn = s.beginTransaction();

			Query q = s.createQuery(aql);
			passParameters(q, parameters);
			if (archetypeId != null && !archetypeId.isEmpty()) {
				q.setResultTransformer(Transformers.aliasToArchetype(archetypeId));
			}
			@SuppressWarnings("rawtypes")
			List results = q.list();

			s.flush();
			txn.commit();
			s.close();

			List<String> dadlResults = new ArrayList<String>();
			for (Object arr : results) {
				if (arr.getClass().isArray()) {
					for (int i = 0; i < Array.getLength(arr); i++) {
						generateReturnDADL(Array.get(arr, i), dadlResults);
					}
				} else {
					generateReturnDADL(arr, dadlResults);
				}
			}

			return dadlResults;			
		} catch (Exception e) {
			logger.error("select", e);
			return null;
		}

	}

	protected void generateReturnDADL(Object obj, List<String> dadlResults)
			throws Exception {

		if (obj instanceof Locatable) {
			DADLBinding binding = new DADLBinding();
			Locatable loc = (Locatable) obj;
			String str = binding.toDADLString(loc);
			if (!dadlResults.contains(str)) {
				logger.info(str);
				dadlResults.add(str);

				for (Object associatedObject : loc.getAssociatedObjects().values()) {
					generateReturnDADL(associatedObject, dadlResults);
				}
			}
		}

	}

	@Override
	@WebMethod
	@WebResult
	public void insert(@WebParam List<String> dadls)
			throws UnsupportedEncodingException, ParseException,
			DADLBindingException, RMObjectBuildingException {
		
		logger.info("insert");

		try {
			if (!getServiceStatus()) {
				return;
			}

			List<Object> objects = new ArrayList<Object>();

			for (String dadl : dadls) {			
				logger.info(dadl);
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
		} catch (Exception e) {
			logger.error("insert", e);
		}

	}

	@Override
	@WebMethod
	@WebResult
	public int delete(@WebParam String aql) {

		return delete(aql, null);

	}

	// @Override
	// @WebMethod
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

	// @Override
	// @WebMethod
	@WebResult
	public int update(@WebParam String aql,
			@WebParam Map<String, Object> parameters) {

		return executeUpdate(aql, parameters);

	}

	protected int executeUpdate(String aql, Map<String, Object> parameters) {
		
		logger.info("executeUpdate");
		
		logger.info(aql);

		try {
			if (!getServiceStatus()) {
				return -1;
			}

			Session s = sessionFactory.openSession();
			Transaction txn = s.beginTransaction();

			Query q = s.createQuery(aql);
			passParameters(q, parameters);
			int ret = q.executeUpdate();

			s.flush();
			txn.commit();
			s.close();
			
			logger.info(ret);

			return ret;			
		} catch (Exception e) {
			logger.error("executeUpdate", e);
			return -2;
		}

	}

	protected void passParameters(Query q, Map<String, Object> parameters) {

		if (parameters != null) {
			for (String paraName : parameters.keySet()) {
				q.setParameter(paraName, parameters.get(paraName));
			}
		}

	}

	@Override
	@WebMethod
	@WebResult
	public List<String> getSQL(@WebParam String aql) {
		
		logger.info("getSQL");
		
		try {
			if (!getServiceStatus()) {
				return null;
			}
			
			if (aql == null || aql.trim().length() <= 0) {
				return null;
			}

	    	final QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
	    	final SessionFactoryImplementor factory = (SessionFactoryImplementor) sessionFactory;
	    	final QueryTranslator translator = translatorFactory.
	   			createQueryTranslator(aql, aql, Collections.EMPTY_MAP, factory);
	    	translator.compile(Collections.EMPTY_MAP, false);
	    	List<String> sqls = translator.collectSqlStrings();

			return sqls;			
		} catch (Exception e) {
			logger.error("getSQL", e);
			return null;
		}

	}

}
