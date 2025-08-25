public class ToDo extends Task {
    public ToDo(String desc) { super(desc); }
    @Override public TaskType getType() { return TaskType.TODO; }
}
