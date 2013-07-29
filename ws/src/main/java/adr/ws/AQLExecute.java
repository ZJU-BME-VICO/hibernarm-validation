package adr.ws;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

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
	 */
	@WebMethod
	@WebResult
	int reconfigure();

	/**
	 * @param archetypeId
	 * @param archetype
	 * @param arm
	 * @return
	 */
	@WebMethod
	@WebResult
	void registerArchetype(
			@WebParam String archetypeId,
			@WebParam String archetype,
			@WebParam String arm);

	/**
	 * @param aql
	 * @param archetypeId
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult
	List<String> select(@WebParam String aql) throws Exception;

	/**
	 * @param aql
	 * @return
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
	 */
	@WebMethod
	@WebResult
	int delete(@WebParam String aql);
	
	/**
	 * @param aql
	 * @param parameters
	 * @return
	 */
//	@WebMethod
//	@WebResult
//	int delete(@WebParam String aql, @WebParam Map<String, Object> parameters);

	/**
	 * @param aql
	 * @return
	 */
	@WebMethod
	@WebResult
	int update(@WebParam String aql);

	/**
	 * @param aql
	 * @param parameters
	 * @return
	 */
//	@WebMethod
//	@WebResult
//	int update(@WebParam String aql, @WebParam Map<String, Object> parameters);

}
