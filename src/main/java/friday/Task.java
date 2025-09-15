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
        assert desc != null : "Task description should not be null";

        this.desc = desc;

        assert this.desc == desc : "Task description should be correctly assigned";
        assert !this.done : "New task should not be marked as done initially";
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

        assert done : "Task should be marked as done after calling markDone()";
    }

    /**
     * Marks the task as undone.
     */
    public void markUndone() {
        done = false;

        assert !done : "Task should be marked as undone after calling markUndone()";
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
