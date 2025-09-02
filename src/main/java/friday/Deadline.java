package friday;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with a due date.
 */
public class Deadline extends Task {
    private LocalDate by;
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Constructs a Deadline task with the given description and due date.
     *
     * @param desc The description of the deadline.
     * @param by   The due date.
     */
    public Deadline(String desc, LocalDate by) {
        super(desc);
        this.by = by;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskType getType() {
        return TaskType.DEADLINE;
    }

    /**
     * Returns the display string for the deadline task, including the due date.
     *
     * @return The display string.
     */
    @Override
    public String display() {
        String base = super.display();
        if (by != null) {
            return base + " (by: " + by.format(DISPLAY_FORMAT) + ")";
        }
        return base;
    }

    /**
     * Returns the due date of the deadline.
     *
     * @return The due date.
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns the formatted due date string for serialization.
     *
     * @return The formatted due date string.
     */
    public String getByFormatted() {
        return by != null ? by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
    }
}
