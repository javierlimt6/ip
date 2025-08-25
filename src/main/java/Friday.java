import java.util.Scanner;

public class Friday {
    private static final String IND = "____________________________________________________________";
    private static Task[] tasks = new Task[100];
    private static int memPointer = 0;

    private static abstract class Task {
        boolean done;
        String desc;
        Task(String desc) { this.desc = desc; }
        void markDone() { done = true; }
        void markUndone() { done = false; }
        abstract String typeLetter();
        String statusBox() { return "[" + (done ? "X" : " ") + "]"; }
        String display() {
            return "[" + typeLetter() + "]" + statusBox() + " " + desc;
        }
    }

    private static class ToDo extends Task {
        ToDo(String desc) { super(desc); }
        @Override String typeLetter() { return "T"; }
    }

    private static class Deadline extends Task {
        String by;
        Deadline(String desc, String by) { super(desc); this.by = by; }
        @Override String typeLetter() { return "D"; }
        @Override String display() {
            String base = super.display();
            return base + (by != null && !by.isBlank() ? " (by: " + by + ")" : "");
        }
    }

    private static class Event extends Task {
        String from;
        String to;
        Event(String desc, String from, String to) { super(desc); this.from = from; this.to = to; }
        @Override String typeLetter() { return "E"; }
        @Override String display() {
            String base = super.display();
            String details = "";
            if (from != null && !from.isBlank()) {
                details += " (from: " + from;
                if (to != null && !to.isBlank()) details += " to: " + to;
                details += ")";
            } else if (to != null && !to.isBlank()) {
                details += " (to: " + to + ")";
            }
            return base + details;
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
            // split command + rest
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

            // top separator for response
            indent();

            if ("bye".equals(cmd)) {
                bye();
                break;
            } else if ("list".equals(cmd)) {
                list();
            } else if ("mark".equals(cmd)) {
                int idx = parseIndexFromString(rest);
                mark(idx);
            } else if ("unmark".equals(cmd)) {
                int idx = parseIndexFromString(rest);
                unmark(idx);
            } else if ("todo".equals(cmd)) {
                addTodo(rest);
            } else if ("deadline".equals(cmd)) {
                addDeadline(rest);
            } else if ("event".equals(cmd)) {
                addEvent(rest);
            } else if (!cmd.isBlank()) {
                // unknown command: treat entire line as a todo (backwards-compatible)
                addTodo(line);
            } else {
                // empty input -> show nothing (just closing separator already printed)
                indent();
            }
        }
        in.close();
    }

    private static void addTodo(String desc) {
        if (desc == null || desc.isBlank()) {
            System.out.println(" Please provide a description for the todo.");
            indent();
            return;
        }
        tasks[memPointer++] = new ToDo(desc);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks[memPointer - 1].display());
        System.out.println(" Now you have " + memPointer + " tasks in the list.");
        indent();
    }

    private static void addDeadline(String rest) {
        if (rest == null || rest.isBlank()) {
            System.out.println(" Please provide a description for the deadline.");
            indent();
            return;
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
        tasks[memPointer++] = new Deadline(desc, by);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks[memPointer - 1].display());
        System.out.println(" Now you have " + memPointer + " tasks in the list.");
        indent();
    }

    private static void addEvent(String rest) {
        if (rest == null || rest.isBlank()) {
            System.out.println(" Please provide a description for the event.");
            indent();
            return;
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
            // fallback: try to split on "/to" only
            if (toIdx != -1) {
                desc = rest.substring(0, toIdx).trim();
                to = rest.substring(toIdx + 3).trim();
            } else {
                desc = rest;
            }
        }
        tasks[memPointer++] = new Event(desc, from, to);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks[memPointer - 1].display());
        System.out.println(" Now you have " + memPointer + " tasks in the list.");
        indent();
    }

    private static void mark(int idx) {
        if (idx >= 1 && idx <= memPointer) {
            tasks[idx - 1].markDone();
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + tasks[idx - 1].display());
        } else {
            System.out.println(" Invalid task number.");
        }
        indent();
    }

    private static void unmark(int idx) {
        if (idx >= 1 && idx <= memPointer) {
            tasks[idx - 1].markUndone();
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + tasks[idx - 1].display());
        } else {
            System.out.println(" Invalid task number.");
        }
        indent();
    }

    private static void list() {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < memPointer; i++) {
            System.out.println(" " + (i + 1) + "." + tasks[i].display());
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
}
