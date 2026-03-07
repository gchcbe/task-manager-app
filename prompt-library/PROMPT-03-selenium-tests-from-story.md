# PROMPT-03 — Write Selenium Tests from User Story

## Purpose
Use this prompt when asking Copilot to write Selenium UI tests driven purely from
the Selenium Test ACs in a user story. Copilot must not look at the implementation.

## When to Use
- After unit tests are passing
- Before running the full build with --with-selenium
- All implementation files must be CLOSED in VS Code before running this prompt
- Only the story file and TaskManagerSeleniumTest.java should be open

## Critical Rule
> The app MUST be running in Docker before Selenium tests can pass.
> Always run ./build.sh --with-selenium after writing tests — never run
> Selenium tests against a stale deployment.

---

## The Prompt Template

```
#file:stories/in-progress/[STORY-FILE-NAME].md

You are writing Selenium UI tests for a React Task Manager application.

Read ONLY the Selenium Test Acceptance Criteria section of the attached user story.
Write Java Selenium tests that verify the behaviour described in those ACs.

## Target test file
TaskManagerSeleniumTest.java

## ACs to implement
[LIST THE SELENIUM ACS FROM THE STORY — copy them directly, e.g.]
- AC[N]: Selenium — [description]
- AC[N+1]: Selenium — [description]

## App structure hints
[DESCRIBE RELEVANT CSS CLASSES AND UI STRUCTURE FOR THIS STORY — e.g.]
- [NEW ELEMENT]: has CSS class "[class-name]"
- [NEW ELEMENT]: located inside "[parent-class]"
- Navigation: Dashboard button is in ".header-nav", Tasks button is in ".header-nav"
- Task cards have class "task-card", task titles have class "task-title"
- Forms have class "task-form", submit buttons have class "btn-primary"
- Filter buttons have class "filter-btn", active filter has class "filter-btn active"

## Order
- Existing tests go up to @Order([LAST-ORDER-NUMBER])
- Start new tests from @Order([LAST-ORDER-NUMBER + 1])

## Rules
- Do NOT look at any implementation files
- Test the BEHAVIOUR described in the ACs — not the implementation
- Follow the exact patterns already in TaskManagerSeleniumTest.java
- Use existing helper methods (createTask, clickNewTaskButton etc.) where possible
- Add new private helper methods if a pattern is reused across 2+ new tests
- Do not modify any existing test methods
- Use WebDriverWait for all element interactions — no Thread.sleep except after
  state-changing operations (create/edit/delete) where a 500-1000ms settle is needed
```

---

## How to Customise

| Placeholder | What to put |
|---|---|
| `[STORY-FILE-NAME].md` | e.g. `US-008-task-labels.md` |
| `[LIST THE SELENIUM ACS]` | Copy the Selenium ACs section from the story verbatim |
| `[NEW ELEMENT]` | Describe CSS classes of elements introduced by this story |
| `[LAST-ORDER-NUMBER]` | Check the highest @Order in TaskManagerSeleniumTest.java |

## App Structure Reference (current as of US-007)
Use this as your base for the "App structure hints" section:

```
General:
- App header:         .app-header
- Navigation buttons: .header-nav > button
- Main content:       .app-main

Task List view:
- New Task button:    .btn-primary (text: "+ New Task")
- Filter buttons:     .filter-btn / .filter-btn.active
- Task cards:         .task-card
- Task title:         .task-title
- Edit button:        .btn-edit
- Delete button:      .btn-delete
- Priority badge:     .priority-high / .priority-medium / .priority-low

Task Form:
- Form container:     .task-form
- Title input:        #title
- Description input:  #description
- Priority dropdown:  #priority
- Submit button:      .btn-primary (text: "Create Task" or "Update Task")
- Cancel button:      .btn-secondary

Dashboard view:
- Dashboard container: .dashboard
- Total tile:          .tile-total > .tile-count
- Overdue tile:        .tile-overdue > .tile-count
- Status tiles:        .tile-todo / .tile-in-progress / .tile-done (clickable buttons)
- Priority tiles:      .tile-high / .tile-medium / .tile-low
- Tile count:          .tile-count
- Tile label:          .tile-label
```

## Tips
- When verifying new UI elements, give Copilot the exact CSS class name
  introduced by the story's frontend implementation
- If a test depends on specific data (e.g. an overdue task), create that
  data within the test using the existing createTask() helper or a new variant
- AC14-style tests (verifying counts) should assert >= 0 unless the test
  itself creates the data — never assume DB state
- Always verify CSS class names match between App structure reference and
  actual frontend implementation before running

## Example Usage
```
#file:stories/in-progress/US-008-task-labels.md

You are writing Selenium UI tests for a React Task Manager application.

Read ONLY the Selenium Test Acceptance Criteria section of the attached user story.
Write Java Selenium tests that verify the behaviour described in those ACs.

## Target test file
TaskManagerSeleniumTest.java

## ACs to implement
- AC11: Selenium creates a task with label URGENT and verifies label badge appears on card
- AC12: Selenium edits a task and changes label, verifies badge updates

## App structure hints
- Label badge: has CSS class "task-label-badge"
- Label dropdown in form: #label (select element)
- Existing: task cards have class "task-card"
- Navigation: Dashboard/Tasks buttons in ".header-nav"

## Order
- Existing tests go up to @Order(16)
- Start new tests from @Order(17)

## Rules
- Do NOT look at any implementation files
- Test the BEHAVIOUR described in the ACs — not the implementation
- Follow the exact patterns already in TaskManagerSeleniumTest.java
- Use existing helper methods where possible
- Do not modify any existing test methods
```
