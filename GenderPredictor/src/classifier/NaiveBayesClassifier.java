package classifier;

import java.util.HashMap;
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
		String predictedGender = this.nameLengthFeature(name);
		System.out.println(predictedGender);
	}
	
	public NaiveBayesClassifier(Session session) {
		this.trainSet = Utilities.getTrainSet(session);
		LOG.info("No of training data records: " + this.trainSet.size());
		this.testSet = Utilities.getTestSet(session);
		LOG.info("No of training data records: " + this.testSet.size());
	}
	
	private String nameLengthFeature(String name) {
		Double len = (double) name.length();
		HashMap<String, Double> avgLength = Utilities.avgNameLength(this.testSet, name.length());
		String genderLen = (Math.abs(len-avgLength.get("F")) < Math.abs(len - avgLength.get("M"))) ? "F" : "M";
		String genderProb = (avgLength.get("PLF")>avgLength.get("PLM")) ? "F" : "M" ;
		//LOG.info(name + " Prediction on len: " + genderLen + " Prediction on prob: " + genderProb);
		return (genderLen.equals("F") && genderProb.equals("F")) ? "F" : "M"; //Prediction with respect to Females
	}
	
	public void testModel() {
		this.testNameLengthFeatureSet();
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
		System.out.println("Accuracy of Name length feature set: " + accuracy + "%");
	}
	
}
