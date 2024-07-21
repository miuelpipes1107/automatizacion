
package ink_testing_source.login;

import ink_testing_source.configuration.browser_manager;
import ink_testing_source.configuration.configuration_server;
import ink_testing_source.configuration.element_manager;
import ink_testing_source.configuration.method_after_before;
import java.awt.AWTException;
import java.io.IOException;
import java.net.MalformedURLException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import recording_test.test_listener;

/**
 *
 * @author Aurora
 */
public class login extends method_after_before
{

  public static By user = By.id("m_login_ema");
  public static String text_user = "User";
  public static By password = By.id("m_login_password");
  public static String text_password = "password";
  public static WebDriverWait Wait;

  @AfterClass
  @Override
  public void method_after_class() throws IOException, AWTException
  {
    test_listener.method_after_class();
//    System.out.println("testExecKey: "+configuration_server.testExecKey);
//    System.out.println("projectKey: "+configuration_server.projectKey);
  }

  @Test
  public static void t01_open_browser_login() throws MalformedURLException, IOException
  {
    browser_manager.open_browser();
  }

  @Test
  public static void t02_enter_data_login() throws MalformedURLException, IOException
  {
    element_manager.textfield_set_text(user, text_user, "usuario");
    element_manager.textfield_set_text(password, text_password, "123");
  }

}
