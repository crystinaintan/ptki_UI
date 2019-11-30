/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inverted_index.preprocessing;

import inverted_index.Inverted_Index;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class Stemmer {
     /**
     * 
     * Stem a word using Porter Stemming algorithm 
     * By using library from :
     * https://stackoverflow.com/questions/28083377/built-in-porter-stemmer-in-java-opennlp-toolkit
     * 
     * how to add library : 
     *  1. download the library (pasti dong) 
     *  2. add the jar/ library / referenced project by right clicking the libray folder in your project mate 
     *  3. Import the class 
     *  4. create the Porter Stemmer object 
     *  
     * @param word
     * @return 
     */
    public static String stem(String word){
      org.tartarus.snowball.ext.PorterStemmer stemmer = new org.tartarus.snowball.ext.PorterStemmer();
      stemmer.setCurrent(word); //set string you need to stem  
      stemmer.stem();  //stem the word
      String res = stemmer.getCurrent();//get the stemmed word
      return res;
    }
    
    public static String porterStemmer(String dokumen){
        String hasil = "";
        List<String> temp  = Inverted_Index.splitInToList(dokumen);
      for(String s: temp){
            if(!(s.equalsIgnoreCase(" ")))
            {
                hasil+= stem(s)+ " ";
            }
        }
      return hasil;
    }
}
