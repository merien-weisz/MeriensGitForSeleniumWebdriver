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

public class AlphabeticalGeoZonesTest {

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
    public void alphabeticalGeoZonesTest() throws InterruptedException {
        driver.navigate().to("http://localhost:8080/litecart/admin/login.php");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();

        driver.navigate().to("http://localhost:8080/litecart/admin/?app=geo_zones&doc=geo_zones");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dataTable")));

        if (driver.findElement(By.id("content")).isDisplayed()) {
            var countries = driver.findElements(By.xpath("//a[contains (@title, 'Edit')]"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("row")));

            for (WebElement country : countries) {

                country.click();

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dataTable")));
                var zones =
                        driver.findElements(By.xpath("//select[contains(@name, '[zone_code]')]/option[@selected='selected']"));

                for (WebElement zone : zones) {
                    String zoneName = zone.getText();
                    Assert.assertTrue(checkElementsAlphabetical(zoneName));
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