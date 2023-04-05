public class Main {
    public static void main(String[] args) {
        FetchWeather f = new FetchWeather();
        WeatherClass w = f.fetchData();
        w.print();
    }
}