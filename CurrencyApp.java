package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

@Controller
public class CurrencyController {

    private final String API_KEY = "e9786925d84137bf7d905021";

    @GetMapping("/")
    public String index() {
        return "index"; // Looks for index.html in templates folder
    }

    @GetMapping("/convert")
    public String convert(@RequestParam String from, 
                          @RequestParam String to, 
                          @RequestParam double amount, 
                          Model model) {
        try {
            String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/" + from + "/" + to + "/" + amount;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            double result = json.getDouble("conversion_result");

            model.addAttribute("result", amount + " " + from + " = " + result + " " + to);
        } catch (Exception e) {
            model.addAttribute("error", "API Error: Please check your connection.");
        }
        return "index";
    }
}
