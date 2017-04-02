package genderpredictor;

import com.datastax.driver.core.Session;
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
     */
    public static void main(String[] args) {
        final CassandraConnector client = new CassandraConnector();
        client.connect();
        Session session = client.getSession();
        
        KeyspaceInitializer ks = new KeyspaceInitializer();
        ks.createTableIfNotExists(session);
        ks.useKeyspace(session);
        
        LoadDataInKeyspace ld = new LoadDataInKeyspace();
        ld.loadData();
        // TODO: implement naives bayes classifier with feature selection options
        // TODO: Use model for predictions
        
        client.close();
    }
    
}
