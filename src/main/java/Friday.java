import java.util.Scanner;
import java.util.ArrayList;

public class Friday {
    private static final String IND = "____________________________________________________________";
    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        greet();
        listen();
    }

    private static void listen() {
        Scanner in = new Scanner(System.in);
        listenLoop:
        while (true) {
            String line = in.nextLine().trim();
            String cmd;
            String rest;
            int sp = line.indexOf(' ');
            if (sp == -1) {
                cmd = line;
                rest = "";
            } else {
                cmd = line.substring(0, sp);
                rest = line.substring(sp + 1).trim();
            }

            indent();
            try {
                if (cmd.isBlank()) {
                    indent();
                    continue;
                }
                switch (cmd) {
                    case "bye":
                        bye();
                        break listenLoop;
                    case "list":
                        list();
                        break;
                    case "mark":
                        mark(requireIndex(rest));
                        break;
                    case "unmark":
                        unmark(requireIndex(rest));
                        break;
                    case "todo":
                        addTodo(rest);
                        break;
                    case "deadline":
                        addDeadline(rest);
                        break;
                    case "event":
                        addEvent(rest);
                        break;
                    case "delete":
                        delete(requireIndex(rest));
                    default:
                        throw new FridayException(" I don't recognise that command. Try: todo, deadline, event, list, mark, unmark, bye");
                }
            } catch (FridayException e) {
                System.out.println(e.getMessage());
                indent();
            }
        }
        in.close();
    }

    private static void addTodo(String desc) throws FridayException {
        if (desc == null || desc.isBlank()) {
            throw new FridayException(" A todo needs a description.");
        }
        tasks.add(new ToDo(desc));
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks.get(tasks.size() - 1).display());
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        indent();
    }

    private static void addDeadline(String rest) throws FridayException {
        if (rest == null || rest.isBlank()) {
            throw new FridayException(" A deadline needs a description.");
        }
        int byIdx = rest.indexOf("/by");
        String desc;
        String by = "";
        if (byIdx != -1) {
            desc = rest.substring(0, byIdx).trim();
            by = rest.substring(byIdx + 3).trim();
        } else {
            desc = rest;
        }
        tasks.add(new Deadline(desc, by));
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks.get(tasks.size() - 1).display());
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        indent();
    }

    private static void delete(int idx) throws FridayException {
        if (idx >= 1 && idx <= tasks.size()) {
            Task t = tasks.remove(idx - 1);
            System.out.println(" Noted. I've removed this task:");
            System.out.println("   " + t.display());
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        } else {
            System.out.println(" That task number doesn't exist.");
        }
        indent();
    }

    private static void addEvent(String rest) throws FridayException {
        if (rest == null || rest.isBlank()) {
            throw new FridayException(" An event needs a description.");
        }
        int fromIdx = rest.indexOf("/from");
        int toIdx = rest.indexOf("/to");
        String desc;
        String from = "";
        String to = "";
        if (fromIdx != -1) {
            desc = rest.substring(0, fromIdx).trim();
            if (toIdx != -1 && toIdx > fromIdx) {
                from = rest.substring(fromIdx + 5, toIdx).trim();
                to = rest.substring(toIdx + 3).trim();
            } else {
                from = rest.substring(fromIdx + 5).trim();
            }
        } else {
            if (toIdx != -1) {
                desc = rest.substring(0, toIdx).trim();
                to = rest.substring(toIdx + 3).trim();
            } else {
                desc = rest;
            }
        }
        tasks.add(new Event(desc, from, to));
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks.get(tasks.size() - 1).display());
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        indent();
    }

    private static void mark(int idx) {
        if (idx >= 1 && idx <= tasks.size()) {
            Task t = tasks.get(idx - 1);
            t.markDone();
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + t.display());
        } else {
            System.out.println(" That task number doesn't exist.");
        }
        indent();
    }

    private static void unmark(int idx) {
        if (idx >= 1 && idx <= tasks.size()) {
            Task t = tasks.get(idx - 1);
            t.markUndone();
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + t.display());
        } else {
            System.out.println(" That task number doesn't exist.");
        }
        indent();
    }

    private static void list() {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i).display());
        }
        indent();
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
        System.out.println(" Bye. Hope to see you again soon!");
        indent();
    }

    private static int parseIndexFromString(String s) {
        if (s == null || s.isBlank()) return -1;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static int requireIndex(String s) throws FridayException {
        int idx = parseIndexFromString(s);
        if (idx < 1) throw new FridayException(" Provide a valid task number.");
        return idx;
    }
}
