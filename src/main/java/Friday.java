import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.file.Paths;

public class Friday {
    private static final String IND = "____________________________________________________________";
    // Step 1: storage path definitions (OS-independent)
    // Storage path definitions with fallback:
    // Prefer writing inside src/main/data (keeps test expectations aligned), else use ./data
    private static Path DATA_DIR;
    private static Path DATA_FILE;
    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        initStorage();
    load(); // load tasks from duke.txt if present
        greet();
        listen();
    }

    private static void initStorage() {
        DATA_DIR = locateDataDir();
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            System.out.println(" Warning: Could not initialise storage directory: " + e.getMessage());
        }
        DATA_FILE = DATA_DIR.resolve("duke.txt");
    }

    /**
     * Always resolve to the same physical directory: ip/src/main/data
     * regardless of where the program is launched from.
     * Strategy:
     * 1. If env FRIDAY_DATA_DIR set, use it.
     * 2. Walk up from current working directory; at each level check:
     *      <level>/src/main/data
     *      <level>/ip/src/main/data   (handles running from parent of ip)
     * 3. Fallback: currentWorkingDir/data
     */
    private static Path locateDataDir() {
        String env = System.getenv("FRIDAY_DATA_DIR");
        if (env != null && !env.isBlank()) {
            return Paths.get(env).toAbsolutePath().normalize();
        }

        Path cwd = Paths.get("").toAbsolutePath().normalize();
        Path cursor = cwd;

        while (cursor != null) {
            Path direct = cursor.resolve("src").resolve("main").resolve("data");
            if (Files.isDirectory(direct)) {
                return direct;
            }
            Path underIp = cursor.resolve("ip").resolve("src").resolve("main").resolve("data");
            if (Files.isDirectory(underIp)) {
                return underIp;
            }
            cursor = cursor.getParent();
        }

        // If not found, assume we are *inside* ip (or anywhere) and create ip/src/main/data relative if possible
        // Try to detect an 'ip' directory downward from cwd (rare case) — otherwise fallback to ./data
        Path guessIp = cwd.resolve("ip").resolve("src").resolve("main").resolve("data");
        if (Files.exists(guessIp.getParent())) {
            return guessIp;
        }
        return cwd.resolve("data");
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
                        break; // Fix fallthrough
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
    save();
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
    save();
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
            save();
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
    save();
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
            save();
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
            save();
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

    // Step 2: persist tasks after each change
    private static void save() {
        if (DATA_FILE == null) return;
        try {
            if (Files.notExists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
            try (BufferedWriter bw = Files.newBufferedWriter(DATA_FILE)) {
                for (Task t : tasks) {
                    bw.write(serialize(t));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println(" Warning: Could not save tasks: " + e.getMessage());
        }
    }

    private static String serialize(Task t) {
        String type;
        String extra = "";
        if (t instanceof ToDo) {
            type = "T";
        } else if (t instanceof Deadline) {
            type = "D";
            extra = ((Deadline) t).getBy();
        } else if (t instanceof Event) {
            type = "E";
            Event ev = (Event) t;
            // Combine from/to with a delimiter so we can parse later: from || to
            String from = ev.getFrom() == null ? "" : ev.getFrom();
            String to = ev.getTo() == null ? "" : ev.getTo();
            extra = from + " || " + to; // always have the delimiter for parsing
        } else {
            type = "?"; // fallback
        }
        int doneFlag = t.isDone() ? 1 : 0;
        // Format: TYPE | doneFlag | description | extra (extra omitted if blank except for Event delimiter form)
        if (type.equals("E")) {
            return String.join(" | ", type, String.valueOf(doneFlag), t.desc, extra);
        }
        if (!extra.isBlank()) {
            return String.join(" | ", type, String.valueOf(doneFlag), t.desc, extra);
        }
        return String.join(" | ", type, String.valueOf(doneFlag), t.desc);
    }

    // Step 3: load tasks at startup
    private static void load() {
        if (DATA_FILE == null || !Files.exists(DATA_FILE)) return;
        try {
            for (String line : Files.readAllLines(DATA_FILE)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;
                parseAndAddLoaded(trimmed);
            }
        } catch (IOException e) {
            System.out.println(" Warning: Could not load tasks: " + e.getMessage());
        }
    }

    private static void parseAndAddLoaded(String line) {
        // Expected serialized forms:
        // T | done | description
        // D | done | description | by
        // E | done | description | from || to
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) return; // malformed; skip
        String type = parts[0];
        boolean done = "1".equals(parts[1]);
        String desc = parts[2];

        Task t = null;
        switch (type) {
            case "T":
                t = new ToDo(desc);
                break;
            case "D":
                String by = parts.length >= 4 ? parts[3] : "";
                t = new Deadline(desc, by);
                break;
            case "E":
                String from = "";
                String to = "";
                if (parts.length >= 4) {
                    String extra = parts[3];
                    String[] ft = extra.split("\\s*\\|\\|\\s*", -1); // keep empty
                    if (ft.length > 0) from = ft[0];
                    if (ft.length > 1) to = ft[1];
                }
                t = new Event(desc, from, to);
                break;
            default:
                return; // unknown type
        }
        if (t != null) {
            if (done) t.markDone();
            tasks.add(t);
        }
    }
}
