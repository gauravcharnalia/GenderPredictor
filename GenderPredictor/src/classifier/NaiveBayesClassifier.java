package classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.datastax.driver.core.Session;

import genderpredictor.DatabaseHandlers.CassandraConnector;
import genderpredictor.DatabaseHandlers.Utilities;

/**
 * @author Gaurav
 *
 */
public class NaiveBayesClassifier {
	// Logging
    private static final Logger LOG = Logger.getLogger(CassandraConnector.class.getName());
	
	private HashMap<String, String> trainSet;
	private HashMap<String, String> testSet;
	
	public void classify(String name) {
		String predictedGender = (this.nameLengthFeature(name).equals("F")) ? "Female" : "Male";
		System.out.println(predictedGender + "\tBy P(class|Length) and average name lenght ");
		
		predictedGender = (this.namesEndingInVowelFeature(name).equals("F")) ? "Female" : "Male";
		System.out.println(predictedGender + "\tBy P(class|ending=vowel) ");
	}
	
	public NaiveBayesClassifier(Session session) {
		this.trainSet = Utilities.getTrainSet(session);
		LOG.info("No of training data records: " + this.trainSet.size());
		this.testSet = Utilities.getTestSet(session);
		LOG.info("No of testing data records: " + this.testSet.size() + "\n");
	}
	
	private String nameLengthFeature(String name) {
		Double len = (double) name.length();
		HashMap<String, Double> avgLength = Utilities.avgNameLength(this.trainSet, name.length());
		String genderLen = (Math.abs(len-avgLength.get("F")) < Math.abs(len - avgLength.get("M"))) ? "F" : "M";
		String genderProb = (avgLength.get("PFL")>avgLength.get("PML")) ? "F" : "M" ;
		//LOG.info(name + " Prediction on len: " + genderLen + " Prediction on prob: " + genderProb);
		return (genderLen.equals("F") || genderProb.equals("F")) ? "F" : "M"; //Prediction with respect to Females
	}
	
	private String namesEndingInVowelFeature(String name) {
		List<String> vowels = new ArrayList<>(Arrays.asList("A","E","I","O","U"));
		if (vowels.contains(name.substring(name.length() - 1 ))) {
			return "F";
		}
		else {
			return "M";
		}
	}
	
	public void testModel() {
		this.testNameLengthFeatureSet();
		this.testnamesEndingInVowelFeatureSet();
	}
	
	private void testnamesEndingInVowelFeatureSet() {
		Double rightPre = 0.0;
		Double total = (double) this.testSet.size();
		String gender;
		for(Map.Entry<String, String> data: this.testSet.entrySet()) {
			gender = this.namesEndingInVowelFeature(data.getKey());
			rightPre = (gender.equals(data.getValue())) ? ++rightPre : rightPre;
		}
		Utilities.namesEndingInVowel(this.trainSet);
		System.out.println("Right predictions " + rightPre + " out of: " + total);
		Double accuracy = (rightPre/total)*100;
		System.out.println("Accuracy of Name ending in vowel feature set: " + accuracy + "%\n");
	}
	
	private void testNameLengthFeatureSet() {
		Double rightPre = 0.0;
		Double total = (double) this.testSet.size();
		String gender;
		for(Map.Entry<String, String> data: this.testSet.entrySet()) {
			gender = this.nameLengthFeature(data.getKey());
			rightPre = (gender.equals(data.getValue())) ? ++rightPre : rightPre;
		}
		System.out.println("Right predictions " + rightPre + " out of: " + total);
		Double accuracy = (rightPre/total)*100;
		System.out.println("Accuracy of Name length feature set: " + accuracy + "%\n");
	}
	
}
