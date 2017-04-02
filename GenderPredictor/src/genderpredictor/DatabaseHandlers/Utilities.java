package genderpredictor.DatabaseHandlers;

import java.util.HashMap;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * @author Gaurav
 *
 */
public class Utilities {
	
	public static HashMap<String, String> getTrainSet(Session session) {
		HashMap<String, String> trainSet = new HashMap<String, String>();
		String query = "SELECT * FROM genderTableTrain;";
		ResultSet result = session.execute(query);
		
		result.forEach((Row row) -> {
			trainSet.put(row.getString("indianname"), row.getString("gender"));
		});
		
		return trainSet;
	}
	
	public static HashMap<String, String> getTestSet(Session session) {
		HashMap<String, String> testSet = new HashMap<String, String>();
		String query = "SELECT * FROM genderTableTest;";
		ResultSet result = session.execute(query);
		
		result.forEach((Row row) -> {
			testSet.put(row.getString("indianname"), row.getString("gender"));
		});
		
		return testSet;
	}
}
