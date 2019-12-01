/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RankedRetrieval;

import inverted_index.Inverted_Index;
import inverted_index.Searching;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class BM_25 {
    public static void main(String[] args) throws IOException {
        Searching.activateLemmatization();
        
        List<String> allDocAfterPre = Inverted_Index.read_Cleaned_Data();

        
        ArrayList<RSV_BM25>  result = search_BM25("fairest creature" , allDocAfterPre);
        for (int i = 0; i < result.size(); i++) {
            RSV_BM25 now = result.get(i);
            System.out.print("no doc : " + now.getNumDoc() + " rsv : " + now.getRsvScore());
            System.out.println();
        }
        

        
    }
    
    public static ArrayList<RSV_BM25> search_BM25(String input ,List<String> allDocAfterPre) throws IOException{
        ArrayList<RSV_BM25> result = new ArrayList<>();
        
        String preProcessedInput = Searching.inputPreprocessing(input); 
        String[] splittedPreProcessedInput = preProcessedInput.split(" ");
        HashMap<String  , HashMap<Integer,  Integer>> bagOfWord_bm25 =  generateBagOfWord(allDocAfterPre) ; 
        int howManyDoc = allDocAfterPre.size();
        double l_Ave = countL_Average(allDocAfterPre);
        
        HashMap<String , Integer> queryTermBag  = getQueryTermBag(preProcessedInput);
        int numDoc = 1;
        for (int i = 0 ; i < allDocAfterPre.size() ; i++) {
            String[] splittedDocNow = allDocAfterPre.get(i).split(" "); 
            int docLength  = splittedDocNow.length ; 
            
            double rsv_DocNow = RSV_BM25.countRSV(numDoc,bagOfWord_bm25,splittedPreProcessedInput,docLength,l_Ave,howManyDoc,queryTermBag);
            
            RSV_BM25 docObject = new RSV_BM25(numDoc, rsv_DocNow);
            result.add(docObject);
            
            numDoc++;
        }
        
        //sort to rank by rsv
        Collections.sort(result);
        return result;
        
    }

    public static HashMap<String, HashMap<Integer, Integer>> generateBagOfWord(List<String> allDocAfterPre) {
        // first hashmap represents <term , hashmap of number doc and frequency>
        HashMap<String, HashMap<Integer, Integer>> result = new HashMap();

        int numDoc = 1;
        for (int i = 0 ; i < allDocAfterPre.size();i++) {
            String[] splittedWord = allDocAfterPre.get(i).split(" ");

            for (String word : splittedWord) {
                HashMap<Integer, Integer> termBag = result.get(word);
                if (termBag == null) {
                    result.put(word, new HashMap<Integer, Integer>());
                }  
                termBag = result.get(word);
                if (termBag.get(numDoc) != null) {
                    int count = termBag.get(numDoc);
                    termBag.put(numDoc, count + 1);
                } else {
                    termBag.put(numDoc, 1);
                }
                result.put(word, termBag);
            }
            numDoc++;
        }
        return result; 
    }
    
    public static double countL_Average(List<String> allDocAfterPre){
        double result = 0 ; 
        for (String doc : allDocAfterPre) {
            String[] splitted = doc.split(" "); 
            result += splitted.length;
        }
        return result / allDocAfterPre.size();
    }
    
    public static HashMap<String,  Integer> getQueryTermBag(String preProcessedInput){
        HashMap<String , Integer> result = new HashMap(); 
        String[] splittedPreProcessedInput = preProcessedInput.split(" ");
        for(String word : splittedPreProcessedInput){
            int frecuency = 0 ; 
            if(result.get(word) == null){
                result.put(word,1);
            }else{
                frecuency = result.get(word);
                result.put(word, (frecuency+1));
            }
        }
        return result;
    }
}
