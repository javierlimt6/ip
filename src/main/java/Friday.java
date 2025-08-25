import java.util.Scanner;

public class Friday {
    private static final String IND = "____________________________________________________________";
    private static Task[] tasks = new Task[100]; 
    private static int memPointer = 0;

    private static class Task {
        boolean done;
        String desc;
        Task(String desc) { 
            this.desc = desc; 
        }
        String display() {
             return "[" + (done ? "X" : " ") + "] " + desc; 
        }
    }

    public static void main(String[] args) {

        greet();
        listen();
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
        indent();
    }

    private static void listen() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String line = in.nextLine().trim();
            indent();
            if ("bye".equals(line)) {
                bye();
                break;
            } else if ("list".equals(line)) {
                int index = 1;
                for (Task el: tasks) {
                    if (index > memPointer) {
                        break;
                    }
                    System.out.println(
                        index + "." + el.display()
                    );
                    index++;                
                }
            } else {
                // currently adds text to memory
                tasks[memPointer] = new Task(line);
                memPointer++;
                System.out.println("added: " + line);
                indent();
            }
        }
        in.close();
    }
}
