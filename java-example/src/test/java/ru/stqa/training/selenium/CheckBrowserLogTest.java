package ru.stqa.training.selenium;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;

public class CheckBrowserLogTest {

    public WebDriver driver;
    public WebDriverWait wait;

    boolean isElementPresent(WebDriver driver, By locator) {
        return driver.findElements(locator).size() > 0;
    }

    @Before
    public void start() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void test() throws InterruptedException {
        driver.navigate().to("http://localhost:8080/litecart/admin/login.php");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();

        driver.navigate().to("http://localhost:8080/litecart/admin/?app=catalog&doc=catalog&category_id=2");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));

        if (isElementPresent(driver, By.id("content"))) {
            var rows =
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//tr[@class='row']/td[3]/a")));

            for (WebElement row : rows) {
                row.click();

                driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
                ArrayList<LogEntry> logs = new ArrayList<>(driver.manage().logs().get("browser").getAll());

                if (logs.isEmpty()) {
                    System.out.println("Логи браузера пусты");
                } else {
                    System.out.println(logs);
                }
                driver.navigate().back();
            }
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
