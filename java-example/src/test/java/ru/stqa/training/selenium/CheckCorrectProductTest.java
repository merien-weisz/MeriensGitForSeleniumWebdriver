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

import java.util.concurrent.TimeUnit;

public class CheckProductValuesTest {

    public WebDriver driver;
    public WebDriverWait wait;

    public boolean checkBiggerValue(float bigger, float smaller) {
        return (bigger > smaller);
    }

    public float stringToFloat(String value) {
        String valueFormatted = value.substring(0, value.length() - 2);
        return Float.parseFloat(valueFormatted);
    }

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void checkProductValuesTest() {

        driver.navigate().to("http://localhost:8080/litecart");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content")));

        if (driver.findElement(By.className("content")).isDisplayed()) {

            var product = driver.findElement(By.xpath("//div[@id='box-campaigns']//li[@class='product column shadow hover-light'][1]"));

            String productName = product.findElement(By.className("name")).getText();

            // а)
            // б)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", product);
            product.click();

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            Assert.assertEquals(driver.findElement(By.xpath("//h1[@itemprop='name']")).getText(), productName);
            Assert.assertEquals(driver.findElement(By.className("regular-price")).getText(), productRegularPriceValue);
            Assert.assertEquals(driver.findElement(By.className("campaign-price")).getText(), productCampaignPriceValue);

            WebElement productRegularPrice = product.findElement(By.className("regular-price"));
            String productRegularPriceValue = productRegularPrice.getText();

            //в)
            Assert.assertTrue(productRegularPrice.getCssValue("color")
                    .matches("^rgba\\((\\d{1,3}), (\\1\\b), (\\1\\b), (\\d*)\\)$"));
            Assert.assertEquals("line-through solid rgb(119, 119, 119)", productRegularPrice.getCssValue("text-decoration"));
            Assert.assertEquals("400", productRegularPrice.getCssValue("font-weight"));
            Assert.assertEquals("14.4px", productRegularPrice.getCssValue("font-size"));

            WebElement productCampaignPrice = product.findElement(By.className("campaign-price"));
            String productCampaignPriceValue = productCampaignPrice.getText();

            // г)
            Assert.assertTrue(productCampaignPrice.getCssValue("color")
                    .matches("^rgba\\((\\d{1,3}), ([0]), ([0]), (\\d*)\\)$")); //проверяем соответствие паттерну через regex
            Assert.assertEquals("none solid rgb(204, 0, 0)", productCampaignPrice.getCssValue("text-decoration"));
            Assert.assertEquals("700", productCampaignPrice.getCssValue("font-weight"));
            Assert.assertEquals("18px", productCampaignPrice.getCssValue("font-size"));

            // д)
            Assert.assertTrue(checkBiggerValue(
                    stringToFloat(productCampaignPrice.getCssValue("font-size")),
                    stringToFloat(productRegularPrice.getCssValue("font-size"))
                    )
            );
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}