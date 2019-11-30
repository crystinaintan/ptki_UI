/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inverted_index;

import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class DictionaryAndPosting implements Comparable<DictionaryAndPosting>{
    private ArrayList<Integer> posting ;
    private String term ; 
    
    public DictionaryAndPosting(String term , ArrayList<Integer> posting) {
        this.term = term ; 
        this.posting = posting ; 
    }

    @Override
    public int compareTo(DictionaryAndPosting o) {
        return posting.size() - o.getPosting().size();
        
    }

    public ArrayList<Integer> getPosting() {
        return posting;
    }

    public String getTerm() {
        return term;
    }
    
    
    
    
}
