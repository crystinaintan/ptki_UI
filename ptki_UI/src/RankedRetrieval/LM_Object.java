package RankedRetrieval;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ASUS
 */
public class LM_Object implements Comparable<LM_Object> {

    private int docNum;
    private double lmScore;

    public LM_Object(int docNum, double lmScore) {
        this.docNum = docNum;
        this.lmScore = lmScore;
    }

    /**
     * METHOD TO COUNT LANGUAGE MODEL
     *
     * @param splittedPreProcessedinput
     * @param numDoc
     * @param inverted_Index
     * @return
     */
    public static double countlmScore(String[] splittedPreProcessedinput, int numDoc, HashMap<String, HashMap<Integer, ArrayList<Integer>>> inverted_Index, double alpha, int howManyWordInEveryDoc, int docLength) {
        double result = 1;
        for (String word : splittedPreProcessedinput) {
            // P (tk|Md) this doc 
            double pTerm_ThisDoc = 0;
            // P (tk|Mc) all
            double pTerm_AllDoc = 0;
            HashMap<Integer, ArrayList<Integer>> postingList = inverted_Index.get(word);
            if (postingList != null) {
                ArrayList<Integer> list = postingList.get(numDoc);
                pTerm_AllDoc = postingList.size();
                if (list != null) {
                    pTerm_ThisDoc = list.size();
                }
            }
            result *= (alpha * ((double)pTerm_ThisDoc / docLength) + (1 - alpha) * ((double)pTerm_AllDoc / howManyWordInEveryDoc));
        }
        return result;
    }

    public int getDocNum() {
        return docNum;
    }

    public double getLmScore() {
        return lmScore;
    }

    /**
     * for sorting by descending
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(LM_Object o) {
        if (this.lmScore > o.lmScore) {
            return -1;
        } else if (this.lmScore < o.lmScore) {
            return 1;
        } else {
            return 0;
        }
    }
}
