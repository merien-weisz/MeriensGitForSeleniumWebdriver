package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AddNewProductTest {


    public WebDriver driver;
    public WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void addProductTest() {

        driver.navigate().to("http://localhost:8080/litecart/admin/login.php");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        driver.navigate().to("http://localhost:8080/litecart/admin/?app=catalog&doc=catalog");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));

        //go to add new product
        driver.findElement(By.xpath("//a[@class='button' and contains(.,' Add New Product')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tab-general")));

        //General
        Random rand = new Random();
        String name = "Vader Duck" + ((char)rand.nextInt(10) + 'a');
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(By.xpath("//input[@name='code']")).sendKeys("123123");
        driver.findElement(By.xpath("//input[@name='name[en]']")).isDisplayed();
        driver.findElement(By.xpath("//input[@name='name[en]']")).sendKeys(name);
        driver.findElement(By.xpath("//input[@type='checkbox' and @data-name='Root']")).click();
        driver.findElement(By.xpath("//input[@type='checkbox' and @data-name='Rubber Ducks']")).click();
        driver.findElement(By.xpath("//input[@name='product_groups[]' and @value='1-3']")).click();
        driver.findElement(By.xpath("//input[@name='quantity']")).sendKeys("100");
        driver.findElement(By.xpath("//input[@name='status' and @value='1']")).click();

        File file = new File("src/test/java/files/VaderDuck.jpg");
        driver.findElement(By.xpath("//input[@name='new_images[]']")).sendKeys(file.getAbsolutePath());

        //Information
        driver.findElement(By.xpath("//a[@href='#tab-information']")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//input[@name='short_description[en]']")).sendKeys("Vader Duck");
        driver.findElement(By.xpath("//div[@class='trumbowyg-editor']")).sendKeys("Vader Duck");

        //Prices
        driver.findElement(By.xpath("//a[@href='#tab-prices']")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//input[@name='purchase_price']")).sendKeys("10");
        Select priceCode = new Select(driver.findElement(By.xpath("//select[@name='purchase_price_currency_code']")));
        priceCode.selectByValue("USD");

        //SAVING
        driver.findElement(By.xpath("//button[@name='save']")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //check product exists
        driver.findElement(By.xpath("//tr[@class='row']")).isDisplayed();
        Assert.assertTrue(driver.findElement(By.xpath("//tr[@class='row' and contains(.,'" + name + "')]")).isDisplayed());
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}