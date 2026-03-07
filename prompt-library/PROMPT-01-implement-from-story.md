# PROMPT-01 — Implement Feature from User Story

## Purpose
Use this prompt when asking Copilot to implement code changes from a user story.
Copilot should read the story ACs and decide what to build — not be told how.

## When to Use
- Starting a new user story
- Implementing backend changes (entity, service, controller, repository)
- Implementing frontend changes (components, CSS)
- Can be used separately for backend and frontend passes

---

## The Prompt Template

```
#file:stories/in-progress/[STORY-FILE-NAME].md

You are implementing a feature for a Spring Boot + React Task Manager application.

Read the Functional Acceptance Criteria in the attached user story carefully.
Implement the changes required to satisfy those ACs.

## What to change
[DESCRIBE WHICH LAYER YOU ARE TARGETING — choose one per prompt run]

BACKEND (choose if doing backend pass):
- Files to modify: [LIST FILES e.g. Task.java, TaskService.java, TaskController.java]
- Add any new files needed (e.g. DTOs, repository methods)

FRONTEND (choose if doing frontend pass):
- Files to modify: [LIST FILES e.g. TaskForm.js, TaskCard.js, App.js, App.css]
- Add any new component files needed

## Rules
- Implement only what is needed to satisfy the Functional ACs in the story
- Do not modify any existing methods or endpoints unless the AC explicitly requires it
- Do not write any tests — tests will be written separately
- Do not look at existing test files
- Make minimal, targeted changes only
- If adding a new enum or field, ensure backward compatibility with existing data
```

---

## How to Customise

| Placeholder | What to put |
|---|---|
| `[STORY-FILE-NAME].md` | e.g. `US-008-task-labels.md` |
| `[DESCRIBE WHICH LAYER]` | Delete the section you are NOT doing |
| `[LIST FILES]` | The specific files Copilot should touch |

## Tips
- Run this prompt TWICE — once for backend, once for frontend
- For backend pass: close all frontend files before running
- For frontend pass: close all backend files before running
- Review Copilot's output against each AC before accepting
- If Copilot modifies files you didn't list, reject those changes

## Example Usage (Backend Pass)
```
#file:stories/in-progress/US-008-task-labels.md

You are implementing a feature for a Spring Boot + React Task Manager application.

Read the Functional Acceptance Criteria in the attached user story carefully.
Implement the changes required to satisfy those ACs.

## What to change

BACKEND:
- Files to modify: Task.java, TaskService.java, TaskController.java
- Add any new files needed (e.g. DTOs, repository methods)

## Rules
- Implement only what is needed to satisfy the Functional ACs in the story
- Do not modify any existing methods or endpoints unless the AC explicitly requires it
- Do not write any tests — tests will be written separately
- Do not look at existing test files
- Make minimal, targeted changes only
- If adding a new enum or field, ensure backward compatibility with existing data
```
