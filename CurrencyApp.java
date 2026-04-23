import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyApp extends JFrame {
    // Replace with your verified API Key
    private static final String API_KEY = "e9786925d84137bf7d905021"; 
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    private JComboBox<String> fromBox, toBox;
    private JTextField inputField, resultField;
    private JButton convertButton;

    public CurrencyApp() {
        setTitle("World Currency Converter");
        setLayout(new GridLayout(4, 2, 10, 10));
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // UI Components
        String[] currencies = {"USD", "EUR", "INR", "GBP", "JPY", "AUD", "CAD"};
        fromBox = new JComboBox<>(currencies);
        toBox = new JComboBox<>(currencies);
        inputField = new JTextField();
        resultField = new JTextField();
        resultField.setEditable(false);
        convertButton = new JButton("Convert");

        // Adding to layout
        add(new JLabel(" Amount:"));
        add(inputField);
        add(new JLabel(" From:"));
        add(fromBox);
        add(new JLabel(" To:"));
        add(toBox);
        add(convertButton);
        add(resultField);

        // Button Action
        convertButton.addActionListener(e -> performConversion());
    }

    private void performConversion() {
        try {
            double amount = Double.parseDouble(inputField.getText());
            String from = (String) fromBox.getSelectedItem();
            String to = (String) toBox.getSelectedItem();

            String url = BASE_URL + API_KEY + "/pair/" + from + "/" + to + "/" + amount;
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    String key = "\"conversion_result\":";
                    int start = body.indexOf(key) + key.length();
                    int end = body.indexOf(",", start);
                    if (end == -1) end = body.indexOf("}", start);
                    
                    double result = Double.parseDouble(body.substring(start, end));
                    resultField.setText(String.format("%.2f %s", result, to));
                });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Enter a valid number.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CurrencyApp().setVisible(true);
        });
    }
}
