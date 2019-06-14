package search.analyzers;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    private IDictionary<URI, Double> documentNormDic;

    // Feel free to add extra fields and helper methods.

    /**
     * @param webpages  A set of all webpages we have parsed. Must be non-null and
     *                  must not contain nulls.
     */
    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.documentNormDic = this.buildNormDic(documentTfIdfVectors);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        // throw new NotYetImplementedException();
        int totalWebPageSize = pages.size(); // Count Total number of docs

        // Build Up Dic for count Number of docs containing the term(word)
        IDictionary<String, Integer> wordCountAll = new ChainedHashDictionary<>();
        for (Webpage p : pages) { // Loop each page in all pages
            IList<String> pWords = p.getWords();
            // Build up a temp dic for check if a term appears in a page
            IDictionary<String, Integer> wordCountsInOnePage = new ChainedHashDictionary<>();
            for (String w : pWords) {
                wordCountsInOnePage.put(w, 1);
            }

            // Count Number of docs containing the term
            for (KVPair<String, Integer> pair : wordCountsInOnePage) {
                wordCountAll.put(pair.getKey(), wordCountAll.getOrDefault(pair.getKey(), 0)+1);
            }
        }

        // Compute overall idfScore for return
        IDictionary<String, Double> idfScoreReturn = new ChainedHashDictionary<>();
        for (KVPair<String, Integer> pair : wordCountAll) {
            idfScoreReturn.put(pair.getKey(), Math.log(totalWebPageSize/ (double) pair.getValue()));
        }
        return idfScoreReturn;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        // throw new NotYetImplementedException();
        int totalSize = words.size(); // Compute Total number of words in a doc

        // Count Number of times the term appears in doc
        IDictionary<String, Integer> wordCounts = new ChainedHashDictionary<>();
        for (String w : words) {
            wordCounts.put(w, wordCounts.getOrDefault(w, 0)+1);
        }

        // Compute Tf scores for this doc
        IDictionary<String, Double> tfScores = new ChainedHashDictionary<>();
        for (KVPair<String, Integer> pair : wordCounts) {
            tfScores.put(pair.getKey(), pair.getValue()/(double) totalSize);
        }
        return tfScores;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        // throw new NotYetImplementedException();
        IDictionary<URI, IDictionary<String, Double>> allTfIdfVec = new ChainedHashDictionary<>();
        for (Webpage p : pages) { // Loop each page in all pages
            IList<String> pWords = p.getWords(); // Get page's Word List for computing page's TfScores
            URI pURI = p.getUri(); // Get page's URI

            /**
             * Compute current page's TfIdfVector
             */
            IDictionary<String, Double> pTfIdfVectors = new ChainedHashDictionary<>();
            for (KVPair<String, Double> pair : computeTfScores(pWords)) {
                pTfIdfVectors.put(pair.getKey(), pair.getValue() * this.idfScores.get(pair.getKey()));
            }

            allTfIdfVec.put(pURI, pTfIdfVectors); // Put current page's TfIdfVector into returned Dictionary
        }
        return allTfIdfVec;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        IDictionary<String, Double> documentVector = this.documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = new ChainedHashDictionary<>();

        IDictionary<String, Double> queryTfScores = computeTfScores(query);
        for (String w : query) {
            queryVector.put(w, queryTfScores.get(w) * this.idfScores.get(w));
        }

        double numerator = 0.0;
        for (KVPair<String, Double> pair : queryVector) {
            double docWordScore = documentVector.getOrDefault(pair.getKey(), 0.0);
            double queryWordScore = pair.getValue();
            numerator += docWordScore * queryWordScore;
        }

        double denominator = this.documentNormDic.get(pageUri) * norm(queryVector);

        if (denominator != 0) {
            return (numerator/denominator);
        } else {
            return 0.0;
        }
    }

    private IDictionary<URI, Double> buildNormDic(IDictionary<URI, IDictionary<String, Double>> vectorDic) {
        IDictionary<URI, Double> normDic = new ChainedHashDictionary<>();

        for (KVPair<URI, IDictionary<String, Double>> pair : vectorDic) {
            normDic.put(pair.getKey(), norm(pair.getValue()));
        }

        return normDic;
    }

    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> pair : vector) {
            double score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}
