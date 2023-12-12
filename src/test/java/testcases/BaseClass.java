package testcases;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;


import io.github.bonigarcia.wdm.WebDriverManager;
public class BaseClass {
	
	WebDriver driver;
	Properties prop=new Properties();
	FileReader fr;
	@BeforeSuite
	public void setup() throws IOException 
	{
		Reporter.log("=====Browser Session Started=====", true);
	if(driver==null)	{
	FileReader fr=new FileReader("/home/alokkumarnayak/eclipse-workspace/Crossway/Cofigure/config.properties");
	prop.load(fr);
	}
	if(prop.getProperty("browser").equalsIgnoreCase("chrome")) {
		WebDriverManager.chromedriver().setup();
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--remote-allow-origins=*");
		 driver = new ChromeDriver(op);
		driver.manage().window().maximize();
		driver.get(prop.getProperty("testurl"));
		Reporter.log("=====Application Started=====", true);
	}
	else if(prop.getProperty("browser").equalsIgnoreCase("firefox")) {
		WebDriverManager.firefoxdriver().setup();
		driver=new FirefoxDriver();
		driver.get(prop.getProperty("testurl"));
		Reporter.log("=====Application Started=====", true);
	}
	}	
	
	  @AfterSuite
	  public void closeApplication() throws InterruptedException 
	  { 
		  Thread.sleep(10000);
		 // driver.quit();
	  Reporter.log("=====Browser Session End=====", true); 
	  }

}
