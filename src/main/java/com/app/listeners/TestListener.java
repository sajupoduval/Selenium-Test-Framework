package com.app.listeners;

import com.app.base.BaseClass;
import com.app.utilities.ExtentManager;
import com.app.utilities.RetryAnalyzer;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListener implements ITestListener, IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
//        IAnnotationTransformer.super.transform(annotation, testClass, testConstructor, testMethod);
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    @Override
    public void onStart(ITestContext context) {
    //        Initialize the Extent reports
        ExtentManager.getReporter();
    //        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
    // Flush Extent reports
        ExtentManager.endTest();
    //        ITestListener.super.onFinish(context);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.logSkip("Test Skipped"+testName);
//        ITestListener.super.onTestSkipped(result);
    }

    //Triggered when test fails
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String failureMessage = result.getThrowable().getMessage();
        ExtentManager.logStep(failureMessage );
        ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed!", "Test End: " + testName + " - ❌ Test Failed");
//        ITestListener.super.onTestFailure(result);
    }

    //Triggered when a test succeeds
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
//        if (!result.getTestClass().getName().toLowerCase().contains("api")) {
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed Successfully!",
                    "Test End: " + testName + " - ✔ Test Passed");
//        } else {
//            ExtentManager.logStepValidationForAPI("Test End: " + testName + " - ✔ Test Passed");
//        }//        ITestListener.super.onTestSuccess(result);
    }

    //Triggered when a test starts
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        //Start logging in Extent reports
        ExtentManager.startTest(testName);
        ExtentManager.logStep("Test Started: "+testName);
//        ITestListener.super.onTestStart(result);
    }
}
