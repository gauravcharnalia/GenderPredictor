package genderpredictor.DatabaseHandlers;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for connecting to Cassandra database
 * @author Gaurav
 */
public class CassandraConnector {
    // Cluster defaults
    private static final String NODE = "localhost";
    private static final int PORT = 9042;
    // Logging
    private static final Logger LOG = Logger.getLogger(CassandraConnector.class.getName());
    
    private Cluster cluster;
    private Session session;
    /**
     * Connect to Cassandra Cluster specified by provided node IP 
     * address and port number along with an overloaded method to give
     * default parameters to connect
     * 
     * @param node Cluster node IP address
     * @param port Port of cluster host
     */
    public void connect(final String node, final int port) {
        this.cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
        final Metadata metadata = this.cluster.getMetadata();
        LOG.log(Level.INFO, "Connected to cluster: \n{0}", metadata.getClusterName());
        metadata.getAllHosts().forEach((host) -> {
            LOG.log(Level.INFO, "Datacenter: {0}, Host: {1}, Rack: {2}", new Object[]{host.getDatacenter(), host.getAddress(), host.getRack()});
        });

        this.session = cluster.connect();
    }
    public void connect() {
        this.connect(NODE, PORT);
    }
    
    /**
    * @return My session.
    */
    public Session getSession() {
        LOG.info("Passing session");
        return this.session;
    }
        
    /** Close cluster */
    public void close() {
        this.cluster.close();
        LOG.info("Closing cluster");
    }
    
}
