/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ink_testing_source.configuration;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

/**
 *
 * @author Cristian
 */
public class element_manager
{
  public static String text_ink_testing_source                                  ="ink_testing_source";
  public static String text_test_cases                                          ="test_cases";
  public static String text_method                                              ="Method ";
  public static String text_after_validation_items_repeated                     ="after validation items repeated ";
  public static String text_before_validation_items_repeated                    ="before validation items repeated ";
  public static String text_the_select_does_not_have_this_item                  ="the select does not have this item: ";
  public static String text_the_list_does_not_have_this_item                    ="the List does not have this item: ";   
  public static String text_before_selecting_it                                 ="before selecting it ";
  public static String text_after_selecting_it                                  ="after selecting it "; 
  public static String text_alert_present                                       ="Alert present: ";
  public static String text_not_alert_present                                   ="not alert present";
  public static String text_found_the_element                                   ="found the element";
  public static String text_not_found_the_element                               ="not found the element";
  public static String text_list_element_not_found                              ="List Element not found :";
  public static String text_element_found                                       ="Element found: ";
  public static String text_element_not_found_button                            ="Element not found button:";
  public static String text_element_present_page                                ="Element present page ";
  public static String text_element_not_found                                   =" Element not found ";
  public static String text_element_string_empty                                =" Element string empty.";
  public static String text_element_enable                                      ="ELEMENT ENABLE: ";
  public static String text_select_has_no_selected_item                         ="select has no selected item";
  public static String text_value_button                                        ="value button: ";
  public static String text_null                                                =" text null.";
  public static String text_element_null                                        ="element null. "; 
  public static String text_select_text                                         ="text select ";
  public static String text_failed_to_download_expected_document                ="Failed to download Expected document whith name:";
  public static String text_parth                                               ="parth:";
  public static String text_not_found_day                                       ="not found day ";
  public static String text_not_found_year                                      ="not found Year ";
  public static String text_not_found_month                                     ="not found Month ";   
  public static String text_in_mounth                                           =" in mounth: ";
  public static String text_year                                                =" year ";
  public static String text_flight_not_found                                    ="Flight not found.";
  public static String text_button_upload_txt                                   ="button upload txt";
  public static String text_inside_the_block                                    ="inside the block ";
  public static String text_element_with_the_tagname_does_not_exist             =" the element with the tagname does not exist: ";
  public static String text_and_attribute                                       =" and attribute ";
  public static String text_contains_text                                       =" contains text: ";
  public static String text_contains                                            =" contains: ";
  public static String text_explication                                         =" explication:";
  public static String text_id                                                  =" ID:";
  public static String text_with                                                =" with ";
  public static String text_with_id                                             =" with id: ";
  public static String text_in                                                  =" in ";
  public static String text_get_value_elemet_field                              ="get value elemet field: ";
  public static String text_could_not_leave_blank_in_input                      ="could not leave blank in input: ";
  public static String text_is_not_status                                       ="is not status: ";
  public static String text_and_must_bring_the_text                             =" and must bring the text: ";
  public static String text_was_expected                                        ="... Was expected: ";
  public static String text_selects                                             =" SELECT:";
  public static String text_the_text_should_not_be_present_on_the_page          ="the text should not be present on the page: ";
  public static String text_end_while_and_checkbox_not_found                    ="END WHILE and checkbox not found: ";
  public static String text_checkbox_is_false_and_should_be_true_of             ="checkbox is FALSE and should be TRUE of  ";
  public static String text_checkbox_of                                         ="checkbox of ";
  public static String text_is_true_and_should_be_false                         =" is TRUE and should be FALSE ";
  public static String text_error_capturing_path                                ="error capturing path";
  public static String text_error_capturing_message                             ="error capturing message";
  public static String text_error_fetching_method_name                          ="error fetching method name"; 
  public static String text_error_exception                                     ="Error Exception";
  public static String text_external_explanation_is_missing                     ="external explanation is missing";  
  
