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
import java.io.FileInputStream;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
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

  public void create_jira_issue(String summary, String description, String issueTypeId) throws IOException
  {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(configuration_server.JIRA_URL + configuration_server.JIRA_API_ENDPOINT);

    String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
    byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
    String authHeader = "Basic " + new String(encodedAuth);
    httpPost.setHeader("Authorization", authHeader);
    httpPost.setHeader("Content-Type", "application/json");

    JSONObject issueDetails = new JSONObject();
    JSONObject fields = new JSONObject();
    fields.put("summary", summary);
    fields.put("description", description);
    fields.put("issuetype", new JSONObject().put("id", issueTypeId));
    fields.put("project", new JSONObject().put("key", "AUT")); // Replace with your project key
    issueDetails.put("fields", fields);

    StringEntity entity = new StringEntity(issueDetails.toString());
    httpPost.setEntity(entity);

    try (CloseableHttpResponse response = httpClient.execute(httpPost))
    {
      String responseString = EntityUtils.toString(response.getEntity());
      System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
      System.out.println("Response Body: " + responseString);
      JSONObject responseString_objet = new JSONObject(responseString);
      browser_manager.id_issue_xray = responseString_objet.getString("key");
    }

    httpClient.close();
  }

  public static String search_issue_by_summary(String summary,CloseableHttpClient httpClient) throws IOException
  {

//    CloseableHttpClient httpClient = HttpClients.createDefault();
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

//    httpClient.close();
    return null;
  }
  
  public static void attach_png_files_to_issues() throws IOException
  {
    // Paso 1: Buscar archivos .png en la ruta especificada
    try (Stream<Path> paths = Files.walk(Paths.get("xray")))
    {
      List<File> pngFiles = paths
        .filter(Files::isRegularFile)
        .filter(path -> path.toString().endsWith(".png"))
        .map(Path::toFile)
        .collect(Collectors.toList());

      // Paso 2: Extraer los nombres de esos archivos
      CloseableHttpClient httpClient = HttpClients.createDefault();
      for (File pngFile : pngFiles)
      {
        String fileName = pngFile.getName();
        String file_name_without_extension = fileName.substring(0, fileName.lastIndexOf('.'));            
        String issueKey = search_issue_by_summary(file_name_without_extension, httpClient);
        if (issueKey != null)
        {
          attach_file_to_issue(issueKey, pngFile, httpClient);
        }
      }
      httpClient.close();
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

  public void create_or_update_jira_issue(String summary, String description, String issueTypeId, String issueKey)
  {
    try
    {

      create_jira_issue(summary, description, issueTypeId);

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
//    CloseableHttpClient httpClient = HttpClients.createDefault();
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

//    httpClient.close();
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
