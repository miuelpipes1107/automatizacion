package ink_testing_source.configuration;

import java.awt.AWTException;
import java.io.IOException;
import org.testng.annotations.Listeners;
import recording_test.test_listener;

/**
 *
 * @author felipe
 */
/**
 * The @Listeners(test_listener.class) annotation is used to associate a test
 * listener with a test class or test suite. In this case, the test_listener
 * class is specified as the listener. A test listener is responsible for
 * monitoring the execution of tests and performing certain actions before,
 * during, or after test execution
 */
@Listeners(test_listener.class)
/**
 * This is an abstract class that defines a method to be executed after each
 * test class. Subclasses must implement the 'method_after_class()' method to
 * specify the actions to be taken.
 */
public abstract class method_after_before
{

  public abstract void method_after_class() throws IOException, AWTException;

}
