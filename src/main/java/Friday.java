import java.util.Scanner;

public class Friday {
    private static final String IND = "____________________________________________________________";

    public static void main(String[] args) {

        greet();
        bye();
    }

    private static void indent() {
        System.out.println(IND);
    }

    private static void greet() {
        indent();
        System.out.println(" Hello! I'm Friday");
        System.out.println(" What can I do for you?");
        indent();
    }

    private static void bye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    private static void listen() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String line = in.nextLine().trim();
            indent();
            if ("bye".equals(line)) {
                System.out.println(" Bye. Hope to see you again soon!");
                indent();
                break;
            } else {
                System.out.println(" " + line);
                indent();
            }
        }
        in.close();
    }
}
