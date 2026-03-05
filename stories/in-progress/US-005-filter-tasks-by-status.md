# US-005 - Filter Tasks by Status

## User Story
As a user I want to filter tasks by status so that I can focus on relevant tasks.

## Acceptance Criteria

### Functional
- [x] AC1: Filter buttons shown for All, To Do, In Progress and Done
- [x] AC2: Each button shows count of tasks in that status
- [x] AC3: Clicking a filter shows only matching tasks
- [x] AC4: Clicking All Tasks shows all tasks
- [x] AC5: Active filter is visually highlighted
- [x] AC6: Counts update immediately after task changes

### Unit Tests
- [ ] AC7: TaskServiceTest - getTasksByStatus returns only matching tasks
- [ ] AC8: TaskServiceTest - getTasksByStatus returns empty list when no matches

### Selenium Tests
- [ ] AC9: Selenium clicks To Do filter, verifies only TODO tasks shown
- [ ] AC10: Selenium clicks Done filter, verifies only DONE tasks shown
- [ ] AC11: Selenium clicks All Tasks, verifies all tasks shown

## Definition of Done
- [x] Code written and working
- [x] Manually verified in UI
- [ ] Unit tests written and passing
- [ ] Selenium tests written and passing
- [ ] Committed and pushed to GitHub

## Status: In Progress
