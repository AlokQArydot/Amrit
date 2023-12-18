package testcases;

import java.awt.Color;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class Meter_status extends Login {

	@Test(priority = 2)
	public void Meter_Report() throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.xpath(prop.getProperty("Report"))).click();

		driver.findElement(By.xpath(prop.getProperty("Meters_Analysis"))).click();
		Thread.sleep(3000);

		// List of meter names or identifiers
		String[] meterIdentifiers = { "COMMON AREA LIGHTNING AND POWER PANEL", "DG_Ground-1", "DG_Ground-2",
				"DG_Ground-3", "DG_Ground_Sync_Panels", "External_Lighting_DB", "FDP-1FA1", "FDP-2FA1",
				"FDP-3FA2 & 3FA1", "FDP-4FA1 & 4FA2", "FDP-5FA1", "FDP-6FA", "GF_METER_PANEL_TOWER-A",
				"Life Safety Panel", "MAIN_LT_PANEL-1", "MAIN_LT_PANEL-2", "RETAIL_PANEL_TOWER_B-1",
				"RETAIL_PANEL_TOWER_B-2", "TERRACE_SOLAR_ACDB_PANEL",
				"UPS_OUTPUT_PANEL" /* ... add all 16 meter identifiers here ... */ };

		// Iterate through each meter
		for (String identifier : meterIdentifiers) {
			// Find the meter element by its identifier
			WebElement gateway = driver.findElement(By.xpath("//h6[text()='" + identifier + "']"));

			// Click on the gateway to navigate to meter page
			gateway.click();
			
			Thread.sleep(3000);

			WebElement gatewayname = driver.findElement(By.tagName("h3"));
			String Gatewayname = gatewayname.getText();

			Reporter.log("Gateway== " + Gatewayname + "");

			// Find all meter elements on the page
			List<WebElement> meterElements = driver.findElements(By.xpath("//h6//following-sibling::span"));
			for (WebElement meterElement : meterElements) {
				// Read meter name
				String meterName = meterElement.getText();
				//System.out.println(meterName);
				// Read meter state color
				String meterColor = meterElement.getCssValue("background-color");

				// Convert RGBA to Color object
				Color color = convertToColor(meterColor);
				// Determine color name
				String colorName = determineColorName(color);

				List<WebElement> meterElementsname = driver.findElements(By.tagName("h6"));
				for (WebElement meterElementname : meterElementsname) {

					String meterNames = meterElementname.getText();

					// Print the color name
					Reporter.log("Meter==== " + meterNames + " ====color:=== " + colorName);
					//System.out.println("Meter==== " + meterNames + " =====color:=== " + colorName);

					// Check if the meter is red
					if ("Red".equalsIgnoreCase(colorName)) {
						// If the meter is red, send an email to the manager
						sendEmailToManager("Meter---- " + meterNames + " in gateway---( " + Gatewayname
								+ " ) is displaying red .\n" + "Meter Name:--- " + meterNames + " is not working.");
					}
				}
			}

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,-500)", "");

			// Navigate back to the Analysis page
			driver.findElement(By.xpath("//i[@class='fa fa fa-arrow-circle-left fa-2x mr-2']")).click();
		}

		// Close the browser
	}

	// ===============================================================================================================================================================================================

	private static void sendEmailToManager(String message) {
		final String username = "info@assistant-cap.com"; // Your Gmail username
		final String password = "bqnmlrpypgvmccfv"; // Your Gmail password

		// Set properties for the email server
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.office365.com"); // Microsoft365 port 
		props.put("mail.smtp.port", "587");
		//props.put("mail.smtp.host", "smtp.gmail.com"); // You may need to change this based on your email provider
		//props.put("mail.smtp.port", "587"); // You may need to change this based on your email provider

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
			emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse("hitendrap@rydotinfotech.com")); // Manager's
																														// email
			// Set the email subject and body
			emailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse("yogeshp@rydotinfotech.com")); // CC
																														// recipient's
																														// email
			emailMessage.setSubject("Alert: Red Meter Detected");
			emailMessage.setText(message);
			// Send the email
			Transport.send(emailMessage);
			System.out.println("Email sent to the manager successfully.");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	// =================================================================================================================================================================================================================================

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
