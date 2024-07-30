/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jira_xray;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aurora
 */
public class test_step_logger
{

  private static List<ArrayList<String>> testSteps = new ArrayList<>();
  public static List<List<ArrayList<String>>> list_testSteps= new ArrayList<>();

  public static void log(String step,String data,String result)
  {
    ArrayList<String> data_test= new ArrayList<>();
    data_test.add(0,step);
    data_test.add(1,data);
    data_test.add(2,result);
    testSteps.add(data_test);
  }

  public static List<ArrayList<String>> getTestSteps()
  {
    return new ArrayList<>(testSteps);
  }

  public static void clear()
  {
    testSteps.clear();
  }

  public static void logCurrentMethod(String data,String result)
  {
    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();    
    if (stackTraceElements.length > 1)
    {
      String currentMethod = stackTraceElements[2].getMethodName();
      
      log(currentMethod,data,result);
    }
  }

}
