package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
/*
Class which can wait fool download the FirstPage
 */
public class FirstPage {

    private WebDriver driver;

    public FirstPage(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

/*
method can wait SomeLocatorByXpath on the page
 */
    public void waiter(String SomeLocatorByXpath){
        WebDriverWait waitForOne = new WebDriverWait(driver, 10);
        waitForOne.until(ExpectedConditions.presenceOfElementLocated(By.xpath(SomeLocatorByXpath)));
    }
}



