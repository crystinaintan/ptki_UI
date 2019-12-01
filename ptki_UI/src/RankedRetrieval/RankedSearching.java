/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RankedRetrieval;

import inverted_index.IndexGenerator;
import inverted_index.Inverted_Index;
import inverted_index.Searching;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public class RankedSearching {

    static final String clear_path = "D:\\KULIAH SEMSETER 7\\PTKI\\Tugas\\ptki_UI\\Clean_data";
    
    public static void main(String[] args) throws IOException {
        Searching.activateLemmatization();
       
        System.out.println("MANTAP");
//         HashMap<String, HashMap<Integer, ArrayList<Integer>>> invertedIndexPostingList = IndexGenerator.generatePostingList(document);
         
        List<String> allDocAfterPre = Inverted_Index.read_Cleaned_Data();
        HashMap<String, HashMap<Integer, ArrayList<Integer>>> invertedIndexPostingList = IndexGenerator.readInvertedIndexPostingList();
        ArrayList<DocumentBagOfWord> result = search("from fairest creature", invertedIndexPostingList ,allDocAfterPre  );


        DocumentBagOfWord doc1 = result.get(0);
        
//        for (  Map.Entry<String , TermTfIdf> item  : doc1.getTermTfIdf().entrySet() ) {
//            System.out.println("ashiap");
//            System.out.print("Term :" + item.getKey() + " " + item.getValue().tf + " " + item.getValue().idf);  
//            System.out.println("");
//        }
        
        for (DocumentBagOfWord item : result) {
            System.out.println("mantap mantap");
            System.out.println(item.toString());
        }
    }

    private static final String cleanedDataPath = clear_path;

    public static ArrayList<DocumentBagOfWord> search(String input , HashMap<String, HashMap<Integer, ArrayList<Integer>>> invertedIndexPostingList ,List<String> allDocAfterPre ) throws IOException {
        ArrayList<DocumentBagOfWord> result = new ArrayList<DocumentBagOfWord>();

        // read inverted index posting List 
        
        // preprocessed input
        String inputPreprocessed = Searching.inputPreprocessing(input);

        HashMap<String, HashMap<Integer, ArrayList<Integer>>> invertedIndexPostingListAfter = addQueryToInvertedIndexPostingList(inputPreprocessed, invertedIndexPostingList);


        // get the query cosine similarity 
        DocumentBagOfWord query = getQueryBagOfWords(invertedIndexPostingListAfter, inputPreprocessed, allDocAfterPre.size());

        // read all documents 
        int documentNumber = 1;

        for (int i = 0; i < allDocAfterPre.size(); i++) {

            String document = allDocAfterPre.get(i);
            String[] splittedWord = document.split(" ");
            HashMap<String, TermTfIdf> bagOfWord = new HashMap<String, TermTfIdf>();
            for (String word : invertedIndexPostingListAfter.keySet()) {
                word = word.trim();
                if (bagOfWord.get(word) == null) {
                    double tf = TfIdfCount.countTF(invertedIndexPostingListAfter, word, documentNumber, splittedWord.length);
                    // + 1 because n is all document + 1 qquery as document
                    double idf = TfIdfCount.countIDF(invertedIndexPostingListAfter, word, allDocAfterPre.size() + 1);
                    TermTfIdf now = new TermTfIdf(word, tf, idf);
                    bagOfWord.put(word, now);
                }
            }

            // create the object 
            DocumentBagOfWord now = new DocumentBagOfWord(bagOfWord, documentNumber);
            // count the cosine similarity
            now.setCosineSimilarityWithQuery(query);
            // add to the result
            result.add(now);
            documentNumber++;
        }

        // sort for the ranking 
        Collections.sort(result);
        return result;
    }

    public static HashMap<String, HashMap<Integer, ArrayList<Integer>>> addQueryToInvertedIndexPostingList(String inputPreProcessed, HashMap<String, HashMap<Integer, ArrayList<Integer>>> invertedIndexPostingList) {
        HashMap<String, HashMap<Integer, ArrayList<Integer>>> res = invertedIndexPostingList;

        // specify query doc number = 0 
        int doc = 0;
        int idx = 1;
        for (String word : inputPreProcessed.split(" ")) {
            // remove spaces so word with white space and without is the same 
            word = word.trim();
            if (res.get(word) == null) {
                HashMap<Integer, ArrayList<Integer>> postingList = new HashMap<>();
                ArrayList<Integer> list = new ArrayList<>();
                list.add(idx);

                postingList.put(doc, list);

                res.put(word, postingList);

            } else {
                HashMap<Integer, ArrayList<Integer>> postingList = res.get(word);
                ArrayList<Integer> list = postingList.get(doc);

                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(idx);
                postingList.put(doc, list);
            }
            idx++;
        }

        return res;
    }

    private static DocumentBagOfWord getQueryBagOfWords(HashMap<String, HashMap<Integer, ArrayList<Integer>>> invertedIndexPostingListAfter, String inputPreProcessed, int allDocAfterPreSize) {
        int documentNumber = 0;
        int queryWordLength = inputPreProcessed.split(" ").length;
        HashMap<String, TermTfIdf> bagOfWord = new HashMap<String, TermTfIdf>();
        for (String word : invertedIndexPostingListAfter.keySet()) {
            word = word.trim();
            if (bagOfWord.get(word) == null) {
                double tf = TfIdfCount.countTF(invertedIndexPostingListAfter, word, documentNumber, queryWordLength);
                // + 1 because n is all document + 1 qquery as document
                double idf = TfIdfCount.countIDF(invertedIndexPostingListAfter, word, allDocAfterPreSize + 1);
                TermTfIdf now = new TermTfIdf(word, tf, idf);
                bagOfWord.put(word, now);
            }
        }

        DocumentBagOfWord query = new DocumentBagOfWord(bagOfWord, documentNumber);
        return query;
    }
}
