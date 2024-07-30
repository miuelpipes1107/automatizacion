
package ink_testing_source.login;

import ink_testing_source.configuration.browser_manager;
import ink_testing_source.configuration.element_manager;
import ink_testing_source.configuration.method_after_before;
import java.awt.AWTException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import jira_xray.test_step_logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import recording_test.test_listener;
/**
 *
 * @author Aurora
 */
public class login extends method_after_before
{

  public static By user = By.id("m_login_email");
  public static String text_user = "User";
  public static By password = By.id("m_login_password");
  public static String text_password = "password";
  public static WebDriverWait Wait;

  @BeforeClass
  public static void setup()
  {
    test_step_logger.clear();
  }
  
  @AfterMethod
  public void afterMethod()
  {
    List<ArrayList<String>> testSteps = test_step_logger.getTestSteps();
    test_step_logger.list_testSteps.add(testSteps);
    test_step_logger.clear();
  }
  
  @AfterClass
  @Override
  public void method_after_class() throws IOException, AWTException
  {
    test_listener.method_after_class();

//    List<String> testSteps = test_step_logger.getTestSteps();
//    System.out.println("Captured Test Steps: " + testSteps);
//
//    
//    test_step_logger.clear();

//    System.out.println("testExecKey: "+configuration_server.testExecKey);
//    System.out.println("projectKey: "+configuration_server.projectKey);
  }
  
  @Test
  public static void t01_open_browser_login() throws MalformedURLException, IOException
  { 
    test_step_logger.logCurrentMethod("","");
    browser_manager.open_browser();    
  }

  @Test
  public static void t02_enter_data_login() throws MalformedURLException, IOException
  {
    test_step_logger.logCurrentMethod("","");
    element_manager.textfield_set_text(user, text_user, "user");
    element_manager.textfield_set_text_2(password, text_password, "123");    
  }

}
