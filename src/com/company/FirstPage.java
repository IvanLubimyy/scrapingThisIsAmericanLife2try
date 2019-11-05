package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FirstPage {

    private WebDriver driver;

    public FirstPage(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }


    public void waiter(String SomeLocatorByXpath){
        WebDriverWait waitForOne = new WebDriverWait(driver, 10);
        waitForOne.until(ExpectedConditions.presenceOfElementLocated(By.xpath(SomeLocatorByXpath)));
    }
}



