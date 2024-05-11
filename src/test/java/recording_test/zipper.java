package recording_test;

import ink_testing_source.configuration.configuration_server;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class zipper
{

  ZipOutputStream output;

  /**
   * Constructor for the `zipper` class. Creates a new instance of the class and
   * takes a `File` object as a parameter, representing the zip file to be
   * created. Creates a `File` object `aws` that points to the directory
   * specified by `configuration_universal_server.PATCH_AWS`. If the `aws` directory
   * does not exist, it is created using the `mkdirs()` method. Creates a
   * `ZipOutputStream` object that is initialized with a `FileOutputStream`
   * object pointing to the specified zip file. The `ZipOutputStream` is
   * responsible for writing the zip entries and their corresponding data to the
   * output stream. This enables the creation of a zip file by adding entries
   * and their contents.
   *
   * @param zip:File - The file object representing the zip file to be created.
   * @throws FileNotFoundException If the specified file is not found.
   */
  public zipper(File zip) throws FileNotFoundException
  {
    File aws = new File(configuration_server.PATCH_AWS);    
    
    if (!aws.exists())
    {
      aws.mkdirs();
    }
    
    this.output = new ZipOutputStream(new FileOutputStream(zip));
  }  

  /**
   * Compresses a file into a ZIP archive.
   *
   * @param file:File - The file to be compressed.
   * @return True if the file was successfully compressed, false otherwise.
   */
  private boolean zip_file(File file)
  {
    try
    {
      byte[] buf = new byte[1024];
      output.putNextEntry(new ZipEntry(file.getPath()));
      FileInputStream fis = new FileInputStream(file);
      int len;
      while ((len = fis.read(buf)) > 0)
      {
        output.write(buf, 0, len);
      }
      fis.close();
      output.closeEntry();
      return true;
    }
    catch (IOException e)
    {
      //// All Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  /**
   * compress the routes.
   *
   * @param file:File - The file to be compressed.
   * @return true if the compression is successful; otherwise, false.
   */
  private boolean zip_dir(File file)
  {
    try
    {
      output.closeEntry();
      return true;
    }
    catch (IOException e)
    {
      //// All Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Add the files to the list of their paths.
   *
   * @param files:File - The files to be added.
   * @return True if the files successfully added, false otherwise.
   */
  private boolean add(File... files)
  {
    for (File file : files)
    {
      if (file.isDirectory())
      {
        zip_dir(file);
        add(file.listFiles());
      }
      else
      {
        zip_file(file);
      }
    }
    return true;
  }

  /**
   * Adds the files to the list of their paths, finalize and close the ZIP
   * output stream and release any system resources.
   *
   * @param files:File - The files to be added archive.
   * @throws IOException If an I/O error occurs during the process.
   */
  void zip(File... files) throws IOException
  {
    add(files);
    output.finish();
    output.close();
  }

}
