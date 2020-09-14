package com.mavenTestNgDemoPkg;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC_01_Test {
	public static	WebDriver driver;
	public  String uniquefilename;
	
	@BeforeClass
	public static void setup()
	{
		 // System.setProperty("webdriver.gecko.driver", "D:\\geckodriver.exe");  
		 // driver = new FirefoxDriver();
System.out.println("started");
		 WebDriverManager.firefoxdriver().setup();
			 driver = new FirefoxDriver();

	        driver.manage().window().maximize();
	}

	@BeforeTest
	public void createFileName()
	{
		Date currentDate=new Date();
		uniquefilename=currentDate.toString().replace(" ", "_").replace(":","_");
		System.out.println(uniquefilename);
	}
  @Test
  public void testTakeScreenShot() throws Exception{

	  System.out.println("inside test");
      //goto url
		
		// Step 1 	 Launch Open Cart application https://demo.opencart.com/ 
		driver.get("https://demo.opencart.com/"); 
		// Step 2 	 Click on "Login" Link 
		driver.findElement(By.xpath("//span[contains(text(),'My Account')]")).click();
		
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click(); 
		
		// Step 3 	Enter Email Address and Password and click on "Login" Button. 
		driver.findElement(By.xpath("//input[@type='text' and @name='email']")).sendKeys("jilcy@gmail.com");
		driver.findElement(By.xpath("//input[@type='password' and @name='password']")).sendKeys("Jilcy123*");
		//click on "Login" Button. 
		driver.findElement(By.xpath("//input[@type='submit' and @value='Login']")).click();
		//check whether it is logged in using valid credentials
		try
		{
			// valid credential
			Assert.assertEquals("https://demo.opencart.com/index.php?route=account/account",driver.getCurrentUrl());
			
			System.out.println("Login successful");
			this.takeSnapShot(driver) ;  
			
			
		}
		catch(AssertionError e)
		{
			System.out.println("Login Failed ! invalid credentials");
			driver.close();
			
			
		}

  }
  
  
  public void takeSnapShot(WebDriver webdriver) throws Exception{

	  File screnshotfile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screnshotfile, new File(".//screenshots/"+uniquefilename+"ss.png"));

  }
}
