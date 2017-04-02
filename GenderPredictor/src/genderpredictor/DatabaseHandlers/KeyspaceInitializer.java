package genderpredictor.DatabaseHandlers;

import com.datastax.driver.core.Session;

/**
 *
 * @author Gaurav
 */
public class KeyspaceInitializer {
    
    public void createTableIfNotExists(Session session) {
        session.execute("CREATE KEYSPACE IF NOT EXISTS gender"
            + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3}");
    }
    
    public void useKeyspace(Session session) {
        session.execute("USE gender");
    }
}
