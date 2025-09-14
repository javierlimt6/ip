# AI usage log

T- 2025-08-27 — Refactor: Extracted all parsing/validation logic into Parser class, handling command parsing, deadline/event arguments, task indices, and serialized task data.

- 2025-08-31 — Refactor: Abstracted file I/O logic into Storage class, encapsulating save/load operations and task serialization for better separation of concerns.
- 2025-08-31 — Refactor: Organized all classes into package friday, moving files to src/main/java/friday/ and adding package declarations for better code organization.
- 2025-09-14 — GUI Implementation: Converted CLI to JavaFX GUI following SE-EDU tutorial, created DialogBox custom control, set up AnchorPane layout with ScrollPane, TextField, and Button, integrated command processing into GUI event handlers, replaced Ui prints with DialogBox additions for interactive chat-like interface.ol used
- GPT-5 mini (Preview) via GitHub Copilot assistant.

What I used the tool for (increments)

- 2025-08-25 — Level 0: Rename Duke -> Friday; greeting/exit skeleton.
- 2025-08-25 — Level 1: Echo input and refactor listen() method.
- 2025-08-26 — Added command dispatch map and refactor to Command enum.
- 2025-08-26 — Level 4: Implement Task hierarchy (ToDo, Deadline, Event).
- 2025-08-26 — Level 5: Improve error handling with FridayException.
- 2025-08-27 — Persistence: Step 1 create data dir; Step 2 save tasks to file after mutations; Step 3 load tasks at startup.
- 2025-08-27 — Refactors: extracted Task subclasses into separate files, introduced TaskType enum, Command enum, moved to ArrayList<Task>, added delete command.
- 2025-08-27 — Misc: added .gitignore to exclude persisted duke.txt and updated test input/EXPECTED.TXT.
- 2025-08-27 — Level 8 Step 1: Update Deadline class to use LocalDate for date storage and formatted display.
- 2025-08-27 — Refactor: Abstracted task list logic into TaskList class, encapsulating add/delete/mark/unmark operations and improving modularity.
- 2025-08-27 — Refactor: Extracted all user interactions into Ui class, centralizing input/output operations and improving separation of concerns.
- 2025-08-27 — Refactor: Abstracted all parsing/validation logic into Parser class, handling command parsing, deadline/event arguments, task indices, and serialized task data.

Notes / observations

- What worked: Rapid prototyping of CLI flows, parsing patterns, and serialisation format. Generated code was immediately runnable and easy to adapt. UI refactoring improved code organization by separating concerns. Parser class centralizes all input validation and parsing logic. Storage class isolates file operations for easier testing and maintenance. Package organization improves code structure and prevents naming conflicts. GUI conversion provided interactive chat-like interface using JavaFX, with DialogBox for reusable message components.
- What needed manual work: storage path resolution (CWD issues), small input-validation edge cases, and ensuring save() is called on every mutation. UI extraction required careful replacement of all System.out.println and Scanner usage. Parser extraction involved identifying and moving all parsing logic. Storage extraction required moving serialization and I/O logic. Package refactoring involved moving files and adding package declarations. GUI integration required converting static methods to instance methods and adapting output to DialogBox.
- Time saved: Estimated 2–4 hours across tasks that would otherwise be boilerplate and refactor work. UI refactoring streamlined future maintenance. Parser class makes the code more testable and maintainable. Storage class enables easier mocking for unit tests. Package organization makes the codebase more professional and scalable. GUI implementation provided a modern interface without rewriting core logic.
- Recommendation: Review generated code for edge cases, add unit tests for load/save and parsing, and consider extracting Storage responsibilities into a dedicated class. UI class provides good foundation for future enhancements like colored output or GUI. Parser class enables easier testing of parsing logic and better error handling. Storage class improves modularity and testability of persistence layer. Package structure should be maintained and expanded as the project grows. GUI can be extended with images for DialogBox and better styling.

Record of use

- Assistant: GitHub Copilot (GPT-5 mini, Preview, Grok Code 1 Fast)
- Dates: 2025-08-25 to 2025-08-27, 2025-09-14
- Purpose: code generation, refactoring, persistence and error-handling guidance for the Friday CLI project; GUI implementation using JavaFX following SE-EDU tutorial.
