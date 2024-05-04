/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recording_test;

import static com.google.common.io.Files.copy;
import ink_testing_source.configuration.configuration_server;
import ink_testing_source.configuration.element_manager;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import org.apache.commons.io.FileUtils;
import org.monte.media.*;
import org.monte.media.FormatKeys.MediaType;
import static org.monte.media.VideoFormatKeys.*;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

/**
 *
 * @author User
 */
public class custom_screen_recorder extends ScreenRecorder
{

  private final String hours_date = "_Date(" + LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue()
    + "-" + LocalDateTime.now().getDayOfMonth() + ")_Hour(" + LocalDateTime.now().getHour() + "-" + LocalDateTime.now().getMinute()
    + "-" + LocalDateTime.now().getSecond() + ")";
  private String file_name;
  private String class_file_name;
  private File current_file;

  /**
   * Initializes a custom screen recorder with the following settings: -
   * Captures the screen with a black background. - Saves the recording as an
   * AVI file. - Uses the "TechSmith Screen Capture" codec for video
   * compression. - Sets the frame rate to 30 frames per second for the black
   * background video.
   *
   * @throws IOException if an I/O error occurs.
   * @throws AWTException if the platform configuration does not allow low-level
   * input control.
   */
  public custom_screen_recorder() throws IOException, AWTException
  {
    super(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(),
      new Rectangle(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height),
      new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_AVI),
      new Format(MediaTypeKey, FormatKeys.MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, CompressorNameKey,
        ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey, Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
      new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
      null);
  }

  /**
   * Creates a movie file for the screen recording.
   *
   * @param file_format:Format - The format of the movie file.
   * @return The created movie file.
   * @throws IOException if an I/O error occurs.
   */
  @Override
  public File createMovieFile(Format file_format) throws IOException
  {
    movieFolder = new File(configuration_server.PATCH_VIDEO + this.class_file_name);

    movieFolder.mkdirs();

    String name_file = this.file_name + "." + Registry.getInstance().getExtension(new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_AVI));
    String path_file = movieFolder.getAbsolutePath() + File.separator + name_file;
    this.current_file = get_file_with_unique_name(path_file);
    return this.current_file;
  }

  /**
   * Deletes the directory containing video files.
   *
   * @throws IOException if an I/O error occurs.
   */
  public static void delete_directory_video() throws IOException
  {
    FileUtils.deleteDirectory(new File(configuration_server.PATCH_VIDEO));
  }

  /**
   * Generates a unique file name by appending a counter to the original file
   * name in case a file with the same name already exists.
   *
   * @param file_name:string - The desired file name with extension.
   * @return A File object representing the unique file name.
   */
  private File get_file_with_unique_name(String file_name)
  {
    String extension;
    String name;

    int idxOfDot = file_name.lastIndexOf('.'); //// Get the last index of . to separate extension
    extension = file_name.substring(idxOfDot + 1);
    name = file_name.substring(0, idxOfDot);

    Path path = Paths.get(file_name);
    int counter = 1;
    while (Files.exists(path))
    {
      file_name = name + "-" + counter + "." + extension;
      path = Paths.get(file_name);
      counter++;
    }
    return new File(file_name);
  }

  /**
   * Starts the screen recording process with the specified file name and option
   * to capture the mouse cursor.
   *
   * @param file_name:string - The desired file name for the recording.
   * @param capture_mouse:boolean - true to capture the mouse cursor, false
   * otherwise.
   * @throws IOException if an I/O error occurs.
   */
  public void start_recording(String file_name, boolean capture_mouse) throws IOException
  {
    this.file_name = file_name;
    start();
  }

  /**
   * Stops the screen recording process and optionally deletes the recording
   * file.
   *
   * @param keep_file:boolean - false to keep the recording file, true to delete
   * it.
   * @throws IOException if an I/O error occurs.
   */
  public void stop_recording(boolean keep_file) throws IOException
  {
    stop();
    if (keep_file)
    {
      delete_recording();
    }
  }

  /**
   * Stops the screen recording process.
   *
   * @throws IOException if an I/O error occurs.
   */
  public void stop_recordings() throws IOException
  {
    stop();
  }

  /**
   * Deletes the current screen recording file.generates a message if it is not
   * removed
   */
  private void delete_recording()
  {
    boolean deleted = false;
    try
    {
      if (current_file.exists())
      {
        deleted = current_file.delete();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    if (deleted)
    {
      current_file = null;
    }
    else
    {
      System.out.println("Could not delete the screen-record!");
    }
  }

  /**
   * Copies and moves files to specified destinations used for the allure report
   * to the allure-results folder and the files to open the allure report on
   * windows and ubuntu systems
   */
  public void file_copy_move()
  {
    File source_file;
    File source_file_bat;
    File file_destination_bat;
    File new_file_destination;

    try
    {
      if (configuration_server.GITHUB_ACTIONS == false)
      {
        source_file_bat = new File(System.getProperty("user.dir") + "/src/test/resources/");
      }
      else
      {
        source_file_bat = new File("src/test/resources/");
      }
      source_file = new File(configuration_server.SOURCE_FILES);
      file_destination_bat = new File(configuration_server.PATCH_VIDEO + "/Report_html");
      new_file_destination = new File(file_destination_bat + "/allure-results/");

      if (new_file_destination != null && source_file != null)
      {

        movieFolder = new_file_destination;

        if (!movieFolder.exists())
        {
          movieFolder.mkdirs();
        }

        for (String f : source_file.list())
        {
          ////Copy files from source directory to destination.
          copy(new File(source_file, f), new File(new_file_destination, f));
        }

        FileUtils.deleteDirectory(source_file);

        for (String f : source_file_bat.list())
        {
          ////Copy .bat from source directory to destination.
          if (f.equals("ViewReportWin.bat") || f.equals("ViewReportUbuntu.sh"))
          {
            copy(new File(source_file_bat, f), new File(file_destination_bat, f));
          }
        }
        element_manager.debug_log("File moved successfully");
      }
      else
      {
        element_manager.get_assert_or_message("null routes", true, true, null, false, "");
      }
    }
    catch (IOException e)
    {
      element_manager.debug_log("Error " + e.getMessage());
      element_manager.debug_log("Failed to move the files");
      element_manager.assert_error("Failed moving files to specified path");
    }
  }

  /**
   * Gets the value of the HOURS_DATE variable.
   *
   * @return The value of the HOURS_DATE variable.
   */
  public String get_hours_date()
  {
    return hours_date;
  }

  /**
   * Sets the value of the class_file_name variable.
   *
   * @param class_file_name:string - The value to set for the class_file_name
   * variable.
   */
  public void set_class_file_name(String class_file_name)
  {
    this.class_file_name = class_file_name;
  }

}
