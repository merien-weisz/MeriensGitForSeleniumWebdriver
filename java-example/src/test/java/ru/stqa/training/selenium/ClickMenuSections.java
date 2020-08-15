package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClickAllSectionsTest {

    public WebDriver driver;
    public WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void clickAllSectionsTest() {
        String sideBarSubTitle = "//h1";

        driver.navigate().to("http://localhost:8080/litecart/admin/login.php");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));

        var menuSections = driver.findElements(By.id("//ul/li[@id='app-']"));

        if (driver.findElement(By.id("content")).isDisplayed() && driver.findElement(By.id("sidebar")).isDisplayed()) {
            for (WebElement section : menuSections) {

                wait.until(ExpectedConditions.elementToBeClickable(section));
                section.click();

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sideBarSubTitle)));
                boolean condition = driver.findElement(By.xpath(sideBarSubTitle)).isDisplayed();
                Assert.assertTrue(condition);

                String countSubSections = section.findElement(By.xpath("count(/ul[@class='docs']/li[contains(@id, 'doc-')])")).toString();

                if (section.getAttribute("class").equals("selected") &&
                        (Integer.parseInt(countSubSections) != 0))
                {
                    var subSections = section.findElements(By.xpath("/ul[@class='docs']/li[contains(@id, 'doc-')]"));
                    for (WebElement subSection : subSections) {
                        wait.until(ExpectedConditions.elementToBeClickable(subSection));
                        subSection.click();
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sideBarSubTitle)));
                        Assert.assertTrue(condition);
                    }
                }
            }
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}