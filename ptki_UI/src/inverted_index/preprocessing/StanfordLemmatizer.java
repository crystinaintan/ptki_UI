package inverted_index.preprocessing;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
/**
 * This is class for lemmatizing document, using library from standford nlp 
 * how to use 
 *  download library 
 *  extract zip 
 *  on your project folder in your IDE netbeans on thelefft, right click on the library folder
 *  add JAR/ Folder
 *   find
 *    -> stanford-corenlp-3.9.2.jar
 *    -> stanford-corenlp-3.9.2-models.jar
 * 
 * @author ASUS
 */
public class StanfordLemmatizer {

    protected StanfordCoreNLP pipeline;

    public StanfordLemmatizer() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution
        this.pipeline = new StanfordCoreNLP(props);
    }

    /**
     * this method is to lemmatize a string
     * @param documentText
     * @return line string lemmatized text 
     */
    public String lemmatize(String documentText)
    {
        List<String> lemmas = new LinkedList<String>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);

        // run all Annotators on this text
        this.pipeline.annotate(document);

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }
        
        String result = "";
        for (String item : lemmas) {
            result += item+" ";
        }
        
        return result;
    }
    
    
    public static String lemmatization(String document){
        return lemmatization(document);
    }
    
    
}
