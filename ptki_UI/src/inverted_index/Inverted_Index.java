package inverted_index;

import inverted_index.preprocessing.CaseFolding;
import inverted_index.preprocessing.Normalization;
import inverted_index.preprocessing.StanfordLemmatizer;
import inverted_index.preprocessing.Stemmer;
import inverted_index.preprocessing.Stopwords;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import static jdk.nashorn.tools.ShellFunctions.input;
import org.tartarus.snowball.ext.PorterStemmer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
//import org.ejml.ops.NormOps;

/**
 *
 * @author crystinaintan
 */
public class Inverted_Index {

    /**
     * @param args the command line arguments
     *
     *
     */
    public static String DATA_SET_FOLDER_PATH = "D:\\KULIAH SEMSETER 7\\PTKI\\Tugas\\ptki_UI\\DataSet";
    public static String CLEANED_DATA_SET_FOLDER_PATH   = "D:\\KULIAH SEMSETER 7\\PTKI\\Tugas\\ptki_UI\\Clean_data";
    public static String STOP_WORD_FOLDER_PATH = "D:\\KULIAH SEMSETER 7\\PTKI\\Tugas\\ptki_UI\\stopwords.txt";
    
    public static StanfordLemmatizer sf ;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        String pathFile = DATA_SET_FOLDER_PATH;

