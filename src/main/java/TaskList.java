import java.util.ArrayList;
import java.time.LocalDate;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTodo(String desc) throws FridayException {
        if (desc == null || desc.isBlank()) {
            throw new FridayException(" A todo needs a description.");
        }
        tasks.add(new ToDo(desc));
    }

    public void addDeadline(String desc, LocalDate by) throws FridayException {
        if (desc == null || desc.isBlank()) {
            throw new FridayException(" A deadline needs a description.");
        }
        tasks.add(new Deadline(desc, by));
    }

    public void addEvent(String desc, String from, String to) throws FridayException {
        if (desc == null || desc.isBlank()) {
            throw new FridayException(" An event needs a description.");
        }
        tasks.add(new Event(desc, from, to));
    }

    public void delete(int idx) throws FridayException {
        if (idx >= 1 && idx <= tasks.size()) {
            tasks.remove(idx - 1);
        } else {
            throw new FridayException(" That task number doesn't exist.");
        }
    }

    public void mark(int idx) throws FridayException {
        if (idx >= 1 && idx <= tasks.size()) {
            tasks.get(idx - 1).markDone();
        } else {
            throw new FridayException(" That task number doesn't exist.");
        }
    }

    public void unmark(int idx) throws FridayException {
        if (idx >= 1 && idx <= tasks.size()) {
            tasks.get(idx - 1).markUndone();
        } else {
            throw new FridayException(" That task number doesn't exist.");
        }
    }

    public String list() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(" ").append(i + 1).append(".").append(tasks.get(i).display()).append("\n");
        }
        return sb.toString();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int idx) {
        return tasks.get(idx);
    }

    public void add(Task t) {
        tasks.add(t);
    }
}
