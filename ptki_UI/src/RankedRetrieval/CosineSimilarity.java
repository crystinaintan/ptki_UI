/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RankedRetrieval;

import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class CosineSimilarity {
    /**
     * 
     * @param documentA
     * @param documentB
     * @return 
     * 
     * cosM(A,B) = A*B / |A| * |B|
     */
    public static double count(ArrayList<Double> documentA , ArrayList<Double> documentB){
        double upper = 0; 
        for(int i =0 ; i < documentA.size() ; i++){
            upper += (documentA.get(i)*documentB.get(i)) ;
        } 
        
        Double belowA = belowPipe(documentA); 
        Double belowB = belowPipe(documentB);  
        
        return upper / ((belowA) * (belowB));
        
        
        
    }
    
    private static double belowPipe(ArrayList<Double> document){
        double beforeSQRT = 0 ; 
        for (double num : document) {
            beforeSQRT+= Math.pow(num,2);
        }
        
        double afterSQRT = Math.sqrt(beforeSQRT); 
        return afterSQRT; 
    }
}
