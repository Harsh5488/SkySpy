import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchWeather {
    private String apiKey;
    private String location ;
//    private String location = "Jaipur,Rajasthan,India";
    private String apiUrl;
    WeatherClass w;
    public WeatherClass fetchData(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("APIKey.txt"));
            StringBuilder key =new StringBuilder();
            String temp;
            while((temp=br.readLine())!=null){
                key.append(temp);
            }
            br.close();
            apiKey = key.toString();
            apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey + "&units=metric";
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

                Gson gson = new Gson();
                w = gson.fromJson(response.toString(), WeatherClass.class);
                return w;
            }
        }
        catch (Exception ex ){
            ex.printStackTrace();
        }
        return w;
    }
    public void setLocation(String Location){
        location = Location;
    }
}
