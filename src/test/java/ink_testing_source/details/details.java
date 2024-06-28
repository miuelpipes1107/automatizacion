
package ink_testing_source.details;

import ink_testing_source.configuration.browser_manager;
import ink_testing_source.configuration.element_manager;
import ink_testing_source.configuration.method_after_before;
import java.awt.AWTException;
import java.io.IOException;
import java.net.MalformedURLException;
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import recording_test.test_listener;

/**
 *
 * @author Aurora
 */
public class details extends method_after_before {
    
  public static By user=By.id("m_login_ema");
  public static String text_user ="User";
  public static By password=By.id("m_login_password");
  public static String text_password="password";
  
  @AfterClass
  @Override
  public void method_after_class() throws IOException, AWTException
  {
    test_listener.method_after_class();
  }
    
  @Test
  public static void t01_open_browser_details() throws MalformedURLException
  {
    browser_manager.open_browser();    
  }
  
  @Test
  public static void t02_enter_data_details() throws MalformedURLException
  {      
     element_manager.textfield_set_text(user, text_user, "usuario");
     element_manager.textfield_set_text(password, text_password, "123");
  }
  
}
