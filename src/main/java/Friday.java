import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.file.Paths;

public class Friday {
    private static Path DATA_DIR;
    private static Path DATA_FILE;
    private static final TaskList taskList = new TaskList();

    public static void main(String[] args) {
        initStorage();
        load(); // load tasks from duke.txt if present
        Ui.greet();
        listen(); // listen for any commands then parse them
    }

    private static void initStorage() {
        DATA_DIR = locateDataDir();
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            Ui.printWarning("Could not initialise storage directory: " + e.getMessage());
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
        listenLoop:
        while (true) {
            Ui.printIndent();
            try {
                String line = Ui.readLine();
                Parser.ParsedCommand parsed = Parser.parseCommand(line); //factory method

                if (parsed.command.isBlank()) { //no input
                    Ui.printIndent();
                    continue;
                }

                switch (parsed.command) { // identify all the commands
                    case "bye":
                        Ui.bye();
                        break listenLoop;
                    case "list":
                        list();
                        break;
                    case "mark":
                        mark(Parser.parseIndex(parsed.arguments));
                        break;
                    case "unmark":
                        unmark(Parser.parseIndex(parsed.arguments));
                        break;
                    case "todo":
                        addTodo(parsed.arguments);
                        break;
                    case "deadline":
                        addDeadline(parsed.arguments);
                        break;
                    case "event":
                        addEvent(parsed.arguments);
                        break;
                    case "delete":
                        delete(Parser.parseIndex(parsed.arguments));
                        break;
                    default:
                        throw new FridayException(" I don't recognise that command. Try: todo, deadline, event, list, mark, unmark, bye");
                }
            } catch (FridayException e) {
                Ui.printError(e.getMessage());
            }
        }
        Ui.closeScanner();
    }

    private static void addTodo(String desc) throws FridayException {
        taskList.addTodo(desc);
        Ui.printTaskAdded(taskList.get(taskList.size() - 1), taskList.size());
        save();
    }

    private static void addDeadline(String rest) throws FridayException {
        Parser.DeadlineArgs args = Parser.parseDeadlineArgs(rest);
        taskList.addDeadline(args.description, args.by);
        save();
        Ui.printTaskAdded(taskList.get(taskList.size() - 1), taskList.size());
    }

    private static void delete(int idx) throws FridayException {
        Task deletedTask = taskList.get(idx - 1);
        taskList.delete(idx);
        Ui.printTaskDeleted(deletedTask, taskList.size());
        save();
    }

    private static void addEvent(String rest) throws FridayException {
        Parser.EventArgs args = Parser.parseEventArgs(rest);
        taskList.addEvent(args.description, args.from, args.to);
        save();
        Ui.printTaskAdded(taskList.get(taskList.size() - 1), taskList.size());
    }

    private static void mark(int idx) {
        try {
            taskList.mark(idx);
            Ui.printTaskMarked(taskList.get(idx - 1));
            save();
        } catch (FridayException e) {
            Ui.printError(e.getMessage());
        }
    }

    private static void unmark(int idx) {
        try {
            taskList.unmark(idx);
            Ui.printTaskUnmarked(taskList.get(idx - 1));
            save();
        } catch (FridayException e) {
            Ui.printError(e.getMessage());
        }
    }

    private static void list() {
        Ui.printTaskList(taskList.list());
    }

    private static void save() {
        if (DATA_FILE == null) return;
        try {
            if (Files.notExists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
            try (BufferedWriter bw = Files.newBufferedWriter(DATA_FILE)) {
                for (Task t : taskList.getTasks()) {
                    bw.write(serialize(t));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            Ui.printWarning("Could not save tasks: " + e.getMessage());
        }
    }

    private static String serialize(Task t) {
        String type;
        String extra = "";
        if (t instanceof ToDo) {
            type = "T";
        } else if (t instanceof Deadline) {
            type = "D";
            extra = ((Deadline) t).getByFormatted();
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

    private static void load() {
        if (DATA_FILE == null || !Files.exists(DATA_FILE)) return;
        try {
            for (String line : Files.readAllLines(DATA_FILE)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;
                Task t = Parser.parseSerializedTask(trimmed);
                if (t != null) {
                    taskList.add(t);
                }
            }
        } catch (IOException e) {
            Ui.printWarning("Could not load tasks: " + e.getMessage());
        }
    }
}
