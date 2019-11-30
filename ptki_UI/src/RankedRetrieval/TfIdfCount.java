/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RankedRetrieval;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ASUS
 */
public class TfIdfCount {
    
    /**
     * 
     * @param invertedIndex inverted index lengkap
     * @param term  : term mana yang sedang dicari
     * @param documentNumber : lagi nyari tf untuk dokumen term mana
     * @param document_I_Size : banyak string di document sekarang
     * @return 
     */
    public static double countTF(HashMap<String ,HashMap<Integer , ArrayList<Integer>>> invertedIndex , String term,  int documentNumber  , int document_I_Size ){
       // kemunculan term i pada document dibagi jumlah
        HashMap<Integer , ArrayList<Integer>> postingListTerm = invertedIndex.get(term); 
        int termFrequencyInDocument_I = 0 ; 
        ArrayList<Integer> termListIndexInDoc_I ; 
        if(postingListTerm.get(documentNumber) != null){
            termFrequencyInDocument_I = postingListTerm.get(documentNumber).size();
        }  
        
        double tf = ((double) termFrequencyInDocument_I / document_I_Size);
        return tf;
        
    }
    
    
    public static double countIDF(HashMap<String ,HashMap<Integer , ArrayList<Integer>>> invertedIndex, String term , int howManyDocument){
         // number of document / berapa dokumen dimana term a muncul 
        int howManyDocumentWhereTerm_I_Appear = 0;
        if(invertedIndex.get(term) != null){
            howManyDocumentWhereTerm_I_Appear = invertedIndex.get(term).size();
        }
        return Math.log10(howManyDocument / howManyDocumentWhereTerm_I_Appear);
    }
    
}
