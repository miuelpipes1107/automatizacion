/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jira_xray;

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
import java.nio.file.Paths;
import java.util.Base64;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
/**
 *
 * @author Aurora
 */
public class jira_xray {
    
    public void jira_xray()
    {
    }
    
    public void create_jira_issue(String summary, String description, String issueTypeId) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(configuration_server.JIRA_URL+configuration_server.JIRA_API_ENDPOINT);

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

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
            System.out.println("Response Body: " + responseString);
        }

        httpClient.close();
    }
    
    public String search_issue_by_summary(String summary) throws IOException {
        
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String jqlQuery = null;
    try {
        jqlQuery = URLEncoder.encode("summary~\"" + summary + "\"", "UTF-8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return null;
    }
    
    HttpGet httpGet = new HttpGet(configuration_server.JIRA_URL + "/rest/api/2/search?jql=" + jqlQuery);

    String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
    byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
    String authHeader = "Basic " + new String(encodedAuth);
    httpGet.setHeader("Authorization", authHeader);
    httpGet.setHeader("Content-Type", "application/json");

    try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
        String responseString = EntityUtils.toString(response.getEntity());
        JSONObject jsonResponse = new JSONObject(responseString);
        if (jsonResponse.getInt("total") > 0) {
            return jsonResponse.getJSONArray("issues").getJSONObject(0).getString("key");
        }
    }

    httpClient.close();
    return null;
}
    
    public void update_jira_issue(String issueKey, String summary, String description) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
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

            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                String responseString = EntityUtils.toString(response.getEntity());
                System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
                System.out.println("Response Body: " + responseString);
            } catch (IOException e) {

                e.printStackTrace();
                httpClient.close();
            }

            httpClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void create_or_update_jira_issue(String summary, String description, String issueTypeId, String issueKey) {
        try {

            create_jira_issue(summary, description, issueTypeId);

        } catch (IOException e) {
            System.err.println("An error occurred while creating or updating the issue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void issue_relationship(String id_issue) {

        try {
            Allure.label("jira-prod", id_issue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static void attach_file_to_issue(String issueKey, File file) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String url = configuration_server.JIRA_URL + "/rest/api/2/issue/" + issueKey + "/attachments";

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("X-Atlassian-Token", "no-check");
        String auth = configuration_server.JIRA_USER_EMAIL + ":" + configuration_server.JIRA_API_TOKEN;
        byte[] encodedAuth = java.util.Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);
        httpPost.setHeader("Authorization", authHeader);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, issueKey);
        httpPost.setEntity(builder.build());

        HttpResponse response = httpClient.execute(httpPost);
        String responseString = EntityUtils.toString(response.getEntity());

        System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
        System.out.println("Response Body: " + responseString);

        httpClient.close();
    }

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
    
//    public static void uploadTestResults(File resultsFile) throws IOException {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
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
//        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
//            String responseString = EntityUtils.toString(response.getEntity());
//            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
//            System.out.println("Response Body: " + responseString);
//        }
//
//        httpClient.close();
//    }
    
}
