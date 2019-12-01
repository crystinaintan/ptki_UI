/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ptki_ui;

import RankedRetrieval.BM_25;
import RankedRetrieval.DocumentBagOfWord;
import RankedRetrieval.LM_Object;
import RankedRetrieval.LanguageModel;
import RankedRetrieval.RSV_BM25;
import RankedRetrieval.RankedSearching;
import RankedRetrieval.TermTfIdf;
import inverted_index.IndexGenerator;
import inverted_index.Inverted_Index;
import inverted_index.Searching;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author crystinaintan
 */
public class FXMLDocumentController implements Initializable {

    final ObservableList<Contracts> list = FXCollections.observableArrayList();
    String jawabanSistem = "";
    long timeAwal;
    long timeAkhir;

     List<String> allDocAfterPre ;
     HashMap<String, HashMap<Integer, ArrayList<Integer>>> invertedIndexPostingList ;


    
    ObservableList<Contracts> hasil_n_top = FXCollections.observableArrayList();

    @FXML
    private Label keteranganQuery;

    @FXML
    private TextField input;

    @FXML
    private Label rank;

    @FXML
    private TextField topnrank;

    @FXML
    private Label kethasil;

    @FXML
    private Label isidok;

    @FXML
    private TextArea textareaisidok;

    @FXML
    private Button close;

    @FXML
    private Button cari;
    
    @FXML
    private Button Sbm25;
    
    @FXML
    private Button lm;
    
    @FXML
    private TableView myTabel;

    @FXML
    private TableColumn<Contracts, String> ranking;

    @FXML
    private TableColumn<Contracts, String> dokumen;
    
    @FXML
    private Label loading;
    
    @FXML
    private Label ketRecal;
    
    @FXML
    private Label ketPreci;
    
    @FXML
    private Label ketTime;
    
    @FXML
    private TextField recalValue;
    
    @FXML
    private TextField preciValue;
    
    @FXML
    private TextField timeValue;
    
    @FXML
    private Label ketKunciJawaban;
    
    @FXML
    private TextArea kunjaw;
    
    @FXML
    private Label f1;
    
    @FXML
    private TextField f1value;

    @FXML
    private void search(ActionEvent event) throws IOException {
        list.clear();
        jawabanSistem="";
        timeAwal= System.currentTimeMillis();
        
        System.out.println(list);
        if (input.getText().equalsIgnoreCase("") == false) {
            printToTabel();
//            ObservableList<String> result = myTabel.getSelectionModel().getSelectedItems();
            System.out.println("a");
            ArrayList<DocumentBagOfWord> result =  RankedSearching.search(input.getText() ,this.invertedIndexPostingList , this.allDocAfterPre);
            
//            DocumentBagOfWord d1 = result.get(2); 
//            for (Map.Entry<String, TermTfIdf> item : d1.getTermTfIdf().entrySet()) {
//                System.out.println("Term " + item.getKey() + " tf" + item.getValue().tf  + " idf" + item.getValue().idf);
//            }
            
            for (DocumentBagOfWord item : result) {
                if(item.getCosineSimilarityWithQuery()>0)
                {
                    String namaFile = this.formatNamaFile(item.getDocumentNumber());
                    list.add(new Contracts(this.formatKoma(item.getCosineSimilarityWithQuery()), namaFile + ""));
                    jawabanSistem+= item.getDocumentNumber()+" ";
                }
            }
            
            if (topnrank.getText().equalsIgnoreCase("") == false) {
                this.getTop_n(event, topnrank.getText());
                System.out.println("Kepanggil");
            }
        } else {
            myTabel.setItems(FXCollections.observableArrayList(new Contracts("", "Dokumen Kosong!!")));
        }

        myTabel.setDisable(false);
//            hasil.getItems().add("Milea");
//            hasil.getItems().add(hasil.getItems().get(1));
        kethasil.setVisible(true);
        textareaisidok.setDisable(false);
        isidok.setDisable(false);
        loading.setText("DOne");
        ketRecal.setDisable(false);
        ketPreci.setDisable(false); 
        ketTime.setDisable(false); 
        recalValue.setDisable(false); 
        preciValue.setDisable(false); 
        timeValue.setDisable(false);
        f1.setDisable(false); 
        f1value.setDisable(false);
        timeAkhir = System.currentTimeMillis();
        this.hitungWaktu();
        this.countRecalandPreci();
        ranking.setText("Cosine");
    }
    
