import java.util.*;

public class TransactionAnalyzer {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        long time;
        String account;

        Transaction(int id, int amount, String merchant, long time, String account) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.time = time;
            this.account = account;
        }
    }

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                System.out.println("Pair: (" + map.get(complement).id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    public void findTwoSumWithTime(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction prev = map.get(complement);

                if (Math.abs(t.time - prev.time) <= 3600000) {
                    System.out.println("1hr Pair: (" + prev.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.print("Duplicate: amount=" + list.get(0).amount +
                        " merchant=" + list.get(0).merchant + " accounts=");

                for (Transaction t : list) {
                    System.out.print(t.account + " ");
                }

                System.out.println();
            }
        }
    }

    public void findKSum(int k, int target) {
        kSumHelper(0, k, target, new ArrayList<>());
    }

    private void kSumHelper(int start, int k, int target, List<Integer> path) {

        if (k == 0 && target == 0) {
            System.out.println("KSum IDs: " + path);
            return;
        }

        if (k == 0) return;

        for (int i = start; i < transactions.size(); i++) {

            Transaction t = transactions.get(i);

            path.add(t.id);

            kSumHelper(i + 1, k - 1, target - t.amount, path);

            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {

        TransactionAnalyzer ta = new TransactionAnalyzer();

        long now = System.currentTimeMillis();

        ta.addTransaction(new Transaction(1, 500, "StoreA", now, "acc1"));
        ta.addTransaction(new Transaction(2, 300, "StoreB", now + 1000, "acc2"));
        ta.addTransaction(new Transaction(3, 200, "StoreC", now + 2000, "acc3"));
        ta.addTransaction(new Transaction(4, 500, "StoreA", now + 3000, "acc4"));

        System.out.println("Two Sum:");
        ta.findTwoSum(500);

        System.out.println("\nTwo Sum within 1 hour:");
        ta.findTwoSumWithTime(500);

        System.out.println("\nDuplicates:");
        ta.detectDuplicates();

        System.out.println("\nK-Sum:");
        ta.findKSum(3, 1000);
    }
}