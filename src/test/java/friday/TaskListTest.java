package friday;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    @Test
    public void testAddTodo() throws FridayException {
        TaskList taskList = new TaskList();
        taskList.addTodo("Test todo");
        assertEquals(1, taskList.size());
        assertEquals("Test todo", taskList.get(0).getDesc());
    }

    @Test
    public void testAddTodoEmptyDescription() {
        TaskList taskList = new TaskList();
        assertThrows(FridayException.class, () -> taskList.addTodo(""));
    }

    @Test
    public void testDelete() throws FridayException {
        TaskList taskList = new TaskList();
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        assertEquals(2, taskList.size());
        taskList.delete(1);
        assertEquals(1, taskList.size());
        assertEquals("Task 2", taskList.get(0).getDesc());
    }

    @Test
    public void testDeleteInvalidIndex() {
        TaskList taskList = new TaskList();
        assertThrows(FridayException.class, () -> taskList.delete(1));
    }

    @Test
    public void testMark() throws FridayException {
        TaskList taskList = new TaskList();
        taskList.addTodo("Test task");
        assertFalse(taskList.get(0).isDone());
        taskList.mark(1);
        assertTrue(taskList.get(0).isDone());
    }

    @Test
    public void testMarkInvalidIndex() {
        TaskList taskList = new TaskList();
        assertThrows(FridayException.class, () -> taskList.mark(1));
    }

    @Test
    public void testList() throws FridayException {
        TaskList taskList = new TaskList();
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        String expected = "Here are the tasks in your list:\n 1.[T][ ] Task 1\n 2.[T][ ] Task 2";
        assertEquals(expected, taskList.list());
    }

    @Test
    public void testFind() throws FridayException {
        TaskList taskList = new TaskList();
        taskList.addTodo("Buy milk");
        taskList.addTodo("Read book");
        taskList.addTodo("Buy bread");
        String result = taskList.find("buy");
        assertTrue(result.contains("Buy milk"));
        assertTrue(result.contains("Buy bread"));
        assertFalse(result.contains("Read book"));
    }

    @Test
    public void testFindNoMatch() throws FridayException {
        TaskList taskList = new TaskList();
        taskList.addTodo("Read book");
        String result = taskList.find("nonexistent");
        assertEquals("No matching tasks found.", result);
    }

    @Test
    public void testSize() {
        TaskList taskList = new TaskList();
        assertEquals(0, taskList.size());
        taskList.add(new ToDo("Test"));
        assertEquals(1, taskList.size());
    }

    @Test
    public void testGet() throws FridayException {
        TaskList taskList = new TaskList();
        Task task = new ToDo("Test");
        taskList.add(task);
        assertEquals(task, taskList.get(0));
    }
}
