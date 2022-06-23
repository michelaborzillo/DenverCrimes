/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    Adiacenza a= boxArco.getValue();
    String partenza= a.getV1();
    String destinazione =a.getV2();
   List<String> result= this.model.calcolaPercorso(partenza, destinazione);
   for (String s: result) {
	   txtResult.appendText(s);
   }
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	String categoria= boxCategoria.getValue();
    	Integer mese= boxMese.getValue();
    	this.model.creaGrafo(categoria, mese);
    	txtResult.appendText("Grafo creato!\n");
		txtResult.appendText("VERTICI: "+this.model.nVertici()+"\n");
    	txtResult.appendText("ARCHI: "+this.model.nArchi()+"\n");

    	txtResult.appendText("Archi con peso maggiore del peso medio: ");
    	for (Adiacenza a: model.getArchiMaggioriPesoMedio()) {
    		txtResult.appendText(a+"\n");
    	}
    	boxArco.getItems().clear();
    	boxArco.getItems().addAll(this.model.getArchi(categoria, mese));
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
       // assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
     //   assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
       // assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxCategoria.getItems().clear();
    	boxCategoria.getItems().addAll(this.model.aggiungiCategoria());
    	boxMese.getItems().clear();
    	for (int i=1; i<=12; i++)
    	boxMese.getItems().add(i);
    }
}
