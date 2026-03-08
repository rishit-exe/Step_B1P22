
public class ParkingLot {

    class Spot {
        String licensePlate;
        long entryTime;
        String status;

        Spot() {
            status = "EMPTY";
        }
    }

    private Spot[] table;
    private int size = 500;
    private int occupied = 0;
    private int totalProbes = 0;
    private int totalParks = 0;

    public ParkingLot() {
        table = new Spot[size];
        for (int i = 0; i < size; i++) {
            table[i] = new Spot();
        }
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % size;
    }

    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].status.equals("OCCUPIED")) {
            index = (index + 1) % size;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = "OCCUPIED";

        occupied++;
        totalProbes += probes;
        totalParks++;

        System.out.println("Assigned spot #" + index + " (" + probes + " probes)");
    }

    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (!table[index].status.equals("EMPTY")) {

            if (table[index].status.equals("OCCUPIED") &&
                table[index].licensePlate.equals(plate)) {

                long durationMs = System.currentTimeMillis() - table[index].entryTime;
                double hours = durationMs / 3600000.0;
                double fee = hours * 5;

                table[index].status = "DELETED";
                occupied--;

                System.out.println("Spot #" + index + " freed. Duration: "
                        + String.format("%.2f", hours) + "h Fee: $" + fee);
                return;
            }

            index = (index + 1) % size;
        }

        System.out.println("Vehicle not found.");
    }

    public void getStatistics() {

        double occupancyRate = (occupied * 100.0) / size;
        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("Occupancy: " + occupancyRate + "%");
        System.out.println("Average Probes: " + avgProbes);
    }

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}