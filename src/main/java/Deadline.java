public class Deadline extends Task {
    private String by;
    public Deadline(String desc, String by) {
        super(desc);
        this.by = by;
    }
    @Override public TaskType getType() { return TaskType.DEADLINE; }

    @Override public String display() {
        String base = super.display();
        return base + (by != null && !by.isBlank() ? " (by: " + by + ")" : "");
    }

    public String getBy() { return by; }
}
