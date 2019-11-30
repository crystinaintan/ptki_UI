/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inverted_index.preprocessing;

/**
 *
 * @author ASUS
 */
public class Normalization {
    
     /**
     * @param line adalah isi file per-line Method ini Berguna untuk
     * menghilangkan punctation
     * @return mengembalikan sebuah String (isi file per-line) yang bebas dari
     * tanda baca.
     */
    public static String removePunctation(String line) {
        return line.replaceAll("[^a-zA-Z ]", "");
    }
    
}
