
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Currency Selection
        System.out.print("Enter base currency (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter target currency (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        // Step 2: Fetch Real-Time Exchange Rates
        JsonObject rates = getExchangeRates(baseCurrency);
        if (rates == null || !rates.has(targetCurrency)) {
            System.out.println("Unable to fetch exchange rate for the given currencies.");
            return;
        }

        // Step 3: Amount Input
        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        // Step 4: Currency Conversion
        double exchangeRate = rates.get(targetCurrency).getAsDouble();
        double convertedAmount = amount * exchangeRate;

        // Step 5: Display Result
        System.out.printf("Converted amount: %.2f %s%n", convertedAmount, targetCurrency);
    }

    private static JsonObject getExchangeRates(String baseCurrency) {
        String url = API_URL + baseCurrency;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            String json = EntityUtils.toString(response.getEntity());

            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            return jsonObject.getAsJsonObject("rates");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
