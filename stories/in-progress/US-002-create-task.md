# US-002 - Create a Task

## User Story
As a user I want to create a new task so that I can track things I need to do.

## Acceptance Criteria

### Functional
- [x] AC1: New Task button visible in header
- [x] AC2: Clicking opens a form modal
- [x] AC3: Form has title (required), description, status and due date fields
- [x] AC4: Submitting creates task and closes modal
- [x] AC5: New task appears immediately in list
- [x] AC6: Validation error shown if title is empty

### Unit Tests
- [ ] AC7: TaskServiceTest - createTask saves and returns task
- [ ] AC8: TaskServiceTest - createTask with missing title throws exception

### Selenium Tests
- [ ] AC9: Selenium fills form and submits, verifies task appears in list
- [ ] AC10: Selenium verifies validation error shown when title is empty
- [ ] AC11: Selenium verifies cancelling form makes no changes

## Definition of Done
- [x] Code written and working
- [x] Manually verified in UI
- [ ] Unit tests written and passing
- [ ] Selenium tests written and passing
- [ ] Committed and pushed to GitHub

## Status: In Progress
