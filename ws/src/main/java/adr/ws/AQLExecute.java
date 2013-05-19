package adr.ws;

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
	 */
	@WebMethod
	@WebResult
	Boolean reconfigure();

	/**
	 * @param archetypeId
	 * @param archetype
	 * @return
	 */
	@WebMethod
	@WebResult
	String registerArchetype(@WebParam String archetypeId,
			@WebParam String archetype);

	/**
	 * @param archetypeId
	 * @param arm
	 * @return
	 */
	@WebMethod
	@WebResult
	String registerArm(@WebParam String archetypeId, @WebParam String arm);

	/**
	 * @param aql
	 * @param archetypeId
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult
	List<String> select(@WebParam String aql, @WebParam String archetypeId,
			@WebParam List<String> parameters) throws Exception;

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

}
