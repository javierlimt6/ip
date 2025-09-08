package friday;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    @Test
    public void testGetFrom() {
        Event event = new Event("Meeting", "10am", "12pm");
        assertEquals("10am", event.getFrom());
    }

    @Test
    public void testGetTo() {
        Event event = new Event("Meeting", "10am", "12pm");
        assertEquals("12pm", event.getTo());
    }

    @Test
    public void testDisplay() {
        Event event = new Event("Meeting", "10am", "12pm");
        assertEquals("[E][ ] Meeting (from: 10am to: 12pm)", event.display());
    }

    @Test
    public void testDisplayOnlyFrom() {
        Event event = new Event("Meeting", "10am", null);
        assertEquals("[E][ ] Meeting (from: 10am)", event.display());
    }

    @Test
    public void testDisplayOnlyTo() {
        Event event = new Event("Meeting", null, "12pm");
        assertEquals("[E][ ] Meeting (to: 12pm)", event.display());
    }

    @Test
    public void testGetType() {
        Event event = new Event("Test", "10am", "12pm");
        assertEquals(TaskType.EVENT, event.getType());
    }
}
