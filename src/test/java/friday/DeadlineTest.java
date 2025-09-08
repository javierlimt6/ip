package friday;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class DeadlineTest {

    @Test
    public void testGetBy() {
        LocalDate date = LocalDate.of(2023, 10, 15);
        Deadline deadline = new Deadline("Submit report", date);
        assertEquals(date, deadline.getBy());
    }

    @Test
    public void testGetByFormatted() {
        LocalDate date = LocalDate.of(2023, 10, 15);
        Deadline deadline = new Deadline("Submit report", date);
        assertEquals("2023-10-15", deadline.getByFormatted());
    }

    @Test
    public void testDisplay() {
        LocalDate date = LocalDate.of(2023, 10, 15);
        Deadline deadline = new Deadline("Submit report", date);
        assertEquals("[D][ ] Submit report (by: Oct 15 2023)", deadline.display());
    }

    @Test
    public void testGetType() {
        Deadline deadline = new Deadline("Test", LocalDate.now());
        assertEquals(TaskType.DEADLINE, deadline.getType());
    }
}
