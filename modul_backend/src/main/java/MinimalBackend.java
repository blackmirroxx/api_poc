import spark.Spark; 
import java.sql.*; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.SQLException; 

public class MinimalBackend {
   public static void main(String[] args){
      Spark.post("/store", (req, res) -> {
         String title = req.queryParams("title"); 
         String url = req.queryParams("url");
         String sql = "INSERT INTO pages (title, url) VALUES (?, ?)"; 
         try(
           Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/<your_db>", "<your_user>", "<your_password>"); 
           PreparedStatement stmt = conn.prepareStatement(sql)
         ){
           stmt.setString(1, title); // variable content needed 
           stmt.setString(2, url); // same here  
           stmt.executeUpdate();
           return "--- Data stored successfully ---";


         }catch (SQLException e) {
            return "Error storing data: " + e.getMessage();
         }
      });
   }

}
