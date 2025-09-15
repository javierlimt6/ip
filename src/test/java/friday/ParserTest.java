package friday;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void testParseCommand() throws FridayException {
        Parser.ParsedCommand cmd = Parser.parseCommand("todo read book");
        assertEquals("todo", cmd.command);
        assertEquals("read book", cmd.arguments);
    }

    @Test
    public void testParseCommandNoArgs() throws FridayException {
        Parser.ParsedCommand cmd = Parser.parseCommand("list");
        assertEquals("list", cmd.command);
        assertEquals("", cmd.arguments);
    }

    @Test
    public void testParseCommandEmpty() {
        assertThrows(FridayException.class, () -> Parser.parseCommand(""));
    }

    @Test
    public void testParseCommandNull() {
        assertThrows(FridayException.class, () -> Parser.parseCommand(null));
    }

    @Test
    public void testParseCommandWhitespace() {
        assertThrows(FridayException.class, () -> Parser.parseCommand("   "));
    }

    @Test
    public void testParseSerializedTaskTodo() {
        Task task = Parser.parseSerializedTask("T | 0 | Read book");
        assertNotNull(task);
        assertTrue(task instanceof ToDo);
        assertEquals("Read book", task.getDesc());
        assertFalse(task.checkDone());
    }

    @Test
    public void testParseSerializedTaskTodoDone() {
        Task task = Parser.parseSerializedTask("T | 1 | Read book");
        assertNotNull(task);
        assertTrue(task instanceof ToDo);
        assertEquals("Read book", task.getDesc());
        assertTrue(task.checkDone());
    }

    @Test
    public void testParseSerializedTaskDeadline() {
        Task task = Parser.parseSerializedTask("D | 0 | Submit report | 2023-10-15");
        assertNotNull(task);
        assertTrue(task instanceof Deadline);
        assertEquals("Submit report", task.getDesc());
        assertEquals(java.time.LocalDate.of(2023, 10, 15), ((Deadline) task).getBy());
    }

    @Test
    public void testParseSerializedTaskEvent() {
        Task task = Parser.parseSerializedTask("E | 0 | Meeting | 10am || 12pm");
        assertNotNull(task);
        assertTrue(task instanceof Event);
        assertEquals("Meeting", task.getDesc());
        assertEquals("10am", ((Event) task).getFrom());
        assertEquals("12pm", ((Event) task).getTo());
    }

    @Test
    public void testParseSerializedTaskMalformed() {
        Task task = Parser.parseSerializedTask("invalid");
        assertNull(task);
    }

    @Test
    public void testParseDeadlineArgs() throws FridayException {
        Parser.DeadlineArgs args = Parser.parseDeadlineArgs("Submit report /by 2023-10-15");
        assertEquals("Submit report", args.description);
        assertEquals(java.time.LocalDate.of(2023, 10, 15), args.by);
    }

    @Test
    public void testParseDeadlineArgsNoDate() throws FridayException {
        Parser.DeadlineArgs args = Parser.parseDeadlineArgs("Submit report");
        assertEquals("Submit report", args.description);
        assertNull(args.by);
    }

    @Test
    public void testParseEventArgs() throws FridayException {
        Parser.EventArgs args = Parser.parseEventArgs("Meeting /from 10am /to 12pm");
        assertEquals("Meeting", args.description);
        assertEquals("10am", args.from);
        assertEquals("12pm", args.to);
    }

    @Test
    public void testParseIndex() throws FridayException {
        assertEquals(5, Parser.parseIndex("5"));
    }

    @Test
    public void testParseIndexInvalid() {
        assertThrows(FridayException.class, () -> Parser.parseIndex("abc"));
    }

    @Test
    public void testParseIndexZero() {
        assertThrows(FridayException.class, () -> Parser.parseIndex("0"));
    }
}
