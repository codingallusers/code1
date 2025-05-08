import java.util.*;

public class LeaderElection {
    static int num_pr;        // number of processes
    static int old_cord;      // the failed coordinator or leader
    static int new_cord;      // the new elected leader
    static int curr_elec;     // the current process that is holding the election
    static int isActive[];    // array to track active processes
    static int failed_process; // the failed process

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of processes: ");
        num_pr = sc.nextInt();

        isActive = new int[num_pr + 1]; // 1-based indexing

        // Initialize all processes as active
        for (int i = 1; i <= num_pr; i++) {
            isActive[i] = 1;
        }

        old_cord = num_pr; // Assume highest-numbered process is the coordinator
        // Simulate leader failure
        isActive[old_cord] = 0;

        System.out.println("Enter the process that initiates the election process: ");
        curr_elec = sc.nextInt();

        System.out.println("The process that failed is: " + old_cord + "\n");

        System.out.println("Enter the process that fails (other than the leader process), if none then enter 0: ");
        failed_process = sc.nextInt();
        if (failed_process != 0) {
            isActive[failed_process] = 0; // Mark the failed process as inactive
        }

        // Start election
        new_cord = election_process(isActive, old_cord, curr_elec);

        System.out.println("Finally process " + new_cord + " became the new leader\n");

        // Inform all active processes about the new leader
        for (int i = 1; i <= num_pr; i++) {
            if (isActive[i] == 1 && i != new_cord) {
                System.out.println("Process " + new_cord + " passes a Coordinator (" + new_cord + ") message to process " + i);
            }
        }

        sc.close();
    }

    public static int election_process(int isActive[], int old_cord, int curr_elec) {
        int higher_process = curr_elec;

        // Loop through the processes starting from the current election process
        for (int i = curr_elec; i <= num_pr; i++) {
            if (isActive[i] == 1) {
                // Send election message to all higher processes
                for (int j = i + 1; j <= num_pr; j++) {
                    if (isActive[j] == 1) {
                        System.out.println("Process " + i + " passes Election(" + curr_elec + ") message to process " + j);
                    }
                }

                System.out.println();

                // Send "Ok" message back to the current process from all higher processes
                for (int j = i + 1; j <= num_pr; j++) {
                    if (isActive[j] == 1) {
                        System.out.println("Process " + j + " passes Ok(" + j + ") message to process " + i);

                        if (higher_process < j) {
                            higher_process = j; // Track the process with the highest number
                        }
                    }
                }

                System.out.println();
            }
        }

        return higher_process;
    }
}
