# AI usage log

Tool used
- GPT-5 mini (Preview) via GitHub Copilot assistant.

What I used the tool for (increments)
- Level 0: rename project class to Friday and implement greeting/exit skeleton.
- Level 1: add echo behaviour and refactor listen loop into listen().
- Added command dispatch map (command -> method).
- Level 4: implement Task hierarchy (ToDo, Deadline, Event) and commands: todo, deadline, event, list, mark, unmark.
- Several iterative refactors to improve structure and formatting.

Notes / observations
- What worked: rapid prototyping of command parsing, task classes, and CLI interaction. The assistant provided concise, compilable Java snippets that required minimal edits.
- What didn't: some small stylistic choices and edge cases (e.g., input validation, persistence) needed manual review and minor fixes.
- Time saved: significantly reduced boilerplate and iteration time (estimate: 1–3 hours depending on feature).
- Recommendations: review generated code for edge cases, add unit tests and persistence, and incrementally refactor to smaller classes/files.

Record of use
- Date: 2025-08-25
- Assistant: GitHub Copilot (GPT-5 mini, Preview)
- Purpose: code generation and refactor assistance for the ip (Friday) CLI project.
