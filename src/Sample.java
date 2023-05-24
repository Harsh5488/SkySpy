import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Sample {
    public static void main(String[] args) {
        try {
            String username = "harsh5488";

            String url = "http://api.geonames.org/countryInfoJSON?username=" + username;
//            String url = "http://api.geonames.org/searchJSON?q=India&maxRows=10&username=harsh5488";
            URLConnection connection = new URL(url).openConnection();
            InputStream isr = connection.getInputStream();
//            BufferedReader reader = new BufferedReader(isr);
//            StringBuilder builder = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//            }
//            reader.close();
//            System.out.println(builder);

            FileWriter fw = new FileWriter("sample.json");

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = isr.read(buffer)) != -1) {
                fw.write(new String(buffer, 0, bytesRead));
            }
            fw.close();


//            for (int i = 0; i < countries.length(); i++) {
//                String countryName = countries.getJSONObject(i).getString("countryName");
//                System.out.println(countryName);
//            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
