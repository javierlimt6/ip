class Deadline extends Task {
    String by;
    Deadline(String desc, String by) { super(desc); this.by = by; }
    @Override String typeLetter() { return "D"; }
    @Override String display() {
        String base = super.display();
        return base + (by != null && !by.isBlank() ? " (by: " + by + ")" : "");
    }
}
