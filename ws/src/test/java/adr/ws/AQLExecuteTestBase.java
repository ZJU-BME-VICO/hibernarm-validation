package adr.ws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.archetype.ArchetypeRepository;
import org.hibernate.internal.util.ReflectHelper;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.DADLParser;
import org.openehr.rm.binding.DADLBinding;
import org.openehr.rm.common.archetyped.Locatable;
import org.openehr.rm.composition.content.entry.Observation;
import org.openehr.rm.support.identification.HierObjectID;
import org.openehr.rm.util.GenerationStrategy;
import org.openehr.rm.util.SkeletonGenerator;

public class AQLExecuteTestBase {
	protected Map<String, String> archetypes = new HashMap<String, String>();
	protected Map<String, String> arms = new HashMap<String, String>();
	
	public AQLExecuteTestBase() throws IOException {
		archetypes.put(
				"openEHR-EHR-OBSERVATION.blood_pressure.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/CKM/entry/observation/openEHR-EHR-OBSERVATION.blood_pressure.v1.adl"));
		arms.put(
				"openEHR-EHR-OBSERVATION.blood_pressure.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/CKM/entry/observation/openEHR-EHR-OBSERVATION.blood_pressure.v1.arm.xml"));

		archetypes.put("openEHR-DEMOGRAPHIC-PERSON.patient.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/openEHR-DEMOGRAPHIC-PERSON.patient.v1.adl"));
		arms.put(
				"openEHR-DEMOGRAPHIC-PERSON.patient.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/openEHR-DEMOGRAPHIC-PERSON.patient.v1.arm.xml"));

		archetypes.put("openEHR-EHR-COMPOSITION.visit.v3",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/openEHR-EHR-COMPOSITION.visit.v3.adl"));
		arms.put("openEHR-EHR-COMPOSITION.visit.v3",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/openEHR-EHR-COMPOSITION.visit.v3.arm.xml"));

		archetypes.put("openEHR-EHR-OBSERVATION.adl.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/ad/openEHR-EHR-OBSERVATION.adl.v1.adl"));
		arms.put("openEHR-EHR-OBSERVATION.adl.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/ad/openEHR-EHR-OBSERVATION.adl.v1.arm.xml"));

		archetypes.put("openEHR-EHR-OBSERVATION.cdr.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/ad/openEHR-EHR-OBSERVATION.cdr.v1.adl"));
		arms.put("openEHR-EHR-OBSERVATION.cdr.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/ad/openEHR-EHR-OBSERVATION.cdr.v1.arm.xml"));

		archetypes.put("openEHR-EHR-OBSERVATION.gds.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/ad/openEHR-EHR-OBSERVATION.gds.v1.adl"));
		arms.put("openEHR-EHR-OBSERVATION.gds.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/ad/openEHR-EHR-OBSERVATION.gds.v1.arm.xml"));

		archetypes.put("openEHR-EHR-OBSERVATION.mmse.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/ad/openEHR-EHR-OBSERVATION.mmse.v1.adl"));
		arms.put("openEHR-EHR-OBSERVATION.mmse.v1",
				readLines("../../../../../CDRDocument/knowledge/archetype/ZJU/ad/openEHR-EHR-OBSERVATION.mmse.v1.arm.xml"));		
	}

	protected String[] getDadlFiles() {
		return new String[] {
				"../../../../../CDRDocument/knowledge/archetype/CKM/entry/observation/openEHR-EHR-OBSERVATION.blood_pressure.v1.1.dadl",
				"../../../../../CDRDocument/knowledge/archetype/CKM/entry/observation/openEHR-EHR-OBSERVATION.blood_pressure.v1.2.dadl", };
	}

	protected Map<HashMap<String, Object>, String> getArchetypeValues() {
		Map<HashMap<String, Object>, String> results = new HashMap<HashMap<String, Object>, String>();

		HashMap<String, Object> patient1 = new HashMap<String, Object>();
		patient1.put("/uid/value", "patient1");
		patient1.put("/details[at0001]/items[at0003]/value/value", "M");
		patient1.put("/details[at0001]/items[at0004]/value/value",
				"1984-08-11T19:20:30+08:00");
		patient1.put("/details[at0001]/items[at0009]/value/value", "zhangsan");
		results.put(patient1, "openEHR-DEMOGRAPHIC-PERSON.patient.v1");

		HashMap<String, Object> patient2 = new HashMap<String, Object>();
		patient2.put("/uid/value", "patient2");
		patient2.put("/details[at0001]/items[at0003]/value/value", "F");
		patient2.put("/details[at0001]/items[at0004]/value/value",
				"1986-08-11T19:20:30+08:00");
		patient2.put("/details[at0001]/items[at0009]/value/value", "lisi");
		results.put(patient2, "openEHR-DEMOGRAPHIC-PERSON.patient.v1");

		HashMap<String, Object> patient3 = new HashMap<String, Object>();
		patient3.put("/uid/value", "patient3");
		patient3.put("/details[at0001]/items[at0003]/value/value", "O");
		patient3.put("/details[at0001]/items[at0004]/value/value",
				"1988-08-11T19:20:30+08:00");
		patient3.put("/details[at0001]/items[at0009]/value/value", "wangwu");
		results.put(patient3, "openEHR-DEMOGRAPHIC-PERSON.patient.v1");

		HashMap<String, Object> visit1 = new HashMap<String, Object>();
		visit1.put("/uid/value", "visit1");
		visit1.put("/context/other_context[at0001]/items[at0007]/value/value",
				"2010-01-15T19:20:30+08:00");
		visit1.put("/context/other_context[at0001]/items[at0015]/value/value",
				"patient1");
		results.put(visit1, "openEHR-EHR-COMPOSITION.visit.v3");

		HashMap<String, Object> visit2 = new HashMap<String, Object>();
		visit2.put("/uid/value", "visit2");
		visit2.put("/context/other_context[at0001]/items[at0007]/value/value",
				"2010-01-25T19:20:30+08:00");
		visit2.put("/context/other_context[at0001]/items[at0015]/value/value",
				"patient1");
		results.put(visit2, "openEHR-EHR-COMPOSITION.visit.v3");

		HashMap<String, Object> visit3 = new HashMap<String, Object>();
		visit3.put("/uid/value", "visit3");
		visit3.put("/context/other_context[at0001]/items[at0007]/value/value",
				"2011-02-05T19:20:30+08:00");
		visit3.put("/context/other_context[at0001]/items[at0015]/value/value",
				"patient2");
		results.put(visit3, "openEHR-EHR-COMPOSITION.visit.v3");

		return results;
	}

	protected static String readLines(String name) throws IOException {
		StringBuilder result = new StringBuilder();
		File file = new File(name);
		InputStream is = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		String line = reader.readLine();
		while (line != null) {
			result.append(line);
			result.append("\n");
			line = reader.readLine();
		}
		return result.toString();
	}

}