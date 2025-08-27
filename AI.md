# AI usage log

Tool used
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

Notes / observations
- What worked: Rapid prototyping of CLI flows, parsing patterns, and serialisation format. Generated code was immediately runnable and easy to adapt. UI refactoring improved code organization by separating concerns.
- What needed manual work: storage path resolution (CWD issues), small input-validation edge cases, and ensuring save() is called on every mutation. UI extraction required careful replacement of all System.out.println and Scanner usage.
- Time saved: Estimated 2–4 hours across tasks that would otherwise be boilerplate and refactor work. UI refactoring streamlined future maintenance.
- Recommendation: Review generated code for edge cases, add unit tests for load/save and parsing, and consider extracting Storage responsibilities into a dedicated class. UI class provides good foundation for future enhancements like colored output or GUI.

Record of use
- Assistant: GitHub Copilot (GPT-5 mini, Preview)
- Dates: 2025-08-25 to 2025-08-27
- Purpose: code generation, refactoring, persistence and error-handling guidance for the Friday CLI project.
