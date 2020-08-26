package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;

public class CartOperationsTest {

    public WebDriver driver;
    public WebDriverWait wait;

    boolean isElementPresent(WebDriver driver, By locator) {
        return driver.findElements(locator).size() > 0;
    }

    @Before
    public void start() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "eager");
        driver = new ChromeDriver(capabilities);
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void test() {

        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        driver.navigate().to("https://litecart.stqa.ru/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content")));

        for (int i = 1; i <= 3; i++) {

            WebElement product = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@id='box-most-popular']//li[@class='product column shadow hover-light'][" + i + "]")));

            product.click();
            wait.until(stalenessOf(product));

            WebElement quantity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("quantity")));

            if (isElementPresent(driver, By.xpath("//select[contains(@name, 'options')]"))) {
                Select options = new Select(driver.findElement(By.name("options[Size]")));
                options.selectByIndex(1);
            }

            WebElement addToCartButton = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@name='add_cart_product']")));
            addToCartButton.click();

            wait.until(ExpectedConditions.textToBe(By.className("quantity"), String.valueOf(i)));

            driver.navigate().back();
        }

        WebElement linkCart = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='link' and contains(.,'Checkout')]")));
        linkCart.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='shortcuts']")));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        var elements = driver.findElements(By.xpath("//li[@class='shortcut']"));

        for (int i = 1; i <= elements.size(); i++) {
            if (i != elements.size()) {
                driver.findElement(By.xpath("//li[@class='shortcut'][1]")).click();
            }
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@name='remove_cart_item']")));
            driver.findElement(By.xpath("//button[@name='remove_cart_item']")).click();

            wait.until(stalenessOf(driver.findElement(By.xpath("//table[@class='dataTable rounded-corners']//tr[2]"))));
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
