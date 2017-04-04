package genderpredictor.DatabaseHandlers;

import com.datastax.driver.core.Session;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Gaurav
 */
public class LoadDataInKeyspace {
	// Logging
    private static final Logger LOG = Logger.getLogger(CassandraConnector.class.getName());
	
    private static final String NAMESCSVFILE = "src/Resources/Dataset/Indian-Names.csv";
    
    HashMap<String, String> dataset = new HashMap<String, String>();
    
    public void loadData(Session session) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(NAMESCSVFILE));
            String[] row;
            while((row = reader.readNext()) != null) 
                dataset.put(row[0].toUpperCase(), row[1].toUpperCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info("" + dataset.size());
        int count = 1;
        for (Map.Entry<String, String> data: dataset.entrySet()) {
        	String indianName = data.getKey();
        	String gender = data.getValue();
        	// Adding Training data in gender.genderTableTrain
	        if (count < (dataset.size()*0.80)) {
        		String query = "INSERT INTO gender.genderTableTrain (indianname, gender) "
	        			+ "VALUES (?, ?);";
	        	session.execute(query,indianName, gender);
	        }
	        // Adding Test data in gender.genderTableTest
	        else {
	        	String query = "INSERT INTO gender.genderTableTest (indianname, gender) "
	        			+ "VALUES (?, ?);";
	        	session.execute(query,indianName, gender);
	        }
	        count++;
        }
    }
}
