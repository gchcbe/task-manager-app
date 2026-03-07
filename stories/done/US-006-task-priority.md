# US-006 — Add Priority to Tasks

## User Story
As a user
I want to assign a priority level (High, Medium, Low) to each task
So that I can focus on the most important work first

## Acceptance Criteria

### Functional
- [x] AC1: A priority field (High / Medium / Low) is available when creating a task
- [x] AC2: A priority field is available when editing a task
- [x] AC3: Priority defaults to Medium when not explicitly set
- [x] AC4: Each task card displays the priority visually with a colour-coded badge
- [x] AC5: High priority tasks show a red badge, Medium show amber, Low show green
- [x] AC6: Priority is persisted to the database and survives app restart
- [x] AC7: Existing tasks without a priority are treated as Medium by default

### Unit Tests
- [x] AC8: TaskServiceTest - createTask with HIGH priority saves and returns correct priority
- [x] AC9: TaskServiceTest - createTask with no priority defaults to MEDIUM
- [x] AC10: TaskServiceTest - updateTask can change priority from LOW to HIGH

### Selenium Tests
- [x] AC11: Selenium creates a task with HIGH priority and verifies red badge appears on card
- [x] AC12: Selenium creates a task with no priority and verifies MEDIUM badge appears
- [x] AC13: Selenium edits a task and changes priority from MEDIUM to LOW, verifies badge updates

## Technical Notes
- Backend: Add priority enum (HIGH, MEDIUM, LOW) to Task entity with default MEDIUM
- Frontend: Add priority dropdown to TaskForm
- Frontend: Priority badge with colour coding added to TaskCard
- Database: New priority column auto-added by Hibernate

## Definition of Done
- [x] Code written and working
- [x] Manually verified in UI
- [x] Unit tests written and passing
- [x] Selenium tests written and passing
- [x] Committed and pushed to GitHub

## Status: Done

---

## Defects Log

| ID | AC | Found By | Description | Status | Fix Applied |
|---|---|---|---|---|---|
| BUG-001 | AC10 | Unit Test | TaskService.updateTask() does not copy priority field from incoming update — priority never changes on edit | ✅ Resolved | Added `existing.setPriority(updatedTask.getPriority())` in `TaskService.updateTask()` — all 13 unit tests passing |
| BUG-002 | AC13 | Selenium Test | Priority badge does not update after editing a task — running Docker deployment missing the TaskService fix from BUG-001 | ✅ Resolved | Added `--with-selenium` flag to `build.sh` to run Selenium tests post-deployment; redeployed with `./build.sh --with-selenium` — all 12 Selenium tests passing |
