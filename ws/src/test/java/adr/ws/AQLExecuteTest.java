package adr.ws;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.DADLParser;
import org.openehr.am.parser.ParseException;
import org.openehr.build.RMObjectBuildingException;
import org.openehr.rm.binding.DADLBinding;
import org.openehr.rm.binding.DADLBindingException;
import org.openehr.rm.composition.content.entry.Observation;
import org.openehr.rm.support.identification.HierObjectID;

public class AQLExecuteTest extends AQLExecuteTestBase {

	@Test
	public void testReconfigure() throws IOException {
		AQLExecute aqlImpl = new AQLExecuteImpl();

		aqlImpl.registerArchetype(
				"openEHR-EHR-OBSERVATION.blood_pressure.v1",
				readLines("adr/ws/openEHR-EHR-OBSERVATION.blood_pressure.v1.adl"));
		aqlImpl.registerArm(
				"openEHR-EHR-OBSERVATION.blood_pressure.v1",
				readLines("adr/ws/openEHR-EHR-OBSERVATION.blood_pressure.v1.arm.xml"));

		aqlImpl.registerArchetype("openEHR-DEMOGRAPHIC-PERSON.patient.v1",
				readLines("adr/ws/openEHR-DEMOGRAPHIC-PERSON.patient.v1.adl"));
		aqlImpl.registerArm(
				"openEHR-DEMOGRAPHIC-PERSON.patient.v1",
				readLines("adr/ws/openEHR-DEMOGRAPHIC-PERSON.patient.v1.arm.xml"));

		aqlImpl.registerArchetype("openEHR-EHR-COMPOSITION.visit.v3",
				readLines("adr/ws/openEHR-EHR-COMPOSITION.visit.v3.adl"));
		aqlImpl.registerArm("openEHR-EHR-COMPOSITION.visit.v3",
				readLines("adr/ws/openEHR-EHR-COMPOSITION.visit.v3.arm.xml"));

		aqlImpl.registerArchetype("openEHR-EHR-OBSERVATION.adl.v1",
				readLines("adr/ws/ad/openEHR-EHR-OBSERVATION.adl.v1.adl"));
		aqlImpl.registerArm("openEHR-EHR-OBSERVATION.adl.v1",
				readLines("adr/ws/ad/openEHR-EHR-OBSERVATION.adl.v1.arm.xml"));

		aqlImpl.registerArchetype("openEHR-EHR-OBSERVATION.cdr.v1",
				readLines("adr/ws/ad/openEHR-EHR-OBSERVATION.cdr.v1.adl"));
		aqlImpl.registerArm("openEHR-EHR-OBSERVATION.cdr.v1",
				readLines("adr/ws/ad/openEHR-EHR-OBSERVATION.cdr.v1.arm.xml"));

		aqlImpl.registerArchetype("openEHR-EHR-OBSERVATION.gds.v1",
				readLines("adr/ws/ad/openEHR-EHR-OBSERVATION.gds.v1.adl"));
		aqlImpl.registerArm("openEHR-EHR-OBSERVATION.gds.v1",
				readLines("adr/ws/ad/openEHR-EHR-OBSERVATION.gds.v1.arm.xml"));

		aqlImpl.registerArchetype("openEHR-EHR-OBSERVATION.mmse.v1",
				readLines("adr/ws/ad/openEHR-EHR-OBSERVATION.mmse.v1.adl"));
		aqlImpl.registerArm("openEHR-EHR-OBSERVATION.mmse.v1",
				readLines("adr/ws/ad/openEHR-EHR-OBSERVATION.mmse.v1.arm.xml"));

		assertTrue(aqlImpl.reconfigure());
	}

	@Test
	public void testSelect() throws Exception {
		testReconfigure();

		String query = "select "
				+ "o#/data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/magnitude as /data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/magnitude, "
				+ "o#/data[at0001]/events[at0006]/data[at0003]/items[at0005]/value/magnitude as /data[at0001]/events[at0006]/data[at0003]/items[at0005]/value/magnitude "
				+ "from openEHR-EHR-OBSERVATION.blood_pressure.v1 as o";
		String archetypeId = "openEHR-EHR-OBSERVATION.blood_pressure.v1";

		AQLExecute aqlImpl = new AQLExecuteImpl();
		List<String> responses = aqlImpl.select(query, archetypeId, null);
		assertTrue(responses.size() > 0);

		for (String string : responses) {
			System.out.println(string);
		}
	}

	@Test
	public void testInsert() throws Exception {
		testReconfigure();

		String query = "select "
				+ "o#/data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/magnitude as /data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/magnitude, "
				+ "o#/data[at0001]/events[at0006]/data[at0003]/items[at0005]/value/magnitude as /data[at0001]/events[at0006]/data[at0003]/items[at0005]/value/magnitude "
				+ "from openEHR-EHR-OBSERVATION.blood_pressure.v1 as o";
		String archetypeId = "openEHR-EHR-OBSERVATION.blood_pressure.v1";

		AQLExecute aqlImpl = new AQLExecuteImpl();
		List<String> responses = aqlImpl.select(query, archetypeId, null);
		int count = responses.size();

		List<String> dadls = new ArrayList<String>();
		for (String dadl : getDadlFiles()) {
			InputStream is = Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(dadl);
			DADLParser parser = new DADLParser(is);
			ContentObject contentObj = parser.parse();
			DADLBinding binding = new DADLBinding();
			Observation bp = (Observation) binding.bind(contentObj);
			UUID uuid = UUID.randomUUID();
			HierObjectID uid = new HierObjectID(uuid.toString());
			bp.setUid(uid);
			dadls.add(binding.toDADLString(bp));
		}

		aqlImpl.insert(dadls);

		responses = aqlImpl.select(query, archetypeId, null);
		assertEquals(responses.size(), count + 2);
	}

	@Test
	public void testDelete() throws Exception {
		testReconfigure();

		AQLExecute aqlImpl = new AQLExecuteImpl();
		String queryDelete = "delete from openEHR-EHR-OBSERVATION.blood_pressure.v1";
		aqlImpl.delete(queryDelete, null);

		String query = "select "
				+ "o#/data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/magnitude as /data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/magnitude, "
				+ "o#/data[at0001]/events[at0006]/data[at0003]/items[at0005]/value/magnitude as /data[at0001]/events[at0006]/data[at0003]/items[at0005]/value/magnitude "
				+ "from openEHR-EHR-OBSERVATION.blood_pressure.v1 as o";
		String archetypeId = "openEHR-EHR-OBSERVATION.blood_pressure.v1";

		List<String> responses = aqlImpl.select(query, archetypeId, null);
		assertEquals(responses.size(), 0);
	}

}
