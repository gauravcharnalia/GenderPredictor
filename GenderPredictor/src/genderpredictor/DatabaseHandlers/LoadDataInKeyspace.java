package genderpredictor.DatabaseHandlers;

import com.datastax.driver.core.Session;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gaurav
 */
public class LoadDataInKeyspace {
    private static final String FEMALECSVFILE = "src/Resources/Dataset/Indian-Female-Names.csv";
    private static final String MALECSVFILE = "src/Resources/Dataset/Indian-Male-Names.csv";
    HashMap<String, String> dataset = new HashMap<String, String>();
    
    public void loadData(Session session) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(FEMALECSVFILE));
            String[] row;
            while((row = reader.readNext()) != null) 
                dataset.put(row[0].toUpperCase(), row[1].toUpperCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader = new CSVReader(new FileReader(MALECSVFILE));
            String[] row;
            while((row = reader.readNext()) != null) 
                dataset.put(row[0].toUpperCase(), row[1].toUpperCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Adding values in gender.genderTable
        for (Map.Entry<String, String> data: dataset.entrySet()) {
        	String indianName = data.getKey();
        	String gender = data.getValue();
        	String query = "INSERT INTO gender.genderTable (indianname, gender) "
        			+ "VALUES (?, ?);";
        	session.execute(query,indianName, gender);
        }
    }
}
