/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RankedRetrieval;

import inverted_index.IndexGenerator;
import inverted_index.Inverted_Index;
import inverted_index.Searching;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author TEUKU HASHRUL
 */
public class LanguageModel {
    public static void main(String[] args) throws IOException {
        Searching.activateLemmatization();
        
        // --------------- dummy test ------------------------------------------
        String doc1 = "Xerox reports a profit but revenue is down";
        String afterDoc1 = Searching.inputPreprocessing(doc1);
        
        String doc2 = "Lucent narrows quarter loss but revenue decreases further";
        String afterDoc2 = Searching.inputPreprocessing(doc2);
        
        ArrayList<String> dataset = new ArrayList<String>();
        dataset.add(afterDoc1);
        dataset.add(afterDoc2);        
        HashMap<String , HashMap<Integer , ArrayList<Integer>>> invertedIndex = IndexGenerator.generatePostingList(dataset);
        
        
        //----------------- real test -----------------------------------------
        String input = "fairest creatures"; 
        
        List<String> allDocAfterPre = Inverted_Index.read_Cleaned_Data();
        HashMap<String, HashMap<Integer, ArrayList<Integer>>> invertedIndexPostingList = IndexGenerator.readInvertedIndexPostingList();

        ArrayList<LM_Object> result = search(input ,allDocAfterPre,invertedIndexPostingList);
        for (LM_Object docNow : result) {
            System.out.println("now doc :" + docNow.getDocNum()
                    + " now ranking :"+  docNow.getLmScore());
            System.out.println("");
        }
    }
    public static ArrayList<LM_Object> search(String input ,List<String> allDocAfterPre,HashMap<String, HashMap<Integer, ArrayList<Integer>>> invertedIndexPostingList) throws IOException {
        String preProcessedInput = Searching.inputPreprocessing(input);
        ArrayList<LM_Object> result = new ArrayList<>();
        String[] splittedPreProcessedInput = preProcessedInput.split(" ");
        
        int howManyWordInEveryDoc = howManyWordInEveryDoc(allDocAfterPre);
        double alpha = 0.5; 
        
        int numDoc = 1;
        for (String doc : allDocAfterPre) { 
            String[] splittedDoc = doc.split(" ");
            double lm = LM_Object.countlmScore(splittedPreProcessedInput, numDoc, invertedIndexPostingList, alpha, howManyWordInEveryDoc, splittedDoc.length);
            
            LM_Object doc_LM = new LM_Object(numDoc, lm);
            result.add(doc_LM);
            
            
            numDoc++;
        }
        
        Collections.sort(result);
        return result;

    }
    
    private static int howManyWordInEveryDoc(List<String> allDocAfterPre){
        int result = 0 ; 
        for (String doc : allDocAfterPre) {
            String[] splittedDoc = doc.split(" "); 
            result+= splittedDoc.length;
        }
        return result ;
    }
}
