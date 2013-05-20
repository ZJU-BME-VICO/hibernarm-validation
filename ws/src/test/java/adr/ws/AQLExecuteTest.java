package adr.ws;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.internal.util.ReflectHelper;
import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.DADLParser;
import org.openehr.rm.binding.DADLBinding;
import org.openehr.rm.common.archetyped.Locatable;
import org.openehr.rm.composition.content.entry.Observation;
import org.openehr.rm.support.identification.HierObjectID;
import org.openehr.rm.util.GenerationStrategy;
import org.openehr.rm.util.SkeletonGenerator;

import se.acode.openehr.parser.ADLParser;

public class AQLExecuteTest extends AQLExecuteTestBase {

	public AQLExecuteTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void createTestBaseData() throws Exception {
		AQLExecute aqlImpl = new AQLExecuteImpl();
		DADLBinding binding = new DADLBinding();

		List<String> dadls = new ArrayList<String>();
		Map<HashMap<String, Object>, String> archetypeValues = getArchetypeValues();
		for (HashMap<String, Object> values : archetypeValues.keySet()) {
			String archetypeId = archetypeValues.get(values);
			SkeletonGenerator generator = SkeletonGenerator.getInstance();
			String archetypeString = archetypes.get(archetypeId);
			ADLParser parser = new ADLParser(archetypeString);
			Archetype archetype = parser.parse();
			Object result = generator.create(archetype,
					GenerationStrategy.MAXIMUM_EMPTY);
			if (result instanceof Locatable) {
				Locatable loc = (Locatable) result;
				ReflectHelper.setArchetypeValue(archetype, loc, values);
				dadls.add(binding.toDADLString(loc));
			}
		}

		aqlImpl.insert(dadls);
	}
	
	protected void cleanTestBaseData() {
		AQLExecute aqlImpl = new AQLExecuteImpl();

		for (String str : archetypes.keySet()) {
			String aql = String.format("delete from %s as o", str);
			aqlImpl.delete(aql, null);
		}
	}

	public void testReconfigure() throws IOException {
		AQLExecute aqlImpl = new AQLExecuteImpl();
		
		for (String str : archetypes.keySet()) {
			aqlImpl.registerArchetype(str, archetypes.get(str));
		}
		
		for (String str : arms.keySet()) {
			aqlImpl.registerArm(str, arms.get(str));
		}

		assertTrue(aqlImpl.reconfigure());
		
		cleanTestBaseData();
	}

