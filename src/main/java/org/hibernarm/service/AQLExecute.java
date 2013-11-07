package org.hibernarm.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.openehr.am.parser.ParseException;
import org.openehr.build.RMObjectBuildingException;
import org.openehr.rm.binding.DADLBindingException;

@WebService
public interface AQLExecute {

	/**
	 * @return
	 * 0 success
	 */
	@WebMethod
	@WebResult
	int start();

	/**
	 * @return
	 * 0 success
	 */
	@WebMethod
	@WebResult
	int stop();

	/**
	 * @return
	 */
	@WebMethod
	@WebResult
	boolean getServiceStatus();

	/**
	 * @return
	 * 0 success
	 * -1 service running
	 * -2 internal error
	 */
	@WebMethod
	@WebResult
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
	@WebResult
	int registerArchetype(
			@WebParam String archetypeId,
			@WebParam String archetype,
			@WebParam String arm);

	/**
	 * @param aql
	 * @param archetypeId
	 * @return
	 * -1 service running
	 * @throws Exception
	 */
	@WebMethod
	@WebResult
	List<String> select(@WebParam String aql) throws Exception;

	/**
	 * @param aql
	 * @return
	 * -1 service running
	 * @throws Exception
	 */
//	@WebMethod
//	@WebResult
//	List<String> select(@WebParam String aql, @WebParam String archetypeId) throws Exception;

	/**
	 * @param aql
	 * @param listParameterName
	 * @param listParameterValue
	 * @return
	 * -1 service running
	 * @throws Exception
	 */
//	@WebMethod
//	@WebResult
//	List<String> select(@WebParam String aql, 
//			@WebParam Map<String, Object> parameters) throws Exception;

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
//	@WebResult
//	List<String> select(@WebParam String aql, @WebParam String archetypeId,
//			@WebParam Map<String, Object> parameters) throws Exception;

	/**
	 * @param aql
	 * @param parameters
	 * @return
	 * -1 service running
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 * @throws RMObjectBuildingException
	 * @throws DADLBindingException
	 */
	@WebMethod
	@WebResult
	void insert(@WebParam List<String> dadls)
			throws UnsupportedEncodingException, ParseException,
			DADLBindingException, RMObjectBuildingException;

	/**
	 * @param aql
	 * @return
	 * -1 service running
	 */
	@WebMethod
	@WebResult
	int delete(@WebParam String aql);
	
	/**
	 * @param aql
	 * @param parameters
	 * @return
	 * -1 service running
	 */
//	@WebMethod
//	@WebResult
//	int delete(@WebParam String aql, @WebParam Map<String, Object> parameters);

	/**
	 * @param aql
	 * @return
	 * -1 service running
	 */
	@WebMethod
	@WebResult
	int update(@WebParam String aql);

	/**
	 * @param aql
	 * @param parameters
	 * @return
	 * -1 service running
	 */
//	@WebMethod
//	@WebResult
//	int update(@WebParam String aql, @WebParam Map<String, Object> parameters);

}
