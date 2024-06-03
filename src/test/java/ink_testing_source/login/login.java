
package ink_testing_source.login;

//import app.getxray.xray.junit.customjunitxml.XrayTestReporter;
//import app.getxray.xray.junit.customjunitxml.XrayTestReporterParameterResolver;
//import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
//import app.getxray.xray.junit.customjunitxml.annotations.XrayTest;
import ink_testing_source.configuration.browser_manager;
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
public class login extends method_after_before {
    
  public static By user=By.id("m_login_email");
  public static By password=By.id("m_login_password");
  public static WebDriverWait Wait;
  @AfterClass
  @Override
  public void method_after_class() throws IOException, AWTException
  {
    test_listener.method_after_class();
  }
    
  @Test
  public static void t01_open_browser_login() throws MalformedURLException
  {
    browser_manager.open_browser();    
  }
  
  @Test
  public static void t02_enter_data_login() throws MalformedURLException
  {      
     Wait = new WebDriverWait(browser_manager.web_driver_instace, 10);
     browser_manager.web_driver_instace.findElement(user).sendKeys("usuario");
     browser_manager.web_driver_instace.findElement(password).sendKeys("123");
  }
  
}
