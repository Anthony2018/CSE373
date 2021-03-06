package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed. Must be non-null and must not contain
     *                  nulls.
     * @param decay     Represents the "decay" factor when computing page rank (see spec). Must be a
     *                  number between 0 and 1, inclusive.
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating. Must be a non-negative number.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges. Must be a non-negative number. (A limit of 0 should
     *                  simply return the initial page rank values from 'computePageRank'.)
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        /*
         Implementation note: We have commented these method calls out so your
         search engine doesn't immediately crash when you try running it for the
         first time.
         Step 1: Make a graph representing the 'internet'
        */


        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

         // Step 2: Use this graph to compute the page rank for each webpage
         this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

         // Note: we don't store the graph as a field: once we've computed the
         // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> returnGraph = new ChainedHashDictionary<>();

        for (Webpage w : webpages) {
            returnGraph.put(w.getUri(), null);
        }

        for (Webpage w: webpages) {
            ISet<URI> wSet = new ChainedHashSet<>();
            for (URI wl : w.getLinks()) {
                if (returnGraph.containsKey(wl)) {
                    wSet.add(wl);
                }
            }
            returnGraph.put(w.getUri(), wSet);
        }

        return  returnGraph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
        IDictionary<URI, Double> rank = new ChainedHashDictionary<>();
        int totalWebPageNumber = graph.size();

        for (KVPair<URI, ISet<URI>> pair : graph) {
            rank.put(pair.getKey(), (double) 1 / totalWebPageNumber);
        }

        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here
            IDictionary<URI, Double> temp = new ChainedHashDictionary<>();
            for (KVPair<URI, ISet<URI>> pair : graph) {
                // for this part has no out pointer
                if (pair.getValue().isEmpty()) {
                    double update = rank.get(pair.getKey()) / rank.size() * decay;
                    for (KVPair<URI, Double> newRank : rank) {
                        //if not contain key put it
                        temp.put(newRank.getKey(), temp.getOrDefault(newRank.getKey(), 0.0) + update);
                    }
                } else {
                    ISet<URI> children = pair.getValue();
                    int linkedMember = children.size();
                    double oldRank = rank.get(pair.getKey());
                    for (URI child : children) {
                        temp.put(child, temp.getOrDefault(child, 0.0) + (oldRank / linkedMember * decay));
                    }
                }
                temp.put(pair.getKey(), temp.getOrDefault(pair.getKey(), 0.0)+(1-decay)/rank.size());
            }

            // Step 3: the convergence step should go here.
            // Return early if we've converged.
            boolean convergence = true;
            for (KVPair<URI, Double> u : temp) {
                if (Math.abs(u.getValue() - rank.get(u.getKey())) >= epsilon) {
                    convergence = false;
                }
            }
            if (convergence) {
                return rank;
            } else {
                rank = temp;
            }
        }
        return rank;
    }
    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}
