import java.util.*;

class PlagiarismDetector {
    private HashMap<String, Set<String>> index = new HashMap<>();

    private int N = 5;

    public void addDocument(String docId, String text) {
        List<String> ngrams = getNGrams(text);

        for (String gram : ngrams) {
            index.putIfAbsent(gram, new HashSet<>());
            index.get(gram).add(docId);
        }
    }

    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = getNGrams(text);
        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {
            if (index.containsKey(gram)) {
                for (String d : index.get(gram)) {
                    matchCount.put(d, matchCount.getOrDefault(d, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String d : matchCount.keySet()) {
            int matches = matchCount.get(d);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Matches with " + d + ": " + matches +
                    " -> Similarity: " + similarity + "%");
        }
    }

    private List<String> getNGrams(String text) {
        String[] words = text.split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            grams.add(sb.toString().trim());
        }

        return grams;
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        detector.addDocument("essay_089.txt",
                "machine learning is a field of artificial intelligence");

        detector.addDocument("essay_092.txt",
                "machine learning is a field of artificial intelligence used today");

        detector.analyzeDocument("essay_123.txt",
                "machine learning is a field of artificial intelligence");
    }
}