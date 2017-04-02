package classifier;

import java.util.HashMap;
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
	
	public NaiveBayesClassifier(Session session) {
		this.trainSet = Utilities.getTrainSet(session);
		LOG.info("No of training data records: " + this.trainSet.size());
		this.testSet = Utilities.getTestSet(session);
		LOG.info("No of training data records: " + this.testSet.size());
	}
}
