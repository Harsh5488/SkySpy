import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ShowWeather extends JFrame{
    private JPanel panelAll;
    private JComboBox cBoxCity;
    private JComboBox cBoxState;
    private JComboBox cBoxCountry;
    private JLabel labelIcon;
    private JLabel labelLocation;
    private JLabel labelMainTemp;
    private JPanel panelDetail;
    List<String> countryCode;
    List<String> stateCode;
    String Location="";
    DefaultComboBoxModel<String> modelCountry;
    DefaultComboBoxModel<String> modelState;
    DefaultComboBoxModel<String> modelCity;

    ShowWeather(){
        setContentPane(panelAll);
        setTitle("SkySpy");
        setSize(new Dimension(650,400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        cBoxCountry.setPreferredSize(new Dimension(150,30));
        cBoxState.setPreferredSize(new Dimension(150,30));
        cBoxCity.setPreferredSize(new Dimension(150,30));

//        modelCountry = (DefaultComboBoxModel<String>) cBoxCountry.getModel();
//        modelCountry.addElement("Select");
//        cBoxCountry.setModel(modelCountry);

        modelState = (DefaultComboBoxModel<String>) cBoxState.getModel();
        modelState.addElement("Select");
        cBoxState.setModel(modelState);

        modelCity = (DefaultComboBoxModel<String>) cBoxCity.getModel();
        modelCity.addElement("Select");
        cBoxCity.setModel(modelCity);

        String apiUrl = "http://api.geonames.org/countryInfoJSON?lang=en&username=harsh5488";

        try {
            URLConnection connection = new URL(apiUrl).openConnection();

            InputStream responseStream = connection.getInputStream();

            JsonParser parser = new JsonParser();
            JsonObject jsonResponse = parser.parse(new java.io.InputStreamReader(responseStream)).getAsJsonObject();

            System.out.println(jsonResponse);

            List<String> countryName = new ArrayList<>();
            countryCode = new ArrayList<>();
            JsonArray geonames = jsonResponse.getAsJsonArray("geonames");
            for (JsonElement geoname : geonames) {
                String name = geoname.getAsJsonObject().get("countryName").getAsString();
                String code = geoname.getAsJsonObject().get("countryCode").getAsString();
                countryCode.add(code);
                countryName.add(name);
            }

            System.out.println(countryName);

            modelCountry = (DefaultComboBoxModel<String>) cBoxCountry.getModel();

            for (String item : countryName) {
                modelCountry.addElement(item);
            }

            cBoxCountry.setModel(modelCountry);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        cBoxCountry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cBoxCountry.getSelectedIndex()!=-1){
                    cBoxState.removeAllItems();
                    int i = cBoxCountry.getSelectedIndex();
                    String Code = countryCode.get(i);
                    String apiUrl = "http://api.geonames.org/searchJSON?lang=en&username=harsh5488&country=" + Code + "&featureCode=ADM1";
                    try {
                        URLConnection connection = new URL(apiUrl).openConnection();

                        InputStream responseStream = connection.getInputStream();

                        JsonParser parser = new JsonParser();
                        JsonObject jsonResponse = parser.parse(new java.io.InputStreamReader(responseStream)).getAsJsonObject();

                        System.out.println(jsonResponse);

                        List<String> stateNames = new ArrayList<>();
                        stateCode= new ArrayList<>();
                        JsonArray geonames = jsonResponse.getAsJsonArray("geonames");
                        for (JsonElement geoname : geonames) {
                            String name = geoname.getAsJsonObject().get("name").getAsString();
                            String code = geoname.getAsJsonObject().get("adminCode1").getAsString();
                            stateCode.add(code);
                            stateNames.add(name);
                        }

                        System.out.println(stateNames);
                        modelState = (DefaultComboBoxModel<String>) cBoxState.getModel();

                        for (String item : stateNames) {
                            modelState.addElement(item);
                        }

                        cBoxState.setModel(modelState);

                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        cBoxState.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cBoxState.getSelectedIndex()!=-1){
                    cBoxCity.removeAllItems();
                    int i = cBoxCountry.getSelectedIndex();
                    int j = cBoxState.getSelectedIndex();
                    String cCode = countryCode.get(i);
                    String sCode = stateCode.get(j);
                    String apiUrl = "http://api.geonames.org/searchJSON?lang=en&username=harsh5488&country=" + cCode + "&adminCode1=" + sCode;
                    try{
                        URLConnection connection = new URL(apiUrl).openConnection();

                        InputStream responseStream = connection.getInputStream();

                        JsonParser parser = new JsonParser();
                        JsonObject jsonResponse = parser.parse(new java.io.InputStreamReader(responseStream)).getAsJsonObject();

                        System.out.println(jsonResponse);

                        List<String> cityNames = new ArrayList<>();
                        JsonArray geonames = jsonResponse.getAsJsonArray("geonames");
                        for (JsonElement geoname : geonames) {
                            String name = geoname.getAsJsonObject().get("name").getAsString();
                            cityNames.add(name);
                        }

                        System.out.println(cityNames);
                        modelCity = (DefaultComboBoxModel<String>) cBoxCity.getModel();

                        for (String item : cityNames) {
                            modelCity.addElement(item);
                        }

                        cBoxCity.setModel(modelCity);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

        cBoxCity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cBoxCity.getSelectedIndex()!=-1){
                    String country = cBoxCountry.getSelectedItem().toString();
                    String state = cBoxState.getSelectedItem().toString();
                    String city = cBoxCity.getSelectedItem().toString();
                    Location = city+","+state+","+country;
                    labelLocation.setText("Location = "+Location);
                    update();
                }
            }
        });

        setVisible(true);
    }
    public void update(){
        //Fetching data
        FetchWeather f = new FetchWeather();
        f.setLocation(Location);
        WeatherClass w = f.fetchData();

        //for the description label
        labelIcon.setText(w.getWeather()[0].getDescription());

        //for the icon label
        String id = w.getWeather()[0].getIcon();
        String imageUrl = "https://openweathermap.org/img/w/"+id+".png";
        try{
            URL url = new URL(imageUrl);
            ImageIcon icon = new ImageIcon(url);
            labelIcon.setIcon(icon);
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        //Main temp
        labelMainTemp.setText(w.getMain().getTemp()+" °C");

        //for the console
        w.print();
    }
}