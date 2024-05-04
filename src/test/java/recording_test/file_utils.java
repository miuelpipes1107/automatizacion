package recording_test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class file_utils
{

  /**
   * Writes the lines of a list to a file, starting from a specified index.
   *
   * @param file_name:string - The name of the file to write to.
   * @param lines:List(string) - The list of lines to write to the file.
   * @param start_index:int - The index to start writing from in the list.
   */
  public static void write_file_to_list(String file_name, List<String> lines, int start_index)
  {

    try (FileWriter file_writer = new FileWriter(file_name); 
      BufferedWriter buffered_writer = new BufferedWriter(file_writer))
    {

      for (int i = start_index; i < lines.size(); i++)
      {
        buffered_writer.write(String.valueOf(lines.get(i)));
        buffered_writer.newLine();
      }
    }
    catch (IOException e)
    {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  /**
   * Reads the lines of a file and returns them as a list of strings.
   *
   * @param file_name:string - The name of the file to read.
   * @return A list of strings representing the lines of the file.
   */
  public static List<String> read_file_to_list(String file_name)
  {
    ArrayList<String> cadena = new ArrayList<>();
    try (BufferedReader buffered_reader = new BufferedReader(new FileReader(file_name)))
    {
      String line;
      while ((line = buffered_reader.readLine()) != null)
      {
        cadena.add(line);
      }
    }
    catch (FileNotFoundException e)
    {
      System.out.println("File not found: " + file_name);
    }
    catch (IOException e)
    {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return cadena;
  }

}
