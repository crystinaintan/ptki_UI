/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RankedRetrieval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public class DocumentBagOfWord implements Comparable<DocumentBagOfWord> {
    private int documentNumber ;
    private HashMap<String  , TermTfIdf> termTfIdf; 
    private ArrayList<Double> bagOfWord;
    private double cosineSimilarityWithQuery;

    public int getDocumentNumber() {
        return documentNumber;
    }

    public double getCosineSimilarityWithQuery() {
        return cosineSimilarityWithQuery;
    }
    
    
    
    public DocumentBagOfWord(HashMap<String , TermTfIdf> termTfIdf  , int documentNumber){
        this.termTfIdf = termTfIdf;
        this.documentNumber = documentNumber;
        this.bagOfWord = getBagOfTfIdf();
        if(documentNumber == 0) this.cosineSimilarityWithQuery = 1;
        this.cosineSimilarityWithQuery = 0;
    }
    
    public void setCosineSimilarityWithQuery(DocumentBagOfWord query){
        this.cosineSimilarityWithQuery = CosineSimilarity.count(bagOfWord,query.bagOfWord);
    }
            
    public ArrayList<Double> getBagOfTfIdf(){
        ArrayList<Double> bagOfWord = new ArrayList<Double>(); 
        for(Map.Entry<String , TermTfIdf> item : termTfIdf.entrySet()){
            double tf = item.getValue().tf;
            double idf = item.getValue().idf ; 
            double tfidf = tf * idf ; 
            
            bagOfWord.add(tfidf);
        }
        return bagOfWord; 
    }

    @Override
    public int compareTo(DocumentBagOfWord o) {
        if(this.cosineSimilarityWithQuery > o.cosineSimilarityWithQuery){
            return -1;
        }else if(this.cosineSimilarityWithQuery < o.cosineSimilarityWithQuery){
            return 1;
        }else{
            return 0;
        }
    }

    public HashMap<String, TermTfIdf> getTermTfIdf() {
        return termTfIdf;
    }

    @Override
    public String toString() {
        String res = "Doc : " + this.documentNumber +"\n" ;
        res+= " Cosine Similarity " + this.cosineSimilarityWithQuery + " \n";
        return res;
    }

   
}
