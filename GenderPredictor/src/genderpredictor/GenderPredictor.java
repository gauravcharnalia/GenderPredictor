package genderpredictor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.datastax.driver.core.Session;

import classifier.NaiveBayesClassifier;
import genderpredictor.DatabaseHandlers.CassandraConnector;
import genderpredictor.DatabaseHandlers.KeyspaceInitializer;
import genderpredictor.DatabaseHandlers.LoadDataInKeyspace;

/**
 *
 * @author Gaurav
 */
public class GenderPredictor {

    /**
     * @param args the command line arguments
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
    	InputStreamReader isr = new InputStreamReader(System.in);
    	BufferedReader br = new BufferedReader(isr);
    	
        final CassandraConnector client = new CassandraConnector();
        client.connect();
        Session session = client.getSession();
        
        KeyspaceInitializer ks = new KeyspaceInitializer();
        ks.createKeyspaceIfNotExists(session);
        ks.useKeyspace(session);
        ks.createTableIfNotExists(session);
        ks.truncateTable(session);
        
        LoadDataInKeyspace ld = new LoadDataInKeyspace();
        ld.loadData(session);
        
        // TODO: implement basic naives bayes classifier
        NaiveBayesClassifier nb = new NaiveBayesClassifier(session);
        nb.testModel();
        System.out.println("Enter name to predict: ");
        String name = br.readLine();
        nb.classify(name.toUpperCase());
        // TODO: implement naives bayes classifier with feature selection options
        // TODO: Use model for predictions
        
        client.close();
    }
    
}
