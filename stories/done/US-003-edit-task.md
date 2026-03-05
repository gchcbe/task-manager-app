# US-003 - Edit a Task

## User Story
As a user I want to edit an existing task so that I can update its details or status.

## Acceptance Criteria

### Functional
- [x] AC1: Each task card has an Edit button
- [x] AC2: Clicking Edit opens form pre-filled with current data
- [x] AC3: User can modify all fields
- [x] AC4: Submitting saves changes and closes modal
- [x] AC5: Updated task reflected immediately in list

### Unit Tests
- [x] AC6: TaskServiceTest - updateTask updates fields and returns saved task
- [x] AC7: TaskServiceTest - updateTask with invalid ID throws exception

### Selenium Tests
- [x] AC8: Selenium clicks Edit, changes title, saves, verifies update in list
- [x] AC9: Selenium verifies cancelling edit makes no changes

## Definition of Done
- [x] Code written and working
- [x] Manually verified in UI
- [x] Unit tests written and passing
- [x] Selenium tests written and passing
- [x] Committed and pushed to GitHub

## Status: Done
