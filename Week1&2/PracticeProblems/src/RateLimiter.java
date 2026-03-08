import java.util.*;

class RateLimiter {

    class TokenBucket {
        int tokens;
        int maxTokens;
        long lastRefillTime;

        TokenBucket(int maxTokens) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }

        void refill() {
            long now = System.currentTimeMillis();
            long diff = now - lastRefillTime;

            if (diff >= 3600000) {
                tokens = maxTokens;
                lastRefillTime = now;
            }
        }
    }

    private HashMap<String, TokenBucket> clients = new HashMap<>();
    private int LIMIT = 1000;

    public synchronized String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(LIMIT));
        TokenBucket bucket = clients.get(clientId);

        bucket.refill();

        if (bucket.tokens > 0) {
            bucket.tokens--;
            return "Allowed (" + bucket.tokens + " requests remaining)";
        } else {
            return "Denied (0 requests remaining)";
        }
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("No requests yet.");
            return;
        }

        int used = bucket.maxTokens - bucket.tokens;
        System.out.println("{used: " + used +
                ", limit: " + bucket.maxTokens +
                ", reset in ms: " + (3600000 - (System.currentTimeMillis() - bucket.lastRefillTime)) + "}");
    }

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        limiter.getRateLimitStatus("abc123");
    }
}