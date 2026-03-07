# US-006 — Add Priority to Tasks

## User Story
As a user
I want to assign a priority level (High, Medium, Low) to each task
So that I can focus on the most important work first

## Acceptance Criteria

### Functional
- [ ] AC1: A priority field (High / Medium / Low) is available when creating a task
- [ ] AC2: A priority field is available when editing a task
- [ ] AC3: Priority defaults to Medium when not explicitly set
- [ ] AC4: Each task card displays the priority visually with a colour-coded badge
- [ ] AC5: High priority tasks show a red badge, Medium show amber, Low show green
- [ ] AC6: Priority is persisted to the database and survives app restart
- [ ] AC7: Existing tasks without a priority are treated as Medium by default

### Unit Tests
- [ ] AC8: TaskServiceTest - createTask with HIGH priority saves and returns correct priority
- [ ] AC9: TaskServiceTest - createTask with no priority defaults to MEDIUM
- [ ] AC10: TaskServiceTest - updateTask can change priority from LOW to HIGH

### Selenium Tests
- [ ] AC11: Selenium creates a task with HIGH priority and verifies red badge appears on card
- [ ] AC12: Selenium creates a task with no priority and verifies MEDIUM badge appears
- [ ] AC13: Selenium edits a task and changes priority from MEDIUM to LOW, verifies badge updates

## Technical Notes
- Backend: Add priority enum (HIGH, MEDIUM, LOW) to Task entity with default MEDIUM
- Frontend: Add priority dropdown to TaskForm
- Frontend: Priority badge with colour coding added to TaskCard
- Database: New priority column auto-added by Hibernate

## Definition of Done
- [ ] Code written and working
- [ ] Manually verified in UI
- [ ] Unit tests written and passing
- [ ] Selenium tests written and passing
- [ ] Committed and pushed to GitHub

## Status: Backlog

---

## Defects Log

| ID | AC | Found By | Description | Status | Fix Applied |
|---|---|---|---|---|---|
| BUG-001 | AC10 | Unit Test | TaskService.updateTask() does not copy priority field from incoming update — priority never changes on edit | ✅ Resolved | Added `existing.setPriority(updatedTask.getPriority())` in `TaskService.updateTask()` — all 13 unit tests passing |
| BUG-002 | AC13 | Selenium Test | Priority badge does not update after editing a task — running Docker deployment missing the TaskService fix from BUG-001 | ✅ Resolved | Added `--with-selenium` flag to `build.sh` to run Selenium tests post-deployment; redeployed with `./build.sh --with-selenium` — all 12 Selenium tests passing |
