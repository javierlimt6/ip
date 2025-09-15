package friday;

/**
 * Represents an abstract task with a description and completion status.
 */
public abstract class Task {
    private boolean isDone;
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
        assert !this.isDone : "New task should not be marked as done initially";
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
        isDone = true;

        assert isDone : "Task should be marked as done after calling markDone()";
    }

    /**
     * Marks the task as undone.
     */
    public void markUndone() {
        isDone = false;

        assert !isDone : "Task should be marked as undone after calling markUndone()";
    }

    /**
     * Returns whether the task is done.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean checkDone() {
        return isDone;
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
        return "[" + (isDone ? "X" : " ") + "]";
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
