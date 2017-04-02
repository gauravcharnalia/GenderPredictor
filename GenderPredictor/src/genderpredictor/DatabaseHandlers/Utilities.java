package genderpredictor.DatabaseHandlers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * @author Gaurav
 *
 */
public class Utilities {
	// Logging
    private static final Logger LOG = Logger.getLogger(CassandraConnector.class.getName());
		
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
	
	public static HashMap<String, Double> avgNameLength(HashMap<String, String> trainSet, int nameLen) {
		Double feTotal = 1.0, mTotal = 1.0;
		Double feNameLength = 0.0, mNameLength = 0.0;
		Double pLen_Female, pLen_Male, pFemale, pMale;
		Double feNamesWithLen = 0.0, mNamesWithLen = 0.0;
		for(Map.Entry<String, String> data: trainSet.entrySet()) {
			if (data.getValue().equals("F")) {
				feNameLength += data.getKey().length();
				feTotal++;
				if (data.getKey().length() == nameLen)
					feNamesWithLen++;
			}
			else {
				mNameLength += data.getKey().length();
				mTotal++;
				if (data.getKey().length() == nameLen)
					mNamesWithLen++;
			}
		}
		HashMap<String, Double> avgLength = new HashMap<String, Double>();
		//LOG.info("Total female count: " + feTotal + " giving total length: " + feNameLength);
		//LOG.info("Total male count: " + mTotal + " giving total length: " + mNameLength);
		avgLength.put("F", (double) (feNameLength/feTotal));
		avgLength.put("M", (double) (mNameLength/mTotal));
		
		pFemale = feTotal/trainSet.size();
		pMale = mTotal/trainSet.size();
		
		pLen_Female = (feNamesWithLen/feTotal)*pFemale;
		pLen_Male = (mNamesWithLen/mTotal)*pMale;
		
		avgLength.put("PLF", pLen_Female);
		avgLength.put("PLM", pLen_Male);
		
		//LOG.info("P(Length=" + nameLen + "|Female): " + pLen_Female + "P(Length=" + nameLen + "|Male): " + pLen_Male);
		
		return avgLength;
	}
}
