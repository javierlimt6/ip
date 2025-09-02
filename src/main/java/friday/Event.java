package friday;

/**
 * Represents an event task with from and to times.
 */
public class Event extends Task {
    private String from;
    private String to;

    /**
     * Constructs an Event task with the given description, from and to times.
     *
     * @param desc The description of the event.
     * @param from The start time.
     * @param to The end time.
     */
    public Event(String desc, String from, String to) {
        super(desc);
        this.from = from;
        this.to = to;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskType getType() {
        return TaskType.EVENT;
    }

    /**
     * Returns the display string for the event task, including from and to times.
     *
     * @return The display string.
     */
    @Override
    public String display() {
        String base = super.display();
        String details = "";
        if (from != null && !from.isBlank()) {
            details += " (from: " + from;
            if (to != null && !to.isBlank()) {
                details += " to: " + to;
            }
            details += ")";
        } else if (to != null && !to.isBlank()) {
            details += " (to: " + to + ")";
        }
        return base + details;
    }

    /**
     * Returns the start time of the event.
     *
     * @return The start time.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the end time of the event.
     *
     * @return The end time.
     */
    public String getTo() {
        return to;
    }
}