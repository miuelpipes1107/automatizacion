
package ink_testing_source.login;

import ink_testing_source.configuration.browser_manager;
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
  public static String testExecKey = System.getenv("TEST_EXEC_KEY");
  public static String projectKey = System.getenv("PROJECT_KEY");

  @AfterClass
  @Override
  public void method_after_class() throws IOException, AWTException
  {
    test_listener.method_after_class();
    System.out.println("testExecKey: "+testExecKey);
    System.out.println("projectKey: "+projectKey);
  }

  @Test
  public static void t01_open_browser_login() throws MalformedURLException, IOException
  {
//    browser_manager.id_issue_xray="AUT-8";  
//    Allure.label("jira-prod",browser_manager.id_issue_xray );
    browser_manager.open_browser();
  }

  @Test
  public static void t02_enter_data_login() throws MalformedURLException, IOException
  {
//     browser_manager.id_issue_xray="AUT-10"; 
//     Allure.label("jira-prod", browser_manager.id_issue_xray); 
    element_manager.textfield_set_text(user, text_user, "usuario");
    element_manager.textfield_set_text(password, text_password, "123");
  }

}
