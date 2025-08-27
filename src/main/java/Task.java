public abstract class Task {
    private boolean done;
    public String desc;

    public Task(String desc) {
        this.desc = desc;
    }

    public void markDone() { done = true; }
    public void markUndone() { done = false; }

    public boolean isDone() { return done; }

    // refactored to use enum
    public abstract TaskType getType();

    public String statusBox() { return "[" + (done ? "X" : " ") + "]"; }

    public String display() {
        return "[" + getType().shortName() + "]" + statusBox() + " " + desc;
    }
}