  public static String text_software                                            ="(SOFTWARE): ";
  public static String text_with_selenium                                       =" with (SELENIUM): ";
  public static String text_doesnt_match_selenium                               =" Text doesn't match: (SELENIUM): ";
  public static String text_doesnt_match_software                               =" Text doesn't match (SOFTWARE): ";
  public static String text_does_not_match_the_alert_software                   ="The text does not match the alert (SOFTWARE): ";
  public static String text_button_doesnt_match                                 =" Text button doesn't match: ";   
  public static String text_doesnt_contains                                     =" Text doesn't contains: ";
  public static String text_test_boolean_doesnt_match                           ="Test, boolean doesn't match: "; 
  
  public static String text_case_entry_to_the_method                            ="CASE, entry to the ";
  public static String text_case_found_the_element                              ="CASE, found the element ";
  
  public static String text_success_pass_find_element                           ="SUCCESS, Pass findElement: ";   
  public static String text_success_pass_method                                 ="SUCCESS, PASS ";
  public static String text_success_click_to_pop_up_window_text                 ="success, click to: pop up window text: ";
  public static String text_success_click_cancel_to_pop_up_window               ="success, click cancel to: pop up window ";
  public static String text_success_get_text_alert_pop_up_window                ="success, get text alert: pop up window";
  public static String text_success_select_from_the_list                        ="SUCCESS, select from the list : ";
  public static String text_success_text_doesnt_match                           ="SUCCESS, Text doesn't match: ";
  public static String text_success_element_disabled                            ="SUCCESS, element disabled : ";
  public static String text_success_the_checkbox_is_true                        ="SUCCESS, the checkbox is true ";
  public static String text_success_the_checkbox_is_false                       ="SUCCESS, the checkbox is false ";   
  public static String text_success_set_text                                    ="SUCCESS set text ";
  public static String text_success_text                                        ="SUCCESS, text:";
  public static String text_success_click_to                                    ="success, click to: "; 
  public static String text_success                                             ="SUCCESS";
  public static String text_error                                               ="ERROR";
  
  public static String text_error_software                                      ="ERROR, (SOFTWARE): ";
  public static String text_error_element_present_page                          ="ERROR, element present page: ";
  public static String text_error_the_text_should_not_be_present_on_the_page    ="ERROR, the text should not be present on the page: "; 
  public static String text_error_alert_not_present                             ="ERROR, alert not present: ";
  public static String text_error_alert_present                                 ="ERROR, alert present: "; 
  public static String text_error_element_not_found_button                      ="ERROR, Element not found button:";
  public static String text_error_element_enabled                               ="ERROR, element enabled : "; 
  public static String text_error_not_found_button                              ="ERROR, not found button ";   
  public static String text_error_element_not_found_checkbox                    ="ERROR, Element not found checkbox: ";
  public static String text_error_not_enable_the_checkbox                       ="ERROR, not enable the checkbox "; 
  public static String text_error_element_stale_button                          ="ERROR, Element STALE button: "; 
  public static String text_error_element_stale                                 ="ERROR, Element STALE: ";
  public static String text_error_list_element_stale                            ="ERROR, List Element STALE: ";
  public static String text_error_list_element_not_found                        ="ERROR, List Element not found: ";
  public static String text_error_list_element_not_found_id_dinamic             ="ERROR, List Element not found id dinamic: ";
  public static String text_error_list_element_not_enabled_cycle_number         ="ERROR, List Element not Enabled cycle number: ";
  public static String text_error_list_element_enabled_cycle_number             ="ERROR, List Element Enabled cycle number: ";            
  public static String text_error_not_found_List_id_dinamic                     ="ERROR, not found List id dinamic: ";
  public static String text_error_not_found_element_id_dinamic                  ="ERROR, not found  Element id dinamic: "; 
  public static String text_error_list_select_element_not_found                 ="ERROR, List Select Element not found :";
  public static String text_error_text_select_of_list_doesnt_match_software     ="ERROR, Text select of list doesn't match SOFTWARE: ";
  public static String text_error_select_disabled                               ="ERROR, select disabled : "; 
  public static String text_error_text_doesnt_match_the_select                  ="ERROR, Text doesn't match the select: "; 
  public static String text_error_select_not_found_value_or_selected            ="ERROR, Select not found value or selected:";
  public static String text_error_select_element_stale                          ="ERROR, select Element STALE: ";  
  public static String text_error_text_doesnt_match                             ="ERROR, Text doesn't match ";
  public static String text_error_element_not_found                             ="ERROR, Element not found: "; 
  public static String text_error_not_found                                     ="ERROR, not found the ";
  public static String text_error_not_found_list                                ="ERROR, not found List ";
  public static String text_error_time_cycle                                    ="ERROR, TIME CYCLE: ";
  public static String text_error_time_accumulated                              ="ERROR, TIME ACCUMULATED: ";
  public static String text_error_not_select_the_checkbox                       ="ERROR, not select the checkbox ";
  public static String text_error_not_select                                    ="ERROR, not Select the "; 
  public static String text_error_text_null                                     ="ERROR, Text null: ";
  public static String text_error_value_null                                    ="ERROR, VALUE NULL: ";
  public static String text_error_get_instance_is_empty                         ="ERROR, getInstance is empty: ";
  public static String text_error_list_not_have_items                           ="ERROR, List not have items :";
  public static String text_error_text_null_or_empty                            ="ERROR, Text null or empty: ";
  public static String text_error_text_element_by_is_empty                      ="ERROR: text element by is empty: ";  
  public static String text_error_text_doesnt_contains                          ="ERROR, Text doesn't contains: ";
  public static String text_error_not_found_the_file                            ="ERROR, not found the file ";
  public static String text_error_flight_not_found                              ="ERROR, Flight not found. ";
  public static String text_error_button                                        ="ERROR: button ";
  public static String text_error_end_while_element_not_found                   ="ERROR, END WHILE Element not found :";
  public static String text_error_the_checkbox_is_active                        ="ERROR, the checkbox is active ";  
  
