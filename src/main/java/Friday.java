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

    private static void listen() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String line = in.nextLine().trim();
            indent();
            if ("bye".equals(line)) {
                bye();
                break;
            } else if ("list".equals(line)) {
                list();
            } else if (line.startsWith("mark ")) {
                String[] parts = line.split("\\s+", 2);
                mark(parseIndex(parts));
            } else if (line.startsWith("unmark ")) {
                String[] parts = line.split("\\s+", 2);
                unmark(parseIndex(parts));
            } else {
                // currently adds task to memory
                tasks[memPointer] = new Task(line);
                memPointer++;
                System.out.println("added: " + line);
                indent();
            }
        }
        in.close();
    }

    private static void mark(int idx) {
        indent();
        if (idx >= 1 && idx <= memPointer) {
            tasks[idx - 1].done = true;
            System.out.println(" Marked this task as done:");
            System.out.println("   " + tasks[idx - 1].display());
        } else {
            System.out.println(" Invalid task number.");
        }
        indent();
    }

    private static void unmark(int idx) {
        indent();
        if (idx >= 1 && idx <= memPointer) {
            tasks[idx - 1].done = false;
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + tasks[idx - 1].display());
        } else {
            System.out.println(" Invalid task number.");
        }
        indent();
    }

    private static void list() {
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
    private static int parseIndex(String[] parts) {
        if (parts.length < 2) return -1;
        try {
            return Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
