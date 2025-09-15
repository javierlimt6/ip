package friday;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Friday using FXML.
 */
public class Friday extends Application {
    private Path DATA_DIR;
    private Path DATA_FILE;
    private TaskList taskList = new TaskList();
    private Storage storage;

    /**
     * The main entry point of the application.
     * Initializes storage, loads tasks, greets the user, and starts the GUI.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        initStorage();
        storage.load(); // load tasks from duke.txt if present

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Friday.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setFriday(this); // inject the Friday instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Friday");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        stage.show();
    }

    /**
     * Generates a response for the given command and returns it as a string.
     * This method is called by MainWindow to get Friday's response to user input.
     *
     * @param input The user's input command.
     * @return Friday's response as a string.
     */
    public String getResponse(String input) {
        assert input != null : "Input should not be null";
        assert taskList != null : "Task list should be initialized";
        assert storage != null : "Storage should be initialized";

        try {
            Parser.ParsedCommand parsed = Parser.parseCommand(input);
            assert parsed != null : "Parsed command should not be null";
            assert parsed.command != null : "Parsed command should have a command";

            if (parsed.command.isBlank()) {
                return "No input provided.";
            }

            int initialTaskCount = taskList.size();

            switch (parsed.command) {
                case "bye":
                    return "Bye. Hope to see you again soon!";
                case "list":
                    String listResult = taskList.list();
                    assert listResult != null : "List result should not be null";
                    return listResult;
                case "mark":
                    int markIndex = Parser.parseIndex(parsed.arguments);
                    assert markIndex >= 1 : "Mark index should be 1 or greater";
                    taskList.mark(markIndex);
                    storage.save();
                    return "Nice! I've marked this task as done:\n  "
                            + taskList.get(markIndex - 1).display();
                case "unmark":
                    int unmarkIndex = Parser.parseIndex(parsed.arguments);
                    assert unmarkIndex >= 1 : "Unmark index should be 1 or greater";
                    taskList.unmark(unmarkIndex);
                    storage.save();
                    return "OK, I've marked this task as not done yet:\n  "
                            + taskList.get(unmarkIndex - 1).display();
                case "todo":
                    taskList.addTodo(parsed.arguments);
                    storage.save();
                    assert taskList.size() == initialTaskCount + 1
                            : "Task count should increase by 1 after adding todo";
                    return "Got it. I've added this task:\n  " + taskList.get(taskList.size() - 1).display()
                            + "\nNow you have " + taskList.size() + " tasks in the list.";
                case "deadline":
                    Parser.DeadlineArgs deadlineArgs = Parser.parseDeadlineArgs(parsed.arguments);
                    assert deadlineArgs != null : "Deadline args should not be null";
                    taskList.addDeadline(deadlineArgs.description, deadlineArgs.by);
                    storage.save();
                    assert taskList.size() == initialTaskCount + 1
                            : "Task count should increase by 1 after adding deadline";
                    return "Got it. I've added this task:\n  " + taskList.get(taskList.size() - 1).display()
                            + "\nNow you have " + taskList.size() + " tasks in the list.";
                case "event":
                    Parser.EventArgs eventArgs = Parser.parseEventArgs(parsed.arguments);
                    assert eventArgs != null : "Event args should not be null";
                    taskList.addEvent(eventArgs.description, eventArgs.from, eventArgs.to);
                    storage.save();
                    assert taskList.size() == initialTaskCount + 1
                            : "Task count should increase by 1 after adding event";
                    return "Got it. I've added this task:\n  " + taskList.get(taskList.size() - 1).display()
                            + "\nNow you have " + taskList.size() + " tasks in the list.";
                case "delete":
                    int deleteIndex = Parser.parseIndex(parsed.arguments);
                    assert deleteIndex >= 1 : "Delete index should be 1 or greater";
                    Task deletedTask = taskList.get(deleteIndex - 1);
                    assert deletedTask != null : "Task to delete should exist";
                    taskList.delete(deleteIndex);
                    storage.save();
                    assert taskList.size() == initialTaskCount - 1 : "Task count should decrease by 1 after deletion";
                    return "Noted. I've removed this task:\n  " + deletedTask.display() + "\nNow you have "
                            + taskList.size() + " tasks in the list.";
                case "find":
                    String findResult = taskList.find(parsed.arguments);
                    assert findResult != null : "Find result should not be null";
                    return findResult;
                default:
                    throw new FridayException("I don't recognise that command. Try: todo, deadline, event, " +
                            "list, mark, unmark, delete, find, bye");
            }
        } catch (FridayException e) {
            return e.getMessage();
        }
    }

    /**
     * Initializes the storage by locating the data directory and setting up the
     * data file.
     */
    private void initStorage() {
        DATA_DIR = locateDataDir();
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            Ui.printWarning("Could not initialise storage directory: " + e.getMessage());
        }
        DATA_FILE = DATA_DIR.resolve("duke.txt");
        storage = new Storage(DATA_FILE, taskList);
    }

    /**
     * Always resolve to the same physical directory: ip/src/main/data
     * regardless of where the program is launched from.
     * Strategy:
     * 1. If env FRIDAY_DATA_DIR set, use it.
     * 2. Walk up from current working directory; at each level check:
     * <level>/src/main/data
     * <level>/ip/src/main/data (handles running from parent of ip)
     * 3. Fallback: currentWorkingDir/data
     *
     * @return The path to the data directory.
     */
    private Path locateDataDir() {
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

        // If not found, assume we are *inside* ip (or anywhere) and create
        // ip/src/main/data relative if possible
        // Try to detect an 'ip' directory downward from cwd (rare case) — otherwise
        // fallback to ./data
        Path guessIp = cwd.resolve("ip").resolve("src").resolve("main").resolve("data");
        if (Files.exists(guessIp.getParent())) {
            return guessIp;
        }
        return cwd.resolve("data");
    }
}