  public static String text_cycle_number                                        =" cycle number: ";
  public static String text_cycle                                               =" cycle ";
  public static String text_milliseconds_and_time_accumulated                   =" milliseconds AND TIME ACCUMULATED: ";
  public static String text_milliseconds                                        =" milliseconds";
  
  public static String text_nosuch_element_exception                            =" NoSuchElementException";
  public static String text_stale_element_reference_exception                   =" StaleElementReferenceException";
  public static String text_exception                                           =" Exception";
  
  ////============================== TYPE LABELS ===============================
  /**
   * These variables are used in test automation to locate and interact with
   * different HTML elements present on a web page.
   */
  public static String       element_type_input                                 = "input";
  public static String       element_type_img                                   = "img";
  public static String       element_type_td                                    = "td";
  public static String       element_type_id                                    = "id";
  public static String       element_type_select                                = "select";
  public static String       element_type_button                                = "button";
  public static String       element_type_tr                                    = "tr";
  public static String       element_type_span                                  = "span";
  public static String       element_type_div                                   = "div";
  public static String       element_type_a                                     = "a";
  public static String       element_type_li                                    = "li";
  public static String       element_type_table                                 = "table"; 
  public static String       element_type_tbody                                 = "tbody"; 
  public static String       attribute_type                                     = "type";
  public static String       attribute_type_checkbox                            = "checkbox"; 
  public static String       attribute_type_radio                               = "radio";
  public static String       attribute_type_id                                  = "id"; 
  public static String       attribute_type_class                               = "class";  
  public static String       attribute_type_style                               = "style";  
  public static String       attribute_type_src                                 = "src";
  public static String       element_type_b                                     = "b";
  
  public static String       INPUT_CLASS_ONFOCUS_ACTIVE                         = "onfocus_class active";  
  public static String       INPUT_CLASS_ONFOCUS                                = "onfocus_class";  
  public static String       INPUT_CLASS_FORM                                   = "form_input";  
  
  public static String       CLASS_LIST_ITEM_OUT                                = "list_item_out"; 
  public static String       CLASS_LIST_ITEM_OVER                               = "list_item_over"; 
   
  public element_manager()
  {
  }

  
  public static void sconds_sleep(int milliseconds)
  {
    try
    {
      Thread.sleep(milliseconds);
    }
    catch (InterruptedException ex)
    {
    }
  }

  /**
   * Prints a debug log message with the current date and time.
   *
   * @param text_log:string - The log message to be printed.
   */
  public static void debug_log(String text_log)
  {
    System.out.println(text_log);
  }

