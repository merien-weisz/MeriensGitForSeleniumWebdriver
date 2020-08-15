package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AlphabeticalCountriesTest {

    public WebDriver driver;
    public WebDriverWait wait;

    public boolean checkElementsAlphabetical(String current) {
        String previous = "";
        if (current.compareTo(previous) < 0) {
            previous = current;
            return false;
        } else {
            previous = current;
            return true;
        }
    }

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void alphabeticalCountriesTest() throws InterruptedException {
        driver.navigate().to("http://localhost:8080/litecart/admin/login.php");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();

        driver.navigate().to("http://localhost:8080/litecart/admin/?app=countries&doc=countries");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dataTable")));

        if (driver.findElement(By.id("content")).isDisplayed()) {
            var countries = driver.findElements(By.className("row"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("row")));

            for (WebElement country : countries) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", country);
                WebElement namePath = country.findElement(By.xpath("//td[5]/a[contains(@href, '/litecart/admin/?app=countries&doc=edit_country')]"));
                Assert.assertTrue(checkElementsAlphabetical(namePath.getText()));

                int zonesCount = Integer.parseInt(country.findElement(By.xpath("//td[6]")).getText());

                if (zonesCount != 0) {
                    WebElement button = country.findElement(By.xpath("//a[contains (@title, 'Edit')]"));
                    button.click();

                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dataTable")));
                    if (driver.findElement(By.id("content")).isDisplayed()) {
                        var zones = driver.findElements(By.xpath("//table[@class='dataTable']//td[3]/input[contains(@name, 'zones')]"));

                        for (WebElement zone : zones) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", zone);
                            String zoneName = zone.getText();
                            Assert.assertTrue(checkElementsAlphabetical(zoneName));
                        }
                        driver.navigate().back();
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