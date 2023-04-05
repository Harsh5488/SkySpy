import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchWeather {
    String apiKey = "fc853b4d52b5b246574b3f6ff1f63387";
    String location = "Jaipur,Rajasthan,India";
    String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey + "&units=metric";
    WeatherClass w;
    public WeatherClass fetchData(){
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

//                System.out.println(response);
                // Process the JSON response here
                Gson gson = new Gson();
                w = gson.fromJson(response+"", WeatherClass.class);
                return w;
            }
        }
        catch (Exception ex ){
            ex.printStackTrace();
        }
        return w;
    }
}
