/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ink_testing_source.configuration;

import ink_testing_source.configuration.driver.driver;
import ink_testing_source.configuration.driver.driver_manager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Cristian
 */
public class browser_manager
{

  public static WebDriver web_driver_instace;
  public static WebDriverWait wait;
  public static WebDriverWait wait_hidden;
  public static WebDriverWait wait_alert;
  public static String id_issue_xray;

  /**
   * Opens the web browser and navigates to the login page. Initializes the
   * driver, configures implicit and explicit waits, maximizes the window, sets
   * the script timeout, and waits for the page to load completely. Throws a
   * RuntimeException if there is an error initializing the driver.
   */
  public static void open_browser()
  {
    close_browser();

    //// initialize the driver
    try
    {
      driver.init_driver();
    }
    catch (Exception ex)
    {
      throw new RuntimeException("Error initializing the driver", ex);
    }
    web_driver_instace = driver_manager.get_driver();
    //// configuration implicit waits
    web_driver_instace.manage().timeouts().implicitlyWait(configuration_server.WAIT_IMPLICITLY_TIME_BEFORE_ELEMENT,
      TimeUnit.SECONDS);

    //// configuration explicit waits
    wait = new WebDriverWait(web_driver_instace, configuration_server.WAIT_TIME_BEFORE_ELEMENT);
    wait_hidden = new WebDriverWait(web_driver_instace, configuration_server.WAIT_HIDEEN);
    wait_alert = new WebDriverWait(web_driver_instace, 1);

    //// navigate to the login page
    web_driver_instace.manage().window().maximize();
    web_driver_instace.get(configuration_server.URL_LOGIN);

    //// configuration script timeout
    web_driver_instace.manage().timeouts().setScriptTimeout(configuration_server.WAIT_TIME_BEFORE_ELEMENT, TimeUnit.SECONDS);
    wait.until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState")
      .equals("complete"));
  }

  /**
   * Opens the web browser for web check-in and navigates to the specified URL.
   * Initializes the driver, configures implicit and explicit waits, maximizes
   * the window, sets the script timeout, and waits for the page to load
   * completely. Throws a RuntimeException if there is an error initializing the
   * driver.
   *
   * @param url The URL to navigate to.
   */
  public static void open_browser_web_checkin(String url)
  {
    close_browser();

    configuration_server.BROWSER = configuration_server.BROWSER_CHROME;
    //// initialize the driver
    try
    {
      driver.init_driver();
    }
    catch (Exception ex)
    {
      throw new RuntimeException("Error initializing the driver", ex);
    }
    web_driver_instace = driver_manager.get_driver();
    //// configuration implicit waits
    web_driver_instace.manage().timeouts().implicitlyWait(configuration_server.WAIT_IMPLICITLY_TIME_BEFORE_ELEMENT,
      TimeUnit.SECONDS);

    //// configuration explicit waits
    wait = new WebDriverWait(web_driver_instace, configuration_server.WAIT_TIME_BEFORE_ELEMENT);
    wait_hidden = new WebDriverWait(web_driver_instace, configuration_server.WAIT_HIDEEN);
    wait_alert = new WebDriverWait(web_driver_instace, 1);

    //// navigate to the login page
    web_driver_instace.manage().window().maximize();
    web_driver_instace.get(url);

    //// configuration script timeout
    web_driver_instace.manage().timeouts().setScriptTimeout(configuration_server.WAIT_TIME_BEFORE_ELEMENT, TimeUnit.SECONDS);
    wait.until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState")
      .equals("complete"));
  }

  /**
   * Retrieves the title of the web page and performs validation. Waits for
   * jQuery to load and measures the loading time. If the title contains "Web
   * Check-in" or "Ink Cloud", it assigns the title to the variable. Then, it
   * checks if the page title matches the assigned title using explicit wait. If
   * the title matches, it prints the title. Otherwise, it asserts the error
   * message along with the page title and current URL.
   */
  public static void title_page()
  {
    element_manager.debug_log("Method, entry to the method title_page");
    long startTime = System.currentTimeMillis();
    wait_for_jsond_jquery_to_load();
    long endTime = System.currentTimeMillis();
    long elapsedTime = endTime - startTime;
    element_manager.debug_log("the charge ended in:  " + elapsedTime / 1000.0 + " milisegundos.");
    String tittle_page = "";

    if (web_driver_instace.getTitle().contains("Web Check-in"))
    {
      tittle_page = web_driver_instace.getTitle();
    }

    if (web_driver_instace.getTitle().contains("Ink Cloud"))
    {
      tittle_page = web_driver_instace.getTitle();
    }

    if (wait.until(ExpectedConditions.titleContains(tittle_page)))
    {
      element_manager.debug_log(web_driver_instace.getTitle());
    }
    else
    {
      element_manager.assert_error(web_driver_instace.getTitle() + " ERROR PAGE NO LOADING... URL: "
        + web_driver_instace.getCurrentUrl());
    }
  }

  /**
   * Waits for jQuery and AJAX requests to complete before proceeding. Uses an
   * explicit wait to check the active count of jQuery and AJAX requests, as
   * well as the page loaded status. Prints the jQuery active count, AJAX active
   * count, and page loaded status for debugging purposes. Returns when there
   * are no active jQuery or AJAX requests and the page is loaded. Prints a
   * success message along with the page title when the page is loaded
   * successfully.
   */
  public static void wait_for_ajax()
  {
    element_manager.debug_log("CASE, entry to the method wait_for_ajax ");
    //// Wait for jQuery and AJAX requests to load
    new WebDriverWait(web_driver_instace, 60).until((ExpectedCondition<Boolean>) driver ->
    {
      long jqueryActiveCount = (long) ((JavascriptExecutor) driver).executeScript(
        "return typeof jQuery !== 'undefined' ? jQuery.active : 0;");
      long ajaxActiveCount = (long) ((JavascriptExecutor) driver).executeScript(
        "return typeof $ !== 'undefined' ? $.active : 0;");

      boolean pageLoaded = !driver.getTitle().equals("");

      element_manager.debug_log("jQuery active count: " + jqueryActiveCount + ", Ajax active count: "
        + ajaxActiveCount + ", Page loaded: " + pageLoaded);

      return (jqueryActiveCount == 0 && ajaxActiveCount == 0 && pageLoaded);
    });
    element_manager.debug_log("Page loaded successfully: " + web_driver_instace.getTitle());
  }

  /**
   * Waits for JSOND, jQuery, and document readiness state to load before
   * proceeding. Uses an explicit wait to check if the document readiness state
   * is complete, the active count of jQuery, and if jQuery has been loaded
   * successfully. Prints the jQuery active count, page loaded status, and
   * jQuery loaded status for debugging purposes. Returns when the document
   * readiness state is complete and jQuery has been loaded successfully. If the
   * timeout is exceeded, throws a TimeoutException and displays an error
   * message along with the page title and current URL.
   */
  public static void wait_for_jsond_jquery_to_load()
  {
    element_manager.debug_log("CASE, entry to the method wait_for_jsond_jquery_to_load ");

    ExpectedCondition<Boolean> js_and_jquery_load = driver ->
    {
      boolean jsLoaded = ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
      long jqueryActiveCount = (long) ((JavascriptExecutor) driver).executeScript(
        "return typeof jQuery !== 'undefined' ? jQuery.active : 0;");
      boolean jqueryLoaded = (jqueryActiveCount <= 3);

      element_manager.debug_log("jQuery active count: " + jqueryActiveCount + ", Page loaded: "
        + jsLoaded + ", jQuery loaded: " + jqueryLoaded);
      return jsLoaded && jqueryLoaded;
    };

    try
    {
      wait.until(js_and_jquery_load);
    }
    catch (TimeoutException e)
    {
      ////throw new TimeoutException("Error loading page, timeout exceeded");
      element_manager.assert_error(web_driver_instace.getTitle() + " ERROR exceeded time of wait... URL: "
        + web_driver_instace.getCurrentUrl());
    }
  }

  /**
   * Verifies that the current browser URL contains the specified URL and
   * performs validations.
   *
   * @param url_page_to_redirect type String The URL to be contained in the
   * current browser URL.
   * @return The current browser URL type String .
   *
   * Steps: 1. Log the entry to the method. 2. Get the current page title. 3.
   * Retrieve the current browser URL and the URL to redirect. 4. Ensure that
   * both URLs are not null. 5. If the current browser URL does not contain the
   * redirect URL, wait for one second and recheck. 6. If the URL still does not
   * contain the redirect URL, log an error message, perform an assertion, and
   * return the current browser URL. 7. If the URL contains the redirect URL,
   * log a success message. 8. Return the current browser URL.
   */
  public static String assert_true_contains_url_with_url_webdriver(String url_page_to_redirect)
  {
    element_manager.debug_log("CASE, entry to the method assert_true_contains_url_with_url_webdriver ");
    browser_manager.title_page();

    String url_webdriver = null;

    url_webdriver = web_driver_instace.getCurrentUrl();
    element_manager.get_assert_not_null(url_webdriver + " url webdriver null.", null, url_webdriver, true);
    element_manager.get_assert_not_null(url_page_to_redirect + " url page to redirect null.", null,
      url_page_to_redirect, true);

    if (!url_webdriver.contains(url_page_to_redirect))
    {
      element_manager.sconds_sleep(1000);
      if (!url_webdriver.contains(url_page_to_redirect))
      {
        element_manager.debug_log("ERROR, redirect a page: " + "\"" + url_webdriver + "\"" + " AND EXPECTED URL "
          + "\"" + url_page_to_redirect + "\"" + " | Method assert_true_contains_url_with_url_webdriver");

        element_manager.assert_error("redirect a page: " + "\"" + url_webdriver + "\"" + " AND EXPECTED URL "
          + "\"" + url_page_to_redirect + "\"" + " | Method assert_true_contains_url_with_url_webdriver");
        browser_manager.title_page();
        return url_webdriver;
      }
      else
      {
        browser_manager.title_page();
        element_manager.debug_log("SUCCESS, method assert_true_contains_url_with_url_webdriver");
        return url_webdriver;
      }
    }
    else
    {
      browser_manager.title_page();
      element_manager.debug_log("SUCCESS, method assert_true_contains_url_with_url_webdriver");
      return url_webdriver;
    }
  }

  /**
   * Closes the browser and terminates the WebDriver session.
   *
   * Steps: 1. Determine the process name to kill based on the browser version.
   * 2. If not running in GitHub Actions: - Terminate the driver process based
   * on the determined process name. - Terminate the Firefox process. 3. Set the
   * WebDriver to null. 4. If the WebDriver is not null: - Delete all browser
   * cookies. - Set the wait object to null. - Quit the browser. - Set the
   * WebDriver to null.
   *
   */
  public static void close_browser()
  {
    String killProces = (configuration_server.VERSION_BROWSER < 91.0) ? "geckodriver_16_1.exe" : "geckodriver_30.exe";
    try
    {
      if (!configuration_server.GITHUB_ACTIONS)
      {
        kill_process_by_name(killProces);
        kill_process_by_name("firefox.exe");
      }

      web_driver_instace = null;
    }
    catch (IOException ex)
    {
      element_manager.debug_log("" + ex);
    }

    if (web_driver_instace != null)
    {
      web_driver_instace.manage().deleteAllCookies();
      wait = null;
      web_driver_instace.quit();
      web_driver_instace = null;
    }
  }

  /**
   * Refreshes the current page in the browser.
   *
   * Steps: 1. If the WebDriver instance is not null: - Navigate to the
   * refreshed page. 2. Retrieve the title of the refreshed page using the
   * title_page() method.
   */
  public static void refresh_browser()
  {
    if (web_driver_instace != null)
    {
      web_driver_instace.navigate().refresh();
    }
    title_page();
  }

  public static void kill_process_by_name(String process_name) throws IOException
  {
    BufferedReader br = get_tasklist();
    String task_line = br.readLine();
    task_line = br.readLine();
    task_line = br.readLine();
    while (task_line != null)
    {

      StringTokenizer st = new StringTokenizer(task_line, " ");
      String process_name_var = st.nextToken();
      String process_id_var = st.nextToken();
      if (process_name_var.equals(process_name))
      {
        Runtime.getRuntime().exec("cmd /c taskkill /f /t /pid " + process_id_var);
      }
      task_line = br.readLine();
    }
  }

  private static BufferedReader get_tasklist()
  {
    Process p = null;
    try
    {
      p = Runtime.getRuntime().exec("cmd /c tasklist /nh");
    }
    catch (IOException e)
    {
      System.out.println("Error getting tasklist from OS: " + e.getMessage());
    }
    InputStream is = p.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    return br;
  }

  /**
   * returns the server name
   *
   * @param server:string - The input text.
   * @return the server name
   */
  public static String get_name_server(String server)
  {
    String name_server = "";
    try
    {
      if (server.contains("qa"))
      {
        name_server = "QA_";
      }
      else if (server.contains("uat"))
      {
        name_server = "UAT_";
      }
      else if (server.contains("sabre") || server.contains("staging-php8"))
      {
        name_server = "SABRE_";
      }
      else if (server.contains("rx-stg"))
      {
        name_server = "RX_STG_";
      }
      else if (server.contains("staging.inkcompute"))
      {
        name_server = "INKCOMPUTE_";
      }
      else if (server.contains("development"))
      {
        name_server = "DEVELOPMENT_";
      }
      else if (server.contains("inkstaging"))
      {
        name_server = "INKSTAGING_";
      }
      else if (server.contains("j21inkstgcent01"))
      {
        name_server = "J2LINKSTGCENT01_";
      }
      else if (server.contains("docker-web1"))
      {
        name_server = "DOCKER_WEB1_";
      }
      else if (server.contains("staging.radixxgo"))
      {
        name_server = "RADIXXGO_";
      }
      else
      {
        name_server = "DEVQA_";
      }
    }
    catch (Exception e)
    {
      element_manager.assert_error(e.getMessage());
    }
    return name_server;
  }

}
