package genderpredictor;

/**
 *
 * @author Gaurav
 */
public class GenderPredictor {

    /**
     * @param args the command line arguments 
     */
    public void genderPredictor(String name) {
    	/*InputStreamReader isr = new InputStreamReader(System.in);
    	BufferedReader br = new BufferedReader(isr);
    	String ch = "Y";*/
    	
        /*final CassandraConnector client = new CassandraConnector();
        client.connect();
        Session session = client.getSession();*/
        
        /*KeyspaceInitializer ks = new KeyspaceInitializer();
        ks.createKeyspaceIfNotExists(session);
        ks.useKeyspace(session);
        ks.createTableIfNotExists(session);
        ks.truncateTable(session);*/
        
        /*LoadDataInKeyspace ld = new LoadDataInKeyspace();
        ld.loadData(session);*/
        
        /*NaiveBayesClassifier nb = new NaiveBayesClassifier(session);
        nb.testModel();
        while (ch.equals("Y") || ch.equals("y")) {
        	System.out.println("Enter name to predict: ");
            String name = br.readLine();
            nb.classify(name.toUpperCase());
            System.out.println("Q/q to quit or Y/y to continue:");
            ch = br.readLine();
        }*/
        
        //client.close();
    }
    
}
