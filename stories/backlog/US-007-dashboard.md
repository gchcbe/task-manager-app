# US-007 — Task Dashboard

## User Story
As a user
I want a dashboard that gives me an at-a-glance summary of my tasks
So that I can quickly understand the state of my work without scrolling through all tasks

## Acceptance Criteria

### Functional
- [ ] AC1: A Dashboard view is accessible from the main navigation
- [ ] AC2: Dashboard shows total number of tasks
- [ ] AC3: Dashboard shows count of tasks by status (To Do, In Progress, Done)
- [ ] AC4: Dashboard shows count of tasks by priority (High, Medium, Low)
- [ ] AC5: Dashboard shows count of overdue tasks (due date past, status not Done)
- [ ] AC6: Dashboard counts update in real time when tasks are added, edited or deleted
- [ ] AC7: Clicking a status count navigates to task list filtered by that status
- [ ] AC8: Dashboard is visually distinct from task list using summary tiles

### Unit Tests
- [ ] AC9: TaskServiceTest - getOverdueTasks returns only tasks past due date with non-DONE status
- [ ] AC10: TaskServiceTest - getTaskCountByStatus returns correct counts per status
- [ ] AC11: TaskServiceTest - getTaskCountByPriority returns correct counts per priority

### Selenium Tests
- [ ] AC12: Selenium verifies dashboard shows correct total task count
- [ ] AC13: Selenium creates a task and verifies dashboard total count increments
- [ ] AC14: Selenium verifies overdue task count is correct for a known overdue task
- [ ] AC15: Selenium clicks a status tile and verifies task list is filtered correctly

## Technical Notes
- Backend: New GET /api/tasks/summary endpoint returning counts by status, priority and overdue
- Frontend: New Dashboard.js component with summary tiles
- Frontend: Navigation toggle between Dashboard and Task List views
- Database: No schema changes needed - derived from existing tasks table

## Definition of Done
- [ ] Code written and working
- [ ] Manually verified in UI
- [ ] Unit tests written and passing
- [ ] Selenium tests written and passing
- [ ] Committed and pushed to GitHub

## Status: Backlog
