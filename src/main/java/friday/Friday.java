package friday;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main class for the Friday chatbot application.
 * This class handles the GUI interface, command parsing, and task management.
 */
public class Friday extends Application {
    private Path DATA_DIR;
    private Path DATA_FILE;
    private TaskList taskList = new TaskList();
    private Storage storage;

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

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

        // Setting up required components
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        // Set layout constraints
        stage.setTitle("Friday");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        userInput.setPrefWidth(325.0);

        sendButton.setPrefWidth(55.0);

        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        // Add event handlers
        sendButton.setOnMouseClicked((event) -> {
            handleUserInput(userInput.getText());
            userInput.clear();
        });

        userInput.setOnAction((event) -> {
            handleUserInput(userInput.getText());
            userInput.clear();
        });

        // Display greeting
        dialogContainer.getChildren().add(new DialogBox("Hello! I'm Friday\nWhat can I do for you?", null));
    }

    private void handleUserInput(String input) {
        dialogContainer.getChildren().add(new DialogBox(input, null)); // User input
        try {
            Parser.ParsedCommand parsed = Parser.parseCommand(input);
            if (parsed.command.isBlank()) {
                dialogContainer.getChildren().add(new DialogBox("No input provided.", null));
                return;
            }
            switch (parsed.command) {
                case "bye":
                    dialogContainer.getChildren().add(new DialogBox("Bye. Hope to see you again soon!", null));
                    break;
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
                case "find":
                    find(parsed.arguments);
                    break;
                default:
                    throw new FridayException("I don't recognise that command. Try: todo, deadline, event, " +
                            "list, mark, unmark, delete, find, bye");
            }
        } catch (FridayException e) {
            dialogContainer.getChildren().add(new DialogBox(e.getMessage(), null));
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

    /**
     * Adds a todo task to the list and saves the changes.
     *
     * @param desc The description of the todo.
     * @throws FridayException If the description is invalid.
     */
    private void addTodo(String desc) throws FridayException {
        taskList.addTodo(desc);
        String message = "Got it. I've added this task:\n  " + taskList.get(taskList.size() - 1).display()
                + "\nNow you have " + taskList.size() + " tasks in the list.";
        dialogContainer.getChildren().add(new DialogBox(message, null));
        storage.save();
    }

    /**
     * Adds a deadline task to the list and saves the changes.
     *
     * @param rest The arguments string for the deadline.
     * @throws FridayException If parsing or validation fails.
     */
    private void addDeadline(String rest) throws FridayException {
        Parser.DeadlineArgs args = Parser.parseDeadlineArgs(rest);
        taskList.addDeadline(args.description, args.by);
        storage.save();
        String message = "Got it. I've added this task:\n  " + taskList.get(taskList.size() - 1).display()
                + "\nNow you have " + taskList.size() + " tasks in the list.";
        dialogContainer.getChildren().add(new DialogBox(message, null));
    }

    /**
     * Deletes a task from the list and saves the changes.
     *
     * @param idx The 1-based index of the task to delete.
     * @throws FridayException If the index is invalid.
     */
    private void delete(int idx) throws FridayException {
        Task deletedTask = taskList.get(idx - 1);
        taskList.delete(idx);
        String message = "Noted. I've removed this task:\n  " + deletedTask.display() + "\nNow you have "
                + taskList.size() + " tasks in the list.";
        dialogContainer.getChildren().add(new DialogBox(message, null));
        storage.save();
    }

    /**
     * Adds an event task to the list and saves the changes.
     *
     * @param rest The arguments string for the event.
     * @throws FridayException If parsing or validation fails.
     */
    private void addEvent(String rest) throws FridayException {
        Parser.EventArgs args = Parser.parseEventArgs(rest);
        taskList.addEvent(args.description, args.from, args.to);
        storage.save();
        String message = "Got it. I've added this task:\n  " + taskList.get(taskList.size() - 1).display()
                + "\nNow you have " + taskList.size() + " tasks in the list.";
        dialogContainer.getChildren().add(new DialogBox(message, null));
    }

    /**
     * Marks a task as done and saves the changes.
     *
     * @param idx The 1-based index of the task to mark.
     */
    private void mark(int idx) {
        try {
            taskList.mark(idx);
            String message = "Nice! I've marked this task as done:\n  " + taskList.get(idx - 1).display();
            dialogContainer.getChildren().add(new DialogBox(message, null));
            storage.save();
        } catch (FridayException e) {
            dialogContainer.getChildren().add(new DialogBox(e.getMessage(), null));
        }
    }

    /**
     * Marks a task as undone and saves the changes.
     *
     * @param idx The 1-based index of the task to unmark.
     */
    private void unmark(int idx) {
        try {
            taskList.unmark(idx);
            String message = "OK, I've marked this task as not done yet:\n  " + taskList.get(idx - 1).display();
            dialogContainer.getChildren().add(new DialogBox(message, null));
            storage.save();
        } catch (FridayException e) {
            dialogContainer.getChildren().add(new DialogBox(e.getMessage(), null));
        }
    }

    private void list() {
        String message = taskList.list();
        dialogContainer.getChildren().add(new DialogBox(message, null));
    }

    /**
     * Finds and displays tasks matching the given keyword.
     *
     * @param keyword The keyword to search for.
     */
    private void find(String keyword) {
        String message = taskList.find(keyword);
        dialogContainer.getChildren().add(new DialogBox(message, null));
    }
}
