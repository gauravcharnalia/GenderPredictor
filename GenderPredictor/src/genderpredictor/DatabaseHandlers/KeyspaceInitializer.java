package genderpredictor.DatabaseHandlers;

import com.datastax.driver.core.Session;

/**
 *
 * @author Gaurav
 */
public class KeyspaceInitializer {
    
    public void createKeyspaceIfNotExists(Session session) {
        session.execute("CREATE KEYSPACE IF NOT EXISTS gender"
            + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3}");
    }
    
    public void createTableIfNotExists(Session session) {
        String query = "CREATE TABLE IF NOT EXISTS gender.genderTable"
            + "(indianName text PRIMARY KEY, "
        	+ "gender text );";
    	session.execute(query);
    }
    
    public void truncateTable(Session session) {
    	session.execute("TRUNCATE genderTable");
    }
    
    public void useKeyspace(Session session) {
        session.execute("USE gender");
    }
}
