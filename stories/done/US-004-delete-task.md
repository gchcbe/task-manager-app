# US-004 - Delete a Task

## User Story
As a user I want to delete a task so that my list stays clean and relevant.

## Acceptance Criteria

### Functional
- [x] AC1: Each task card has a Delete button
- [x] AC2: Clicking Delete shows a confirmation dialog
- [x] AC3: Confirming removes task from list immediately
- [x] AC4: Cancelling leaves task unchanged

### Unit Tests
- [x] AC5: TaskServiceTest - deleteTask calls deleteById once
- [x] AC6: TaskServiceTest - deleteTask with invalid ID throws exception

### Selenium Tests
- [x] AC7: Selenium clicks Delete, confirms, verifies task is removed
- [x] AC8: Selenium clicks Delete, cancels, verifies task remains

## Definition of Done
- [x] Code written and working
- [x] Manually verified in UI
- [x] Unit tests written and passing
- [x] Selenium tests written and passing
- [x] Committed and pushed to GitHub

## Status: Done
