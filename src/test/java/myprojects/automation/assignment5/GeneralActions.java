package myprojects.automation.assignment5;


//import com.sun.xml.internal.ws.server.ServerRtException;
import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.Properties;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Random;

import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    public ProductData pr;
    public float price;
    public int quantity;
    public String name;




    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void allProductsPage() {


        WebElement allProductsLink = driver.findElement(By.xpath("//*[@id='content']/section/a"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click()", allProductsLink);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id='main']/div[1]/h1"))));

    }

    public void openRandomProduct() {
        // TODO implement logic to open random product before purchase
        Random random = new Random();
        List<WebElement> elements = driver.findElements(By.xpath("//*[@class='thumbnail product-thumbnail']"));
        //System.out.println(elements);
        int elem_number = random.nextInt(elements.size());
        if (elem_number==0) {elem_number++;}
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click()", elements.get(elem_number));
        //driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector(
                ".btn.btn-primary.add-to-cart"))));
    }

    /**
     * Extracts product information from opened product details page.
     *
     * @return
     */
    public void getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");
        name = driver.findElement(By.cssSelector(".h1")).getText();
        String pr_price = driver.findElement(By.cssSelector(".current-price > span:nth-child(1)")).getText();

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("scroll(0, 350)");
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("li.nav-item:nth-child(2) > a:nth-child(1)"))));

        driver.findElement(By.cssSelector("li.nav-item:nth-child(2) > a:nth-child(1)")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".h6"))));
        String pr_quantity = driver.findElement(By.cssSelector(".product-quantities > span:nth-child(2)")).getText();

        price = DataConverter.parsePriceValue(pr_price);
        quantity = DataConverter.parseStockValue(pr_quantity);

        //return pr;
    }

    public void goToBasket() {
        driver.findElement(By.xpath("//*[@id='add-to-cart-or-refresh']/div[2]/div[1]/div[2]/button")).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='blockcart-modal']/div/div/div[2]/div/div[2]/div/button"))));
        driver.findElement(By.xpath("//*[@id='blockcart-modal']/div/div/div[2]/div/div[2]/div/a")).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("i.pull-xs-left"))));
    }

    public void fillRequiredFields() {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click()", driver.findElement(By.xpath("//*[@class='btn btn-primary']")));

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@class='custom-radio']"))));
        driver.findElement(By.name("firstname")).sendKeys("Tetiana");
        driver.findElement(By.name("lastname")).sendKeys("Grotan");
        driver.findElement(By.name("email")).sendKeys("grotan@test.mail.com");
        executor.executeScript("arguments[0].click()",driver.findElement(By.xpath("//*[@id='customer-form']/footer/button")));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.name("confirm-addresses"))));
        driver.findElement(By.name("address1")).sendKeys("Soborna, 1");
        driver.findElement(By.name("postcode")).sendKeys("33000");
        driver.findElement(By.name("city")).sendKeys("Rivne");
        executor.executeScript("arguments[0].click()", driver.findElement(By.name("confirm-addresses")));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.name("confirmDeliveryOption"))));
        driver.findElement(By.name("confirmDeliveryOption")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#checkout-payment-step > h1:nth-child(1)"))));
        driver.findElement(By.id("payment-option-2")).click();
        executor.executeScript("arguments[0].click()", driver.findElement(By.id("conditions_to_approve[terms-and-conditions]")));
        executor.executeScript("arguments[0].click()", driver.findElement(By.xpath("//*[@id='payment-confirmation']/div[1]/button")));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id='content-hook_order_confirmation']/div/div/div/h3"))));
    }

    public String capitalizeFirstLetter(String str) {
        str = str.trim().toLowerCase();
        String data[] = str.split("\\s");
        str = "";
        for(int i =0;i< data.length;i++)
        {
            if(data[i].length()>1)
                str = str + data[i].substring(0,1).toUpperCase()+data[i].substring(1)+" ";
            else
                str = str + data[i].toUpperCase();
        }
        System.out.println(str);
        return str.trim();
    }

    public void openProductPage() {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click()", driver.findElement(By.xpath("//*[contains(text(),'"+capitalizeFirstLetter(name)+"')]")));
        executor.executeScript("scroll(0, 350)");
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("li.nav-item:nth-child(2) > a:nth-child(1)"))));

        driver.findElement(By.cssSelector("li.nav-item:nth-child(2) > a:nth-child(1)")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".h6"))));

    }
}
