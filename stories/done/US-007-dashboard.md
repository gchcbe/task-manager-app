# US-007 — Task Dashboard

## User Story
As a user
I want a dashboard that gives me an at-a-glance summary of my tasks
So that I can quickly understand the state of my work without scrolling through all tasks

## Acceptance Criteria

### Functional
- [x] AC1: A Dashboard view is accessible from the main navigation
- [x] AC2: Dashboard shows total number of tasks
- [x] AC3: Dashboard shows count of tasks by status (To Do, In Progress, Done)
- [x] AC4: Dashboard shows count of tasks by priority (High, Medium, Low)
- [x] AC5: Dashboard shows count of overdue tasks (due date past, status not Done)
- [x] AC6: Dashboard counts update in real time when tasks are added, edited or deleted
- [x] AC7: Clicking a status count navigates to task list filtered by that status
- [x] AC8: Dashboard is visually distinct from task list using summary tiles

### Unit Tests
- [x] AC9: TaskServiceTest - getOverdueTasks returns only tasks past due date with non-DONE status
- [x] AC10: TaskServiceTest - getTaskCountByStatus returns correct counts per status
- [x] AC11: TaskServiceTest - getTaskCountByPriority returns correct counts per priority

### Selenium Tests
- [x] AC12: Selenium verifies dashboard shows correct total task count
- [x] AC13: Selenium creates a task and verifies dashboard total count increments
- [x] AC14: Selenium verifies overdue task count is correct for a known overdue task
- [x] AC15: Selenium clicks a status tile and verifies task list is filtered correctly

## Technical Notes
- Backend: New GET /api/tasks/summary endpoint returning counts by status, priority and overdue
- Frontend: New Dashboard.js component with summary tiles
- Frontend: Navigation toggle between Dashboard and Task List views
- Database: No schema changes needed - derived from existing tasks table

## Definition of Done
- [x] Code written and working
- [x] Manually verified in UI
- [x] Unit tests written and passing
- [x] Selenium tests written and passing
- [x] Committed and pushed to GitHub

## Status: Done

---

## Defects Log

| ID | AC | Type | Found By | Description | Status | Fix Applied |
|---|---|---|---|---|---|---|
| BUG-003 | AC1 | Test defect | Selenium Test | Selenium tests 01–12 hang after US-007 implementation — those tests had an undocumented assumption that the app starts on the Tasks view. Making Dashboard the default landing page (correct per AC1) invalidated that assumption. `@BeforeEach loadApp()` navigates to the Dashboard, so elements like `task-card` and `+ New Task` are not present and waits time out. Fix required in the tests, not the implementation. | ✅ Resolved | Added `navigateToTasksView()` call at the end of `@BeforeEach loadApp()` with a wait on `status-filter` — all tests now start from the Tasks view as expected |