    @FXML
    private void searchBM25(ActionEvent event) throws IOException {
        list.clear();
        jawabanSistem="";
        timeAwal= System.currentTimeMillis();
        System.out.println(list);
        if (input.getText().equalsIgnoreCase("") == false) {
            printToTabel();
//            ObservableList<String> result = myTabel.getSelectionModel().getSelectedItems();
            System.out.println("a");
            ArrayList<RSV_BM25> result =  BM_25.search_BM25(input.getText() , this.allDocAfterPre);
            
            for (RSV_BM25 item : result) {
                if(item.rsvScore>0)
                {
                    String namaFile = this.formatNamaFile(item.numDoc);
                    list.add(new Contracts(this.formatKoma(item.rsvScore), namaFile + ""));
                    jawabanSistem+=item.numDoc+" ";
                }
            }
            
            if (topnrank.getText().equalsIgnoreCase("") == false) {
                this.getTop_n(event, topnrank.getText());
                System.out.println("Kepanggil");
            }
        } else {
            myTabel.setItems(FXCollections.observableArrayList(new Contracts("", "Dokumen Kosong!!")));
        }

        myTabel.setDisable(false);
//            hasil.getItems().add("Milea");
//            hasil.getItems().add(hasil.getItems().get(1));
        kethasil.setVisible(true);
        textareaisidok.setDisable(false);
        isidok.setDisable(false);
        loading.setText("DOne");
        ketRecal.setDisable(false);
        ketPreci.setDisable(false); 
        ketTime.setDisable(false); 
        recalValue.setDisable(false); 
        preciValue.setDisable(false); 
        timeValue.setDisable(false);
        f1.setDisable(false); 
        f1value.setDisable(false);
        timeAkhir = System.currentTimeMillis();
        this.hitungWaktu();
        this.countRecalandPreci();
        ranking.setText("RSV");
    }
    
    @FXML
    private void searchLanguageModel(ActionEvent event) throws IOException {
        list.clear();
        jawabanSistem="";
        timeAwal= System.currentTimeMillis();
        System.out.println(list);
        if (input.getText().equalsIgnoreCase("") == false) {
            printToTabel();
//            ObservableList<String> result = myTabel.getSelectionModel().getSelectedItems();
            ArrayList<LM_Object> result =  LanguageModel.search(input.getText() , this.allDocAfterPre, this.invertedIndexPostingList);
            
            
            for (LM_Object item : result) {
                if(item.getLmScore()> cekLanguageModel(result))
                {
                    String namaFile = this.formatNamaFile(item.getDocNum());
                    list.add(new Contracts(item.getLmScore()+"", namaFile + ""));
                    jawabanSistem+=item.getDocNum()+" ";
                }
            }
            
            if (topnrank.getText().equalsIgnoreCase("") == false) {
                this.getTop_n(event, topnrank.getText());
                System.out.println("Kepanggil");
            }
        } else {
            myTabel.setItems(FXCollections.observableArrayList(new Contracts("", "Dokumen Kosong!!")));
        }

        myTabel.setDisable(false);
//            hasil.getItems().add("Milea");
//            hasil.getItems().add(hasil.getItems().get(1));
        kethasil.setVisible(true);
        textareaisidok.setDisable(false);
        isidok.setDisable(false);
        loading.setText("DOne");
        ketRecal.setDisable(false);
        ketPreci.setDisable(false); 
        ketTime.setDisable(false); 
        recalValue.setDisable(false); 
        preciValue.setDisable(false); 
        timeValue.setDisable(false);
        f1.setDisable(false); 
        f1value.setDisable(false);
        timeAkhir = System.currentTimeMillis();
        this.hitungWaktu();
        this.countRecalandPreci();
        ranking.setText("LM Score");
    }
    
    private void search(char method){
        
    }

    @FXML
    private void close(ActionEvent event) {
        System.exit(0);

    }

//    @FXML
//    private void resetin(ActionEvent event) {
//        hasil.setItems(list);
//        
//    }
    @FXML
    private void getTop_n(ActionEvent event, String n) {
        hasil_n_top.clear();
        ObservableList<Contracts> result = myTabel.getItems();
        int angka = Integer.parseInt(n);
        for (int i = 0; i < angka; i++) {
            Contracts baru = new Contracts(result.get(i).getRanking(), result.get(i).getDokumen());

            hasil_n_top.add(baru);
            System.out.println(hasil_n_top);
            System.out.println(list.size());
            if (i >= list.size()) {
                break;
            }
        }
        myTabel.setItems(hasil_n_top);

    }

    public void onListViewItemClicked(MouseEvent event) throws IOException {
//        if (this.client.getClientNotes().isEmpty()) {
//            this.deleteButton.setDisable(true);
//            this.saveButton.setDisable(true);
//            this.notesTextArea.setDisable(true);
//        } else {
//            this.deleteButton.setDisable(false);
//            this.saveButton.setDisable(false);
//            this.notesTextArea.setDisable(false);
//            System.out.println(hasil.getSelectionModel().getSelectedItem());
        
        ObservableList<Contracts> result = myTabel.getSelectionModel().getSelectedItems();
//        textareaisidok.setText(result.get(0).getDokumen());
        
       
        String nama_dok = result.get(0).getDokumen();
//        
//        System.out.println("Tipe dan nilai dari nama_dok : "+ nama_dok);
//        if(nama_dok < 10)
//        {
//            after_concat = "Doc00"+nama_dok+".txt";
//        }
//        else if(nama_dok < 100)
//        {
//            after_concat = "Doc0"+nama_dok+".txt";
//        }
//        else
//        {
//            after_concat = "Doc"+nama_dok+".txt";
//        }
        
        String doc ="";
        File[] folder_Cleaned = inverted_index.Inverted_Index.findFoldersInDirectory(inverted_index.Inverted_Index.DATA_SET_FOLDER_PATH); 
        for (File item : folder_Cleaned) {
            if(item.getName().equalsIgnoreCase(nama_dok))
            {
                doc = inverted_index.Inverted_Index.generateLine(item);
                break;
            }
        } 
        System.out.println("Ini dokumennya : "+ nama_dok);
        textareaisidok.setText(doc);
        
//            String[] temp = result.split("/");
//            this.hasil = temp[0];
//            result = this.client.clientClickNote(temp[0]);
//            this.notesTextArea.setText(result);
//        }
//
    }
    
