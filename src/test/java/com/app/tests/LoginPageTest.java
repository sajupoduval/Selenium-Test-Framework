package com.app.tests;

import com.app.base.BaseClass;
import com.app.pages.HomePage;
import com.app.pages.LoginPage;
import com.app.utilities.DataProviders;
import com.app.utilities.ExtentManager;
import com.app.utilities.RetryAnalyzer;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import javax.naming.ldap.ExtendedRequest;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
    public void verifyValidLoginTest(String username, String password){
//        ExtentManager.startTest("valid login test"); --This has been implemented in TestListener

//        SoftAssert softAssert = getSoftAssert();
        ExtentManager.logStep("Navigating to Login page entering username and password");
        loginPage.login(username, password);
        ExtentManager.logStep("Verifying Admin tab is visible or not");
        Assert.assertTrue(homePage.isAdminTabAvailable(),"Admin tab should be visible");
//        softAssert.assertTrue(homePage.isAdminTabAvailable(),"Admin tab should be visible");
        ExtentManager.logStep("Validation Successful");
        homePage.logout();
        ExtentManager.logStep("Logged out successfully");
        staticWait(10);
//        softAssert.assertAll();
    }

    @Test(dataProvider="InValidLoginData", dataProviderClass = DataProviders.class,retryAnalyzer =  RetryAnalyzer.class)
    public void invalidLoginTest(String username, String password){
//        ExtentManager.startTest("Invalid login test"); --This has been implemented in TestListener
        SoftAssert softAssert = getSoftAssert();
        ExtentManager.logStep("Navigating to Login page entering invalid username and password");
        loginPage.login(username, password);
        String expectedErrorMessage = "Invalid credentials";
//        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test Failed: Invalid error message");
        softAssert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test Failed: Invalid error message");

        ExtentManager.logStep("Validation Successful");
        ExtentManager.logStep("Logged out successfully");
        softAssert.assertAll();
    }
}
