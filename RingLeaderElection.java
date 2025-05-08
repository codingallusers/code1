import java.util.*;

public class RingLeaderElection {
    static int num_pr;           // number of processes
    static int old_cord;         // the failed coordinator or leader
    static int new_cord;         // the new elected leader
    static int initiator;        // the process that is holding the election
    static int isActive[];       // array to track active/inactive processes
    static int failed_process;   // the failed process
    static int arr[];            // array to store election messages

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of processes : ");
        num_pr = sc.nextInt();

        isActive = new int[num_pr + 1]; // process numbering starts from 1
        for (int i = 1; i <= num_pr; i++) {
            isActive[i] = 1; // all processes initially active
        }

        old_cord = num_pr; // highest numbered process is initial coordinator
        isActive[old_cord] = 0; // leader fails

        System.out.println("Enter the process that initiates the election process: ");
        initiator = sc.nextInt();

        System.out.println("The process that failed is: " + old_cord + "\n");

        System.out.println("Enter the process that fails (other than the leader process), if none then enter 0 : ");
        failed_process = sc.nextInt();
        if (failed_process != 0) {
            isActive[failed_process] = 0; // mark failed process as inactive
        }

        // Output
        new_cord = election_process(isActive, old_cord, initiator);
        System.out.println("\nFinally process " + new_cord + " became the new leader\n");

        // Passing the leader message to all active processes
        for (int i = 1; i <= num_pr; i++) {
            if (isActive[i] == 1 && i != new_cord) {
                System.out.println("Process " + new_cord + " passes a Coordinator (" + new_cord + ") message to process " + i);
            }
        }
    }

    // Method to handle the election process
    public static int election_process(int isActive[], int old_cord, int initiator) {
        System.out.println("\nThe election process is started by process " + initiator);
        int index = 0;
        arr = new int[num_pr + 1];
        int i = initiator;
        int receiver = (i % num_pr) + 1;

        // The election process continues until all processes are considered
        while (index < num_pr) {
            if (isActive[i] == 1 && i != receiver) {
                // Skip failed receiver
                while (isActive[receiver] == 0 && receiver != i) {
                    receiver = (receiver % num_pr) + 1;
                }
                if (receiver == i) {
                    break; // All others failed
                }

                // Send election message
                System.out.println("Process " + i + " sends the Election message to process " + receiver);
                arr[index] = i;
                print_array(arr, index + 1); // print the election message sequence
                index++;
            }

            i = (i % num_pr) + 1; // Circular increment of process number
            receiver = (i % num_pr) + 1;
        }

        // Find the new coordinator (leader)
        new_cord = 0;
        for (int j = 0; j < arr.length; j++) {
            if (new_cord < arr[j]) {
                new_cord = arr[j];
            }
        }
        return new_cord;
    }

    // Method to print the election message sequence
    public static void print_array(int arr[], int size) {
        System.out.print("[ ");
        for (int i = 0; i < size; i++) {
            if (arr[i] == 0) continue; // skip zero
            System.out.print(arr[i] + " ");
        }
        System.out.println("]");
    }
}
