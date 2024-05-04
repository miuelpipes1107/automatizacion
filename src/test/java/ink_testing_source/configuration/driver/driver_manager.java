package ink_testing_source.configuration.driver;

import org.openqa.selenium.WebDriver;

public class driver_manager
{

  private static final ThreadLocal<WebDriver> thread_local = new ThreadLocal<>();

  /**
   * Default Constructor for the driver_manager class.Initializes driver_manager
   * object.
   */
  public driver_manager()
  {
  }

  public static WebDriver get_driver()
  {
    return thread_local.get();
  }

  public static void set_driver(WebDriver driver)
  {
    thread_local.set(driver);
  }

}
