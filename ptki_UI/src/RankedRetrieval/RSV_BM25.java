/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RankedRetrieval;

import java.util.HashMap;

/**
 *
 * @author ASUS
 */
public class RSV_BM25 implements Comparable<RSV_BM25> {

    public int numDoc;
    public double rsvScore;

    public static double k1 = 1.2;
    public static double b = 0.75;
    public static double k3 = 2;

    public RSV_BM25(int numDoc, double rsvScore) {
        this.numDoc = numDoc;
        this.rsvScore = rsvScore;
    }

    /**
     * BM25 SEARCHING FORMULA
     *
     * @param numDoc
     * @param bm25_BagOfWord
     * @param splittedPreProcessedInput
     * @return
     */
    public static double countRSV(int numDoc, HashMap<String, HashMap<Integer, Integer>> bm25_BagOfWord, String[] splittedPreProcessedInput, int docLength, double lengthAve, int howManyDoc, HashMap<String, Integer> queryTermBag) {
        double result = 0;
        // SUM OF FORMULA
        for (String word : splittedPreProcessedInput) {
            double df_WordNow = 0;
            double tf_WordDocNow = 0;
            HashMap<Integer, Integer> termBag = bm25_BagOfWord.get(word);
            if (termBag != null) {
                df_WordNow = termBag.size();
                if (termBag.get(numDoc) != null) {

                    tf_WordDocNow = termBag.get(numDoc);
                }
            }
            double tf_WordQueryNow = queryTermBag.get(word);
            result += Math.log(howManyDoc / df_WordNow) * ((k1 + 1) * tf_WordDocNow / k1 * ((1 - b) + b * (docLength / lengthAve)) + tf_WordDocNow)
                    * ((k3 + 1) * tf_WordQueryNow / (k3 + tf_WordQueryNow));
        }

        return result;
    }

    public int getNumDoc() {
        return numDoc;
    }

    public double getRsvScore() {
        return rsvScore;
    }

    @Override
    public int compareTo(RSV_BM25 o) {
        if (this.rsvScore > o.rsvScore) {
            return -1;
        } else if (this.rsvScore < o.rsvScore) {
            return 1;
        } else {
            return 0;
        }
    }

}
