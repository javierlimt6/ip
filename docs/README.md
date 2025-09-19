# Friday User Guide

Welcome to **Friday**, your friendly task management chatbot! Friday helps you organize your tasks, deadlines, and events with a simple and intuitive interface. Whether you're managing your daily to-do list or planning ahead, Friday is here to assist you.

## Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
- [Command Summary](#command-summary)
- [FAQ](#faq)

## Quick Start

1. **Download and Run**: Launch the Friday application. The GUI will open automatically.
2. **Start Chatting**: Type commands in the input field at the bottom of the window.
3. **Get Help**: If you're stuck, try typing `help` or refer to this guide.

Friday uses a chat-like interface where you can type commands and receive responses instantly.

## Features

### Adding Tasks

#### Todo Tasks

Add a simple task without a deadline.

**Format**: `todo <description>`

**Example**:

```
todo Buy groceries
```

**Expected Output**:

```
Got it. I've added this task:
  [T][ ] Buy groceries
Now you have 1 tasks in the list.
```

#### Deadline Tasks

Add a task with a due date.

**Format**: `deadline <description> /by <date>`

**Example**:

```
deadline Submit report /by 2025-10-15
```

**Expected Output**:

```
Got it. I've added this task:
  [D][ ] Submit report (by: Oct 15 2025)
Now you have 2 tasks in the list.
```

#### Event Tasks

Add an event with a start and end time.

**Format**: `event <description> /from <start> /to <end>`

**Example**:

```
event Team meeting /from 10am /to 12pm
```

**Expected Output**:

```
Got it. I've added this task:
  [E][ ] Team meeting (from: 10am to: 12pm)
Now you have 3 tasks in the list.
```

### Managing Tasks

#### List All Tasks

View all your tasks!

**Format**: `list`

**Example**:

```
list
```

**Expected Output**:

```
Here are the tasks in your list:
 1.[T][ ] Buy groceries
 2.[D][ ] Submit report (by: Oct 15 2025)
 3.[E][ ] Team meeting (from: 10am to: 12pm)
```

#### Mark Task as Done

Mark a task as completed.

**Format**: `mark <task number>`

**Example**:

```
mark 1
```

**Expected Output**:

```
Nice! I've marked this task as done:
  [T][X] Buy groceries
```

#### Mark Task as Undone

Mark a completed task as not done.

**Format**: `unmark <task number>`

**Example**:

```
unmark 1
```

**Expected Output**:

```
OK, I've marked this task as not done yet:
  [T][ ] Buy groceries
```

#### Delete a Task

Remove a task from the list.

**Format**: `delete <task number>`

**Example**:

```
delete 3
```

**Expected Output**:

```
Noted. I've removed this task:
  [E][ ] Team meeting (from: 10am to: 12pm)
Now you have 2 tasks in the list.
```

### Tagging Tasks

#### Add a Tag

Tag a task with a custom label (e.g., #fun, #work).

**Format**: `tag <task number> <tag>`

**Example**:

```
tag 1 fun
```

**Expected Output**:

```
Got it! I've tagged this task:
  [T][ ] Buy groceries #fun
```

#### Remove a Tag

Remove a tag from a task.

**Format**: `untag <task number> <tag>`

**Example**:

```
untag 1 fun
```

**Expected Output**:

```
Got it! I've removed the tag from this task:
  [T][ ] Buy groceries
```

### Finding Tasks

#### Find Tasks by Keyword

Search for tasks containing a specific keyword.

**Format**: `find <keyword>`

**Example**:

```
find report
```

**Expected Output**:

```
Here are the matching tasks in your list:
 1.[D][ ] Submit report (by: Oct 15 2025)
```

### Exiting the Application

#### Bye Command

Close the application.

**Format**: `bye`

**Example**:

```
bye
```

**Expected Output**:

```
Bye. Hope to see you again soon!
```

## Command Summary

| Command  | Format                                        | Description              |
| -------- | --------------------------------------------- | ------------------------ |
| todo     | `todo <description>`                          | Add a todo task          |
| deadline | `deadline <description> /by <date>`           | Add a deadline task      |
| event    | `event <description> /from <start> /to <end>` | Add an event task        |
| list     | `list`                                        | List all tasks           |
| mark     | `mark <task number>`                          | Mark task as done        |
| unmark   | `unmark <task number>`                        | Mark task as undone      |
| delete   | `delete <task number>`                        | Delete a task            |
| tag      | `tag <task number> <tag>`                     | Add a tag to a task      |
| untag    | `untag <task number> <tag>`                   | Remove a tag from a task |
| find     | `find <keyword>`                              | Find tasks by keyword    |
| bye      | `bye`                                         | Exit the application     |

## FAQ

**Q: How do I edit a task?**  
A: Friday doesn't support direct editing. Delete the task and add a new one.

**Q: What date format should I use?**  
A: Use YYYY-MM-DD format for deadlines (e.g., 2025-10-15).

**Q: Can I have multiple tags on one task?**  
A: Yes! Add multiple tags using separate `tag` commands.

**Q: What happens if I enter an invalid command?**  
A: Friday will show an error message and suggest valid commands.

**Q: Are my tasks saved automatically?**  
A: Yes, all changes are saved to a file automatically.

**Q: How do I view only completed tasks?**  
A: Use `list` and look for tasks marked with [X].

---

For more help, feel free to explore the features or contact support. Happy task managing with Friday! 🎉
