
package adr.ws;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class AQLExecuteTest extends AQLExecuteTestBase {
	
	@Test
	public void testReconfigure() throws IOException {
    	AQLExecute aqlImpl = new AQLExecuteImpl();
    	
    	aqlImpl.registerArchetype("openEHR-EHR-OBSERVATION.blood_pressure.v1", 
    			readLines("adr/ws/openEHR-EHR-OBSERVATION.blood_pressure.v1.adl"));
    	aqlImpl.registerArm("openEHR-EHR-OBSERVATION.blood_pressure.v1", 
    			readLines("adr/ws/openEHR-EHR-OBSERVATION.blood_pressure.v1.arm.xml"));
    	
    	assertTrue(aqlImpl.reconfigure());
	}
	
    @Test
	public void testSelect() throws IOException {
    	testReconfigure();
    	
		String query = "select " + 
				"o#/data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/magnitude as /data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/magnitude, " + 
				"o#/data[at0001]/events[at0006]/data[at0003]/items[at0005]/value/magnitude as /data[at0001]/events[at0006]/data[at0003]/items[at0005]/value/magnitude " + 
				"from openEHR-EHR-OBSERVATION.blood_pressure.v1 as o";
		String archetypeId = "openEHR-EHR-OBSERVATION.blood_pressure.v1";
		
    	AQLExecute aqlImpl = new AQLExecuteImpl();    
    	List<String> responses = aqlImpl.select(query, archetypeId, null);
    	assertTrue(responses.size() > 0);
    	
    	for (String string : responses) {
			System.out.println(string);
		}
    }
    
}