    public void printToTabel() {
        ranking.setCellValueFactory(new PropertyValueFactory<Contracts, String>("ranking"));
        dokumen.setCellValueFactory(new PropertyValueFactory<Contracts, String>("dokumen"));

        myTabel.setItems(list);
        
    }
    
    public String formatNamaFile(int angka)
    {
        String after_concat = "";
        
        System.out.println("Tipe dan nilai dari nama_dok : "+ angka);
        if(angka < 10)
        {
            after_concat = "Doc00"+angka+".txt";
        }
        else if(angka < 100)
        {
            after_concat = "Doc0"+angka+".txt";
        }
        else
        {
            after_concat = "Doc"+angka+".txt";
        }
        return after_concat;
    }
    
    public String formatKoma(double x)
    {
        DecimalFormat df = new DecimalFormat("#.####");
        return df.format(x);
    }
    
    public void resetAll()
    {
        ketRecal.setDisable(true);
        ketPreci.setDisable(true); 
        ketTime.setDisable(true); 
        recalValue.setDisable(true); 
        preciValue.setDisable(true); 
        timeValue.setDisable(true);
        
        recalValue.setText(""); 
        preciValue.setText("");  
        timeValue.setText(""); 
    }
    
    public void countRecalandPreci()
    {
        String [] arrJS = jawabanSistem.split(" ");
        String [] arrJkunjaw = kunjaw.getText().split(" ");
        
        //System.out.println(arrJkunjaw[0] + ","+ arrJkunjaw[1]);
        double tp = irisan(arrJS, arrJkunjaw);
        double fp = arrJkunjaw.length - tp;
        double fn = 154-(arrJS.length + arrJkunjaw.length - (2*tp)+tp);
        double precision = tp/(tp+fp);
        double recall = tp/(tp+fn);
        double fscore = (2*precision*recall)/(precision+recall);
        
        System.out.println("Ini tp : "+ tp);
        System.out.println("Ini fp : "+ fp);
        System.out.println("Ini fn : "+ fn);
        
        
        preciValue.setText(formatKoma(precision)+"");
        recalValue.setText(formatKoma(recall)+"");
        f1value.setText(formatKoma(fscore)+"");
        System.out.println("Ini RECALL : "+ recalValue.getText());
        System.out.println("Ini PRECI : "+ preciValue.getText());
    }
    
    public int irisan(String[]arrJS, String [] arrJkunjaw)
    {
        int hasil = 0;
        for(int i = 0; i<arrJkunjaw.length ;i++)
        {
            for(int j = 0; j<arrJS.length ;j++)
            {
                hasil+= (arrJS[j].equalsIgnoreCase(arrJkunjaw[i]))? 1:0;
                System.out.println("Ini arr Jawaban sistem : "+arrJS[j]);
                System.out.println("Ini arr Jawaban kunjaw : "+arrJkunjaw[i]);
            }
        }
        return hasil;
    }
    
    public double cekLanguageModel(ArrayList<LM_Object> result)
    {
        double hasil= 0.00;
        double valueitem = 0.00;
        double count= 1;
        for (LM_Object item : result) {
                if(item.getLmScore()== valueitem)
                {
                    count+=1;
                }
                else
                {
                    valueitem = item.getLmScore();
                }
            if(count >= 5)
            {
                hasil = valueitem;
                break;
            }
        }
        return hasil;
    }
    
    public void hitungWaktu()
    {
        double time = (timeAkhir - timeAwal) ; 
        String waktu = String.format("%.4f seconds", (time /1000) );
         timeValue.setText(waktu);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         loading.setText("Loading Initialization");
         Searching.activateLemmatization();
        try {
            this.allDocAfterPre = Inverted_Index.read_Cleaned_Data();
            this.invertedIndexPostingList = IndexGenerator.readInvertedIndexPostingList();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }

    public static class Contracts {

        private SimpleStringProperty ranking;
        private SimpleStringProperty dokumen;

        public Contracts(String rank, String dokumen) {
            this.dokumen = new SimpleStringProperty(dokumen);
            this.ranking = new SimpleStringProperty(rank);
        }

        public String getRanking() {
            return ranking.get();
        }

        public String getDokumen() {
            return dokumen.get();
        }

    }
}
