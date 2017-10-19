package myprojects.automation.assignment5.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    /**
     *
     * @param browser Driver type to use in tests.
     * @return New instance of {@link WebDriver} object.
     */
    public static WebDriver initDriver(String browser) {
        DesiredCapabilities capabilities;
        switch (browser) {
            case "android":
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(DriverFactory.class.getResource("/chromedriver.exe").getFile()).getPath());

                Map<String, Object> deviceMetrics = new HashMap<String, Object>();
                deviceMetrics.put("width", 360);
                deviceMetrics.put("height", 640);
                deviceMetrics.put("pixelRatio", 3.0);
                Map<String, Object> mobileEmulation = new HashMap<String, Object>();

                mobileEmulation.put("deviceMetrics", deviceMetrics);
                mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");

                Map<String, Object> chromeOptions = new HashMap<String, Object>();
                chromeOptions.put("mobileEmulation", mobileEmulation);
                capabilities = DesiredCapabilities.chrome();
                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                return new ChromeDriver(capabilities);
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        new File(DriverFactory.class.getResource("/geckodriver.exe").getFile()).getPath());
                return new FirefoxDriver();
            case "ie":
            case "internet explorer":
                System.setProperty(
                        "webdriver.ie.driver",
                        new File(DriverFactory.class.getResource("/IEDriverServer.exe").getFile()).getPath());
                capabilities = DesiredCapabilities.internetExplorer();
                capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
                capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                return new InternetExplorerDriver(capabilities);
            case "phantomjs":
                System.setProperty(
                        "phantomjs.binary.path",
                        new File(DriverFactory.class.getResource("/phantomjs.exe").getFile()).getPath());
                return new PhantomJSDriver();
            case "chrome":
            default:
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(DriverFactory.class.getResource("/chromedriver.exe").getFile()).getPath());
                return new ChromeDriver();
        }
    }

    /**
     *
     * @param browser Remote driver type to use in tests.
     * @param gridUrl URL to Grid.
     * @return New instance of {@link RemoteWebDriver} object.
     */
    public static RemoteWebDriver initDriver(String browser, String gridUrl) throws MalformedURLException {
        DesiredCapabilities capabilities;
        switch (browser) {
            case "android":
                capabilities = DesiredCapabilities.android();
                break;
            case "firefox":
                capabilities = DesiredCapabilities.firefox();
                break;
            case "ie":
            case "internet explorer":
                capabilities = DesiredCapabilities.internetExplorer();
                break;
            case "phantomjs":
                capabilities = DesiredCapabilities.phantomjs();
                break;
            case "chrome":
            default:
                capabilities = DesiredCapabilities.chrome();
                break;
        }
//        capabilities.setCapability("applicationName", "Node1");
        return new RemoteWebDriver(new URL(gridUrl), capabilities);
    }
}
