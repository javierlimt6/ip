import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private LocalDate by;
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    public Deadline(String desc, LocalDate by) {
        super(desc);
        this.by = by;
    }

    @Override public TaskType getType() { return TaskType.DEADLINE; }

    @Override public String display() {
        String base = super.display();
        if (by != null) {
            return base + " (by: " + by.format(DISPLAY_FORMAT) + ")";
        }
        return base;
    }

    public LocalDate getBy() {
        return by;
    }

    // For serialization compatibility, return formatted string
    public String getByFormatted() {
        return by != null ? by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
    }
}
