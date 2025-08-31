package friday;

public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String shortName;

    TaskType(String shortName) {
        this.shortName = shortName;
    }

    public String shortName() {
        return shortName;
    }
}