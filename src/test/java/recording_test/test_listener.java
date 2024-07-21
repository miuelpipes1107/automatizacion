/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recording_test;

import ink_testing_source.configuration.browser_manager;
import ink_testing_source.configuration.configuration_server;
import ink_testing_source.configuration.element_manager;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jira_xray.jira_xray;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.lang3.SystemUtils;
import org.testng.*;

/**
 *
 * @author User
 */
public class test_listener implements ITestListener, IExecutionListener
{

  public static custom_screen_recorder screen_recorder;
  int number_of_characters = 80;
  private static String status;
  public static String name_of_the_running_class;
  public static String name_server;
  public static File screenshot;
  public static jira_xray jira_xray_issue;  

  /**
   * Test listener class constructor. Initializes the custom screen recorder.
   */
  public test_listener()
  {
    try
    {
      screen_recorder = new custom_screen_recorder();
    }
    catch (IOException | AWTException e)
    {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Gets the name of the class from the given TestNG ITestContext.
   *
   * @param i_test_context:ITestContext - The TestNG i_test_context object.
   * @return The name of the class.
   */
  public String get_class_context_name(ITestContext i_test_context)
  {
    return i_test_context.getCurrentXmlTest().getClasses().stream()
      .findFirst().get().getName().substring(i_test_context.getCurrentXmlTest().getClasses().stream()
        .findFirst().get().getName().lastIndexOf('.') + 1);
  }

  /**
   * Gets the name of the class from the given TestNG ITestResult.
   *
   * @param i_test_result:ITestResult - The TestNG i_test_result object.
   * @return The name of the class.
   */
  public String get_class_result_name(ITestResult i_test_result)
  {
    name_of_the_running_class = i_test_result.getTestClass().getName().substring(i_test_result.getTestClass().getName().lastIndexOf('.') + 1);
    return name_of_the_running_class;
  }

  /**
   * Gets the name of the test method from the given TestNG ITestResult.
   *
   * @param i_test_result:ITestResult - The TestNG i_test_result object.
   * @return The name of the test method.
   */
  private static String getTestMethodName(ITestResult i_test_result)
  {
    return i_test_result.getMethod().getConstructorOrMethod().getName();
  }

  /**
   * Returns the status of the test based on the test result. Stops the screen
   * recording if necessary.
   *
   * @param i_test_result:ITestResult - The test result.
   * @return The status of the test with class name and test.
   */
  private String test_status(ITestResult i_test_result)
  {
    String duration =""; 
    //slack_incoming_webhook.get_duration_break_down(i_test_result.getEndMillis() - i_test_result.getStartMillis());

    switch (i_test_result.getStatus())
    {
      case ITestResult.FAILURE ->
      {
        //// If the exception is an instance of StaleElementReferenceException, set the state to "BROKEN"
        if (i_test_result.getThrowable() instanceof StaleElementReferenceException)
        {
          status = "BROKEN";
        }
        else
        {
          status = "FAILED";
        }
        stop_screen_recording(false);
      }
      case ITestResult.SKIP ->
      {
        status = "SKIPPED";
        stop_screen_recording(false);
      }
      case ITestResult.SUCCESS_PERCENTAGE_FAILURE ->
      {
        Allure.addAttachment(getTestMethodName(i_test_result), new ByteArrayInputStream(((TakesScreenshot) browser_manager.web_driver_instace)
          .getScreenshotAs(OutputType.BYTES)));               
        status = "SUCCESS_PERCENTAGE_FAILURE";
        stop_screen_recording(false);
      }
      default ->
      {
        status = "PASSED";
        stop_screen_recording(false);
      }
    }
    return "Test " + status + " - " + get_class_result_name(i_test_result) + "/" + getTestMethodName(i_test_result) + " - finish time: " + duration;
  }

  /**
   * Saves a text log as an attachment in Allure report.
   *
   * @param message:string - The text log message.
   * @return The message itself.
   */
  @Attachment(value = "{0}", type = "text/plain")
  public static String save_text_log(String message)
  {
    return message;
  }

  ////===================== Videos =====================
  /**
   * Deletes the video directory at the start of the execution. This method is
   * called by the onExecutionStart() method. It throws a RuntimeException if an
   * IOException occurs.
   */
  @Override
  public void onExecutionStart()
  {
    try
    {
      custom_screen_recorder.delete_directory_video();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes the video directory at the end of the execution. This method is
   * called by the onExecutionFinish() method. It throws a RuntimeException if
   * an IOException occurs.
   */
  @Override
  public void onExecutionFinish()
  {
    try
    {
      custom_screen_recorder.delete_directory_video();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Performs actions at the end of the test execution. This method is called by
   * the onFinish() method. It performs various operations such as copying and
   * zipping video files, and posting a Slack webhook request for Linux systems.
   *
   * @param context:ITestContext - The test context object.
   */
  @Override
  public void onFinish(ITestContext context)
  {
    String suite = context.getName();  

    if (context.getName().equals("Surefire test") || context.getName().equals("tests_functional"))
    {
      suite = get_class_context_name(context);
    }

    name_server = browser_manager.get_name_server(configuration_server.SERVER);
    
    String Zip_name = name_server + suite + screen_recorder.get_hours_date() + ".zip";
    test_listener.screen_recorder.file_copy_move();
    try
    {
      zipper z = new zipper(new File(configuration_server.PATCH_AWS + "/" + Zip_name));
      z.zip(new File("execution_of_cases"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
//    if (SystemUtils.IS_OS_LINUX && !context.getName().equals("Surefire test") && !context.getName().equals("tests_functional"))
//    {
//      slack_incoming_webhook.slack_webhook_post_request(context, Zip_name);
//    }
  }

  /**
   * Performs actions at the start of each test. This method is called by the
   * onTestStart() method. It logs the entry of the test case and starts
   * recording the screen.
   *
   * @param iTestResult:ITestResult - The test result object.
   */
  @Override
  public void onTestStart(ITestResult iTestResult)
  {
    element_manager.debug_log("###################### CASE, entry Case " + iTestResult.getName() + "######################");
    try
    {
      number_of_characters = 80;
      configuration_server.NAME_CASE = iTestResult.getName();
      configuration_server.list_created_cases.add(configuration_server.NAME_CASE);
      
      if (iTestResult.getMethod().getMethodName().length() < number_of_characters)
      {
        number_of_characters = iTestResult.getMethod().getMethodName().length();
      }
      screen_recorder.set_class_file_name(get_class_result_name(iTestResult));
      screen_recorder.start_recording(iTestResult.getMethod().getMethodName().substring(0, number_of_characters), true);
    }
    catch (IOException e)
    {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Performs actions when a test case is successful. This method is called by
   * the onTestSuccess() method. It logs the test status.
   *
   * @param iTestResult:ITestResult - The test result object.
   */
  @Override
  public void onTestSuccess(ITestResult iTestResult)
  {
    element_manager.debug_log(test_status(iTestResult));
  }

  /**
   * Performs actions when a test case fails. This method is called by the
   * onTestFailure() method. It adds a screenshot as an attachment to the Allure
   * report and logs the test status.
   *
   * @param iTestResult:ITestResult - The test result object.
   */
  @Override
  public void onTestFailure(ITestResult iTestResult)
  {
    File new_file_destination_xray;
    new_file_destination_xray = new File(configuration_server.PATCH_XRAY);

    if (!new_file_destination_xray.exists())
    {
      new_file_destination_xray.mkdirs();
    }

    Allure.addAttachment(getTestMethodName(iTestResult), new ByteArrayInputStream(((TakesScreenshot) browser_manager.web_driver_instace)
      .getScreenshotAs(OutputType.BYTES)));
    screenshot = ((TakesScreenshot) browser_manager.web_driver_instace).getScreenshotAs(OutputType.FILE);
    try
    {
//      int lastDotIndex = iTestResult.getInstanceName().lastIndexOf(".");
//      FileUtils.copyFile(screenshot, new File("xray/" + iTestResult.getInstanceName().substring(lastDotIndex + 1)
//        + "_" + getTestMethodName(iTestResult) + ".png"));
      
      int lastDotIndex = iTestResult.getInstanceName().lastIndexOf(".");
      FileUtils.copyFile(screenshot, new File("xray/" + getTestMethodName(iTestResult) + ".png"));
    }
    catch (IOException ex)
    {
      Logger.getLogger(test_listener.class.getName()).log(Level.SEVERE, null, ex);
    }

//    try
//    {
//      jira_xray.attach_file_to_issue(browser_manager.id_issue_xray, screenshot);
//    }
//    catch (IOException ex)
//    {
//      Logger.getLogger(test_listener.class.getName()).log(Level.SEVERE, null, ex);
//    }

    element_manager.debug_log(test_status(iTestResult));
  }

  /**
   * Performs actions when a test case is skipped. This method is called by the
   * onTestSkipped() method. It logs the test status.
   *
   * @param iTestResult:ITestResult - The test result object.
   */
  @Override
  public void onTestSkipped(ITestResult iTestResult)
  {
    element_manager.debug_log(test_status(iTestResult));
  }

  /**
   * Stops the screen recording.
   *
   * @param keepFile:boolean - indicating whether to keep the recorded file or
   * not.
   */
  public void stop_screen_recording(boolean keepFile)
  {
    try
    {
      screen_recorder.stop_recording(keepFile);
    }
    catch (IOException e)
    {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Method executed after the test class.Clears the list of created flights.
   *
   * @throws IOException If an I/O error occurs.
   * @throws AWTException If an error occurs during AWT operations.
   */
  public static void method_after_class() throws IOException, AWTException
  {
    element_manager.debug_log("###################### Case, entry method method_after_class ###################### ");

    jira_xray_issue = new jira_xray();
    jira_xray_issue.create_or_update_jira_issue(configuration_server.list_created_cases,"10005","");
    
    configuration_server.list_created_cases= new ArrayList<>();
    configuration_server.matrix_issues= new ArrayList<>();
    
//    String token="";
//    
//    try
//    {
//      token=jira_xray.authenticate(configuration_server.CLIENT_ID_XRAY, configuration_server.CLIENT_SECRET_XRAY);
//      jira_xray.upload_test_results(token);
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
  }

}
