import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.InvalidInputException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        final String CURRENCY_CODE_ARG = "ARS";
        final String CURRENCY_CODE_USD = "USD";
        final String CURRENCY_CODE_BRA = "BRL";
        final String CURRENCY_CODE_COL = "COP";

        double rate = 0.0;
        int option = 10;

        while(option != 7){
            try {
                option = getMenu();
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
            switch (option){
                case 1:
                    rate = getRate(CURRENCY_CODE_USD, CURRENCY_CODE_ARG);
                    getValue(rate, CURRENCY_CODE_USD, CURRENCY_CODE_ARG);
                    break;

                case 2:
                    rate = getRate(CURRENCY_CODE_ARG, CURRENCY_CODE_USD);
                    getValue(rate, CURRENCY_CODE_ARG, CURRENCY_CODE_USD);
                    break;

                case 3:
                    rate = getRate(CURRENCY_CODE_USD, CURRENCY_CODE_BRA);
                    getValue(rate, CURRENCY_CODE_USD, CURRENCY_CODE_BRA);
                    break;

                case 4:
                    rate = getRate(CURRENCY_CODE_BRA, CURRENCY_CODE_USD);
                    getValue(rate, CURRENCY_CODE_BRA, CURRENCY_CODE_USD);
                    break;

                case 5:
                    rate = getRate(CURRENCY_CODE_USD, CURRENCY_CODE_COL);
                    getValue(rate, CURRENCY_CODE_USD, CURRENCY_CODE_COL);
                    break;

                case 6:
                    rate = getRate(CURRENCY_CODE_COL, CURRENCY_CODE_USD);
                    getValue(rate, CURRENCY_CODE_COL, CURRENCY_CODE_USD);
                    break;

                default:
                    if(option != 7) System.out.println("Opción no válida");
            }
        }

        System.out.println("Saliendo del programa, gracias por utilizar nuestros servicios.");

    }

    public static void getValue(double rate, String currencyCode, String code){
        Scanner console = new Scanner(System.in);
        System.out.println("Ingrese el valor que desea convertir:");
        double value = console.nextDouble();
        double convertedValue = value * rate;
        String convertedValueStr = String.format("%.2f", convertedValue);
        System.out.println("El valor " + value + " [" + currencyCode + "] corresponde al valor final de =>> " + convertedValueStr + " [" + code + "]");
    }

    public static double getRate(String endpoint, String currencyCode) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://v6.exchangerate-api.com/v6/f6e30c4cc318935edee2135f/latest/"+endpoint))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al obtener la tasa de conversion.");
            e.getMessage();
        }

        JsonElement element = JsonParser.parseString(response.body());
        JsonObject jsonObject = element.getAsJsonObject();

        JsonObject conversionRates = jsonObject.get("conversion_rates").getAsJsonObject();

        double rate = conversionRates.get(currencyCode).getAsDouble();

        return rate;

    }

    public static int getMenu() throws InvalidInputException {
        Scanner console = new Scanner(System.in);
        System.out.println("""
                ***********************************************
                Sea bienvenido/a al Conversor de Moneda =]
                
                1) Dólar =>> Peso argentino
                2) Peso argentino =>> Dólar
                3) Dólar =>> Real brasileño
                4) Real Brasileño =>> Dólar
                5) Dólar =>> Peso colombiano
                6) Peso colombiano =>> Dólar
                7) Salir
                Elija una opcíon válida:
                ***********************************************                
                """);

        if(!console.hasNextInt()){
            throw new InvalidInputException("Entrada invalida: Debe ingresar un valor numerico.");
        }

        return console.nextInt();
    }
}