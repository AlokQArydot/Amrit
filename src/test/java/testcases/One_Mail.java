package testcases;

	import java.util.ArrayList;
	import java.time.Duration;
	import java.time.LocalDate;
	import java.time.format.DateTimeFormatter;
	import java.util.List;
	import java.util.NoSuchElementException;
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
	import org.openqa.selenium.TimeoutException;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.interactions.Actions;
	import org.testng.Reporter;
	import org.testng.annotations.Test;
	import org.openqa.selenium.support.ui.ExpectedConditions;
	import org.openqa.selenium.support.ui.WebDriverWait;
	
	
	public class One_Mail  extends Login {

	    // Create a list to store information about frequency values and no data found cases
	    private ArrayList<String> emailDetails = new ArrayList<>();

	    @Test(priority = 5)
	    public void Frequency_Report() throws InterruptedException {

	    	Thread.sleep(5000);

			Actions act = new Actions(driver);

			act.moveToElement(driver.findElement(By.xpath(prop.getProperty("Report")))).click().perform();

			Thread.sleep(3000);

			act.moveToElement(driver.findElement(By.xpath(prop.getProperty("AC/DC_Energy_Meters")))).click().perform();
			Thread.sleep(3000);

			WebElement Plant = driver.findElement(By.xpath(prop.getProperty("Plant_dropdown")));

			Thread.sleep(3000);

			Plant.click();

			Plant.sendKeys("The Baroda Crossway");

			Plant.sendKeys(Keys.ENTER);

			Thread.sleep(3000);

			WebElement Gateway_Dropdown = (driver.findElement(By.xpath(prop.getProperty("Gateways"))));

			Gateway_Dropdown.click();

			Thread.sleep(3000);

			// driver.findElement(By.xpath("//*[@placeholder='Gateway']")).sendKeys("FDP-3FA2
			// & 3FA1",Keys.ENTER);

			Thread.sleep(3000);

			List<WebElement> gatewayOptions = driver.findElements(By.xpath("//*[@role='menuitem']"));


	        for (int i = 1; i < gatewayOptions.size(); i++) {

	        	String xpathForItem = "//*[@role='menuitem'][" + i + "]";

				// Find the element and click on it
				WebElement gatewayOption = driver.findElement(By.xpath(xpathForItem));

				String gatewayName = gatewayOption.getText();

				Reporter.log(gatewayName, true);

				gatewayOption.click();
				
				WebElement Meter_Dropdown = (driver.findElement(By.xpath(prop.getProperty("Meter"))));

				Meter_Dropdown.click();

				Thread.sleep(5000);
				
				List<WebElement> MeterOptions = driver.findElements(By.xpath("//*[@role='menuitem']"));


	            for (int j = 1; j < MeterOptions.size(); j++) {

	            	String xpathForItem1 = "//*[@role='menuitem'][" + j + "]";

					// Find the element and click on it
					WebElement meterOption = driver.findElement(By.xpath(xpathForItem1));

					String metername = meterOption.getText();

					Reporter.log(metername, true);

					meterOption.click();

					WebElement Date = (driver.findElement(By.xpath(prop.getProperty("Date"))));

					LocalDate currentDate = LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					String formattedDate = currentDate.format(formatter);

					// Calculate the end date (e.g., 7 days from today)
					LocalDate endDate = currentDate.plusDays(0); // Adjust this as needed
					String formattedEndDate = endDate.format(formatter);

					// Concatenate start and end dates with a separator
					String dateRange = formattedDate + " - " + formattedEndDate;

					// Send the date range to the WebElement
					Date.sendKeys(dateRange);

					driver.findElement(By.xpath(prop.getProperty("Generate_Report"))).click();

					Thread.sleep(8000);

					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("window.scrollBy(0,250)", "");

					Thread.sleep(5000);

	                try {
	                    // Check if FrequencyValue is displayed
	                    WebElement frequencyElement = getWebDriverWait()
	                            .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tr[1]//td[3]")));
	                    String frequencyValue = frequencyElement.getText();

	                    // Assuming Frequency_Hz is a double value, you can convert it to a string
	                    double frequency = Double.parseDouble(frequencyValue);

	                    // Check if the frequency is zero
	                    if (frequency == 0) {
	                        String details = "Gateway Name ==" + gatewayName + ",Meter Name ----" + metername
	                                + "The frequency value is zero. Please check.";
	                        emailDetails.add(details);
	                        Reporter.log(details, true);
	                    }
	                } catch (org.openqa.selenium.TimeoutException e) {
	                    if (isElementPresent(By.xpath("//table//p"))) {
	                        handleFrequencyNotPresent(gatewayName, metername);
	                    } else {
	                        handleOtherNoSuchElementException();
	                    }
	                }

	                JavascriptExecutor js1 = (JavascriptExecutor) driver;
					js1.executeScript("window.scrollBy(0,-750)", "");

					Thread.sleep(6000);

					Meter_Dropdown.click();

	            }

	            Gateway_Dropdown.click();
				Thread.sleep(3000);

	        }

	        // Send a single email to the manager with all the details
	        if (!emailDetails.isEmpty()) {
	            StringBuilder emailContent = new StringBuilder();
	            for (String detail : emailDetails) {
	                emailContent.append(detail).append("\n");
	            }
	            sendEmailToManager(emailContent.toString());
	        }
	    }

	    private WebDriverWait getWebDriverWait() {
			WebDriverWait getWebDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
			return getWebDriverWait;

		}

		private boolean isElementPresent(By by) {
			try {
				getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated(by));
				return true;
			} catch (org.openqa.selenium.NoSuchElementException e) {
				return false;
			}
		}

		private void handleOtherNoSuchElementException() {
			// Handle the case when the exception is not due to the absence of
			// //table//tr[1]//td[3]
			System.out.println("NoSuchElementException occurred for some other reason.");
		}

		private void handleFrequencyNotPresent(String gatewayName, String metername) {
			try {
				// Read the text from //table//p
				WebElement tablePElement = getWebDriverWait()
						.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//p")));
				String tablePText = tablePElement.getText();
				Reporter.log("Gateway Name ==" + gatewayName + ",Meter Name ----" + metername + "Text Found is "+tablePText+ "", true);
				sendEmailToManagerforGatewayDown("Gateway Name ==" + gatewayName + ",Meter Name ----" + metername + "Text Found is "+tablePText+ "");
			} catch (org.openqa.selenium.TimeoutException ex) {
				// Handle the case when neither FrequencyValue nor //table//p is present
				System.out.println("Neither FrequencyValue nor //table//p is present");
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
			// props.put("mail.smtp.host", "smtp.gmail.com"); // You may need to change this
			// based on your email provider
			// props.put("mail.smtp.port", "587"); // You may need to change this based on
			// your email provider

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
																															
				// Set the email subject and body
				//emailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse("yogeshp@rydotinfotech.com")); // CC // recipient's email
																														
				emailMessage.setSubject("Alert: Frequency is Zero");
				emailMessage.setText(message);
				// Send the email
				Transport.send(emailMessage);
				System.out.println("Email sent to the manager successfully.");
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
	
	
	private static void sendEmailToManagerforGatewayDown(String message) {
		final String username = "info@assistant-cap.com"; // Your Gmail username
		final String password = "bqnmlrpypgvmccfv"; // Your Gmail password

		// Set properties for the email server
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.office365.com"); // Microsoft365 port
		props.put("mail.smtp.port", "587");
		// props.put("mail.smtp.host", "smtp.gmail.com"); // You may need to change this
		// based on your email provider
		// props.put("mail.smtp.port", "587"); // You may need to change this based on
		// your email provider

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
																														
			// Set the email subject and body
			//emailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse("yogeshp@rydotinfotech.com")); // CC // recipient's email
																													
			emailMessage.setSubject("Alert: Gateway is Dowm");
			emailMessage.setText(message);
			// Send the email
			Transport.send(emailMessage);
			System.out.println("Email sent to the manager successfully for Gateway.");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
	