	@Test
	public void testSelect() throws Exception {
		testReconfigure();
		
		createTestBaseData();
		
		AQLExecute aqlImpl = new AQLExecuteImpl();
		DADLBinding binding = new DADLBinding();

		{
			String query = "select "
					+ "o#/uid/value as /uid/value, "
					+ "o#/details[at0001]/items[at0003]/value/value as /details[at0001]/items[at0003]/value/value, "
					+ "o#/details[at0001]/items[at0004]/value/value as /details[at0001]/items[at0004]/value/value, "
					+ "o#/details[at0001]/items[at0009]/value/value as /details[at0001]/items[at0009]/value/value "
					+ "from openEHR-DEMOGRAPHIC-PERSON.patient.v1 as o ";
			String archetypeId = "openEHR-DEMOGRAPHIC-PERSON.patient.v1";
			List<String> results = aqlImpl.select(query, archetypeId, null);

			assertEquals(results.size(), 3);

			DADLParser parser1 = new DADLParser(results.get(0));
			ContentObject contentObj1 = parser1.parse();
			Locatable loc1 = (Locatable) binding.bind(contentObj1);
			String d1 = (String) loc1.itemAtPath("/uid/value");
			String d2 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0003]/value/value");
			String d3 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0004]/value/value");
			String d4 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0009]/value/value");
			assertEquals(d1, "patient1");
			assertEquals(d2, "M");
			assertEquals(d3, "1984-08-11T19:20:30+08:00");
			assertEquals(d4, "zhangsan");

			DADLParser parser2 = new DADLParser(results.get(1));
			ContentObject contentObj2 = parser2.parse();
			Locatable loc2 = (Locatable) binding.bind(contentObj2);
			String d5 = (String) loc2.itemAtPath("/uid/value");
			String d6 = (String) loc2
					.itemAtPath("/details[at0001]/items[at0003]/value/value");
			String d7 = (String) loc2
					.itemAtPath("/details[at0001]/items[at0004]/value/value");
			String d8 = (String) loc2
					.itemAtPath("/details[at0001]/items[at0009]/value/value");
			assertEquals(d5, "patient2");
			assertEquals(d6, "F");
			assertEquals(d7, "1986-08-11T19:20:30+08:00");
			assertEquals(d8, "lisi");

			DADLParser parser3 = new DADLParser(results.get(2));
			ContentObject contentObj3 = parser3.parse();
			Locatable loc3 = (Locatable) binding.bind(contentObj3);
			String d9 = (String) loc3.itemAtPath("/uid/value");
			String d10 = (String) loc3
					.itemAtPath("/details[at0001]/items[at0003]/value/value");
			String d11 = (String) loc3
					.itemAtPath("/details[at0001]/items[at0004]/value/value");
			String d12 = (String) loc3
					.itemAtPath("/details[at0001]/items[at0009]/value/value");
			assertEquals(d9, "patient3");
			assertEquals(d10, "O");
			assertEquals(d11, "1988-08-11T19:20:30+08:00");
			assertEquals(d12, "wangwu");
		}

		{
			String query = "select "
					+ "o#/uid/value as /uid/value, "
					+ "o#/details[at0001]/items[at0003]/value/value as /details[at0001]/items[at0003]/value/value, "
					+ "o#/details[at0001]/items[at0004]/value/value as /details[at0001]/items[at0004]/value/value, "
					+ "o#/details[at0001]/items[at0009]/value/value as /details[at0001]/items[at0009]/value/value "
					+ "from openEHR-DEMOGRAPHIC-PERSON.patient.v1 as o "
					+ "where o#/details[at0001]/items[at0009]/value/value = :name";
			String archetypeId = "openEHR-DEMOGRAPHIC-PERSON.patient.v1";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", "lisi");
			List<String> results = aqlImpl.select(query, archetypeId, parameters);

			assertEquals(results.size(), 1);

			DADLParser parser2 = new DADLParser(results.get(0));
			ContentObject contentObj2 = parser2.parse();
			Locatable loc2 = (Locatable) binding.bind(contentObj2);
			String d5 = (String) loc2.itemAtPath("/uid/value");
			String d6 = (String) loc2
					.itemAtPath("/details[at0001]/items[at0003]/value/value");
			String d7 = (String) loc2
					.itemAtPath("/details[at0001]/items[at0004]/value/value");
			String d8 = (String) loc2
					.itemAtPath("/details[at0001]/items[at0009]/value/value");
			assertEquals(d5, "patient2");
			assertEquals(d6, "F");
			assertEquals(d7, "1986-08-11T19:20:30+08:00");
			assertEquals(d8, "lisi");
		}

		{
			String query = "select "
					+ "o#/uid/value as /uid/value, "
					+ "o#/details[at0001]/items[at0003]/value/value as /details[at0001]/items[at0003]/value/value, "
					+ "o#/details[at0001]/items[at0004]/value/value as /details[at0001]/items[at0004]/value/value, "
					+ "o#/details[at0001]/items[at0009]/value/value as /details[at0001]/items[at0009]/value/value "
					+ "from openEHR-DEMOGRAPHIC-PERSON.patient.v1 as o "
					+ "where o#/uid/value = :name";
			String archetypeId = "openEHR-DEMOGRAPHIC-PERSON.patient.v1";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", "patient1");
			List<String> results = aqlImpl.select(query, archetypeId, parameters);

			assertEquals(results.size(), 1);
			
			DADLParser parser1 = new DADLParser(results.get(0));
			ContentObject contentObj1 = parser1.parse();
			Locatable loc1 = (Locatable) binding.bind(contentObj1);
			String d1 = (String) loc1.itemAtPath("/uid/value");
			String d2 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0003]/value/value");
			String d3 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0004]/value/value");
			String d4 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0009]/value/value");
			assertEquals(d1, "patient1");
			assertEquals(d2, "M");
			assertEquals(d3, "1984-08-11T19:20:30+08:00");
			assertEquals(d4, "zhangsan");
		}

		{
			String query = "select "
					+ "o#/uid/value as /uid/value, "
					+ "o#/context/other_context[at0001]/items[at0007]/value/value as /context/other_context[at0001]/items[at0007]/value/value, "
					+ "o#/context/other_context[at0001]/items[at0015]/value/value as /context/other_context[at0001]/items[at0015]/value/value "
					+ "from openEHR-EHR-COMPOSITION.visit.v3 as o "
					+ "where o#/context/other_context[at0001]/items[at0015]/value/value = :pid";
			String archetypeId = "openEHR-EHR-COMPOSITION.visit.v3";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("pid", "patient1");
			List<String> results = aqlImpl.select(query, archetypeId, parameters);

			assertEquals(results.size(), 2);
		}

		{
			String query = "select "
					+ "o#/uid/value as /uid/value, "
					+ "o#/context/other_context[at0001]/items[at0007]/value/value as /context/other_context[at0001]/items[at0007]/value/value, "
					+ "o#/context/other_context[at0001]/items[at0015]/value/value as /context/other_context[at0001]/items[at0015]/value/value "
					+ "from openEHR-EHR-COMPOSITION.visit.v3 as o "
					+ "where o#/uid/value = :name and o#/context/other_context[at0001]/items[at0015]/value/value = :pid";
			String archetypeId = "openEHR-EHR-COMPOSITION.visit.v3";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", "visit1");
			parameters.put("pid", "patient1");
			List<String> results = aqlImpl.select(query, archetypeId, parameters);

			assertEquals(results.size(), 1);
		}
	}

	@Test
	public void testInsert() throws Exception {
		testReconfigure();

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

		AQLExecute aqlImpl = new AQLExecuteImpl();
		aqlImpl.insert(dadls);

		String query = "select "
				+ "o#/data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/magnitude as /data[at0001]/events[at0006]/data[at0003]/items[at0004]/value/magnitude, "
				+ "o#/data[at0001]/events[at0006]/data[at0003]/items[at0005]/value/magnitude as /data[at0001]/events[at0006]/data[at0003]/items[at0005]/value/magnitude "
				+ "from openEHR-EHR-OBSERVATION.blood_pressure.v1 as o";
		String archetypeId = "openEHR-EHR-OBSERVATION.blood_pressure.v1";
		assertEquals(aqlImpl.select(query, archetypeId, null).size(), 2);
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

	@Test
	public void testUpdate() throws Exception {
		testReconfigure();
		
		createTestBaseData();

		AQLExecute aqlImpl = new AQLExecuteImpl();
		DADLBinding binding = new DADLBinding();
		
		{
			String query = "select "
					+ "o#/uid/value as /uid/value, "
					+ "o#/details[at0001]/items[at0003]/value/value as /details[at0001]/items[at0003]/value/value, "
					+ "o#/details[at0001]/items[at0004]/value/value as /details[at0001]/items[at0004]/value/value, "
					+ "o#/details[at0001]/items[at0009]/value/value as /details[at0001]/items[at0009]/value/value "
					+ "from openEHR-DEMOGRAPHIC-PERSON.patient.v1 as o "
					+ "where o#/uid/value = :name";
			String archetypeId = "openEHR-DEMOGRAPHIC-PERSON.patient.v1";			

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", "patient1");
			List<String> results = aqlImpl.select(query, archetypeId, parameters);

			assertEquals(results.size(), 1);
			
			DADLParser parser1 = new DADLParser(results.get(0));
			ContentObject contentObj1 = parser1.parse();
			Locatable loc1 = (Locatable) binding.bind(contentObj1);
			String d1 = (String) loc1.itemAtPath("/uid/value");
			String d2 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0003]/value/value");
			String d3 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0004]/value/value");
			String d4 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0009]/value/value");
			assertEquals(d1, "patient1");
			assertEquals(d2, "M");
			assertEquals(d3, "1984-08-11T19:20:30+08:00");
			assertEquals(d4, "zhangsan");
		}

		{
			String query = "update openEHR-DEMOGRAPHIC-PERSON.patient.v1 as o set "
					+ "o#/details[at0001]/items[at0009]/value/value = :name, "
					+ "o#/details[at0001]/items[at0004]/value/value = :birthday "
					+ "where " + "o#/uid/value = :pid ";

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", "lisi");
			parameters.put("birthday", "1994-08-11T19:20:30+08:00");
			parameters.put("pid", "patient1");
			int ret = aqlImpl.update(query, parameters);

			assertEquals(ret, 1);
		}

		{
			String query = "select "
					+ "o#/uid/value as /uid/value, "
					+ "o#/details[at0001]/items[at0003]/value/value as /details[at0001]/items[at0003]/value/value, "
					+ "o#/details[at0001]/items[at0004]/value/value as /details[at0001]/items[at0004]/value/value, "
					+ "o#/details[at0001]/items[at0009]/value/value as /details[at0001]/items[at0009]/value/value "
					+ "from openEHR-DEMOGRAPHIC-PERSON.patient.v1 as o "
					+ "where o#/uid/value = :name";
			String archetypeId = "openEHR-DEMOGRAPHIC-PERSON.patient.v1";

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", "patient1");
			List<String> results = aqlImpl.select(query, archetypeId, parameters);
			
			assertEquals(results.size(), 1);

			DADLParser parser1 = new DADLParser(results.get(0));
			ContentObject contentObj1 = parser1.parse();
			Locatable loc1 = (Locatable) binding.bind(contentObj1);
			String d1 = (String) loc1.itemAtPath("/uid/value");
			String d2 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0003]/value/value");
			String d3 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0004]/value/value");
			String d4 = (String) loc1
					.itemAtPath("/details[at0001]/items[at0009]/value/value");
			assertEquals(d1, "patient1");
			assertEquals(d2, "M");
			assertEquals(d3, "1994-08-11T19:20:30+08:00");
			assertEquals(d4, "lisi");
		}
	}

}
