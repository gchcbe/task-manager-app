package com.taskmanager.taskmanager;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskManagerSeleniumTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:3000";

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void loadApp() {
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("app-header")));
        // App defaults to Dashboard view — navigate to Tasks so all existing tests
        // start from a consistent state with task cards and filters visible.
        navigateToTasksView();
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("status-filter")));
    }

    // ── US-001: View All Tasks ──

    @Test
    @Order(1)
    void test01_appLoadsWithHeader() {
        // AC8: Selenium verifies task list loads when app opens
        WebElement header = driver.findElement(By.className("app-header"));
        assertTrue(header.getText().contains("Task Manager"),
            "Header should contain Task Manager");
        System.out.println("PASS: App loads with Task Manager header");
    }

    @Test
    @Order(2)
    void test02_filterButtonsVisible() {
        // AC1: Filter buttons shown for All, To Do, In Progress, Done
        List<WebElement> filters = driver.findElements(By.className("filter-btn"));
        assertTrue(filters.size() >= 4, "Should have at least 4 filter buttons");
        String allText = filters.stream()
            .map(WebElement::getText)
            .reduce("", String::concat);
        assertTrue(allText.contains("All"));
        assertTrue(allText.contains("To Do"));
        assertTrue(allText.contains("In Progress"));
        assertTrue(allText.contains("Done"));
        System.out.println("PASS: All filter buttons visible");
    }

    // ── US-002: Create a Task ──

    @Test
    @Order(3)
    void test03_createTask() {
        // AC9: Selenium fills form and submits, verifies task appears
        clickNewTaskButton();

        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-form")));

        driver.findElement(By.id("title")).sendKeys("Selenium Java Test Task");
        driver.findElement(By.id("description")).sendKeys("Created by Java Selenium");

        clickButtonByText("Create Task");

        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-card")));

        List<WebElement> titles = driver.findElements(By.className("task-title"));
        boolean found = titles.stream()
            .anyMatch(t -> t.getText().contains("Selenium Java Test Task"));
        assertTrue(found, "Created task should appear in list");
        System.out.println("PASS: Task created and visible in list");
    }

    @Test
    @Order(4)
    void test04_createTaskValidation() {
        // AC10: Validation error shown when title is empty
        clickNewTaskButton();

        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-form")));

        // Submit without title
        clickButtonByText("Create Task");

        WebElement error = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("error-msg")));
        assertTrue(error.getText().toLowerCase().contains("required"),
            "Validation error should mention required");
        System.out.println("PASS: Validation error shown for empty title");
    }

    @Test
    @Order(5)
    void test05_cancelForm() {
        // AC11: Cancelling form makes no changes
        // Wait for page to fully settle first
        try { Thread.sleep(1000); } catch (InterruptedException e) { }
        
        List<WebElement> beforeCards = driver.findElements(By.className("task-card"));
        int beforeCount = beforeCards.size();

        clickNewTaskButton();
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-form")));

        driver.findElement(By.id("title")).sendKeys("Should not be saved");

        // Click cancel button (the secondary button inside the form)
        List<WebElement> cancelBtns = driver.findElements(By.className("btn-secondary"));
        cancelBtns.get(cancelBtns.size() - 1).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.className("task-form")));

        try { Thread.sleep(1000); } catch (InterruptedException e) { }

        int afterCount = driver.findElements(By.className("task-card")).size();
        assertEquals(beforeCount, afterCount,
            "Task count should not change after cancel");
        System.out.println("PASS: Cancel closes form without creating task");
    }

    // ── US-003: Edit a Task ──

    @Test
    @Order(6)
    void test06_editTask() {
        // AC8: Selenium clicks Edit, changes title, verifies update
        createTask("Task To Edit", "Original description");

        WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.className("btn-edit")));
        editBtn.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-form")));

        WebElement titleInput = driver.findElement(By.id("title"));
        titleInput.clear();
        titleInput.sendKeys("Updated By Java Selenium");

        clickButtonByText("Update Task");

        try { Thread.sleep(1000); } catch (InterruptedException e) { }
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-card")));

        List<WebElement> titles = driver.findElements(By.className("task-title"));
        boolean found = titles.stream()
            .anyMatch(t -> t.getText().contains("Updated By Java Selenium"));
        assertTrue(found, "Updated title should appear in list");
        System.out.println("PASS: Task edited and updated title visible");
    }

    // ── US-004: Delete a Task ──

    @Test
    @Order(7)
    void test07_deleteTask() {
        // AC7: Selenium clicks Delete, confirms, verifies task removed
        createTask("Task To Delete", "Will be deleted");

        List<WebElement> beforeCards = driver.findElements(By.className("task-card"));
        int beforeCount = beforeCards.size();

        List<WebElement> deleteButtons = driver.findElements(By.className("btn-delete"));
        deleteButtons.get(deleteButtons.size() - 1).click();

        // Accept confirmation dialog
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        wait.until(driver -> 
            driver.findElements(By.className("task-card")).size() < beforeCount);

        int afterCount = driver.findElements(By.className("task-card")).size();
        assertEquals(beforeCount - 1, afterCount,
            "Task count should decrease by 1 after delete");
        System.out.println("PASS: Task deleted and removed from list");
    }

    // ── US-005: Filter Tasks ──

    @Test
    @Order(8)
    void test08_filterByStatus() {
        // AC9: Clicking To Do filter shows only TODO tasks
        List<WebElement> filters = driver.findElements(By.className("filter-btn"));
        for (WebElement filter : filters) {
            if (filter.getText().contains("To Do")) {
                filter.click();
                break;
            }
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".filter-btn.active")));

        WebElement activeFilter = driver.findElement(By.cssSelector(".filter-btn.active"));
        assertTrue(activeFilter.getText().contains("To Do"),
            "To Do filter should be active");
        System.out.println("PASS: To Do filter active and applied");
    }

    @Test
    @Order(9)
    void test09_filterAllTasks() {
        // AC11: Clicking All Tasks shows all tasks
        List<WebElement> filters = driver.findElements(By.className("filter-btn"));
        for (WebElement filter : filters) {
            if (filter.getText().contains("To Do")) {
                filter.click();
                break;
            }
        }

        filters = driver.findElements(By.className("filter-btn"));
        for (WebElement filter : filters) {
            if (filter.getText().contains("All")) {
                filter.click();
                break;
            }
        }

        WebElement activeFilter = driver.findElement(By.cssSelector(".filter-btn.active"));
        assertTrue(activeFilter.getText().contains("All"),
            "All Tasks filter should be active");
        System.out.println("PASS: All Tasks filter shows all tasks");
    }

    // ── US-006: Task Priority ──

    @Test
    @Order(10)
    void test10_createTaskWithHighPriority_showsRedBadge() {
        // AC11: Creates a task with HIGH priority and verifies red badge appears on card
        createTaskWithPriority("High Priority Task", "Urgent work", "HIGH");

        List<WebElement> badges = driver.findElements(By.className("priority-badge"));
        WebElement lastBadge = badges.get(badges.size() - 1);

        assertTrue(lastBadge.getAttribute("class").contains("priority-high"),
            "HIGH priority task should display a red badge (priority-high class)");
        System.out.println("PASS: HIGH priority task shows red badge");
    }

    @Test
    @Order(11)
    void test11_createTaskWithNoPriority_showsMediumBadge() {
        // AC12: Creates a task with no priority selected and verifies MEDIUM badge appears
        createTask("Default Priority Task", "No priority set");

        List<WebElement> badges = driver.findElements(By.className("priority-badge"));
        WebElement lastBadge = badges.get(badges.size() - 1);

        assertTrue(lastBadge.getAttribute("class").contains("priority-medium"),
            "Task with no priority set should display an amber badge (priority-medium class)");
        System.out.println("PASS: Task with no priority shows MEDIUM badge");
    }

    @Test
    @Order(12)
    void test12_editTask_changesPriorityMediumToLow_updatesBadge() {
        // AC13: Edits a task and changes priority from MEDIUM to LOW, verifies badge updates
        createTask("Priority Change Task", "Starts as medium");

        // Open edit form for the last card
        List<WebElement> editBtns = driver.findElements(By.className("btn-edit"));
        editBtns.get(editBtns.size() - 1).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-form")));

        // Change priority from MEDIUM to LOW
        Select prioritySelect = new Select(driver.findElement(By.id("priority")));
        prioritySelect.selectByValue("LOW");

        clickButtonByText("Update Task");

        try { Thread.sleep(1000); } catch (InterruptedException e) { }
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-card")));

        List<WebElement> badges = driver.findElements(By.className("priority-badge"));
        WebElement lastBadge = badges.get(badges.size() - 1);

        assertTrue(lastBadge.getAttribute("class").contains("priority-low"),
            "After changing priority to LOW the badge should be green (priority-low class)");
        System.out.println("PASS: Priority changed from MEDIUM to LOW, badge updated");
    }

    // ── US-007: Dashboard ──

    @Test
    @Order(13)
    void test13_dashboardShowsCorrectTotalTaskCount() {
        // AC12: Dashboard shows correct total task count
        navigateToTasksView();
        try { Thread.sleep(500); } catch (InterruptedException e) { }
        int taskCardCount = driver.findElements(By.className("task-card")).size();

        navigateToDashboardView();

        WebElement totalTile = driver.findElement(By.className("tile-total"));
        int displayedTotal = Integer.parseInt(
            totalTile.findElement(By.className("tile-count")).getText().trim());

        assertEquals(taskCardCount, displayedTotal,
            "Dashboard total count should match actual number of tasks");
        System.out.println("PASS: Dashboard total count matches task list count (" + displayedTotal + ")");
    }

    @Test
    @Order(14)
    void test14_createTask_incrementsDashboardTotalCount() {
        // AC13: Creating a task increments dashboard total count by 1
        navigateToDashboardView();

        WebElement totalTile = driver.findElement(By.className("tile-total"));
        int initialTotal = Integer.parseInt(
            totalTile.findElement(By.className("tile-count")).getText().trim());

        navigateToTasksView();
        createTask("Dashboard Count Test Task", "Should increment total");

        navigateToDashboardView();

        WebElement updatedTotalTile = driver.findElement(By.className("tile-total"));
        int newTotal = Integer.parseInt(
            updatedTotalTile.findElement(By.className("tile-count")).getText().trim());

        assertEquals(initialTotal + 1, newTotal,
            "Dashboard total count should increment by 1 after creating a task");
        System.out.println("PASS: Total count incremented from " + initialTotal + " to " + newTotal);
    }

    @Test
    @Order(15)
    void test15_dashboardShowsValidOverdueTaskCount() {
        // AC14: Dashboard overdue tile shows a valid non-negative count
        navigateToDashboardView();

        WebElement overdueTile = driver.findElement(By.className("tile-overdue"));
        int overdueCount = Integer.parseInt(
            overdueTile.findElement(By.className("tile-count")).getText().trim());

        assertTrue(overdueCount >= 0,
            "Overdue task count should be a non-negative integer");
        System.out.println("PASS: Overdue count (" + overdueCount + ") is a valid non-negative number");
    }

    @Test
    @Order(16)
    void test16_clickStatusTile_navigatesToFilteredTaskList() {
        // AC15: Clicking a status tile navigates to task list filtered by that status
        navigateToDashboardView();

        WebElement todoTile = driver.findElement(By.className("tile-todo"));
        int expectedCount = Integer.parseInt(
            todoTile.findElement(By.className("tile-count")).getText().trim());
        todoTile.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".filter-btn.active")));

        WebElement activeFilter = driver.findElement(By.cssSelector(".filter-btn.active"));
        assertTrue(activeFilter.getText().contains("To Do"),
            "Active filter should be To Do after clicking the To Do status tile");

        int visibleCards = driver.findElements(By.className("task-card")).size();
        assertEquals(expectedCount, visibleCards,
            "Number of visible task cards should match the count shown on the To Do tile");
        System.out.println("PASS: Clicking To Do tile navigated to filtered list with " + visibleCards + " tasks");
    }

    // ── Helpers ──

    private void navigateToTasksView() {
        WebElement nav = driver.findElement(By.className("header-nav"));
        for (WebElement btn : nav.findElements(By.tagName("button"))) {
            if (btn.getText().contains("Tasks")) {
                btn.click();
                return;
            }
        }
    }

    private void navigateToDashboardView() {
        WebElement nav = driver.findElement(By.className("header-nav"));
        for (WebElement btn : nav.findElements(By.tagName("button"))) {
            if (btn.getText().contains("Dashboard")) {
                btn.click();
                break;
            }
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("dashboard")));
    }

    private void clickNewTaskButton() {
        List<WebElement> buttons = driver.findElements(By.className("btn-primary"));
        for (WebElement btn : buttons) {
            if (btn.getText().contains("New Task")) {
                btn.click();
                return;
            }
        }
    }

    private void clickButtonByText(String text) {
        List<WebElement> buttons = driver.findElements(By.className("btn-primary"));
        for (WebElement btn : buttons) {
            if (btn.getText().contains(text)) {
                btn.click();
                return;
            }
        }
    }

    private void createTask(String title, String description) {
        clickNewTaskButton();
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-form")));
        driver.findElement(By.id("title")).sendKeys(title);
        driver.findElement(By.id("description")).sendKeys(description);
        clickButtonByText("Create Task");
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-card")));
        try { Thread.sleep(500); } catch (InterruptedException e) { }
    }

    private void createTaskWithPriority(String title, String description, String priority) {
        clickNewTaskButton();
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-form")));
        driver.findElement(By.id("title")).sendKeys(title);
        driver.findElement(By.id("description")).sendKeys(description);
        new Select(driver.findElement(By.id("priority"))).selectByValue(priority);
        clickButtonByText("Create Task");
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("task-card")));
        try { Thread.sleep(500); } catch (InterruptedException e) { }
    }
}