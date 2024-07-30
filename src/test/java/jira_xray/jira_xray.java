/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jira_xray;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ink_testing_source.configuration.browser_manager;
import ink_testing_source.configuration.configuration_server;
import io.qameta.allure.Allure;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import java.util.ArrayList;
import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import java.util.Iterator;
/**
 *
 * @author Aurora
 */
public class jira_xray
{

  public static final int MAX_WAIT_TIME = 30; // Maximum wait time in seconds

  public void jira_xray()
  {
  }

  public void create_jira_issue(String summary, String description, String issueTypeId, CloseableHttpClient httpClient) throws IOException
  {
    List<String> issue_information = new ArrayList<>();
    browser_manager.id_issue_xray = search_issue_by_summary(summary, httpClient);
    String token_xray = getAuthToken(httpClient);
//    isTokenValid(httpClient,token_xray);

//	   // Generar el CSV con los pasos del test
////    List<String> testSteps = test_step_logger.getTestSteps();
//    String csvFilePath = "xray/steps.csv"; // Cambia esta ruta a donde quieras guardar el CSV
//    csv_generator.generateCSV(csvFilePath, summary, description, issueTypeId, testSteps);
    if (browser_manager.id_issue_xray == null)
    {

      HttpPost httpPost = new HttpPost(configuration_server.JIRA_URL + configuration_server.JIRA_API_ENDPOINT);

      String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
      byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
      String authHeader = "Basic " + new String(encodedAuth);
      httpPost.setHeader("Authorization", authHeader);
      httpPost.setHeader("Content-Type", "application/json");

      // Obtener los pasos del test
      JSONArray stepsArray = new JSONArray();
      for (int i = 0; i < test_step_logger.list_testSteps.size(); i++)
      {        
        for (int j = 1; j < test_step_logger.list_testSteps.get(i).size(); j++)
        {
          if (test_step_logger.list_testSteps.get(i).get(0).get(0).equals(summary))
          {
            JSONObject stepObject = new JSONObject();
            stepObject.put("step", test_step_logger.list_testSteps.get(i).get(j).get(0).replace("_", " "));
            stepObject.put("data", test_step_logger.list_testSteps.get(i).get(j).get(1));
            stepObject.put("result", test_step_logger.list_testSteps.get(i).get(j).get(2));
            stepsArray.put(stepObject);
          }
        }
        test_step_logger.list_testSteps.remove(i);
        break;
      }

      JSONObject issueDetails = new JSONObject();
      JSONObject fields = new JSONObject();
      fields.put("summary", summary);
      String step = "";
      for (int i = 0; i < stepsArray.length(); i++)
      {
        step += i + 1 + "_Step :" + stepsArray.getJSONObject(i).getString("step")
          + "-Data: " + stepsArray.getJSONObject(i).getString("data")
          + "-Result: " + stepsArray.getJSONObject(i).getString("result") + "\n";
      }
      fields.put("description", step);
      fields.put("issuetype", new JSONObject().put("id", issueTypeId));
      fields.put("project", new JSONObject().put("key", "AUT"));
      issueDetails.put("fields", fields);
      issueDetails.put("definicion", stepsArray);

      StringEntity entity = new StringEntity(issueDetails.toString());
      httpPost.setEntity(entity);

      try (CloseableHttpResponse response = httpClient.execute(httpPost))
      {
        String responseString = EntityUtils.toString(response.getEntity());
        System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
        System.out.println("Response Body: " + responseString);
        JSONObject responseString_objet = new JSONObject(responseString);
        browser_manager.id_issue_xray = responseString_objet.getString("key");

//        add_test_to_execution(httpClient,"AUT-125",browser_manager.id_issue_xray);
//        if (responseString.startsWith("["))
//        {
//          // Si la respuesta es un JSONArray
//          JSONArray fieldss = new JSONArray(responseString);
//
//          for (int i = 0; i < fields.length(); i++)
//          {
//            JSONObject field = fieldss.getJSONObject(i);
//            System.out.println("ID: " + field.getString("id") + ", Name: " + field.getString("name"));
//          }
//        }
//        else if (responseString.startsWith("{"))
//        {
//          // Si la respuesta es un JSONObject
//          JSONObject field = new JSONObject(responseString);
//          System.out.println("ID: " + field.getString("id") + ", Name: " + field.getString("name"));
//        }
//        else
//        {
//          System.out.println("Unexpected response format");
//        }
        if (browser_manager.id_issue_xray != null)
        {
          issue_information.add(browser_manager.id_issue_xray);
          issue_information.add(summary);
          configuration_server.matrix_issues.add(issue_information);
          add_test_to_execution(httpClient, "AUT-3", browser_manager.id_issue_xray, token_xray);

          for (int i = 0; i < configuration_server.matrix_issues.size(); i++)
          {
            String id = configuration_server.matrix_issues.get(i).get(0);
            String name_usse = configuration_server.matrix_issues.get(i).get(1);
          }
        }
      }
    }
    else
    {
      issue_information.add(browser_manager.id_issue_xray);
      issue_information.add(summary);
      configuration_server.matrix_issues.add(issue_information);

      for (int i = 0; i < configuration_server.matrix_issues.size(); i++)
      {
        String id = configuration_server.matrix_issues.get(i).get(0);
        String name_usse = configuration_server.matrix_issues.get(i).get(1);
      }
    }
  }
  
