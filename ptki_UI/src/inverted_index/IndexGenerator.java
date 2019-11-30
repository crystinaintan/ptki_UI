/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inverted_index;

import inverted_index.preprocessing.Stopwords;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public class IndexGenerator {

    public static void main(String[] args) throws IOException {
        String a = "In louing thee thou know’st I am forsworne,\n" +
"But thou art twice forsworne to me loue swearing,\n" +
"In act thy bed-vow broake and new faith torne,\n" +
"In vowing new hate after new loue bearing:\n" +
"But why of two othes breach doe I accuse thee,\n" +
"When I breake twenty: I am periur’d most,\n" +
"For all my vowes are othes but to misuse thee:\n" +
"And all my honest faith in thee is lost\n" +
"For I haue sworne deepe othes of thy deepe kindnesse:\n" +
"Othes of thy loue, thy truth, thy constancie,\n" +
"And to inlighten thee gaue eyes to blindnesse,\n" +
"Or made them swere against the thing they see.\n" +
"For I haue sworne thee faire: more periurde eye,\n" +
"To swere against the truth so foule a lie." ; 
        
        System.out.println("Hasil Search : "+Searching.inputPreprocessing(a));
    }

    /**
     *
     * @param allDocumentAfterPre
     * @return
     */
    public static HashMap<String, ArrayList<Integer>> generatePosting(List<String> allDocumentAfterPre) { 
        
        HashMap<String, ArrayList<Integer>> invertedIndex = new HashMap();
        
        int doc = 1;
        // iterate every document
        for (String document : allDocumentAfterPre) {
            
            String[] splitted = document.split(" ");
            for (String word : splitted) {
                // remove spaces so word with white space and without is the same 
                word = word.trim();
                if (invertedIndex.get(word) == null) {
                    ArrayList<Integer> postings = new ArrayList();
                    postings.add(doc);
                    
                    invertedIndex.put(word, postings);
                } else {
                    ArrayList<Integer> postings = invertedIndex.get(word);
                    postings.add(doc);
                    
                    invertedIndex.put(word, postings);
                }
            }
            doc++;
        }
        
        return invertedIndex;
    }
    
    public static HashMap<String, HashMap<Integer, ArrayList<Integer>>> generatePostingList(List<String> allDocumentAfterPre) {
        HashMap<String, HashMap<Integer, ArrayList<Integer>>> invertedIndex = new HashMap();
        
        int doc = 1;
        // iterate every document
        for (String document : allDocumentAfterPre) {
            
            String[] splitted = document.split(" ");
            int idx = 1;
            for (String word : splitted) {
                // remove spaces so word with white space and without is the same 
                word = word.trim();
                if (invertedIndex.get(word) == null) {
                    HashMap<Integer, ArrayList<Integer>> postingList = new HashMap<>();
                    ArrayList<Integer> list = new ArrayList<>();
                    list.add(idx);
                    
                    postingList.put(doc, list);
                    
                    invertedIndex.put(word, postingList);
                    
                } else {
                    HashMap<Integer, ArrayList<Integer>> postingList = invertedIndex.get(word);
                    ArrayList<Integer> list = postingList.get(doc);
                    
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(idx);
                    postingList.put(doc, list);
                }
                idx++;
                
            }
            doc++;
        }
        
        return invertedIndex;
    }
    
    public static void testPostingList(List<String> all) {
        HashMap<String, HashMap<Integer, ArrayList<Integer>>> res = IndexGenerator.generatePostingList(all);
        
        for (Map.Entry<String, HashMap<Integer, ArrayList<Integer>>> item : res.entrySet()) {
            System.out.println("word :" + item.getKey());
            
            HashMap<Integer, ArrayList<Integer>> postingList = item.getValue();
            for (Map.Entry<Integer, ArrayList<Integer>> postingPerTerm : postingList.entrySet()) {
                System.out.print("Doc : " + postingPerTerm.getKey());
                System.out.print(" " + postingPerTerm.getValue().toString());
            }
            System.out.println("");
        }
    } 
    
     public static void saveInvertedIndex(HashMap<String, ArrayList<Integer>> hmap) {
        try {
            FileOutputStream fos
                    = new FileOutputStream("invertedIndex.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hmap);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in hashmap.ser");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static HashMap<String, ArrayList<Integer>> readInvertedIndex() {
        HashMap<String, ArrayList<Integer>> map = null;
        try {
            FileInputStream fis = new FileInputStream("invertedIndex.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }
        return map;
    }
    
    public static void saveInvertedIndexPostingList(HashMap<String,HashMap<Integer, ArrayList<Integer>>> invertedIndexPostingList) {
        try {
            FileOutputStream fos
                    = new FileOutputStream("invertedIndexPostingList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(invertedIndexPostingList);
            oos.close();
            fos.close();
            System.out.printf("Serialized Inverted Index postling List HashMap data is saved in invertedIndexPostingList.ser");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    } 
    
      public static HashMap<String, HashMap<Integer , ArrayList<Integer>>> readInvertedIndexPostingList() {
       HashMap<String, HashMap<Integer , ArrayList<Integer>>> map = null;
        try {
            FileInputStream fis = new FileInputStream("invertedIndexPostingList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }
        return map;
    }
    
    
    
}
