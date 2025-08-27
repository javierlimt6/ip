public class Event extends Task {
    private String from;
    private String to;
    public Event(String desc, String from, String to) {
        super(desc);
        this.from = from;
        this.to = to;
    }
    @Override public TaskType getType() { return TaskType.EVENT; }

    @Override public String display() {
        String base = super.display();
        String details = "";
        if (from != null && !from.isBlank()) {
            details += " (from: " + from;
            if (to != null && !to.isBlank()) details += " to: " + to;
            details += ")";
        } else if (to != null && !to.isBlank()) {
            details += " (to: " + to + ")";
        }
        return base + details;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
}