  // Autenticación básica para Jira
  private static String getAuthHeader()
  {
    String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
    byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
    return "Basic " + new String(encodedAuth);
  }
  
  public static void getIssueCreateMeta(CloseableHttpClient httpClient) throws IOException
  {
    String META_URL = configuration_server.JIRA_URL + "/rest/api/2/issue/createmeta";
    HttpGet httpGet = new HttpGet(META_URL);
    httpGet.setHeader("Authorization", getAuthHeader());
    httpGet.setHeader("Content-Type", "application/json");

    try (CloseableHttpResponse response = httpClient.execute(httpGet))
    {
      String responseString = EntityUtils.toString(response.getEntity());
      JSONObject jsonResponse = new JSONObject(responseString);
      System.out.println(jsonResponse.toString(2));  // Imprimir la respuesta con formato
    }
  }
  
  // Obtener la lista de campos disponibles para un tipo de issue específico
  public static void getIssueTypeFields(CloseableHttpClient httpClient) throws IOException
  {
    String ISSUE_TYPE_URL = configuration_server.JIRA_URL + "/rest/api/2/issue/createmeta?projectKeys=AUT&expand=projects.issuetypes.fields";
    HttpGet httpGet = new HttpGet(ISSUE_TYPE_URL);
    httpGet.setHeader("Authorization", getAuthHeader());
    httpGet.setHeader("Content-Type", "application/json");

    try (CloseableHttpResponse response = httpClient.execute(httpGet))
    {
      String responseString = EntityUtils.toString(response.getEntity());
      System.out.println("Respuesta completa de la API: " + responseString);

      JSONObject jsonResponse = new JSONObject(responseString);
      JSONArray projects = jsonResponse.getJSONArray("projects");

      for (int i = 0; i < projects.length(); i++)
      {
        JSONObject project = projects.getJSONObject(i);
        JSONArray issueTypes = project.getJSONArray("issuetypes");

        for (int j = 0; j < issueTypes.length(); j++)
        {
          JSONObject issueType = issueTypes.getJSONObject(j);
          String issueTypeName = issueType.getString("name");

          System.out.println("Campos para el tipo de issue: " + issueTypeName);

          JSONObject fields = issueType.getJSONObject("fields");
          for (String fieldKey : fields.keySet())
          {
            JSONObject fieldDetails = fields.getJSONObject(fieldKey);
            System.out.println("Nombre del campo: " + fieldDetails.getString("name") + ", ID del campo: " + fieldKey);
          }
        }
      }
    }
  }
  
