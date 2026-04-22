import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject; // Requires org.json library

public class CurrencyConverter {

    private static final String API_KEY = "YOUR_API_KEY";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static double convert(String from, String to, double amount) throws Exception {
        String url = BASE_URL + API_KEY + "/pair/" + from + "/" + to + "/" + amount;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject obj = new JSONObject(response.body());
            return obj.getDouble("conversion_result");
        } else {
            throw new RuntimeException("API Error: " + response.statusCode());
        }
    }

    public static void main(String[] args) {
        try {
            double result = convert("USD", "EUR", 100.0);
            System.out.println("100 USD is approximately: " + result + " EUR");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
