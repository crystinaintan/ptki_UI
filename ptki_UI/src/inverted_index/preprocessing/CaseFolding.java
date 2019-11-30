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
public class CaseFolding {
    /**
     * @param line adalah isi file per-line
     * Method ini Berguna untuk menangani casefolding
     * @return mengembalikan sebuah String (isi file per-line) yang huruf kecil semua.
     * 
     */
    public static String caseFolding(String line){
        return line.toLowerCase();
    }
}
