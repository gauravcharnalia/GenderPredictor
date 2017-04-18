package genderpredictor.DatabaseHandlers;

import com.datastax.driver.core.Session;

/**
 *
 * @author Gaurav
 */
public class KeyspaceInitializer {
    
    public void createKeyspaceIfNotExists(Session session) {
        session.execute("CREATE KEYSPACE IF NOT EXISTS gender"
            + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");
    }
    
    public void createTableIfNotExists(Session session) {
        String query = "CREATE TABLE IF NOT EXISTS gender.genderTableTrain"
            + "(indianName text PRIMARY KEY, "
        	+ "gender text );";
    	session.execute(query);
    	
    	query = "CREATE TABLE IF NOT EXISTS gender.genderTableTest"
                + "(indianName text PRIMARY KEY, "
            	+ "gender text );";
    	session.execute(query);
    }
    
    public void truncateTable(Session session) {
    	session.execute("TRUNCATE genderTableTrain");
    	session.execute("TRUNCATE genderTableTest");
    }
    
    public void useKeyspace(Session session) {
        session.execute("USE gender");
    }
}
