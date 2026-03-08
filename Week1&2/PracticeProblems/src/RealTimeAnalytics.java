import java.util.*;

class RealTimeAnalytics {
    private HashMap<String, Integer> pageViews = new HashMap<>();

    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    private HashMap<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(String url, String userId, String source) {

        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    public void getDashboard() {

        System.out.println("Top Pages:");

        List<Map.Entry<String, Integer>> pages =
                new ArrayList<>(pageViews.entrySet());

        pages.sort((a, b) -> b.getValue() - a.getValue());

        for (int i = 0; i < Math.min(10, pages.size()); i++) {
            String page = pages.get(i).getKey();
            int views = pages.get(i).getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println((i + 1) + ". " + page +
                    " - " + views + " views (" + unique + " unique)");
        }

        System.out.println("\nTraffic Sources:");
        int total = trafficSources.values().stream().mapToInt(i -> i).sum();

        for (String source : trafficSources.keySet()) {
            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;

            System.out.println(source + ": " + percent + "%");
        }
    }

    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent("/article/breaking-news", "user_123", "google");
        analytics.processEvent("/article/breaking-news", "user_456", "facebook");
        analytics.processEvent("/sports/championship", "user_789", "direct");
        analytics.processEvent("/article/breaking-news", "user_123", "google");

        analytics.getDashboard();
    }
}