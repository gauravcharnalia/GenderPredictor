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
		int result = 0;
		
		String predictedGenderByNameLength = (this.nameLengthFeature(name).equals("F")) ? "Female" : "Male";
		System.out.println(predictedGenderByNameLength + "\tBy P(class|Length) and average name length ");
		//result = (predictedGenderByNameLength.equals("Female")) ? result++ : result;
		if (predictedGenderByNameLength.equals("Female"))	result++;
		//LOG.info("result: " + result);
		
		String predictedGenderByEndingVowel = (this.namesEndingInVowelFeature(name).equals("F")) ? "Female" : "Male";
		//result = (predictedGenderByEndingVowel.equals("Female")) ? result++ : result;
		if (predictedGenderByEndingVowel.equals("Female"))	result++;
		System.out.println(predictedGenderByEndingVowel + "\tBy P(class|ending=vowel) ");
		//LOG.info("result: " + result);
		
		String predictedGenderByEnding= (this.namesEndingInVowelFeature(name).equals("F")) ? "Female" : "Male";
		//result = (predictedGenderByEndingVowel.equals("Female")) ? result++ : result;
		if (predictedGenderByEnding.equals("Female"))	result++;
		System.out.println(predictedGenderByEnding + "\tBy P(class|ending=vowel) ");
		//LOG.info("result: " + result);
		
		System.out.println("\nResult:\t" + ((result >= 2) ? "Female" : "Male") + "\n");
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
		return (genderLen.equals("F") && genderProb.equals("F")) ? "F" : "M"; //Prediction with respect to Females
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
	
	private String nameEndingFeature(String name) {
		String nameEnd = name.substring(name.length() - 1);
		HashMap<String, Double> namesEnd = Utilities.nameEnding(this.trainSet, nameEnd);
		String genderProb = (namesEnd.get("PFE") > namesEnd.get("PME")) ? "F" : "M" ;
		return (genderProb.equals("F")) ? "F" : "M"; //Prediction with respect to Females
	}
	
	public HashMap<String, Double> testAllFeatures() {
		int vote = 0;
		Double rightPre = 0.0;
		Double total = (double) this.testSet.size();
		String predictedGenderByNameLength, predictedGenderByLastVowel, predictedGenderByLast;
		for(Map.Entry<String, String> data: this.testSet.entrySet()) {
			
			predictedGenderByNameLength = (this.nameLengthFeature(data.getKey()).equals("F") ? "Female" : "Male");
			if (predictedGenderByNameLength.equals("Female"))	vote++;
			
			predictedGenderByLastVowel = (this.namesEndingInVowelFeature(data.getKey()).equals("F") ? "Female" : "Male");
			if (predictedGenderByLastVowel.equals("Female"))	vote++;
			
			predictedGenderByLast = (this.nameEndingFeature(data.getKey()).equals("F") ? "Female" : "Male");
			if (predictedGenderByLast.equals("Female")) vote++;
			
			rightPre = (vote >= 2) ? ++rightPre : rightPre;
		}
		LOG.info("Right predictions " + rightPre + " out of: " + total);
		//Double accuracy = (rightPre/total)*100;
		LOG.info("Accuracy of Name ending in vowel feature set: " + (rightPre/total)*100 + "%\n");
		
		HashMap<String, Double> result = new HashMap<>();
		result.put("rPre", rightPre);
		result.put("total", total);
		
		return result;
	}
	
	public HashMap<String, Double> testnameEndingFeatureSet() {
		Double rightPre = 0.0;
		Double total = (double) this.testSet.size();
		String gender;
		for(Map.Entry<String, String> data: this.testSet.entrySet()) {
			gender = this.nameEndingFeature(data.getKey());
			rightPre = (gender.equals(data.getValue())) ? ++rightPre : rightPre;
		}
		LOG.info("Right predictions " + rightPre + " out of: " + total);
		//Double accuracy = (rightPre/total)*100;
		LOG.info("Accuracy of Name ending in vowel feature set: " + (rightPre/total)*100 + "%\n");
		
		HashMap<String, Double> result = new HashMap<>();
		result.put("rPre", rightPre);
		result.put("total", total);
		
		return result;
	}
	
	public HashMap<String, Double> testnamesEndingInVowelFeatureSet() {
		Double rightPre = 0.0;
		Double total = (double) this.testSet.size();
		String gender;
		for(Map.Entry<String, String> data: this.testSet.entrySet()) {
			gender = this.namesEndingInVowelFeature(data.getKey());
			rightPre = (gender.equals(data.getValue())) ? ++rightPre : rightPre;
		}
		Utilities.namesEndingInVowel(this.trainSet);
		LOG.info("Right predictions " + rightPre + " out of: " + total);
		//Double accuracy = (rightPre/total)*100;
		LOG.info("Accuracy of Name ending in vowel feature set: " + (rightPre/total)*100 + "%\n");
		
		HashMap<String, Double> result = new HashMap<>();
		result.put("rPre", rightPre);
		result.put("total", total);
		
		return result;
	}
	
	public HashMap<String, Double> testNameLengthFeatureSet() {
		Double rightPre = 0.0;
		Double total = (double) this.testSet.size();
		String gender;
		for(Map.Entry<String, String> data: this.testSet.entrySet()) {
			gender = this.nameLengthFeature(data.getKey());
			rightPre = (gender.equals(data.getValue())) ? ++rightPre : rightPre;
		}
		LOG.info("Right predictions " + rightPre + " out of: " + total);
		//Double accuracy = (rightPre/total)*100;
		LOG.info("Accuracy of Name ending in vowel feature set: " + (rightPre/total)*100 + "%\n");
		
		HashMap<String, Double> result = new HashMap<>();
		result.put("rPre", rightPre);
		result.put("total", total);
		
		return result;
	}
	
}
