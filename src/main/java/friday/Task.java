package friday;

/**
 * Represents an abstract task with a description and completion status.
 */
public abstract class Task {
    private boolean done;
    private String desc;

    /**
     * Constructs a Task with the given description.
     *
     * @param desc The description of the task.
     */
    public Task(String desc) {
        this.desc = desc;
    }

    /**
     * Returns the description of the task.
     *
     * @return The task description.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Marks the task as done.
     */
    public void markDone() {
        done = true;
    }

    /**
     * Marks the task as undone.
     */
    public void markUndone() {
        done = false;
    }

    /**
     * Returns whether the task is done.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Returns the type of the task.
     *
     * @return The task type.
     */
    public abstract TaskType getType();

    /**
     * Returns the status box string for the task.
     *
     * @return The status box string.
     */
    public String statusBox() {
        return "[" + (done ? "X" : " ") + "]";
    }

    /**
     * Returns the display string for the task.
     *
     * @return The display string.
     */
    public String display() {
        return "[" + getType().shortName() + "]" + statusBox() + " " + getDesc();
    }
}
