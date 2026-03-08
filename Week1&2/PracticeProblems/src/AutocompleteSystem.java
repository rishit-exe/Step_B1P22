import java.util.*;

public class AutocompleteSystem {

    class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<String> queries = new ArrayList<>();
    }

    private TrieNode root = new TrieNode();
    private HashMap<String, Integer> freq = new HashMap<>();

    public void addQuery(String query) {
        freq.put(query, freq.getOrDefault(query, 0) + 1);

        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            if (!node.queries.contains(query)) {
                node.queries.add(query);
            }
        }
    }

    public void updateFrequency(String query) {
        if (!freq.containsKey(query)) {
            addQuery(query);
        } else {
            freq.put(query, freq.get(query) + 1);
        }
    }

    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> freq.get(a) - freq.get(b)
        );

        for (String q : node.queries) {
            pq.offer(q);
            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        Collections.reverse(result);
        return result;
    }

    public static void main(String[] args) {

        AutocompleteSystem ac = new AutocompleteSystem();

        ac.addQuery("java tutorial");
        ac.addQuery("javascript");
        ac.addQuery("java download");
        ac.addQuery("java tutorial");
        ac.addQuery("java 21 features");

        ac.updateFrequency("java 21 features");
        ac.updateFrequency("java 21 features");

        System.out.println(ac.search("jav"));
    }
}