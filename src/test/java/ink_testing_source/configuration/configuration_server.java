/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ink_testing_source.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.lang3.SystemUtils;

/**
 *
 * @author Cristian
 */
public class configuration_server 
{
  /**
   * This class manages all the global variables of the project SERVER, both for
   * configuration and parameterization, to carry out the execution of the test
   * cases
   */ 
  
  //// =============================== LOAD ENV  ===============================
  /**
   * Represents a configuration object for accessing environment variables using
   * Dotenv in Java. Dotenv is a library that loads environment variables from a
   * .env file into the system environment.
   */ 
  public static Dotenv dotenv = Dotenv.load();
  
  //// =========================== RUN TEST PARALLEL  ==========================
  /**
   * This variable NAME_CASE is used to store the name of the test case when it
   * is executed.
   */ 
  public static String  NAME_CASE                                               = "";
  
  //// ===================== GITHUB ACTIONS  ===========================
  /**
   * Configuration variables GITHUB ACTIONS.
   */   
  public static boolean GITHUB_ACTIONS                                          = SystemUtils.IS_OS_LINUX ? true : false;  
  public static boolean LOCAL_WHITH_URL                                         = true;
  
  /**
   * Variables used in the browser_manager class that allow identifying the
   * Firefox browser and its version.
   */
  public static String  BROWSER                                                 = configuration_server.BROWSER_FIREFOX;  
  public static double  VERSION_BROWSER                                         = 51.0;   
  
  //// ===================== FILES  ============================================
  /**
   * These variables are used to define file paths in the system based on the
   * type of operating system (Linux or Windows). They are related to
   * directories for storing test results, including both Allure reports and
   * automation videos.
   */
  public static final String SOURCE_FILES                                       = SystemUtils.IS_OS_LINUX ? "target/allure-results/" : System.getProperty("user.dir")
                                                                                  +"/target/allure-results/";
  public static final String PATCH_VIDEO                                        = SystemUtils.IS_OS_LINUX ? "execution_of_cases/" : System.getProperty("user.dir")
                                                                                  + "/execution_of_cases/";
  public static final String PATCH_AWS                                          = SystemUtils.IS_OS_LINUX ? "aws/" : System.getProperty("user.dir") + "/aws/";
  
  public static final String PATCH_XRAY                                         = SystemUtils.IS_OS_LINUX ? "xray/" : System.getProperty("user.dir") + "/xray/";
  
  //// ======================== browser  =======================================    
  /**
   * Variables that identify the web browsers.
   */
  public static final String BROWSER_FIREFOX                                    = "firefox";
  public static final String BROWSER_CHROME                                     = "chrome";     
  
  ////================================ wait time ===============================
  /**
   * Variables that identify the waiting times used in the automation tests.
   */
  public static final int WAIT_TIME_BEFORE_ELEMENT                              = 30;
  public static final int WAIT_IMPLICITLY_TIME_BEFORE_ELEMENT                   = 2;
  public static final int WAIT_HIDEEN                                           = 2000;
  public static final int RETRY_TIMEOUT                                         = 30000;
  public static final int RETRY_TIMEOUT_OLD                                     = 15000;
  public static final int SLEEP_SECONDS_ALERT                                   = 2000;
  public static final int TIMEOUT_MILLISECONDS                                  = 60000;
  
  /**
   * This variable can be used to construct URL addresses.
   */
  public static final String HTTPS                                              = "https://"; 
  
  ////======================== url Server ======================================
  /**
   * Variable that identifies the server where the tests will be performed.
   */
  public static final String SERVER                                             = dotenv.get("SERVER2");  
  public static final String HTTPS_AND_SERVER                                   = HTTPS + SERVER;
  
  // ================================== URLS ===================================
  
  public static final String JIRA_API_TOKEN                                     = dotenv.get("API_TOKEN");
  public static final String JIRA_USER_EMAIL                                    = dotenv.get("JIRA_USER_EMAIL");
  public static final String JIRA_URL                                           = dotenv.get("JIRA_URL");
  // ================================== LOGIN ==================================  
  /**
   * Login URL and home page URL
   */
  public static       String URL_LOGIN                                          = HTTPS+SERVER + "locale=es_LA&_rdr";
    
  public static final String USER_AUTOMATED_TESTER_CARRIER_ONE                  = dotenv.get("USER_AUTOMATED_TESTER_CARRIER_ONE");
  
}
