package com.wipro.testcases;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wipro.testbase.TestBaseClass;
import com.wipro.utilities.ExcelUtils;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC_002_Login_Edit_Ac_Info extends TestBaseClass{
	
	public TC_002_Login_Edit_Ac_Info(String FilePath) {
		super(FilePath);
		// TODO Auto-generated constructor stub
	}



	public WebDriver driver;
	public TestBaseClass uilocator;
	public TestBaseClass config;
	private String sTestCaseName;
	 
	private int iTestCaseRow;
	
	@BeforeMethod
	public void setUp() throws Exception {
		System.out.println("inside child-Before method");
		config = new TestBaseClass("src\\test\\resources\\config\\config.properties");
		// Create a new instance of the Firefox driver
//		WebDriverManager.firefoxdriver().setup();
//		driver = new FirefoxDriver();
		
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		
		//https://demo.opencart.com/index.php?route=account/success
		
		//step 1 open url https://demo.opencart.com/
		driver.get(config.getData("opencarturl"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
	}

	 @DataProvider
	 public Object[][] Authentication() throws Exception{
	 ExcelUtils.setExcelFile("src\\test\\resources\\testdata\\testdata.xlsx","Sheet1");
	 sTestCaseName = this.toString();
	 sTestCaseName = ExcelUtils.getTestCaseName(this.toString());
	 iTestCaseRow = ExcelUtils.getRowContains(sTestCaseName,0);
	 Object[][] testObjArray = ExcelUtils.getTableArray("src\\test\\resources\\testdata\\testdata.xlsx","Sheet1",iTestCaseRow);
	 return (testObjArray);
	 }

	 @Test(dataProvider = "Authentication")
	 public void login(String email,String password,String new_telephone) throws Exception {
		
		 System.out.println("inside child-@Test");
		// Get object locator file
		uilocator = new TestBaseClass("src\\test\\resources\\config\\locator.properties");

		//step 2 Click on My Account and then click on login
		//driver.manage().getCookies();
//		Robot rbt=new Robot();
//		rbt.keyPress(KeyEvent.VK_DOWN);
//		rbt.keyPress(KeyEvent.KEY_PRESSED);
		
		WebElement myaccount = driver.findElement(uilocator.getLocator("my_account_menu"));
		myaccount.click();
		
		WebElement login_link=driver.findElement(uilocator.getLocator("login_link"));
		login_link.click(); 

		//3 Enter email and password from Excel sheet
		
		driver.findElement(uilocator.getLocator("login_email")).sendKeys(email);
		
		driver.findElement(uilocator.getLocator("password")).sendKeys(password);
		
		//4 Click on Login button	
		
		driver.findElement(uilocator.getLocator("logon_button")).click();
		//check whether it is logged in using valid credentials,Capture screenshot and store in screenshot folder.
		
		try
		{
			// valid credential
			Assert.assertEquals("https://demo.opencart.com/index.php?route=account/account",driver.getCurrentUrl());
			
			this.takeSnapShot(driver);
			
			System.out.println("Login successful");
			
		}
		catch(AssertionError e)
		{
			this.takeSnapShot(driver);
			System.out.println("Login Failed ! invalid credentials");
			driver.close();
			
		}
		
		//5 Click on Edit account link
		driver.findElement(uilocator.getLocator("edit_ac_link")).click();
		//6 Enter new Telephone number
		driver.findElement(uilocator.getLocator("telephone")).clear();
		driver.findElement(uilocator.getLocator("telephone")).sendKeys(new_telephone);
		//7 Click on continue button
		
		driver.findElement(uilocator.getLocator("edit_continue_button")).click();
		
				WebElement edit_succ_msg=driver.findElement(uilocator.getLocator("edit_success_msg"));
				String edit_success_msg=edit_succ_msg.getText();
				this.takeElementSnapShot(driver,edit_succ_msg);
		    try {
				FileWriter fr=new FileWriter("src\\test\\resources\\outputfiles\\status.txt",true);
				BufferedWriter br=new BufferedWriter(fr);
				br.newLine();
				br.write(edit_success_msg);
				br.close();
						}
				catch(FileNotFoundException e)
				{
					System.out.println("File missing ");
				}
		
		    driver.findElement(uilocator.getLocator("my_account_menu")).click();
		    driver.findElement(uilocator.getLocator("logout_link")).click();
		    String logoutmsg=driver.findElement(uilocator.getLocator("logout_message")).getText();
			this.takeSnapShot(driver);
		    
		    try {
				FileWriter fr=new FileWriter("src\\test\\resources\\outputfiles\\status.txt",true);
				BufferedWriter br=new BufferedWriter(fr);
				br.newLine();
				br.write(logoutmsg);
				br.newLine();
				br.close();
						}
				catch(FileNotFoundException e)
				{
					System.out.println("File missing ");
				}
		System.out.println("End of @Test ");
	}

	 //Common function to take screenshot of the current windows
	 public  void takeSnapShot(WebDriver webdriver) throws Exception{

	        //Convert web driver object to TakeScreenshot
		 
		    Date currentDate=new Date();
			String uniquefilename=currentDate.toString().replace(" ", "_").replace(":","_");
			System.out.println(uniquefilename);
			File screnshotfile=((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screnshotfile, new File("src\\test\\resources\\screenshots\\"+uniquefilename+"_TC_002_Login.png"));
		 
	 }
	 
	 //Common function to take screenshot of the Particular Element
	 public  void takeElementSnapShot(WebDriver webdriver,WebElement element) throws Exception{

		 	Date currentDate=new Date();
			String uniquefilename=currentDate.toString().replace(" ", "_").replace(":","_");
			System.out.println(uniquefilename);
		    File f = element.getScreenshotAs(OutputType.FILE);
		    FileUtils.copyFile(f, new File("src\\test\\resources\\screenshots\\"+uniquefilename+"_TC_002_Login.png"));
	
	 }
	 
	 
	@AfterMethod
	public void afterMethod() throws Exception {
		System.out.println("inside child-@after method");
		//driver.quit();
	}
}