  public static void getCustomFields(CloseableHttpClient httpClient) throws IOException
  {
    String CUSTOM_FIELDS_URL = configuration_server.JIRA_URL + "/rest/api/2/field";
    HttpGet httpGet = new HttpGet(CUSTOM_FIELDS_URL);
    httpGet.setHeader("Authorization", getAuthHeader());
    httpGet.setHeader("Content-Type", "application/json");

    try (CloseableHttpResponse response = httpClient.execute(httpGet))
    {
      String responseString = EntityUtils.toString(response.getEntity());
      JSONArray jsonResponse = new JSONArray(responseString);

      // Buscar el campo "Definición" en la lista de campos
      for (int i = 0; i < jsonResponse.length(); i++)
      {
        JSONObject field = jsonResponse.getJSONObject(i);
//        if (field.getString("name").equalsIgnoreCase("Definición"))
//        {
          System.out.println("ID del campo : "+field.getString("name")+" :" + field.getString("id"));
//          break;
//        }
      }
    }
  }
  
  // Obtener un token de autenticación de Xray
  public static String getAuthToken(CloseableHttpClient httpClient) throws IOException
  {
    String authUrl = configuration_server.XRAY_AUTH_URL + "/api/v2/authenticate";
    HttpPost httpPost = new HttpPost(authUrl);
    httpPost.setHeader("Content-Type", "application/json");

    // Credenciales en formato JSON    
    JSONObject authPayload = new JSONObject();
    authPayload.put("client_id", configuration_server.CLIENT_ID_XRAY);
    authPayload.put("client_secret", configuration_server.CLIENT_SECRET_XRAY);

    httpPost.setEntity(new StringEntity(authPayload.toString()));

    try (CloseableHttpResponse response = httpClient.execute(httpPost))
    {
      String responseString = EntityUtils.toString(response.getEntity());
      if (response.getStatusLine().getStatusCode() == 200)
      {
        return responseString.replace("\"", ""); // Este es el token de autenticación
      }
      else
      {
        throw new IOException("Error al obtener el token de autenticación: " + responseString);
      }
    }
  }
  
