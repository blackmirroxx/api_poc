import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.OutputStream; 
import java.net.HttpURLConnection; 
import java.net.URL; 
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets; 

public class MinimalCrawler {
    public static void main(String[] args) {
        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");

        // Initialize the WebDriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to a webpage
            driver.get("https://example.com");

            // Extract and print the title of the webpage
            String pageTitle = driver.getTitle();
            String pageUrl = driver.getCurrentUrl(); 
            System.out.println("Page title: " + pageTitle);

            // You can add more crawling logic here
            // For example, extract links, navigate to other pages, etc.

            // send data to backend
            sendDataToBackend(pageTitle, pageUrl); 
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace(); 
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private static void sendDataToBackend(String title, String url) throws Exception{
       String backendUrl = "http://localhost:4567/store"; // my backend URL 
       String urlParameters = "title=" 
           + URLEncoder.encode(title, StandardCharsets.UTF_8.toString()) 
           + "&url=" 
           + URLEncoder.encode(url, StandardCharsets.UTF_8.toString());                                                           

       byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
       int postDataLength = postData.length; 

       URL requestUrl = new URL(backendUrl); 
       HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection(); // type cast 
       conn.setDoOutput(true); 
       conn.setInstanceFollowRedirects(false); 
       conn.setRequestMethod("POST"); 
       conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
       conn.setRequestProperty("charset","utf-8"); 
       conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
       conn.setUseCaches(false);

       try(OutputStream os = conn.getOutputStream()){
           os.write(postData);
       }
       
       int responseCode = conn.getResponseCode(); 
       System.out.println("Response Code: " + responseCode); 
    }
}
// maven required to be installed -> see readMe.md