  public static String get_method_line()
  {
    String error_method = "";
    String test_or_method = "";
    String line_break = "\n";
    String line_break_two = "";
    try
    {
      StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
      int count_two = 1;

      for (StackTraceElement ste : stackTraceElements)
      {

        if (ste.toString().contains(text_ink_testing_source)
          || ste.toString().contains(text_test_cases))
        {
          if (!ste.getMethodName().equals("get_assert_or_message")
            && !ste.getMethodName().equals("get_method_line"))
          {
            String calss_name = ste.getFileName();
            String[] class_names = calss_name.split("\\.");
            String method_name = ste.getMethodName();
            int line_number = ste.getLineNumber();

            if (method_name.matches(".*\\d.*") && !method_name.contains("case_"))
            {
              test_or_method = "Test";
              line_break_two = "\n";
            }
            else
            {
              test_or_method = text_method;
            }

            error_method = line_break + line_break_two + "|" + class_names[0] + "(" + test_or_method + " : "
              + method_name + " in line : " + line_number + ")" + error_method;
          }
          count_two++;
        }
        if (count_two > 1)
        {
          if (!ste.toString().contains(text_ink_testing_source))
          {
            break;
          }
        }
      }
    }
    catch (Exception e)
    {
      error_method = "\n" + text_error_capturing_path;
    }
    return error_method;
  }

  
  public static String get_method_name()
  {
    String current_method = "";
    try
    {
      Throwable throwable = new Throwable();
      current_method = throwable.getStackTrace()[2].getMethodName();
    }
    catch (Exception e)
    {
      current_method = text_error_fetching_method_name + "\n" + e.getMessage();
      get_assert_or_message("", true, false, null, false, "");
    }
    return current_method;
  }
  
  public static void get_assert_or_message(String message, boolean have_assert_or_message,
    boolean have_assert_true_not_null, WebElement value_assert_not_null, boolean value_assert_true,
    String alt_text)
  {
    try
    {
      if (have_assert_or_message)
      {
        if (have_assert_true_not_null)
        {
          Assert.assertNotNull(value_assert_not_null, message + ", " + text_method + ": " + get_method_name() + " " + alt_text + "\n" + get_method_line() + "\n");
        }
        else
        {
          Assert.assertTrue(value_assert_true, message + ", " + text_method + ": " + get_method_name() + " " + alt_text + "\n" + get_method_line() + "\n");
        }
      }
      else
      {
        debug_log(message + " " + text_method + ": " + get_method_name() + " " + alt_text);
      }
    }
    catch (Exception e)
    {
      debug_log("\n" + text_error_capturing_message + "\n" + e.getMessage());
      Assert.assertTrue(false, get_method_line() + "\n");
    }
  }

  public static void get_assert_not_null(String message, List<WebElement> list, String value, boolean value_or_list)
  {
    try
    {
      if (value_or_list)
      {
        Assert.assertNotNull(value, message + ", " + text_method + ": " + get_method_name() + "\n" + get_method_line());
      }
      else
      {
        Assert.assertNotNull(list, message + ", " + text_method + ": " + get_method_name() + "\n" + get_method_line());
      }
    }
    catch (Exception e)
    {
      debug_log("\n" + text_error_exception + "\n" + e.getMessage());
      Assert.assertTrue(false, get_method_line() + "\n");
    }
  }

  public static void assert_error(String message)
  {
    try
    {
      Assert.assertTrue(false, message + ", " + text_method + ": " + get_method_name() + "\n" + get_method_line());
    }
    catch (Exception e)
    {
      debug_log("\n" + text_error_exception + "\n" + e.getMessage());
      Assert.assertTrue(false, get_method_line() + "\n");
    }
  }

  /**
   * Logs a message using the debug_log() method.
   *
   * @param message:string - The message to be logged.
   */
  public static void get_message_log(String message)
  {
    try
    {
      debug_log(message + " " + text_method + ": " + get_method_name());
    }
    catch (Exception e)
    {
      debug_log("\n" + text_error_exception + "\n" + e.getMessage());
      Assert.assertTrue(false, get_method_line() + "\n");
    }
  }

}
