package support;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class Drivers {

    private WebDriver browser;

    @After
    public void quit() {
        if (browser != null) {
            browser.quit();
        }
    }

    @Test
    public void firefoxCanBeInstantiated() {
        browser = Drivers.firefox();

        assertThat(browser, not(equalTo(null)));
    }

    @Test
    public void chromeCanBeInstantiated() {
        browser = Drivers.chrome();

        assertThat(browser, not(equalTo(null)));
    }

    public static WebDriver chrome() {
        System.setProperty("webdriver.chrome.driver", DriversPath.chrome());
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("marionette", true);

        return new ChromeDriver(capabilities);
    }

    public static WebDriver firefox() {
        System.setProperty("webdriver.gecko.driver", DriversPath.firefox());
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);

        return new FirefoxDriver(capabilities);
    }
}
