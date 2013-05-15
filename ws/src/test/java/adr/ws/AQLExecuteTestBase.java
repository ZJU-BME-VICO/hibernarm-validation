package adr.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AQLExecuteTestBase {
	protected static Map<String, String> archetypes = new HashMap<String, String>();
	protected static Map<String, String> arms = new HashMap<String, String>();

	protected static void initialArchetypes() throws IOException {
		archetypes.put("openEHR-EHR-OBSERVATION.blood_pressure.v1", 
				readLines("atr/rest/openEHR-EHR-OBSERVATION.blood_pressure.v1.adl"));
		arms.put("openEHR-EHR-OBSERVATION.blood_pressure.v1", 
				readLines("atr/rest/openEHR-EHR-OBSERVATION.blood_pressure.v1.arm.xml"));
	}

	protected static String readLines(String name) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				Thread.currentThread().getContextClassLoader().getResourceAsStream(name)));

		String line = reader.readLine();
		while (line != null) {
			result.append(line);
			result.append("\n");
			line = reader.readLine();
		}
		return result.toString();
	}

}
