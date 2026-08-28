package com.app.tests;

import com.app.base.BaseClass;
import com.app.utilities.ExtentManager;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.Objects;

public class DummyClass extends BaseClass {

    @Test
    public void DummyTest(){
//        ExtentManager.startTest("Start dummy test"); --This has been implemented in TestListener
        String title = getDriver().getTitle();
        ExtentManager.logStep("Verifying the test");
        assert title.equals("OrangeHRM"):"Test Failed - Title is not matching";
        assert Objects.equals(title, "OrangeHRM") :"Test Failed - Title Object is not matching";

        System.out.println("Test Passed - Title is matching");
//        ExtentManager.logSkip("This test case is skipped");
//        throw new SkipException("Skipping the test as part of Testing");
    }
}
