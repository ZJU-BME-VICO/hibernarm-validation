package adr.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

public class AQLExecuteTestBase {

	protected String[] getDadlFiles() {
		return new String[] {
				"adr/ws/openEHR-EHR-OBSERVATION.blood_pressure.v1.1.dadl",
				"adr/ws/openEHR-EHR-OBSERVATION.blood_pressure.v1.2.dadl", };
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
