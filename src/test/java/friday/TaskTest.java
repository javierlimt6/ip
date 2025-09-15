package friday;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testMarkDone() {
        Task task = new ToDo("Test task");
        assertFalse(task.checkDone());
        task.markDone();
        assertTrue(task.checkDone());
    }

    @Test
    public void testMarkUndone() {
        Task task = new ToDo("Test task");
        task.markDone();
        assertTrue(task.checkDone());
        task.markUndone();
        assertFalse(task.checkDone());
    }

    @Test
    public void testCheckDone() {
        Task task = new ToDo("Test task");
        assertFalse(task.checkDone());
        task.markDone();
        assertTrue(task.checkDone());
    }

    @Test
    public void testStatusBox() {
        Task task = new ToDo("Test task");
        assertEquals("[ ]", task.statusBox());
        task.markDone();
        assertEquals("[X]", task.statusBox());
    }

    @Test
    public void testDisplay() {
        Task task = new ToDo("Test task");
        assertEquals("[T][ ] Test task", task.display());
        task.markDone();
        assertEquals("[T][X] Test task", task.display());
    }

    @Test
    public void testGetDesc() {
        Task task = new ToDo("Test description");
        assertEquals("Test description", task.getDesc());
    }
}
