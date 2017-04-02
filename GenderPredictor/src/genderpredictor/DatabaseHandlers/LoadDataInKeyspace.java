package genderpredictor.DatabaseHandlers;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Gaurav
 */
public class LoadDataInKeyspace {
    private static final String FEMALECSVFILE = "src/Resources/Dataset/Indian-Female-Names.csv";
    private static final String MALECSVFILE = "src/Resources/Dataset/Indian-Male-Names.csv";
    HashMap<String, String> dataset = new HashMap<String, String>();
    
    public void loadData() {
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
            // TODO: Put values in database table
            // TODO: Display values on console
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
