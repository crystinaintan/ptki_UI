/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inverted_index.preprocessing;

import static inverted_index.Inverted_Index.STOP_WORD_FOLDER_PATH;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class Stopwords {

    /**
     * Methods used for get list of stop words in english
     */
    public static List<String> readStopWords(String path_stop_word) throws IOException {
        List<String> listOfStopWords = new LinkedList<String>();
        Path thisPath = Paths.get(STOP_WORD_FOLDER_PATH);
        List<String> allLines = Files.readAllLines(thisPath);
        for (String lines : allLines) {
            listOfStopWords.add(" " + lines + " ");
        }
        return listOfStopWords;
    }

    public static String stopWord(String dokumen) throws IOException {
        List<String> listOfStopWords = readStopWords(STOP_WORD_FOLDER_PATH);

        String hasilAkhir = removeStopWords(dokumen, listOfStopWords);
        return hasilAkhir;
    }

    /**
     *
     * @param line : line of string as an input
     * @param listOfStopWords : a list contains stopwords
     * @return line of sting with removed stop words
     */
    public static String removeStopWords(String line, List<String> listOfStopWords) {
        String hasil = line;
        for (String stopWord : listOfStopWords) {

            hasil = hasil.replaceAll(stopWord, " ");

            stopWord = stopWord.trim();
            stopWord = "\n" + stopWord + " ";

            hasil = hasil.replaceAll(stopWord, " ");

            stopWord = stopWord.trim();
            stopWord = " " + stopWord + "\n";
            hasil = hasil.replaceAll(stopWord, " "); 
            
           
           
            
        }
        return hasil;
    }

    
}
