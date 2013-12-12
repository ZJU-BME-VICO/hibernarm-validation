package org.hibernarm.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface AQLExecute {

	/**
	 * @return
	 * 0 success
	 */
	@WebMethod
	int start();

	/**
	 * @return
	 * 0 success
	 */
	@WebMethod
	int stop();

	/**
	 * @return
	 */
	@WebMethod
	boolean getServiceStatus();

	/**
	 * @return
	 * 0 success
	 * -1 service running
	 * -2 internal error
	 */
	@WebMethod
	int reconfigure();

	/**
	 * @param archetypeId
	 * @param archetype
	 * @param arm
	 * @return
	 * 0 success
	 * -1 service running
	 */
	@WebMethod
	int registerArchetype(
			String archetypeId,
			String archetype,
			String arm);

	/**
	 * @param aql
	 * @param archetypeId
	 * @return
	 * -1 service running
	 * @throws Exception
	 */
	@WebMethod
	List<String> select(String aql);

	/**
	 * @param aql
	 * @return
	 * -1 service running
	 * @throws Exception
	 */
//	@WebMethod
//	List<String> select(
//			String aql, 
//			String archetypeId);

	/**
	 * @param aql
	 * @param listParameterName
	 * @param listParameterValue
	 * @return
	 * -1 service running
	 * @throws Exception
	 */
//	@WebMethod
//	List<String> select(
//			@WebParam(name = "aql") String aql, 
//			@WebParam(name = "parameters") Map<String, Object> parameters);

	/**
	 * @param aql
	 * @param archetypeId
	 * @param listParameterName
	 * @param listParameterValue
	 * @return
	 * -1 service running
	 * @throws Exception
	 */
//	@WebMethod
//	List<String> select(
//			@WebParam(name = "aql") String aql, 
//			@WebParam(name = "archetypeId") String archetypeId,
//			@WebParam(name = "parameters") Map<String, Object> parameters);

	/**
	 * @param dadls
	 * @return
	 * -1 service running
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 * @throws RMObjectBuildingException
	 * @throws DADLBindingException
	 */
	@WebMethod
	int insert(String[] dadls);

	/**
	 * @param aql
	 * @return
	 * -1 service running
	 */
	@WebMethod
	int delete(String aql);
	
	/**
	 * @param aql
	 * @param parameters
	 * @return
	 * -1 service running
	 */
//	@WebMethod
//	int delete(@WebParam(name = "aql") String aql, @WebParam(name = "parameters") Map<String, Object> parameters);

	/**
	 * @param aql
	 * @return
	 * -1 service running
	 */
	@WebMethod
	int update(String aql);

	/**
	 * @param aql
	 * @param parameters
	 * @return
	 * -1 service running
	 */
//	@WebMethod
//	int update(@WebParam(name = "aql") String aql, @WebParam(name = "parameters") Map<String, Object> parameters);

	/**
	 * @param aql
	 * @return sql
	 */
	@WebMethod
	List<String> getSQL(String aql);
}
