package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CheckOpenedLinksTest {

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

        driver.navigate().to("http://localhost:8080/litecart/admin/?app=countries&doc=edit_country");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));

        if (isElementPresent(driver, By.id("content"))) {
            var links =
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("[class='fa fa-external-link']")));

            for (WebElement link : links) {
                String mainWindow = driver.getWindowHandle();
                String newWindow = "";
                link.click();

                driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
                Set<String> windows = driver.getWindowHandles();
                for (String window : windows) {
                    if (!window.equals(mainWindow)) {
                        newWindow = window;
                        break;
                    }
                }
                driver.switchTo().window(newWindow);
                Assert.assertEquals(newWindow, driver.getWindowHandle()); //проверим, что текущее окно - новое
                driver.close();

                driver.switchTo().window(mainWindow);
                Assert.assertEquals(mainWindow, driver.getWindowHandle()); //проверим, что текущее окно - изначальное
            }
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}