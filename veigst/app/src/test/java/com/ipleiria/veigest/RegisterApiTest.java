package com.ipleiria.veigest;

import org.junit.Test;
import org.junit.Ignore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

@Ignore("Teste de integração temporariamente desativado para não travar o build")
public class RegisterApiTest {

    @Test
    public void testRegisterEndpoint() throws Exception {
        // Ajuste a URL se necessário
        String base = "http://10.0.2.2/android-app/Website/website-VeiGest/veigest/backend/web/api"; // ambiente informado
        String endpoint = base + "/auth/register";

        // Gerar username único para evitar conflitos
        String username = "testuser_" + System.currentTimeMillis();
        String email = username + "@example.com";
        String password = "Password123";
        String name = "Test User";
        String phone = "+351900000000";

        String jsonBody = "{" +
            "\"username\":\"" + username + "\"," +
            "\"email\":\"" + email + "\"," +
            "\"password\":\"" + password + "\"," +
            "\"name\":\"" + name + "\"," +
            "\"phone\":\"" + phone + "\"," +
            "\"tipo\":\"driver\"," +
            "\"company_id\":1" +
            "}";

        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(15000);

        byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(out);
        }

        int code = conn.getResponseCode();
        StringBuilder resp = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                code >= 200 && code < 400 ? conn.getInputStream() : conn.getErrorStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                resp.append(line).append('\n');
            }
        }

        String body = resp.toString();

        // Aceitar 200/201 e/ou JSON com "success": true
        boolean successFlag = body.toLowerCase().contains("\"success\":true") || body.toLowerCase().contains("created") || code == 201 || code == 200;

        if (!successFlag) {
            String msg = String.format("Registro falhou. HTTP=%d; body=%s", code, body);
            fail(msg);
        }

        conn.disconnect();
    }
}
