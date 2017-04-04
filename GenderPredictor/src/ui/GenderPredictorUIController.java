package ui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.datastax.driver.core.Session;
import com.jfoenix.controls.JFXComboBox;

import classifier.NaiveBayesClassifier;
import genderpredictor.DatabaseHandlers.CassandraConnector;
import genderpredictor.DatabaseHandlers.KeyspaceInitializer;
import genderpredictor.DatabaseHandlers.LoadDataInKeyspace;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * @author Gaurav
 *
 */
public class GenderPredictorUIController implements Initializable{
	
	// Logging
    private static final Logger LOG = Logger.getLogger(CassandraConnector.class.getName());
	
	@FXML
	private Label rPre;
	@FXML
	private Label total;
	@FXML
	private Label acc;
	@FXML
	private JFXComboBox<String> featureChoice;
	
	ObservableList<String> options = 
			FXCollections.observableArrayList(
					"1. Name length",
					"2. Ending with vowel",
					"3. Last letter",
					"4. All features"
			);
	private NaiveBayesClassifier nb;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		featureChoice.setItems(options);
		
		//Creating client and connecting to database
		final CassandraConnector client = new CassandraConnector();
        client.connect();
        Session session = client.getSession();
		
        //Initializing Keyspace with required structure
		KeyspaceInitializer ks = new KeyspaceInitializer();
        ks.createKeyspaceIfNotExists(session);
        ks.useKeyspace(session);
        ks.createTableIfNotExists(session);
        ks.truncateTable(session);
        
        //Loading values into keyspace
        LoadDataInKeyspace ld = new LoadDataInKeyspace();
        ld.loadData(session);
		
		this.nb = new NaiveBayesClassifier(session);
		
		client.close();
	}
	
	@FXML
	private void testSelectedModel(ActionEvent event) {
		int choice = featureChoice.getSelectionModel().getSelectedIndex();
		HashMap<String, Double> result;
		Double accuracy;
		switch(choice) {
			case 0: result = this.nb.testNameLengthFeatureSet();
					rPre.setText(result.get("rPre").toString());
					total.setText(result.get("total").toString());
					accuracy = ( result.get("rPre")/result.get("total") ) * 100;
					acc.setText(accuracy.toString());
					LOG.info("" + choice);
					break;
			case 1: result = this.nb.testnamesEndingInVowelFeatureSet();
					rPre.setText(result.get("rPre").toString());
					total.setText(result.get("total").toString());
					accuracy = ( result.get("rPre")/result.get("total") ) * 100;
					acc.setText(accuracy.toString());
					LOG.info("" + choice);
					break;
			case 2: result = this.nb.testnameEndingFeatureSet();
					rPre.setText(result.get("rPre").toString());
					total.setText(result.get("total").toString());
					accuracy = ( result.get("rPre")/result.get("total") ) * 100;
					acc.setText(accuracy.toString());
					LOG.info("" + choice);
					break;
			case 3: result = this.nb.testAllFeatures();
					rPre.setText(result.get("rPre").toString());
					total.setText(result.get("total").toString());
					accuracy = ( result.get("rPre")/result.get("total") ) * 100;
					acc.setText(accuracy.toString());
					LOG.info("" + choice);
					break;
			default:LOG.info("" + choice);
					break;
		}
	}
	
}
