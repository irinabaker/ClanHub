package ClanHub.pages;

import ClanHub.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class DashBoardPage extends BasePage {

    public DashBoardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @FindBy(className = "_addTaskButton_pc5kb_15")
    WebElement addTaskBtn;

    @FindBy(className = "_taskTitleInput_18fp3_18")
    WebElement taskTitleInput;

    @FindBy(className = "_dateInput_18fp3_54")
    WebElement dateInput;

    @FindBy(className = "_checkmarkIcon_18fp3_103")
    WebElement checkmarkIcon;

    public DashBoardPage createTask(String taskName, String dueDate) {
        click(addTaskBtn);
        type(taskTitleInput, taskName);
        Actions action = new Actions(driver);
        action.moveToElement(dateInput);
        action.click();
        action.sendKeys(dueDate).build().perform();
        click(checkmarkIcon);
        clickOkBtn();
        return this;
    }

    @FindBy(css = "[class*='okButton']")
    WebElement okBtn;

    @FindBy(css = "[class*='yesButton']")
    WebElement yesBtn;

    public DashBoardPage viewTheTask(String taskName) {
        List<WebElement> taskRows = driver.findElements(By.cssSelector("div._taskRow_pc5kb_68"));
        for (WebElement taskRow : taskRows) {
            if (taskRow.getText().contains(taskName)) {
                WebElement viewButton = taskRow.findElement(By.cssSelector("img[alt='view']"));
                viewButton.click();
                break;
            }
        }
        return this;
    }

    @FindBy(className = "_modal_pc5kb_154")
    WebElement modalWindow;

    public void verifyPopupWindow() {
        Assert.assertTrue(isElementPresent(modalWindow));
    }

    public DashBoardPage clickOkBtn() {
        click(okBtn);
        return this;
    }

    public void clickYesBtn() {
        click(yesBtn);
    }

    public void verifyPopupWindowIsClosed() {
        List<WebElement> modal = driver.findElements(By.cssSelector("[class*='modal']"));
        Assert.assertTrue(modal.isEmpty());
    }

    public DashBoardPage deleteTheTask(String taskName) {
        List<WebElement> taskRows = driver.findElements(By.cssSelector("div._taskRow_pc5kb_68"));
        for (WebElement taskRow : taskRows) {
            if (taskRow.getText().contains(taskName)) {
                WebElement deleteButton = taskRow.findElement(By.cssSelector("img[alt='delete']"));
                deleteButton.click();
                clickYesBtn();
                wait.until(ExpectedConditions.invisibilityOf(taskRow));
                break;
            }
        }
        return this;
    }

    public void verifyTaskIsDeleted(String taskName) {
        List<WebElement> tasks = driver.findElements(By.cssSelector("div._taskRow_pc5kb_68"));
        boolean taskStillVisible = tasks.stream()
                .anyMatch(task -> task.getText().contains(taskName));

        Assert.assertFalse(taskStillVisible, "⚠️ The task is still visible but must be deleted!");
    }

    public DashBoardPage updateTheTaskStatus(String taskTitle) {
        List<WebElement> taskRows = driver.findElements(By.cssSelector("div._taskRow_pc5kb_68"));
        for (WebElement taskRow : taskRows) {
            if (taskRow.getText().contains(taskTitle)) {
                WebElement completeButton = taskRow.findElement(By.cssSelector("img[alt='complete']"));
                completeButton.click();
                clickYesBtn();
                wait.until(ExpectedConditions.invisibilityOf(taskRow));
                break;
            }
        }
        return this;
    }

    @FindBy(className = "_completedButton_pc5kb_125")
    WebElement completedButton;

    @FindBy(css = "[class*='today']")
    WebElement todayBtn;

    @FindBy(className = "task-list")
    WebElement taskList;

    public void verifyTaskIsCompleted(String taskTitle) {
        click(completedButton);
        click(todayBtn);
        Assert.assertTrue(taskList.getText().contains(taskTitle));
    }

    public DashBoardPage closeModals() {
        try {
            wait.withTimeout(Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("_modal_pc5kb_154")));
            clickOkBtn();
        } catch (TimeoutException e) {
            // ignore
        } finally {
            wait.withTimeout(Duration.ofSeconds(5));
        }
        return this;
    }
}
