package com.app.pages;

import com.app.actiondriver.ActionDriver;
import com.app.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {


//button[contains(normalize-space(.), 'Login')]
// button[text() = ' Login ')]
//p[text() = 'Invalid credentials']

    private ActionDriver actionDriver;

    private By usernameField = By.name("username");
//    private By passwordField = By.cssSelector("//input[@name='password']");
    private By passwordField = By.name("password");
    private By loginButton =   By.xpath("//button[contains(normalize-space(.), 'Login')]");
    private By errorMessage =  By.xpath("//p[text() = 'Invalid credentials']");


    public LoginPage(WebDriver driver){
//        this.actionDriver = new ActionDriver(driver);
        this.actionDriver = BaseClass.getActionDriver();
    }

    public void login(String username, String password){
        actionDriver.enterText(usernameField, username);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);
    }

    public boolean isErrorMessageDisplayed(){
        return actionDriver.isDisplayed(errorMessage);
    }

    public String getErrorMessageText(){
        return actionDriver.getText(errorMessage);
    }

    public boolean verifyErrorMessage(String expectedError){
         return actionDriver.compareText(errorMessage, expectedError);
    }



}
