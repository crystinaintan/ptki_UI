/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inverted_index;

import static inverted_index.Inverted_Index.generateLine;
import inverted_index.preprocessing.CaseFolding;
import inverted_index.preprocessing.Normalization;
import inverted_index.preprocessing.StanfordLemmatizer;
import inverted_index.preprocessing.Stemmer;
import inverted_index.preprocessing.Stopwords;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ASUS
 */
public class Searching {
   

    public static List<Integer> intersectTwoPostingList(List<Integer> term1, List<Integer> term2) {
        List<Integer> result = new ArrayList<>();
        int idx = 0;
        while (idx <= term1.size() - 1) {
            int term1Doc = term1.get(idx);
            if (term2.contains(term1Doc)) {
                result.add(term1Doc);
            }
            idx++;
        }

        return result;
    }
    
    public static List<Integer> conjuntTwoPostingList(List<Integer> term1, List<Integer> term2){
         List<Integer> result = new ArrayList<Integer>(term1);
         result.addAll(term2);
        
        // convert result to hashset to remove duplicates
        LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(result);         
        ArrayList<Integer> listWithoutDuplicates = new ArrayList<>(hashSet);
        return listWithoutDuplicates;
    }
    
    public static void activateLemmatization(){
        Inverted_Index.sf = new StanfordLemmatizer();
    }

    public static List<Integer> searchUsingPosting(String input, HashMap<String, ArrayList<Integer>> invertedIndex) throws IOException {
        List<Integer> result;
        // method searchUsingPosting (String input , hashmap generatedPosting)
        //read input 
        // preprocess 
        // split 
        // insert into set
        //  for every element in set get the posting from the hashmap result 
        // create into object comparable and insert into  list then sort 
        // for every sorted dictionary and posting, intersect two of them 
        // return the numberdoc result 
        Inverted_Index.sf = new StanfordLemmatizer();
        String preProcessedInput = inputPreprocessing(input);
        String[] splittedInput = preProcessedInput.split(" ");
        Set<String> distinctWord = new HashSet<String>();
        for (String term : splittedInput) {
            distinctWord.add(term);
        }

        List<DictionaryAndPosting> sortedList = new ArrayList<>();

        for (String word : distinctWord) {
            ArrayList<Integer> posting = invertedIndex.get(word);
            if (posting != null) {
                DictionaryAndPosting now = new DictionaryAndPosting(word, invertedIndex.get(word));
                sortedList.add(now);
            }
        }
        // sort for intersect optimization
        Collections.sort(sortedList);

        // Start intersecting every posting 
        result = sortedList.get(0).getPosting();
        for (int i = 0; i < sortedList.size(); i++) {
            DictionaryAndPosting now = sortedList.get(i);
            ArrayList<Integer> postingNow = now.getPosting();

            // there are 2 searching methods : 1 with AND then 1 with OR
            //  USE THIS FOR INSTERSECT : AND
            //result = intersectTwoPostingList(result, postingNow);
            
            // USE THIS FOR CONJUNCTION : OR 
            result = conjuntTwoPostingList(result,postingNow);
            
            
        }
        return result;
    }

    public static String inputPreprocessing(String input) throws IOException {
        String merged = "";
        String inputted = input.replaceAll("[\r\n]", " ");
      for (String perItem : inputted.split(" ")) {
            perItem = " " + perItem + " "; 
            merged+= perItem; 
        }

        String caseFolding = CaseFolding.caseFolding(merged);
//        System.out.println("case flding :" + caseFolding);
        String punctuation = Normalization.removePunctation(caseFolding);
//         System.out.println("punctuation : \n"+punctuation);
//        System.out.println("");
        String stopWord = Stopwords.stopWord(punctuation);
//         System.out.println("stopWord : \n"+stopWord);
//        System.out.println("");

        // lemmatize every document 
        String lemmatizedDocument = Inverted_Index.sf.lemmatize(stopWord);
//         System.out.println("lemmatizedDocument : \n"+lemmatizedDocument);
//         System.out.println("");
        String porterStammer = Stemmer.porterStemmer(lemmatizedDocument);
//           System.out.println("lemmatized" + porterStammer);
        return porterStammer;
    }
}
