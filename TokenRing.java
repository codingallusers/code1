import java.util.*;

public class TokenRing {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of processes: ");
        int n = sc.nextInt();

        System.out.println("The processes are: ");
        for (int i = 0; i < n; i++) {   // displays all processes from 0 to n-1
            System.out.print(i + " ");
        }
        System.out.println("0\n"); // Showing circular ring

        int choice;
        do {
            System.out.println("Enter the message: ");
            sc.nextLine(); // consume leftover newline
            String message = sc.nextLine();

            System.out.println("Enter the sender (0 to " + (n - 1) + "): ");
            int sender = sc.nextInt();

            System.out.println("Enter the receiver (0 to " + (n - 1) + "): ");
            int receiver = sc.nextInt();

            // Sender gets the token
            System.out.println("\nSender process -> " + sender);
            System.out.println("The token is received by the sender");
            System.out.println("The message to be sent: " + message);

            // Pass the token in a circular manner to the receiver
            int i = (sender + 1) % n;
            while (true) {  // infinite loop which keeps passing token until receiver gets it
                System.out.println("Token passed to process " + i);
                if (i == receiver) {
                    System.out.println("\nReceiver process -> " + receiver);
                    System.out.println("The message is received: " + message);
                    break;
                }
                i = (i + 1) % n;
            }

            System.out.print("\nDo you want to continue? Press 1 for Yes, 0 for No: ");
            choice = sc.nextInt();
            System.out.println();

        } while (choice == 1);

        System.out.println("Exiting Token Ring simulation.");
        sc.close();
    }
}
