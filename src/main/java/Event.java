class Event extends Task {
    String from;
    String to;
    Event(String desc, String from, String to) { super(desc); this.from = from; this.to = to; }
    @Override String typeLetter() { return "E"; }
    @Override String display() {
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
}