 //1       File[] folder = findFoldersInDirectory(pathFile);

//        int totalWord = countInAllDoc(folder);
//        String output = "";
//        output += "Jumlah Seluruh Words : " + totalWord + "\n";
//1        List<String> dataProcessed = dataProcessingAll(folder);
//        output += "Rata-rata Words : " + wordAverage(totalWord, folder.length) + "\n";
//1        HashMap<String, ArrayList<Integer>> map = IndexGenerator.generatePosting(dataProcessed);
//        output += "Jumlah Seluruh Term : " + map.size() + "\n";
//        float average = (float) map.size() / folder.length;
//        output += "Rata-rata Term : " + average + "\n";
//        output += dataProcessed.get(2) + "\n";
//        System.out.println("Statisktika Laporan  \n" + output);
//        System.out.println("Jumlah Term pada masing-masing dokumen : \n" + printEveryDocAFterPre(dataProcessed));             
//        HashMap<String, ArrayList<Integer>> map = IndexGenerator.readInvertedIndex();
//        System.out.println(map.size());
//        for (Map.Entry<String, ArrayList<Integer>> item : map.entrySet()) {
//            System.out.println(item.getKey() + " " + item.getValue());
//        }
        
        
//        System.out.println("Query 1 : For Nothing \n Hasil Search 1 : "+Searching.searchUsingPosting("For Nothing", map));
//        System.out.println("");
//        System.out.println("Query 2 : Loe in the Orient when the gracious light \n Hasil Search 2 :  "+Searching.searchUsingPosting("Loe in the Orient when the gracious light", map));
        
       
        List<String> dataProcessed = Inverted_Index.read_Cleaned_Data();
        HashMap<String , HashMap<Integer , ArrayList<Integer>>>inverted_IndexPostingList = IndexGenerator.readInvertedIndexPostingList();
     
        
        
        

    }

    public static List<String> read_Cleaned_Data() throws IOException{
         File[] folder_Cleaned = findFoldersInDirectory(CLEANED_DATA_SET_FOLDER_PATH); 
        List<String> dataProcessed = new ArrayList<String>();
        for (File item : folder_Cleaned) {
            String doc = generateLine(item);
            dataProcessed.add(doc);
        } 
        return dataProcessed;
    }
    
   

    /**
     * @param directoryPath adalah path directory Method ini Berguna untuk
     * mendapatkan seluruh file yang ada di dalam sebuah folder path
     * @return listOfFiles adalah sebuah array yang berisi file-file yang ada di
     * dalam sebuah folder path
     *
     */
    public static File[] findFoldersInDirectory(String directoryPath) {

        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }

    /**
     * METHOD UNTUK TESTING STOP WORDS
     *
     * @param line : line of string as an input
     * @param listOfStopWords : a list contains stopwords
     * @return line of sting with removed stop words
     */
    public static String checking(String line, List<String> listOfStopWords) {
        String hasil = line;
        for (String stopWord : listOfStopWords) {
            hasil = hasil.replaceAll(stopWord, "****");
        }
        return hasil;
    }

    /**
     * METHOD UNTUK TESTING STOP WORDS
     *
     * @param line : line of string as an input
     * @param listOfStopWords : a list contains stopwords
     * @return line of sting with removed stop words
     */
    public static List<String> splitInToList(String line) {
        List<String> listOfClearWords = new LinkedList<String>();
        for (String s : line.split(" ")) {
            if (s != null || s.trim() != " ") {
                listOfClearWords.add(s);
            }
            //System.out.println(s);
        }
        //System.out.println(listOfClearWords.size());
        return listOfClearWords;
    }

    /**
     * METHOD UNTUK Menghitung jumlah Kata dalam 1 list (1 doc)
     *
     * @param dokumen : list berupa KUmpulan kata dalam sebuah doc
     * @return countWord jumlah kata setelah dilakukan data preprocesing dalam
     * sebuah dokumen
     */
    public static int countWordsinDoc(List<String> dokumen) {
        int countWord = 0;
        for (String s : dokumen) {
            String afterTrim = s.trim();
            countWord += (afterTrim.length() > 0) ? 1 : 0;
        }
        return countWord;
    }

    /**
     * METHOD UNTUK Mendapatkan isi file dalam sebuah dokumen
     *
     * @param path : String yang berisi path dari sebuah dokumen
     * @return hasil : String yang berisi kata-kata (isi file) dalam sebuah
     * dokumen
     */
    public static String readFileByPath(String path) throws IOException {
        Path thisPath = Paths.get(path);
        String hasil = "";
        List<String> allLines = Files.readAllLines(thisPath);
        //    System.out.println("INI ADALAH ISI FILE AWAL : \n");
        for (String lines : allLines) {
            //        System.out.println(lines);
            hasil += " " + lines + " \n";
        }
        return hasil;
    }

    /**
     * METHOD UNTUK Mencari rata-rata kata dalam seluruh dokumen (Method Untuk
     * keperluan Statistika Laporan)
     *
     * @param marks : List berupa (KUmpulan) jumlah kata untuk masing-masing doc
     * @return hasil : rata-rata element dari list marks
     */
    public static double calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        double hasil = 0.0;
        if (!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            hasil = sum.doubleValue() / marks.size();
        }
        return hasil;
    }

    /**
     * METHOD UNTUK Menghitung jumlah seluruh kata dalam seluruh dokumen (untuk
     * keperluan Statistika Laporan awal Tugas Besar)
     *
     * @param marks : List berupa (KUmpulan) jumlah kata untuk masing-masing doc
     * @return hasil : jumlah element dari list marks
     */
    public static int calculateSumOfWords(List<Integer> marks) {
        Integer sum = 0;
        for (Integer mark : marks) {
            sum += mark;
        }
        return sum;
    }

    public static String dataPreprocessing(File dokumen) throws IOException {
        String document = generateLine(dokumen);

       String merged = "";
        String inputted = document.replaceAll("[\r\n]", " ");
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

    public static List<String> dataProcessingAll(File[] folder) throws IOException {
        List<String> result = new LinkedList<String>();

        Inverted_Index.sf = new StanfordLemmatizer();
        for (File itemFile : folder) {
            String hasil = dataPreprocessing(itemFile);
            makeFile(hasil, itemFile.getName());
            result.add(hasil);
        }

        return result;
    }

    public static String generateLine(File fileDocument) throws IOException {
        Path thisPath = Paths.get(fileDocument.getPath());
        String hasil = "";
        List<String> allLines = Files.readAllLines(thisPath);
        for (String lines : allLines) {
            hasil += " " + lines + " \n";
        }

        return hasil;
    }

    public static int countInAllDoc(File[] folder) throws IOException {
        int result = 0;
        for (File fileItem : folder) {
            String file = generateLine(fileItem);
            List<String> list = splitInToList(file);

            result += countWordsinDoc(list);
        }
        return result;
    }

    public static float wordAverage(int totalWord, int totalDocument) {
        return totalWord / totalDocument;
    }

    public static List<Integer> printEveryDocAFterPre(List<String> afterProcessed) {
        List<Integer> result = new LinkedList<Integer>();
        for (String document : afterProcessed) {
            String[] splitted = document.split(" ");
            int sizeDocument = splitted.length;

            result.add(sizeDocument);

        }

        return result;
    }

    public static void makeFile(String isi, String namaFile) throws IOException {

        Files.write(Paths.get("D://KULIAH SEMSETER 7/PTKI/Tugas/Tugas Besar/Clean_data/" + namaFile + ".txt"), isi.getBytes());
    }

//    public static void generateAllProcessedFile() throws IOException{
//        List<String> allDataPreprocessed = dataProcessingAll(findFoldersInDirectory(DATA_SET_FOLDER_PATH));
//        for(String docAfter : allDataPreprocessed){
//             makeFile(docAfter,itemFile.getName());
//            
//        }        
//
//    }
}