  public static boolean isTokenValid(CloseableHttpClient httpClient, String authToken) throws IOException
  {
    HttpGet httpGet = new HttpGet(configuration_server.XRAY_AUTH_URL + "/api/v2/testexec");
    httpGet.setHeader("Authorization", "Bearer " + authToken);
    httpGet.setHeader("Content-Type", "application/json");

    try (CloseableHttpResponse response = httpClient.execute(httpGet))
    {
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == 200)
      {
        // El token es válido
        return true;
      }
      else
      {
        // El token es inválido o ha expirado
        System.out.println("Error: " + statusCode + " - " + EntityUtils.toString(response.getEntity()));
        return false;
      }
    }
  }
  
  // Asociar un test a una ejecución
  public static void add_test_to_execution(CloseableHttpClient httpClient, String testExecKey, String testKey, String authToken) throws IOException
  {
    String ADD_TEST_TO_EXECUTION_URL = "https://xray.cloud.getxray.app/api/v2/testexec/" + testExecKey + "/test";
    HttpPost httpPost = new HttpPost(ADD_TEST_TO_EXECUTION_URL);
    httpPost.setHeader("Authorization", "Bearer " + authToken);
    httpPost.setHeader("Content-Type", "application/json");

    JSONArray testsArray = new JSONArray();
    testsArray.put(testKey);

    JSONObject requestPayload = new JSONObject();
    requestPayload.put("add", testsArray);

    httpPost.setEntity(new StringEntity(requestPayload.toString()));

    try (CloseableHttpResponse response = httpClient.execute(httpPost))
    {
      int statusCode = response.getStatusLine().getStatusCode();
      String responseString = EntityUtils.toString(response.getEntity());
      if (statusCode == 200)
      {
        System.out.println("Test agregado exitosamente: " + responseString);
      }
      else
      {
        System.out.println("Error al agregar test: " + responseString);
      }
    }
  }
  
  public String search_issue_by_summary(String summary, CloseableHttpClient httpClient) throws IOException
  {

    String jqlQuery = null;
    try
    {
      jqlQuery = URLEncoder.encode("summary~\"" + summary + "\"", "UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
      return null;
    }

    HttpGet httpGet = new HttpGet(configuration_server.JIRA_URL + "/rest/api/2/search?jql=" + jqlQuery);

    String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
    byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
    String authHeader = "Basic " + new String(encodedAuth);
    httpGet.setHeader("Authorization", authHeader);
    httpGet.setHeader("Content-Type", "application/json");

    try (CloseableHttpResponse response = httpClient.execute(httpGet))
    {
      String responseString = EntityUtils.toString(response.getEntity());
      JSONObject jsonResponse = new JSONObject(responseString);
      if (jsonResponse.getInt("total") > 0)
      {
        return jsonResponse.getJSONArray("issues").getJSONObject(0).getString("key");
      }
    }

    return null;
  }
  
  public void attach_png_files_to_issues(List<List<String>> list_cases, CloseableHttpClient httpClient) throws IOException
  {

    String issueKey = null;
    String name_issue = null;
    // Paso 1: Buscar archivos .png en la ruta especificada
    try (Stream<Path> paths = Files.walk(Paths.get("xray")))
    {
      List<File> pngFiles = paths
        .filter(Files::isRegularFile)
        .filter(path -> path.toString().endsWith(".png"))
        .map(Path::toFile)
        .collect(Collectors.toList());

      // Paso 2: Extraer los nombres de esos archivos      
      for (File pngFile : pngFiles)
      {
        String fileName = pngFile.getName();
        String file_name_without_extension = fileName.substring(0, fileName.lastIndexOf('.'));
        if (list_cases != null)
        {
          for (int i = 0; i < list_cases.size(); i++)
          {
            name_issue = list_cases.get(i).get(1);
            if (name_issue.equals(file_name_without_extension))
            {
              issueKey = list_cases.get(i).get(0);
            }
          }
        }

        if (issueKey != null)
        {
          attach_file_to_issue(issueKey, pngFile, httpClient);
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void update_jira_issue(String issueKey, String summary, String description) throws IOException
  {
    try (CloseableHttpClient httpClient = HttpClients.createDefault())
    {
      HttpPut httpPut = new HttpPut(configuration_server.JIRA_URL + "/rest/api/2/issue/" + issueKey);

      String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
      byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
      String authHeader = "Basic " + new String(encodedAuth);
      httpPut.setHeader("Authorization", authHeader);
      httpPut.setHeader("Content-Type", "application/json");

      JSONObject issueDetails = new JSONObject();
      JSONObject fields = new JSONObject();
      fields.put("summary", summary);
      fields.put("description", description);
      issueDetails.put("fields", fields);

      StringEntity entity = new StringEntity(issueDetails.toString(), ContentType.APPLICATION_JSON);
      httpPut.setEntity(entity);

      try (CloseableHttpResponse response = httpClient.execute(httpPut))
      {
        String responseString = EntityUtils.toString(response.getEntity());
        System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
        System.out.println("Response Body: " + responseString);
      }
      catch (IOException e)
      {

        e.printStackTrace();
        httpClient.close();
      }

      httpClient.close();

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

  }

  public void create_or_update_jira_issue(List<String> list_cases, String issue_type_id, String issue_key)
  {
    try
    {
      CloseableHttpClient httpClient = HttpClients.createDefault();
      
      if (!list_cases.isEmpty())
      {
        for (String list_name_case : list_cases)
        {
          create_jira_issue(list_name_case, list_name_case, issue_type_id, httpClient);
          
          attach_png_files_to_issues(configuration_server.matrix_issues,httpClient);
        }
      }
      httpClient.close();
    }
    catch (IOException e)
    {
      System.err.println("An error occurred while creating or updating the issue: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void issue_relationship(String id_issue)
  {

    try
    {
      Allure.label("jira-prod", id_issue);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  public static void attach_file_to_issue(String issueKey, File file,CloseableHttpClient httpClient) throws IOException
  {
    String baseUrl = configuration_server.JIRA_URL + "/rest/api/2/issue/" + issueKey;
    String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
    byte[] encodedAuth = java.util.Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
    String authHeader = "Basic " + new String(encodedAuth);

    // Obtener todos los adjuntos del issue
    HttpGet httpGet = new HttpGet(baseUrl);
    httpGet.setHeader("Authorization", authHeader);
    HttpResponse getResponse = httpClient.execute(httpGet);
    String responseString = EntityUtils.toString(getResponse.getEntity());
    JsonNode attachments = new ObjectMapper().readTree(responseString).path("fields").path("attachment");

    // Buscar y eliminar el adjunto existente si tiene el mismo nombre
    for (JsonNode attachment : attachments)
    {
      if (attachment.path("filename").asText().equals(issueKey + ".png"))
      {
        String attachmentId = attachment.path("id").asText();
        HttpDelete httpDelete = new HttpDelete(baseUrl + "/attachments/" + attachmentId);
        httpDelete.setHeader("Authorization", authHeader);
        HttpResponse deleteResponse = httpClient.execute(httpDelete);
        System.out.println("Deleted attachment: " + attachmentId);
      }
    }

    // Adjuntar el nuevo archivo
    String url = baseUrl + "/attachments";
    HttpPost httpPost = new HttpPost(url);
    httpPost.setHeader("X-Atlassian-Token", "no-check");
    httpPost.setHeader("Authorization", authHeader);

    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, issueKey + ".png");
    httpPost.setEntity(builder.build());

    HttpResponse postResponse = httpClient.execute(httpPost);
    String postResponseString = EntityUtils.toString(postResponse.getEntity());

    System.out.println("Response Code: " + postResponse.getStatusLine().getStatusCode());
    System.out.println("Response Body: " + postResponseString);

  }

  public static void upload_test_results_(String token) throws IOException
  {
    //target/allure-results
    try (Stream<Path> paths = Files.walk(Paths.get("target/allure-results")))
    {
      List<File> resultsFiles = paths
        .filter(Files::isRegularFile)
        .filter(path -> path.toString().endsWith(".json"))
        .map(Path::toFile)
        .collect(Collectors.toList());

      CloseableHttpClient httpClient = HttpClients.createDefault();
      for (File resultsFile : resultsFiles)
      {
        HttpPost httpPost = new HttpPost("https://xray.cloud.getxray.app/api/v2/import/execution");
        httpPost.setHeader("Authorization", "Bearer " + token);
        httpPost.setHeader("Content-Type", "application/json");

        byte[] fileContent = Files.readAllBytes(resultsFile.toPath());
        StringEntity entity = new StringEntity(new String(fileContent, StandardCharsets.UTF_8));
        httpPost.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(httpPost))
        {
          String responseString = EntityUtils.toString(response.getEntity());
          System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
          System.out.println("Response Body: " + responseString);
        }
      }
      httpClient.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public static void upload_test_results(String token) throws IOException
  {
    try (Stream<Path> paths = Files.walk(Paths.get("target/allure-results")))
    {
      List<Path> resultsFiles = paths
        .filter(Files::isRegularFile)
        .filter(path -> path.toString().endsWith(".json"))
        .collect(Collectors.toList());

      CloseableHttpClient httpClient = HttpClients.createDefault();
      for (Path resultsFilePath : resultsFiles)
      {
        // Lee el contenido del archivo JSON de Allure
        String allureJson = new String(Files.readAllBytes(resultsFilePath), StandardCharsets.UTF_8);
        // Transforma el JSON de Allure al formato Xray
        String xrayJson = transform_allure_to_xray_format(allureJson);

        HttpPost httpPost = new HttpPost("https://xray.cloud.getxray.app/api/v2/import/execution");
        httpPost.setHeader("Authorization", "Bearer " + token);
        httpPost.setHeader("Content-Type", "application/json");

        StringEntity entity = new StringEntity(xrayJson);
        httpPost.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(httpPost))
        {
          String responseString = EntityUtils.toString(response.getEntity());
          System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
          System.out.println("Response Body: " + responseString);
        }
      }
      httpClient.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  public static String authenticate(String clientId, String clientSecret) throws IOException
  {
    String authUrl = "https://xray.cloud.getxray.app/api/v2/authenticate";
    String authPayload = String.format("{\"client_id\": \"%s\", \"client_secret\": \"%s\"}", clientId, clientSecret);

    try (CloseableHttpClient httpClient = HttpClients.createDefault())
    {
      HttpPost authPost = new HttpPost(authUrl);
      authPost.setHeader("Content-Type", "application/json");
      authPost.setEntity(new StringEntity(authPayload));

      try (CloseableHttpResponse response = httpClient.execute(authPost))
      {
        if (response.getStatusLine().getStatusCode() != 200)
        {
          throw new RuntimeException("Failed to authenticate: " + response.getStatusLine().getStatusCode());
        }
        String jsonResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        return jsonResponse.replace("\"", ""); // Token is returned as a plain string
      }
    }
  }
  
  public static void getIssueFields(String issueKey) throws IOException
  {
    try (CloseableHttpClient httpClient = HttpClients.createDefault())
    {
      HttpGet httpGet = new HttpGet(configuration_server.JIRA_URL + configuration_server.JIRA_API_ENDPOINT + "/"+issueKey);

      String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
      byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
      String authHeader = "Basic " + new String(encodedAuth);
      httpGet.setHeader("Authorization", authHeader);
      httpGet.setHeader("Content-Type", "application/json");

      try (CloseableHttpResponse response = httpClient.execute(httpGet))
      {
        String responseString = EntityUtils.toString(response.getEntity());
        System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
        System.out.println("Response Body: " + responseString);

        JSONObject issue = new JSONObject(responseString);
        JSONObject fields = issue.getJSONObject("fields");

        // Iterar sobre los campos y listar sus nombres e IDs
        Iterator<String> keys = fields.keys();
        while (keys.hasNext())
        {
          String key = keys.next();
          Object value = fields.get(key);
          System.out.println("Field ID: " + key + ", Field Value: " + value);
        }

//                // Aquí puedes procesar los campos como desees
//                System.out.println("Summary: " + fields.getString("summary"));
//                System.out.println("Description: " + fields.getString("description"));
//
//                // Si tienes un campo personalizado específico, puedes acceder a él así:
//                // System.out.println("Custom Field: " + fields.getString("customfield_12345"));
      }
    }
  }
  
  public static void getTestDetails(String testKey) throws IOException
  {
    try (CloseableHttpClient httpClient = HttpClients.createDefault())
    {
      HttpGet httpGet = new HttpGet(configuration_server.JIRA_URL + "/rest/raven/1.0/api/test/" + testKey+"/steps");

      String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
      byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
      String authHeader = "Basic " + new String(encodedAuth);
      httpGet.setHeader("Authorization", authHeader);
      httpGet.setHeader("Content-Type", "application/json");

      try (CloseableHttpResponse response = httpClient.execute(httpGet))
      {
        String responseString = EntityUtils.toString(response.getEntity());
        System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
        System.out.println("Response Body: " + responseString);

        JSONObject testDetails = new JSONObject(responseString);

        // Obtener y mostrar los pasos del test
        if (testDetails.has("steps"))
        {
          JSONArray steps = testDetails.getJSONArray("steps");
          for (int i = 0; i < steps.length(); i++)
          {
            JSONObject step = steps.getJSONObject(i);
            String stepDescription = step.getString("step");
            String stepData = step.getString("data");
            String stepResult = step.getString("result");

            System.out.println("Step " + (i + 1) + ":");
            System.out.println("  Description: " + stepDescription);
            System.out.println("  Data: " + stepData);
            System.out.println("  Result: " + stepResult);
          }
        }
        else
        {
          System.out.println("No steps found for the test.");
        }
      }
    }
  }

  public static String transform_allure_to_xray_format(String allureJson)
  {
    Gson gson = new Gson();
    JsonParser parser = new JsonParser();
    JsonArray xrayTests = new JsonArray();

    // Parsea el JSON de Allure
    JsonElement element = parser.parse(allureJson);

    JsonArray allureTests;
    if (element.isJsonArray())
    {
      // Si el JSON es un array
      allureTests = element.getAsJsonArray();
    }
    else if (element.isJsonObject())
    {
      // Si el JSON es un objeto, lo envolvemos en un array
      allureTests = new JsonArray();
      allureTests.add(element.getAsJsonObject());
    }
    else
    {
      throw new IllegalArgumentException("El JSON de Allure no es un array ni un objeto válido.");
    }

    // Itera sobre cada caso de prueba de Allure
    for (JsonElement testElement : allureTests)
    {
      JsonObject allureTest = testElement.getAsJsonObject();
      JsonObject xrayTest = new JsonObject();

      // Mapea los campos de Allure a los campos de Xray
      xrayTest.addProperty("testKey", allureTest.get("name").getAsString());
      xrayTest.addProperty("status", transform_status(allureTest.get("status").getAsString()));
      if (allureTest.has("description"))
      {
        xrayTest.addProperty("comment", allureTest.get("description").getAsString());
      }
      else
      {
        xrayTest.addProperty("comment", "");
      }

      xrayTests.add(xrayTest);
    }
    
    // Crea el objeto JSON final para Xray
    JsonObject xrayJson = new JsonObject();
    xrayJson.addProperty("testExecutionKey", browser_manager.id_issue_xray); // 
    JsonObject info = new JsonObject();
    info.addProperty("summary", "Execution of automated tests");
    info.addProperty("description", "This contains the results of automated tests");
    info.addProperty("user", configuration_server.CLIENT_ID_XRAY); // Reemplaza con el ID del usuario de Xray
    info.addProperty("admin", "admin");
    xrayJson.add("info", info);
    xrayJson.add("tests", xrayTests);

    return gson.toJson(xrayJson);
  }

  // Helper method to transform Allure status to Xray status
  private static String transform_status(String allureStatus)
  {
    switch (allureStatus)
    {
      case "passed":
        return "PASS";
      case "failed":
        return "FAIL";
      default:
        return "TODO";
    }
  }
  
  //    public static void attach_file_to_issue(String issueKey, File file) throws IOException {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        String url = configuration_server.JIRA_URL + "/rest/api/2/issue/" + issueKey + "/attachments";
//
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("X-Atlassian-Token", "no-check");
//        String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
//        byte[] encodedAuth = java.util.Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
//        String authHeader = "Basic " + new String(encodedAuth);
//        httpPost.setHeader("Authorization", authHeader);
//        
//        file.getName();
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, issueKey+".png");
//        httpPost.setEntity(builder.build());
//
//        HttpResponse response = httpClient.execute(httpPost);
//        String responseString = EntityUtils.toString(response.getEntity());
//
//        System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
//        System.out.println("Response Body: " + responseString);
//
//        httpClient.close();
//    }
//    public String authenticate() throws Exception {
//
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            HttpPost post = new HttpPost(configuration_server.XRAY_AUTH_URL);
//            post.setHeader("Content-Type", "application/json");
//
//            JSONObject json = new JSONObject();
//            json.put("client_id", configuration_server.CLIENT_ID_XRAY);
//            json.put("client_secret", configuration_server.CLIENT_SECRET_XRAY);
//
//            StringEntity entity = new StringEntity(json.toString());
//            post.setEntity(entity);
//
//            return EntityUtils.toString(client.execute(post).getEntity());
//        }
//    }
//  public static void upload_test_results() throws IOException
//  {
//    try (Stream<Path> paths = Files.walk(Paths.get("target/surefire-reports")))
//    {
//      List<File> resultsFiles = paths
//        .filter(Files::isRegularFile)
//        .filter(path -> path.toString().endsWith(".xml"))
//        .map(Path::toFile)
//        .collect(Collectors.toList());
//
//      CloseableHttpClient httpClient = HttpClients.createDefault();
//      for (File resultsFile : resultsFiles)
//      {
//        HttpPost httpPost = new HttpPost(configuration_server.XRAY_AUTH_URL + configuration_server.XRAY_API_ENDPOINT);
//
//        String auth = configuration_server.CLIENT_ID_XRAY + ":" + configuration_server.CLIENT_SECRET_XRAY;
//        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
//        String authHeader = "Basic " + new String(encodedAuth);
//        httpPost.setHeader("Authorization", authHeader);
//        httpPost.setHeader("Content-Type", "application/xml");
//
//        FileInputStream fileInputStream = new FileInputStream(resultsFile);
//        byte[] fileContent = new byte[(int) resultsFile.length()];
//        fileInputStream.read(fileContent);
//        fileInputStream.close();
//
//        StringEntity entity = new StringEntity(new String(fileContent, StandardCharsets.UTF_8));
//        httpPost.setEntity(entity);
//
//        try (CloseableHttpResponse response = httpClient.execute(httpPost))
//        {
//          String responseString = EntityUtils.toString(response.getEntity());
//          System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
//          System.out.println("Response Body: " + responseString);
//        }
//      }
//      httpClient.close();
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
//  }

}
