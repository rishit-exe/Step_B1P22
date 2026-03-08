import java.util.*;

class FlashSaleInventory {
    private HashMap<String, Integer> stock = new HashMap<>();

    private HashMap<String, Queue<Integer>> waitingList = new HashMap<>();

    public void addProduct(String productId, int count) {
        stock.put(productId, count);
        waitingList.put(productId, new LinkedList<>());
    }

    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    public synchronized String purchaseItem(String productId, int userId) {

        int currentStock = stock.getOrDefault(productId, 0);

        if (currentStock > 0) {
            stock.put(productId, currentStock - 1);
            return "Success, " + (currentStock - 1) + " units remaining";
        } 
        else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }

    public static void main(String[] args) {

        FlashSaleInventory inventory = new FlashSaleInventory();

        inventory.addProduct("IPHONE15_256GB", 100);

        System.out.println("Stock: " + inventory.checkStock("IPHONE15_256GB"));

        System.out.println(inventory.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(inventory.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 100; i++) {
            inventory.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(inventory.purchaseItem("IPHONE15_256GB", 99999));
    }
}