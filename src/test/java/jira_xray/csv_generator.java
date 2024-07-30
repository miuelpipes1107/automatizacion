/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jira_xray;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Aurora
 */
public class csv_generator
{

  public static void generateCSV(String filePath, String summary, String description, String issueTypeId, List<String> testSteps)
  {
    try (FileWriter writer = new FileWriter(filePath))
    {
      // Escribir la cabecera del CSV
      writer.append("Summary,Description,Issue Type,Test Steps\n");

      // Crear la cadena de los pasos del test
      StringBuilder stepsDescription = new StringBuilder();
      for (String step : testSteps)
      {
        stepsDescription.append(step).append("\\n"); // Escapar nueva línea para el CSV
      }

      // Escribir los detalles del issue
      writer.append(summary).append(',')
        .append(description).append(',')
        .append(issueTypeId).append(',')
        .append(stepsDescription.toString()).append('\n');
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

}
