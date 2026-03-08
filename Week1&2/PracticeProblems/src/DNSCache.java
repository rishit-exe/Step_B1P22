import java.util.*;

class DNSCache {

    class DNSEntry {
        String ip;
        long expiryTime;

        DNSEntry(String ip, long ttlSeconds) {
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private int MAX_SIZE = 100;
    private HashMap<String, DNSEntry> cache = new HashMap<>();
    private LinkedHashMap<String, String> lru =
            new LinkedHashMap<>(16, 0.75f, true);

    private int hits = 0;
    private int misses = 0;

    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            lru.get(domain);
            return "Cache HIT -> " + entry.ip;
        }

        misses++;

        String ip = queryUpstream(domain);

        if (cache.size() >= MAX_SIZE) {
            String oldest = lru.keySet().iterator().next();
            cache.remove(oldest);
            lru.remove(oldest);
        }

        cache.put(domain, new DNSEntry(ip, 300));
        lru.put(domain, ip);

        return "Cache MISS -> " + ip;
    }

    private String queryUpstream(String domain) {
        return "172.217.14." + new Random().nextInt(255);
    }

    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) {

        DNSCache dns = new DNSCache();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("facebook.com"));

        dns.getCacheStats();
    }
}