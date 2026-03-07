# PROMPT-02 — Write Unit Tests from User Story

## Purpose
Use this prompt when asking Copilot to write unit tests driven purely from
the Unit Test ACs in a user story. Copilot must not look at the implementation.

## When to Use
- After implementation is complete and committed
- Before writing Selenium tests
- All implementation files must be CLOSED in VS Code before running this prompt

## Critical Rule
> Close Task.java, TaskService.java, TaskController.java and all component files
> before opening Copilot Chat. The only files open should be the story file
> and TaskServiceTest.java.

---

## The Prompt Template

```
#file:stories/in-progress/[STORY-FILE-NAME].md

You are writing unit tests for a Spring Boot Task Manager application.

Read ONLY the Unit Test Acceptance Criteria section of the attached user story.
Write JUnit 5 unit tests that verify the behaviour described in those ACs.

## Target test file
TaskServiceTest.java

## ACs to implement
[LIST THE UNIT TEST ACS FROM THE STORY — copy them directly, e.g.]
- AC[N]: TaskServiceTest — [description]
- AC[N+1]: TaskServiceTest — [description]
- AC[N+2]: TaskServiceTest — [description]

## Rules
- Do NOT look at any implementation files (Task.java, TaskService.java etc.)
- Test the BEHAVIOUR described in the ACs — not the implementation
- Follow the existing Mockito patterns already in TaskServiceTest.java
- Each AC must have exactly one test method
- Name test methods descriptively: methodName_shouldBehaviour_whenCondition()
- Do not modify any existing test methods
- Add tests below the existing ones
```

---

## How to Customise

| Placeholder | What to put |
|---|---|
| `[STORY-FILE-NAME].md` | e.g. `US-008-task-labels.md` |
| `[LIST THE UNIT TEST ACS]` | Copy the Unit Test ACs section from the story verbatim |

## Tips
- Copy the AC text exactly from the story — do not paraphrase
- If Copilot starts referencing implementation details (field names, method signatures),
  stop it and remind it to test behaviour from the AC only
- Run `mvn test` immediately after — failing tests indicate either a real bug
  (good — log it!) or a test that needs AC clarification

## Example Usage
```
#file:stories/in-progress/US-008-task-labels.md

You are writing unit tests for a Spring Boot Task Manager application.

Read ONLY the Unit Test Acceptance Criteria section of the attached user story.
Write JUnit 5 unit tests that verify the behaviour described in those ACs.

## Target test file
TaskServiceTest.java

## ACs to implement
- AC8: TaskServiceTest — createTask with label URGENT saves and returns correct label
- AC9: TaskServiceTest — createTask with no label saves with null label
- AC10: TaskServiceTest — updateTask can change label from URGENT to null

## Rules
- Do NOT look at any implementation files (Task.java, TaskService.java etc.)
- Test the BEHAVIOUR described in the ACs — not the implementation
- Follow the existing Mockito patterns already in TaskServiceTest.java
- Each AC must have exactly one test method
- Name test methods descriptively: methodName_shouldBehaviour_whenCondition()
- Do not modify any existing test methods
- Add tests below the existing ones
```

## What Good Looks Like
A good unit test from this prompt:
- Arranges mock data using only what the AC describes
- Does NOT import or reference specific field names from the implementation
- Asserts the exact outcome stated in the AC
- Would still be valid if the implementation was rewritten differently
