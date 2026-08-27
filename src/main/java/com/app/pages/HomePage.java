package com.app.pages;

import com.app.actiondriver.ActionDriver;
import com.app.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private ActionDriver actionDriver;

    private By adminTab = By.xpath("//span[text() = 'Admin']");
    private By userIDButton = By.className("oxd-userdropdown-name");
    private By logoutButton = By.xpath("//a[text() = 'Logout']");
    private By orangeHRMlogo = By.xpath("//div[@class = 'oxd-brand-banner']//img");

    public HomePage(WebDriver driver){
//        this.actionDriver = new ActionDriver(driver);
        this.actionDriver = BaseClass.getActionDriver();
    }

    public boolean isAdminTabAvailable(){
        return actionDriver.isDisplayed(adminTab);
    }

    public boolean verifyOrangeHRMlogo(){
        return actionDriver.isDisplayed(orangeHRMlogo);
    }

    public void logout(){
        actionDriver.click(userIDButton);
        actionDriver.click(logoutButton);
    }

}
