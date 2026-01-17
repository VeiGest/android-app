package com.veigest.sdk.util;

import com.veigest.sdk.config.ApiConfig;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiSimpleTerminalTest {
    public static void main(String[] args) throws Exception {
        // Teste de registro
        String registerUrl = ApiConfig.API_BASE_URL + "/auth/register";
        String jsonRegister = "{" +
                "\"username\":\"testjava\"," +
                "\"email\":\"testjava@email.com\"," +
                "\"password\":\"123456\"," +
                "\"role\":\"driver\"}";
        System.out.println("Enviando registro: " + jsonRegister);
        String regResponse = postJson(registerUrl, jsonRegister);
        System.out.println("Resposta registro: " + regResponse);

        // Teste de registro duplicado
        System.out.println("Testando registro duplicado...");
        String regDupResponse = postJson(registerUrl, jsonRegister);
        System.out.println("Resposta registro duplicado: " + regDupResponse);

        // Teste de login
        String loginUrl = ApiConfig.API_BASE_URL + "/auth/login";
        String jsonLogin = "{" +
                "\"username\":\"testjava\"," +
                "\"password\":\"123456\"}";
        System.out.println("Enviando login: " + jsonLogin);
        String loginResponse = postJson(loginUrl, jsonLogin);
        System.out.println("Resposta login: " + loginResponse);

        // Extrair token do login
        String token = extractToken(loginResponse);
        if (token != null) {
            // Teste GET veículos autenticado
            String vehiclesUrl = ApiConfig.API_BASE_URL + "/vehicle";
            System.out.println("\nListando veículos (autenticado):");
            String vehiclesResp = getWithToken(vehiclesUrl, token);
            System.out.println("Resposta veículos: " + vehiclesResp);

            // Teste GET usuários autenticado
            String usersUrl = ApiConfig.API_BASE_URL + "/user";
            System.out.println("\nListando usuários (autenticado):");
            String usersResp = getWithToken(usersUrl, token);
            System.out.println("Resposta usuários: " + usersResp);
        } else {
            System.out.println("Token de login não encontrado. Não foi possível testar endpoints autenticados.");
        }
    }

    // Extrai o access_token do JSON de resposta do login
    private static String extractToken(String loginResponse) {
        try {
            int idx = loginResponse.indexOf("access_token");
            if (idx == -1) return null;
            int start = loginResponse.indexOf('"', idx + 13) + 1;
            int end = loginResponse.indexOf('"', start);
            return loginResponse.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }

    // Faz GET autenticado com token Bearer
    private static String getWithToken(String urlStr, String token) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Accept", "application/json");
        int code = conn.getResponseCode();
        Scanner scanner = new Scanner(
            code >= 200 && code < 300 ? conn.getInputStream() : conn.getErrorStream(), "UTF-8");
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) sb.append(scanner.nextLine());
        scanner.close();
        return "HTTP " + code + ": " + sb.toString();
    }

    private static String postJson(String urlStr, String json) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
            writer.write(json);
        }
        int code = conn.getResponseCode();
        Scanner scanner = new Scanner(
            code >= 200 && code < 300 ? conn.getInputStream() : conn.getErrorStream(), "UTF-8");
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) sb.append(scanner.nextLine());
        scanner.close();
        return "HTTP " + code + ": " + sb.toString();
    }
}
