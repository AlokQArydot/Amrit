package testcases;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class Login extends BaseClass {
	
	@Test(priority = 1)
	public void LOGIN() throws InterruptedException, IOException 
	{
		driver.findElement(By.xpath(prop.getProperty("login_btn"))).click();
		
		Reporter.log("click login button ");
		
		Thread.sleep(2000);
		
		WebElement username=driver.findElement(By.xpath(prop.getProperty("username_field")));
		
		username.sendKeys(prop.getProperty("username"));
		
		Reporter.log("enter username");
		
		WebElement password=driver.findElement(By.xpath(prop.getProperty("password_field")));
		
		password.sendKeys(prop.getProperty("password"));
		
		Reporter.log("enter password ");
		
		driver.findElement(By.xpath(prop.getProperty("login_btn1"))).click();
		
		Reporter.log("click login button ");
		
		Thread.sleep(2000);
	}

}
