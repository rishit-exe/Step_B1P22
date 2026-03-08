import java.util.*;

public class MultiLevelCache {
    private LinkedHashMap<String, String> L1 =
            new LinkedHashMap<>(10000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, String> e) {
                    return size() > 10000;
                }
            };

    private LinkedHashMap<String, String> L2 =
            new LinkedHashMap<>(100000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, String> e) {
                    return size() > 100000;
                }
            };

    private HashMap<String, String> database = new HashMap<>();

    private HashMap<String, Integer> accessCount = new HashMap<>();

    private int L1Hits = 0;
    private int L2Hits = 0;
    private int L3Hits = 0;

    public MultiLevelCache() {

        database.put("video_123", "VideoData123");
        database.put("video_999", "VideoData999");
        database.put("video_555", "VideoData555");
    }

    public String getVideo(String videoId) {

        long start = System.currentTimeMillis();

        if (L1.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        if (L2.containsKey(videoId)) {
            L2Hits++;

            String data = L2.get(videoId);

            System.out.println("L2 Cache HIT (5ms)");

            promoteToL1(videoId, data);

            return data;
        }

        System.out.println("L2 Cache MISS");

        if (database.containsKey(videoId)) {

            L3Hits++;

            String data = database.get(videoId);

            System.out.println("L3 Database HIT (150ms)");

            L2.put(videoId, data);

            accessCount.put(videoId, 1);

            return data;
        }

        return null;
    }

    private void promoteToL1(String videoId, String data) {
        L1.put(videoId, data);

        int count = accessCount.getOrDefault(videoId, 0) + 1;
        accessCount.put(videoId, count);
    }

    public void invalidate(String videoId) {
        L1.remove(videoId);
        L2.remove(videoId);
        System.out.println("Cache invalidated for " + videoId);
    }

    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        double l1Rate = (L1Hits * 100.0) / total;
        double l2Rate = (L2Hits * 100.0) / total;
        double l3Rate = (L3Hits * 100.0) / total;

        System.out.println("\nCache Statistics");

        System.out.println("L1 Hit Rate: " + l1Rate + "% Avg Time: 0.5ms");
        System.out.println("L2 Hit Rate: " + l2Rate + "% Avg Time: 5ms");
        System.out.println("L3 Hit Rate: " + l3Rate + "% Avg Time: 150ms");
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        System.out.println("\nRequest 1");
        cache.getVideo("video_123");

        System.out.println("\nRequest 2");
        cache.getVideo("video_123");

        System.out.println("\nRequest 3");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}