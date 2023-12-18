package testcases;

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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class Frequency_Read extends Login {
	
	@Test(priority = 3)
	public void Frequency_Report() throws InterruptedException {
		
		Thread.sleep(5000);
		
		Actions act = new Actions(driver);
		
		act.moveToElement(driver.findElement(By.xpath(prop.getProperty("Report")))).click().perform();
		
		Thread.sleep(3000);
		
		act.moveToElement(driver.findElement(By.xpath(prop.getProperty("AC/DC_Energy_Meters")))).click().perform();
		Thread.sleep(3000);
		
	    WebElement Plant=driver.findElement(By.xpath(prop.getProperty("Plant_dropdown")));
	    
	    Thread.sleep(3000);
	
	    Plant.click();
	    
	    Plant.sendKeys("The Baroda Crossway");
	    
	    Plant.sendKeys(Keys.ENTER);
	    
	    Thread.sleep(3000);
	    
	    WebElement Gateway_Dropdown=(driver.findElement(By.xpath(prop.getProperty("Gateways"))));
	    
	    Gateway_Dropdown.click();
	    
	    Thread.sleep(4000);
	    
	    driver.findElement(By.xpath("//*[@placeholder='Gateway']")).sendKeys("FDP-3FA2 & 3FA1",Keys.ENTER);
	    
	    
	    Thread.sleep(3000);
	    
/*	    List<WebElement> gatewayOptions = driver.findElements(By.xpath("//*[@role='menuitem']"));
	    
	    for (int i = 1; i <= 20; i++) {
	        // Construct the XPath for each item based on its index
	        String xpathForItem = "//*[@role='menuitem'][" + i + "]";
	        
	        // Find the element and click on it
	        WebElement gatewayOption = driver.findElement(By.xpath(xpathForItem));
	        gatewayOption.click();*/
	        
	        WebElement Meter_Dropdown=(driver.findElement(By.xpath(prop.getProperty("Meter"))));
	        
	        Meter_Dropdown.click();
	        
	        Thread.sleep(5000);
	        
	        List<WebElement> MeterOptions = driver.findElements(By.xpath("//*[@role='menuitem']"));
	        
	        for (int i = 1; i <= 2; i++) {
		        // Construct the XPath for each item based on its index
		        String xpathForItem1 = "//*[@role='menuitem'][" + i + "]";
		        
		        // Find the element and click on it
		        WebElement meterOption = driver.findElement(By.xpath(xpathForItem1));
		        
		        String metername= meterOption.getText();
		        
		        Reporter.log(metername,true);
		        System.out.println(metername);
		       
		        meterOption.click();
		
		      
		        WebElement Date=(driver.findElement(By.xpath(prop.getProperty("Date"))));
		        
		        Date.sendKeys("12/18/2023 - 12/18/2023");
		        
		        driver.findElement(By.xpath(prop.getProperty("Generate_Report"))).click();
		        
		        Thread.sleep(5000);
		        
		        JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("window.scrollBy(0,500)", "");
				
				 Thread.sleep(5000);
		        
		      WebElement FrequencyValue= driver.findElement(By.xpath("//tbody/tr[5]/td[5]"));
		      
		        String Frequency_Hz= FrequencyValue.getText();
		      
		      Reporter.log(Frequency_Hz,true); 
		      System.out.println(Frequency_Hz);
		      
		   // Assuming Frequency_Hz is a double value, you can convert it to a string
		        double frequencyValue = Double.parseDouble(Frequency_Hz);

		        // Check if the frequency is zero
		        if (frequencyValue == 0) {
		            // If frequency is zero, send an email
		        	sendEmailToManager("Gateway Name == FDP-3FA2 & 3FA1 ,Meter Name ----" + metername +  "The frequency value is zero. Please check.");

		      
		      JavascriptExecutor js1 = (JavascriptExecutor) driver;
				js1.executeScript("window.scrollBy(0,-500)", "");
				
				Thread.sleep(4000);
		      
		      Meter_Dropdown.click();
		      
		      
	        //Gateway_Dropdown.click();
	    
	    
	        }
	    
	    }
		
	}
	
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
			emailMessage.setSubject("Alert: Frequency is Zero");
			emailMessage.setText(message);
			// Send the email
			Transport.send(emailMessage);
			System.out.println("Email sent to the manager successfully.");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}}


