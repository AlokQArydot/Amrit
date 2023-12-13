package testcases;



import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;



public class Analysis extends Login{
	
	@Test(priority = 2)
	public void Analysis_Report() throws InterruptedException
	{
		Thread.sleep(3000);
		driver.findElement(By.xpath(prop.getProperty("Analysis"))).click();
		
		driver.findElement(By.xpath(prop.getProperty("Meters_Analysis"))).click();
		Thread.sleep(3000);

		 // List of meter names or identifiers
	       String[] meterIdentifiers = {"COMMON AREA LIGHTNING AND POWER PANEL", "DG_Ground-1","DG_Ground-2","DG_Ground-3",
	    	
	    		"DG_Ground_Sync_Panels","External_Lighting_DB","FDP-1FA1","FDP-2FA1","FDP-3FA2 & 3FA1","FDP-4FA1 & 4FA2",
	    		
	    		"FDP-5FA1","FDP-6FA","GF_METER_PANEL_TOWER-A","Life Safety Panel","MAIN_LT_PANEL-1","MAIN_LT_PANEL-2",
	    		
	    		"RETAIL_PANEL_TOWER_B-1","RETAIL_PANEL_TOWER_B-2","TERRACE_SOLAR_ACDB_PANEL","UPS_OUTPUT_PANEL"  /*... add all 16 meter identifiers here ...*/};
	       // Iterate through each meter
	       
	    // Color mapping
	       Map<String, String> colorNames = new HashMap<>();
	       
	       for (String identifier : meterIdentifiers) {
	           // Find the meter element by its identifier
	           WebElement meter = driver.findElement(By.xpath("//h6[text()='" + identifier + "']/following-sibling::span"));
	           // Check the color of the meter (assuming red is represented as RGB value #FF0000)
	           String meterColor = meter.getCssValue("background-color");
	           
	           // Convert RGBA to Color object
	           Color color = convertToColor(meterColor);
	           // Determine color name
	           String colorName = determineColorName(color);
	           
	           // Print the color name
	           Reporter.log ("Gateway===: " + identifier + " color==: " + colorName);
	           
	           // Check if the meter is red
	         
	           if ("Red".equalsIgnoreCase(colorName)) {
	               // If the meter is red, send an email to the manager
	               sendEmailToManager("Gateway " + identifier + " is displaying red.");
	               System.out.println(" mail sent to Manager");
	           }
	       }
	       // Close the browser
	       driver.quit();
	   }
//===============================================================================================================================================================================================
	
	   private static void sendEmailToManager(String message) {
	       final String username = "alokqaengineer@gmail.com"; // Your Gmail username
	       final String password = "ecypzarrvyszgycn"; // Your Gmail password
	       // Set properties for the email server
	       Properties props = new Properties();
	       props.put("mail.smtp.auth", "true");
	       props.put("mail.smtp.starttls.enable", "true");
	       //props.put("mail.smtp.host", "smtp-mail.outlook.com");// You may need to change this based on your email provider
	       props.put("mail.smtp.host", "smtp.gmail.com"); // You may need to change this based on your email provider
	       props.put("mail.smtp.port", "587"); // You may need to change this based on your email provider
	       // Create a session with the email server
	       Session session = Session.getInstance(props, new Authenticator() {
	           protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	           }
	       });
	       try {
	           // Create a MimeMessage object
	           Message emailMessage = new MimeMessage(session);
	           // Set the sender and recipient addresses
	           emailMessage.setFrom(new InternetAddress(username));
	           emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse("hitendrap@rydotinfotech.com")); // Manager's email
	         //  emailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse("cc@example.com")); // CC recipient's email
	           // Set the email subject and body
	           emailMessage.setSubject("Alert: Red Meter Detected");
	           emailMessage.setText(message);
	           // Send the email
	           Transport.send(emailMessage);
	           System.out.println("Email sent to the manager successfully.");
	       } catch (MessagingException e) {
	           throw new RuntimeException(e);
	       }
	   }
	       
//=================================================================================================================================================================================================================================	       
	       
	       
	       private static Color convertToColor(String rgbaValue) {
	           String[] rgbaComponents = rgbaValue.replace("rgba(", "").replace(")", "").split(",");
	           int red = Integer.parseInt(rgbaComponents[0].trim());
	           int green = Integer.parseInt(rgbaComponents[1].trim());
	           int blue = Integer.parseInt(rgbaComponents[2].trim());
	           return new Color(red, green, blue);
	       }
	       private static String determineColorName(Color color) {
	           // Define threshold values to determine if color is closer to green or red
	           int greenThreshold = 100;
	           int redThreshold = 150;
	           // Calculate color distance from pure green and red
	           int greenDistance = Math.abs(color.getGreen() - 255);
	           int redDistance = Math.abs(color.getRed() - 255);
	           // Determine color name based on thresholds
	           if (greenDistance < greenThreshold && redDistance > redThreshold) {
	               return "Green";
	           } else {
	               return "Red";
	           }     
	       
	       
	       
	   }
		
}
