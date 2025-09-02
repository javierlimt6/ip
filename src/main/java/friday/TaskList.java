package friday;

import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Manages a list of tasks, providing methods to add, delete, mark, and list
 * tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a todo task to the list.
     *
     * @param desc The description of the todo.
     * @throws FridayException If the description is null or blank.
     */
    public void addTodo(String desc) throws FridayException {
        if (desc == null || desc.isBlank()) {
            throw new FridayException("A todo needs a description.");
        }
        tasks.add(new ToDo(desc));
    }

    /**
     * Adds a deadline task to the list.
     *
     * @param desc The description of the deadline.
     * @param by   The due date.
     * @throws FridayException If the description is null or blank.
     */
    public void addDeadline(String desc, LocalDate by) throws FridayException {
        if (desc == null || desc.isBlank()) {
            throw new FridayException("A deadline needs a description.");
        }
        tasks.add(new Deadline(desc, by));
    }

    /**
     * Adds an event task to the list.
     *
     * @param desc The description of the event.
     * @param from The start time.
     * @param to   The end time.
     * @throws FridayException If the description is null or blank.
     */
    public void addEvent(String desc, String from, String to) throws FridayException {
        if (desc == null || desc.isBlank()) {
            throw new FridayException("An event needs a description.");
        }
        tasks.add(new Event(desc, from, to));
    }

    /**
     * Deletes a task from the list by index.
     *
     * @param idx The 1-based index of the task to delete.
     * @throws FridayException If the index is out of range.
     */
    public void delete(int idx) throws FridayException {
        if (idx >= 1 && idx <= tasks.size()) {
            tasks.remove(idx - 1);
        } else {
            throw new FridayException("That task number doesn't exist.");
        }
    }

    /**
     * Marks a task as done by index.
     *
     * @param idx The 1-based index of the task to mark.
     * @throws FridayException If the index is out of range.
     */
    public void mark(int idx) throws FridayException {
        if (idx >= 1 && idx <= tasks.size()) {
            tasks.get(idx - 1).markDone();
        } else {
            throw new FridayException("That task number doesn't exist.");
        }
    }

    /**
     * Marks a task as undone by index.
     *
     * @param idx The 1-based index of the task to unmark.
     * @throws FridayException If the index is out of range.
     */
    public void unmark(int idx) throws FridayException {
        if (idx >= 1 && idx <= tasks.size()) {
            tasks.get(idx - 1).markUndone();
        } else {
            throw new FridayException("That task number doesn't exist.");
        }
    }

    /**
     * Returns a string representation of the task list.
     *
     * @return The list string.
     */
    public String list() {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(" ").append(i + 1).append(".").append(tasks.get(i).display());
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns the list of tasks.
     *
     * @return The ArrayList of tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The size of the task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the given index.
     *
     * @param idx The 0-based index.
     * @return The task at the index.
     */
    public Task get(int idx) {
        return tasks.get(idx);
    }

    /**
     * Adds a task to the list.
     *
     * @param t The task to add.
     */
    public void add(Task t) {
        tasks.add(t);
    }
}
