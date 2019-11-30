package RankedRetrieval;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */

public class TermTfIdf {
    public String term ; 
    public double tf ; 
    public double idf ; 
    
    public TermTfIdf(String term , double tf , double idf){
        this.term = term; 
        this.tf = tf ; 
        this.idf = idf ;    
    }
    
}
