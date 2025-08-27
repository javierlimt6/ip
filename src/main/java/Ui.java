import java.util.Scanner;

public class Ui {
    private static final String INDENTATION = "____________________________________________________________";
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Prints the greeting message.
     */
    public static void greet() {
        printIndentation();
        System.out.println(" Hello! I'm Friday");
        System.out.println(" What can I do for you?");
        printIndentation();
    }

    /**
     * Prints the goodbye message.
     */
    public static void bye() {
        System.out.println(" Bye. Hope to see you again soon!");
        printIndentation();
    }

    /**
     * Prints a message with indentation.
     */
    public static void printMessage(String message) {
        System.out.println(message);
        printIndentation();
    }

    /**
     * Prints a warning message.
     */
    public static void printWarning(String message) {
        System.out.println(" Warning: " + message);
    }

    /**
     * Prints the task addition confirmation.
     */
    public static void printTaskAdded(Task task, int totalTasks) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task.display());
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
        printIndentation();
    }

    /**
     * Prints the task deletion confirmation.
     */
    public static void printTaskDeleted(Task task, int totalTasks) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task.display());
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
        printIndentation();
    }

    /**
     * Prints the task marking confirmation.
     */
    public static void printTaskMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task.display());
        printIndentation();
    }

    /**
     * Prints the task unmarking confirmation.
     */
    public static void printTaskUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task.display());
        printIndentation();
    }

    /**
     * Prints the task list.
     */
    public static void printTaskList(String taskListString) {
        System.out.print(taskListString);
        printIndentation();
    }

    /**
     * Prints an error message.
     */
    public static void printError(String message) {
        System.out.println(message);
        printIndentation();
    }

    /**
     * Prints the indentation line.
     */
    public static void printIndentation() {
        System.out.println(INDENTATION);
    }

    /**
     * Reads the next line of input from the user.
     */
    public static String readLine() {
        return scanner.nextLine().trim();
    }

    /**
     * Closes the scanner.
     */
    public static void closeScanner() {
        scanner.close();
    }
}
