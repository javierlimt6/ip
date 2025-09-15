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

        assert desc != null && !desc.isBlank() : "Description should be valid after validation";
        int sizeBefore = tasks.size();

        tasks.add(new ToDo(desc));

        assert tasks.size() == sizeBefore + 1 : "Task list size should increase by 1 after adding";
        assert tasks.get(tasks.size() - 1) instanceof ToDo : "Last added task should be a ToDo";
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

        assert desc != null && !desc.isBlank() : "Description should be valid after validation";
        int sizeBefore = tasks.size();

        tasks.add(new Deadline(desc, by));

        assert tasks.size() == sizeBefore + 1 : "Task list size should increase by 1 after adding";
        assert tasks.get(tasks.size() - 1) instanceof Deadline : "Last added task should be a Deadline";
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
            assert idx >= 1 && idx <= tasks.size() : "Index should be valid before deletion";
            int sizeBefore = tasks.size();
            Task removedTask = tasks.get(idx - 1);

            tasks.remove(idx - 1);

            assert tasks.size() == sizeBefore - 1 : "Task list size should decrease by 1 after deletion";
            assert !tasks.contains(removedTask) : "Removed task should no longer be in the list";
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
            assert idx >= 1 && idx <= tasks.size() : "Index should be valid before marking";
            Task task = tasks.get(idx - 1);

            task.markDone();

            assert task.isDone() : "Task should be marked as done after marking";
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
            assert idx >= 1 && idx <= tasks.size() : "Index should be valid before unmarking";
            Task task = tasks.get(idx - 1);

            task.markUndone();

            assert !task.isDone() : "Task should be marked as undone after unmarking";
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
        assert idx >= 0 && idx < tasks.size() : "Index should be within bounds for 0-based access";

        Task task = tasks.get(idx);

        assert task != null : "Retrieved task should not be null";
        return task;
    }

    /**
     * Adds a task to the list.
     *
     * @param t The task to add.
     */
    public void add(Task t) {
        assert t != null : "Task to add should not be null";
        int sizeBefore = tasks.size();

        tasks.add(t);

        assert tasks.size() == sizeBefore + 1 : "Task list size should increase by 1 after adding";
        assert tasks.contains(t) : "Added task should be in the list";
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     *
     * @param keyword The keyword to search for.
     * @return A formatted string of matching tasks.
     */
    public String find(String keyword) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getDesc().toLowerCase().contains(keyword.toLowerCase())) {
                count++;
                sb.append(" ").append(count).append(".").append(tasks.get(i).display());
                if (i < tasks.size() - 1) {
                    sb.append("\n");
                }
            }
        }
        if (count == 0) {
            sb.setLength(0); // clear
            sb.append("No matching tasks found.");
        }
        return sb.toString();
    }
}
