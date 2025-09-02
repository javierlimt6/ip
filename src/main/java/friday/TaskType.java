package friday;

/**
 * Enumeration of task types.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String shortName;

    /**
     * Constructs a TaskType with the given short name.
     *
     * @param shortName The short name for the task type.
     */
    TaskType(String shortName) {
        this.shortName = shortName;
    }

    /**
     * Returns the short name of the task type.
     *
     * @return The short name.
     */
    public String shortName() {
        return shortName;
    }
}