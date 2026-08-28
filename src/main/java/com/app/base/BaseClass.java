package com.app.base;

import com.app.actiondriver.ActionDriver;
import com.app.utilities.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static Properties prop;
//    protected static WebDriver driver;
//    private static ActionDriver actionDriver;

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static final Logger Logger = LogManager.getLogger(BaseClass.class);

    protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

    public SoftAssert getSoftAssert(){
        return softAssert.get();
    }

    @BeforeSuite
    public void loadConfig() throws IOException {
        // Load Configuration File
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
        Logger.info("config.properties file loaded");

        // start the extend report
//        ExtentManager.getReporter(); -- This has been implemented in TestListener
    }

    @BeforeMethod
    @Parameters("browser")
    public synchronized void setup(String browser) throws IOException {
        System.out.println("Setting up webdriver for :" + this.getClass().getSimpleName());
        launchBrowser(browser);
        configureBrowser();
        staticWait(2);

        Logger.info("Webdriver initialized and browser maximized");
//        Logger.trace("This is trace messge");
//        Logger.error("This is error message");
//        Logger.warn("This is warning message");
//        Logger.fatal("This is fatal message");
//        Logger.debug("This is debug message");

        //Initialize the action driver only once
//        if(actionDriver == null){
//            actionDriver = new ActionDriver(driver);
//            System.out.println("Action Driver Instance is created");
//            Logger.info("Action Driver Instance is created " + Thread.currentThread().getId());
//        }
        actionDriver.set(new ActionDriver(getDriver()));
        Logger.info("Action Driver Instance is created for thread " + Thread.currentThread().getId());
    }

    private synchronized void launchBrowser(String browser) {
//        String browser = prop.getProperty("browser");
        boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
        String gridURL = prop.getProperty("gridURL");
        if (seleniumGrid) {
            try{
                if (browser.equalsIgnoreCase("chrome")) {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
                    driver.set(new RemoteWebDriver(new URL(gridURL), options));
                }
                else if (browser.equalsIgnoreCase("firefox")) {
                    FirefoxOptions options = new FirefoxOptions();
                    options.addArguments("-headless");
                    driver.set(new RemoteWebDriver(new URL(gridURL), options));
                }
                else {
                    throw new IllegalArgumentException("Browser Not Supported: " + browser);
                }
                Logger.info("RemoteWebDriver instance created for Grid in headless mode");
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Grid URL", e);
            }
        }
        else {
            if (browser.equalsIgnoreCase("chrome")) {
//            driver = new ChromeDriver();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                options.addArguments("--disable-notifications");
                driver.set(new ChromeDriver(options));
                ExtentManager.registerDriver(getDriver());
                Logger.info("Chrome driver instance is created");
            } else if (browser.equalsIgnoreCase("firefox")) {
//            driver = new FirefoxDriver();
                driver.set(new FirefoxDriver());
                ExtentManager.registerDriver(getDriver());
                Logger.info("Firefox driver instance is created");
            } else if (browser.equalsIgnoreCase("safari")) {
                driver.set(new SafariDriver());
                ExtentManager.registerDriver(getDriver());
                Logger.info("Safari driver instance is created");
            } else {
                throw new IllegalArgumentException("browser is invalid or not supported : " + browser);
            }
        }
    }

    private synchronized void configureBrowser() {
        boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        getDriver().manage().window().maximize();
//        Navigate to url
//        try {
//        getDriver().get(prop.getProperty("url"));
//        }
//        catch (Exception e){
//            System.out.println("Failed to navigate to the url:" + e.getMessage());
//        }
        if(seleniumGrid){
            getDriver().get(prop.getProperty("url_grid"));
        }else{
            getDriver().get(prop.getProperty("url"));
        }
    }

    @AfterMethod
    public synchronized void tearDown(){
        if(driver.get() != null){
            try {
                driver.get().quit();
            }
            catch (Exception e){
                System.out.println("Unable to quit the browser :" + e.getMessage());
            }
        }
//        System.out.println("Web driver instance is closed");
        Logger.info("Web driver instance is closed");
        driver.remove();
        actionDriver.remove();
//        ExtentManager.endTest(); --This has been implemented in TestListener
//        driver = null;
//        actionDriver = null;
    }

    public static Properties getProp(){
        return prop;
    }

    public static WebDriver getDriver(){
//        if(driver == null){
        if(driver.get() == null){
            System.out.println("Webdriver is not initialized");
            throw new IllegalArgumentException("Webdriver is not initialized");
        }
//        return driver;
        return driver.get();
    }

    public static ActionDriver getActionDriver(){
//        if(actionDriver == null){
        if(actionDriver.get() == null){
//        }
            System.out.println("Actiondriver is not initialized");
            throw new IllegalArgumentException("Actiondriver is not initialized");
        }
//        return actionDriver;
        return actionDriver.get();
    }

    public void setDriver(ThreadLocal<WebDriver> driver){
        this.driver = driver;
    }
    // static wait for pause
    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}
