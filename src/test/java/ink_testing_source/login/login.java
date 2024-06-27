
package ink_testing_source.login;

import ink_testing_source.configuration.browser_manager;
import ink_testing_source.configuration.method_after_before;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import io.qameta.allure.testng.Tag;
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
    
  public static By user=By.id("m_login_ema");
  public static By password=By.id("m_login_password");
  public static WebDriverWait Wait;
  @AfterClass
  @Override
  public void method_after_class() throws IOException, AWTException
  {
    test_listener.method_after_class();
  }
    
  @Test
  @Tag("Critical")
  @Story("External open_browser_login")
  @Feature("open_browser_login")
  public static void t01_open_browser_login() throws MalformedURLException
  {
    Allure.label("jira-prod", "AUT-8");
    browser_manager.open_browser();    
  }
  
  @Test
  @Tag("Critical")
  @Story("External enter_data_login")
  @Feature("enter_data_login")
  public static void t02_enter_data_login() throws MalformedURLException
  {  
     Allure.label("jira-prod", "AUT-10"); 
     Wait = new WebDriverWait(browser_manager.web_driver_instace, 10);
     browser_manager.web_driver_instace.findElement(user).sendKeys("usuario");
     browser_manager.web_driver_instace.findElement(password).sendKeys("123");
  }
  
}
