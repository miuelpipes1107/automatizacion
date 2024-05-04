package ink_testing_source.configuration.driver;

import ink_testing_source.configuration.configuration_server;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class driver
{

  /**
   * Default Constructor for the driver class.Initializes driver object.
   */
  public driver()
  {
  }

  /**
   * Initializes the WebDriver based on the configured browser. This method sets
   * up the appropriate driver and assigns it to the driver manager.
   */
  public static void init_driver()
  {
    WebDriver driver = null;

    switch (configuration_server.BROWSER)
    {

      case "chrome" ->
      {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
      }
      case "firefox" ->
      {
        WebDriverManager.firefoxdriver().driverVersion("0.16.1").setup();
        FirefoxProfile profile = new FirefoxProfile();
        /////cache
        profile.setPreference("browser.cache.disk.enable", false);
        profile.setPreference("browser.cache.memory.enable", false);
        profile.setPreference("browser.cache.offline.enable", false);
        profile.setPreference("network.http.use-cache", false);
        /////download files pdf
        profile.setPreference("browser.download.folderList", 2);
//        profile.setPreference("browser.download.dir", configuration_universal_LC.PATH_FILE_DOWNLOADED);
        profile.setPreference("browser.download.useDownloadDir", true);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/html, application/pdf");
        profile.setPreference("browser.helperApps.neverAsk.openFile", "image/png, text/html, image/tiff,"
          + " text/csv, application/zip, application/octet-stream");
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "image/png, text/html, image/tiff,"
          + " text/csv, application/zip, application/octet-stream, application/pdf");
        profile.setPreference("pdfjs.disabled", true);  //// disable the built-in PDF viewer

        FirefoxOptions rules = new FirefoxOptions();
        rules.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        rules.setProfile(profile);
        driver = new FirefoxDriver(rules);
      }
    }
    driver_manager.set_driver(driver);
  }

}
