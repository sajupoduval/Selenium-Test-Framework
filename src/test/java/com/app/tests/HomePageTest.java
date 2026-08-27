package com.app.tests;

import com.app.base.BaseClass;
import com.app.pages.HomePage;
import com.app.pages.LoginPage;
import com.app.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void verifyOrangeHRMLogo(){
//        ExtentManager.startTest("Home page verify logo test"); --This has been implemented in TestListener
        ExtentManager.logStep("Navigating to Login page entering invalid username and password");
        loginPage.login("Admin", "admin123");
        Assert.assertTrue(homePage.verifyOrangeHRMlogo(),"Logo is not visible");
        ExtentManager.logStep("Validation Successful");
        ExtentManager.logStep("Logged out successfully");
    }
}
