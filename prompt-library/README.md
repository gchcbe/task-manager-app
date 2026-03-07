# Prompt Library — Agentic SDLC

Reusable Copilot prompts for the Task Manager SDLC workflow.
Each prompt is designed to be driven from user story ACs — never from implementation code.

---

## Prompts

| File | Purpose | When to Use |
|---|---|---|
| PROMPT-01-implement-from-story.md | Implement feature code from story ACs | Start of every story |
| PROMPT-02-unit-tests-from-story.md | Write unit tests from story ACs | After implementation, before Selenium |
| PROMPT-03-selenium-tests-from-story.md | Write Selenium tests from story ACs | After unit tests pass |

---

## The Workflow These Prompts Support

```
1. Write story with ACs
        ↓
2. PROMPT-01 (backend pass) → Copilot implements backend
        ↓
3. PROMPT-01 (frontend pass) → Copilot implements frontend
        ↓
4. CLOSE all implementation files
        ↓
5. PROMPT-02 → Copilot writes unit tests from ACs only
        ↓
6. mvn test → fix any bugs found, log in story defects section
        ↓
7. CLOSE all implementation files
        ↓
8. PROMPT-03 → Copilot writes Selenium tests from ACs only
        ↓
9. ./build.sh --with-selenium → fix any bugs found, log in story defects section
        ↓
10. All green → move story to done/
```

---

## Golden Rules

1. **Tests are always written from ACs — never from code**
2. **Close implementation files before writing tests**
3. **Selenium tests always run against a fresh Docker build**
4. **Every bug gets logged in the story defects section before being fixed**
5. **A story is only done when ALL ACs are ticked and ALL tests pass**

---

## App Structure Reference (update as new components are added)

```
CSS Classes — Task List:
  .app-header, .header-nav, .app-main
  .btn-primary, .btn-secondary, .btn-edit, .btn-delete
  .filter-btn, .filter-btn.active
  .task-card, .task-title, .task-form
  .priority-high, .priority-medium, .priority-low
  #title, #description, #priority, #status, #dueDate

CSS Classes — Dashboard:
  .dashboard, .dashboard-section, .dashboard-tile
  .tile-total, .tile-overdue
  .tile-todo, .tile-in-progress, .tile-done
  .tile-high, .tile-medium, .tile-low
  .tile-count, .tile-label
```

---

## Phase 3 Note
In Phase 3 these prompts will be consumed directly by the Claude AI agent.
The agent will read the story file, select the appropriate prompt template,
fill in the placeholders and execute the workflow automatically.
