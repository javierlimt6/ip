package friday;

/**
 * Represents a todo task.
 */
public class ToDo extends Task {

    /**
     * Constructs a ToDo task with the given description.
     *
     * @param desc The description of the todo.
     */
    public ToDo(String desc) {
        super(desc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskType getType() {
        return TaskType.TODO;
    }
}
