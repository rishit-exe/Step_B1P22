class Trade {
    int id, volume;
    Trade(int id, int volume) {
        this.id = id;
        this.volume = volume;
    }
    @Override
    public String toString() {
        return "Trade{id=" + id + ", volume=" + volume + "}";
    }
}
public class TradeAnalysis {
    static void mergeSort(Trade[] a, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(a, l, m);
            mergeSort(a, m + 1, r);
            merge(a, l, m, r);
        }
    }
    static void merge(Trade[] a, int l, int m, int r) {
        int n1 = m - l + 1, n2 = r - m;
        Trade[] L = new Trade[n1];
        Trade[] R = new Trade[n2];
        for (int i = 0; i < n1; i++) L[i] = a[l + i];
        for (int j = 0; j < n2; j++) R[j] = a[m + 1 + j];
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i].volume <= R[j].volume) a[k++] = L[i++];
            else a[k++] = R[j++];
        }
        while (i < n1) a[k++] = L[i++];
        while (j < n2) a[k++] = R[j++];
    }
    static void quickSort(Trade[] a, int l, int h) {
        if (l < h) {
            int p = partition(a, l, h);
            quickSort(a, l, p - 1);
            quickSort(a, p + 1, h);
        }
    }
    static int partition(Trade[] a, int l, int h) {
        int pivot = a[h].volume;
        int i = l - 1;
        for (int j = l; j < h; j++) {
            // Note: >= makes it descending
            if (a[j].volume >= pivot) {
                i++;
                Trade t = a[i]; a[i] = a[j]; a[j] = t;
            }
        }
        Trade t = a[i + 1]; a[i + 1] = a[h]; a[h] = t;
        return i + 1;
    }
    static Trade[] mergeLists(Trade[] a, Trade[] b) {
        int i = 0, j = 0, k = 0;
        Trade[] c = new Trade[a.length + b.length];
        while (i < a.length && j < b.length) {
            if (a[i].volume <= b[j].volume) c[k++] = a[i++];
            else c[k++] = b[j++];
        }
        while (i < a.length) c[k++] = a[i++];
        while (j < b.length) c[k++] = b[j++];
        return c;
    }

    static long total(Trade[] a) {
        long s = 0;
        for (Trade t : a) s += t.volume;
        return s;
    }

    public static void main(String[] args) {
        Trade[] t = { new Trade(3, 500), new Trade(1, 100), new Trade(2, 300) };
        System.out.println("Original: " + java.util.Arrays.toString(t));
        mergeSort(t, 0, t.length - 1);
        System.out.println("After MergeSort (Asc): " + java.util.Arrays.toString(t));
        quickSort(t, 0, t.length - 1);
        System.out.println("After QuickSort (Desc): " + java.util.Arrays.toString(t));
        Trade[] m1 = { new Trade(1, 100), new Trade(2, 300) };
        Trade[] m2 = { new Trade(3, 500) };
        Trade[] merged = mergeLists(m1, m2);
        System.out.println("Merged Result: " + java.util.Arrays.toString(merged));
        System.out.println("Total Volume: " + total(merged));
    }
}
