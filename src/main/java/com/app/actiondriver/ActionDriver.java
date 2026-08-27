package com.app.actiondriver;

import com.app.base.BaseClass;
import com.app.utilities.ExtentManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;
    public static final Logger Logger = BaseClass.Logger;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        Logger.info("Webdriver instance is created");
    }

    public void click(By by){
        String elementDescription  = getElementDescription(by);
        try{
            applyBorder(by,"green");
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            ExtentManager.logStep("Clicked the element: " + elementDescription);
            Logger.info("Clicked the element: " + elementDescription);
        }
        catch (Exception e){
            applyBorder(by,"red");
            System.out.println("Unable to click element:" + e.getMessage());
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element:", elementDescription+"unable to click" );
            Logger.info("Unable to click the element");
        }
    }

    public void enterText(By by, String value){
       try {
           waitForElementToBeVisible(by);
           applyBorder(by,"green");
           WebElement element = driver.findElement(by);
//           String elementDescription  = getElementDescription(by);
//        driver.findElement(by).clear();
//        driver.findElement(by).sendKeys(value);
           element.clear();
           element.sendKeys(value);
           Logger.info("Entered the text:" + value + " in the Field: " + getElementDescription(by));
       } catch (Exception exp){
           applyBorder(by,"red");
           System.out.println("Unable to enter the value:" + exp.getMessage());
           Logger.error("Unable to enter the value:" + exp.getMessage());
       }
    }

    public String getText(By by){
        try{
            waitForElementToBeVisible(by);
            applyBorder(by,"green");
            return driver.findElement(by).getText();
        }
        catch(Exception e){
            e.printStackTrace();
            applyBorder(by,"red");
//            System.out.println("Unable to get text:" + e.getMessage());
            Logger.error("Unable to get text:" + e.getMessage());
            return "";
        }
    }

    public boolean compareText(By by, String expectedText){
        try{
            waitForElementToBeVisible(by);
            applyBorder(by,"green");
            String actualText = driver.findElement(by).getText();
            if(actualText.equals(expectedText)){
//                System.out.println("Text are matching:" + actualText + "equals" + expectedText);
                Logger.info("Text are matching: " + actualText + "equals" + expectedText);
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text", "Text verified successfully "+actualText+" equals "+ expectedText);
                return true;
            }
            else{
//                System.out.println("Text are not matching:" + actualText + "not equals" + expectedText);
                applyBorder(by,"red");
                Logger.error("Text are not matching:" + actualText + "not equals " + expectedText);
                ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison Failed!", "Text comparison failed "+ actualText + " not equals " + expectedText);
                return false;

            }
        }catch (Exception e){
//            System.out.println("Unable to compare texts: " + e.getMessage());
            Logger.error("Unable to compare texts: \" + e.getMessage()");
        }

        return false;
    }

//    public boolean isDisplayed(By by){
//        try{
//            waitForElementToBeVisible(by);
//            boolean isDisplayed = driver.findElement(by).isDisplayed();
//            if(isDisplayed){
//                System.out.println("Element is displayed");
//                return isDisplayed;
//            }
//            else{
//                System.out.println("Element not displayed");
//                return isDisplayed;
//            }
//        }
//        catch (Exception e){
//            System.out.println("Element not displayed");
//            return false;
//        }
//    }
    // simplified method and remove redundant conditions

    public boolean isDisplayed(By by){
        try{
            waitForElementToBeVisible(by);
            applyBorder(by,"green");
            Logger.info("Element is displayed : " + getElementDescription(by));
            ExtentManager.logStep("Element is displayed : " + getElementDescription(by));
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed : ", "Element is displayed : " + getElementDescription(by) );
            return driver.findElement(by).isDisplayed();
        }catch(Exception e){
//            System.out.println("Element is not displayed:" + e.getMessage());
            applyBorder(by,"red");
            Logger.error("Element is not displayed:" + e.getMessage());
            ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed : ", "Element is not displayed : "+getElementDescription(by));
            return false;
        }
    }

    public void waitForPageLoad(int timeOutInSec){
        try{
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
                    .executeScript("return document.readyState").equals("complete"));
//            System.out.println("Page loaded successfully");
            Logger.info("Page loaded successfully");
        }
        catch(Exception e){
//            System.out.println("Page did not load with in " + timeOutInSec + "seconds. Exception:" + e.getMessage());
            Logger.error("Page did not load with in " + timeOutInSec + "seconds. Exception:" + e.getMessage());
        }
    }

    public void scrollToElement (By by){
        try{
            applyBorder(by,"green");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0], scrollIntoView(true);", element);
        }
        catch(Exception e){
//            System.out.println("Unable to locate the element : " + e.getMessage());
            applyBorder(by,"red");
            Logger.error("Unable to locate the element : " + e.getMessage());
        }
    }

    //Wait for element to be clickable
    private void waitForElementToBeClickable(By by){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(by));
        }
        catch (Exception e){
//            System.out.println("element is not clickable:" + e.getMessage());
            Logger.error("element is not clickable:" + e.getMessage());
        }
    }

    //Wait for element to be visible
    private void waitForElementToBeVisible(By by){
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        }
        catch (Exception e){
//            System.out.println("Element is not visible:" + e.getMessage());
            Logger.error("Element is not visible:" + e.getMessage());
        }
    }

    // Method to get the description of an element using By locator
    public String getElementDescription(By locator) {
        // Check for null driver or locator to avoid NullPointerException
        if (driver == null) {
            return "Driver is not initialized.";
        }
        if (locator == null) {
            return "Locator is null.";
        }

        try {
            // Find the element using the locator
            WebElement element = driver.findElement(locator);

            // Get element attributes
            String name = element.getDomProperty("name");
            String id = element.getDomProperty("id");
            String text = element.getText();
            String className = element.getDomProperty("class");
            String placeholder = element.getDomProperty("placeholder");

            // Return a description based on available attributes
            if (isNotEmpty(name)) {
                return "Element with name: " + name;
            } else if (isNotEmpty(id)) {
                return "Element with ID: " + id;
            } else if (isNotEmpty(text)) {
                return "Element with text: " + truncate(text, 50);
            } else if (isNotEmpty(className)) {
                return "Element with class: " + className;
            } else if (isNotEmpty(placeholder)) {
                return "Element with placeholder: " + placeholder;
            } else {
                return "Element located using: " + locator.toString();
            }
        } catch (Exception e) {
            // Log exception for debugging
            e.printStackTrace(); // Replace with a logger in a real-world scenario
            return "Unable to describe element due to error: " + e.getMessage();
        }
    }

    // Utility method to check if a string is not null or empty
    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    // Utility method to truncate long strings
    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }

    //Utility Method to Border an element
    public void applyBorder(By by,String color) {
        try {
            //Locate the element
            WebElement element = driver.findElement(by);
            //Apply the border
            String script = "arguments[0].style.border='3px solid "+color+"'";
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(script, element);
            Logger.info("Applied the border with color "+color+ " to element: "+getElementDescription(by));
        } catch (Exception e) {
            Logger.warn("Failed to apply the border to an element: "+getElementDescription(by),e);
        }
    }
}
