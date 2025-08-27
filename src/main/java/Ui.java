import java.util.Scanner;

public class Ui {
    // the purpose of this class is to abstract the use of any System print calls into a Ui class
    private static final String IND = "____________________________________________________________";
    private static final Scanner in = new Scanner(System.in);

    /**
     * Prints the greeting message.
     */
    public static void greet() {
        printIndent();
        System.out.println(" Hello! I'm Friday");
        System.out.println(" What can I do for you?");
        printIndent();
    }

    /**
     * Prints the goodbye message.
     */
    public static void bye() {
        System.out.println(" Bye. Hope to see you again soon!");
        printIndent();
    }

    /**
     * Prints the task addition confirmation.
     */
    public static void printTaskAdded(Task t, int size) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t.display());
        System.out.println(" Now you have " + size + " tasks in the list.");
        printIndent();
    }

    /**
     * Prints the task deletion confirmation.
     */
    public static void printTaskDeleted(Task t, int size) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + t.display());
        System.out.println(" Now you have " + size + " tasks in the list.");
        printIndent();
    }

    /**
     * Prints the task marking confirmation.
     */
    public static void printTaskMarked(Task t) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + t.display());
        printIndent();
    }

    /**
     * Prints the task unmarking confirmation.
     */
    public static void printTaskUnmarked(Task t) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + t.display());
        printIndent();
    }

    /**
     * Prints the task list.
     */
    public static void printTaskList(String listOutput) {
        System.out.println(listOutput);
        printIndent();
    }

    /**
     * Prints an error message.
     */
    public static void printError(String message) {
        System.out.println(message);
        printIndent();
    }

    /**
     * Prints a warning message.
     */
    public static void printWarning(String message) {
        System.out.println(" Warning: " + message);
    }

    /**
     * Prints a message with indentation.
     */
    public static void printMessage(String message) {
        System.out.println(message);
        printIndent();
    }

    /**
     * Prints the indentation line.
     */
    public static void printIndent() {
        System.out.println(IND);
    }

    /**
     * Reads the next line of input from the user.
     */
    public static String readLine() {
        return in.nextLine();
    }

    /**
     * Closes the scanner.
     */
    public static void closeScanner() {
        in.close();
    }
}
