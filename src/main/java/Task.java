abstract class Task {
    boolean done;
    String desc;

    Task(String desc) { this.desc = desc; }

    void markDone() { done = true; }

    void markUndone() { done = false; }

    abstract String typeLetter();

    String statusBox() { return "[" + (done ? "X" : " ") + "]"; }

    String display() { return "[" + typeLetter() + "]" + statusBox() + " " + desc; }
}
