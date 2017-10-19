package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.Properties;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PlaceOrderTest extends BaseTest {
    public ProductData product;

    @Test
    public void checkSiteVersion() {
        // TODO open main page and validate website version
        driver.get(Properties.getBaseUrl());
        SoftAssert softAssert = new SoftAssert();
        if (isMobileTesting) {
            softAssert.assertTrue(driver.findElement(By.xpath("//*[@id='_mobile_cart']/div/div/i")).isDisplayed());
            softAssert.assertTrue(driver.findElement(By.cssSelector("#menu-icon > i")).isDisplayed());
            softAssert.assertAll();

        } else {
            softAssert.assertTrue(driver.findElement(By.cssSelector("#_desktop_currency_selector > div")).isDisplayed());
            softAssert.assertTrue(driver.findElement(By.xpath("//*[@id='carousel']/ul/li[1]/figure/img")).isDisplayed());
            softAssert.assertAll();
        }
    }

    @Test
    public void createNewOrder() {
        SoftAssert softAssert = new SoftAssert();
        // TODO implement order creation test
        if (!isMobileTesting) {
            actions.allProductsPage();
        }

        // open random product
        actions.openRandomProduct();

        // save product parameters
        actions.getOpenedProductInfo();
        actions.goToBasket();

        softAssert.assertEquals(driver.findElement(By.cssSelector("a.label:nth-child(1)")).getText().toLowerCase(),
                actions.name.toLowerCase());
        softAssert.assertEquals(DataConverter.parsePriceValue(driver.findElement(By.cssSelector(
                "span.value:nth-child(1)")).getText()), actions.price);
        //System.out.println(driver.findElement(By.cssSelector(".js-cart-line-product-quantity")).getText());
        softAssert.assertEquals(driver.findElement(By.cssSelector(".js-cart-line-product-quantity")).getAttribute(
                "value"), "1");
        softAssert.assertAll();

    }

    @Test(dependsOnMethods = "createNewOrder")
    public void fillInOrderInfo() {
        actions.fillRequiredFields();
        SoftAssert softAssert = new SoftAssert();
        String success_order_msg = driver.findElement(By.xpath(
                "//*[@id='content-hook_order_confirmation']/div/div/div/h3")).getText();

        softAssert.assertEquals(success_order_msg.substring(1).toLowerCase(), "Ваш заказ подтверждён".toLowerCase());
        softAssert.assertEquals(DataConverter.parsePriceValue(driver.findElement(By.xpath(
                "//*[@id='order-items']/div/div/div[3]/div/div[1]")).getText()), actions.price);
        String output = driver.findElement(By.xpath("//*[@id='order-items']/div/div/div[2]/span")).getText();
        String [] name = output.split(" -");
        softAssert.assertEquals(name[0].toLowerCase(),actions.name.toLowerCase());
        softAssert.assertEquals(driver.findElement(By.xpath(
                "//*[@id='order-items']/div/div/div[3]/div/div[2]")).getText(), "1");

        driver.get(Properties.getBaseUrl());
        if (!isMobileTesting) {
            actions.allProductsPage();
        }
        actions.openProductPage();
        String qnt = driver.findElement(By.cssSelector(".product-quantities > span:nth-child(2)")).getText();
        softAssert.assertEquals(DataConverter.parseStockValue(qnt), actions.quantity-1);
        softAssert.assertAll();

    }

}
