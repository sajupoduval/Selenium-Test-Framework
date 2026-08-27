package com.app.tests;

import com.app.base.BaseClass;
import com.app.utilities.ExtentManager;
import com.app.utilities.RetryAnalyzer;
import org.testng.annotations.Test;

import java.util.Objects;

public class DummyClass2 extends BaseClass {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void DummyTest2(){
//        ExtentManager.startTest("Start dummy test2"); --This has been implemented in TestListener
        String title = getDriver().getTitle();
        ExtentManager.logStep("Verifying the dummy test2");

//        assert title.equals("OrangeHRM"):"Test Failed - Title is not matching";
        assert Objects.equals(title, "OrangeHRM") :"Test Failed - Title is not matching";
        System.out.println("Test2 Passed - Title is matching");
        ExtentManager.logStep("Validation Successful");
    }
}
