package com.wipro.testcases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.wipro.testbase.TestBaseClass;
import com.wipro.utilities.ExcelUtils;
import io.github.bonigarcia.wdm.WebDriverManager;


public class TC_001_Registration extends TestBaseClass {

	//constructor
	public TC_001_Registration(String FilePath) {
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
	 
	     // Setting up the Test Data Excel file
	 
	 ExcelUtils.setExcelFile("src\\test\\resources\\testdata\\registration.xlsx","Sheet1");
	 
	 sTestCaseName = this.toString();
	 
	   	// From above method we get long test case name including package and class name etc.
	 
	   	// The below method will refine your test case name, exactly the name use have used
	 
	   	sTestCaseName = ExcelUtils.getTestCaseName(this.toString());
	 
	     // Fetching the Test Case row number from the Test Data Sheet
	 
	     // Getting the Test Case name to get the TestCase row from the Test Data Excel sheet
	 
	 iTestCaseRow = ExcelUtils.getRowContains(sTestCaseName,0);
	 
	     Object[][] testObjArray = ExcelUtils.getTableArray("src\\test\\resources\\testdata\\registration.xlsx","Sheet1",iTestCaseRow);
	 
	     	return (testObjArray);
	 
	 }

	 @Test(dataProvider = "Authentication")
	public void registration(String firstName,String lastName,String email,String telephone,String password,String confirm_password) throws Exception {
		System.out.println("inside child-@Test");
		// Get object locator file
		uilocator = new TestBaseClass("src\\test\\resources\\config\\locator.properties");

//	
//		//assertEquals("Hi, John Smith", onlineuser.getText());
		
		//step 2 Click on My Account and then register
		
		WebElement myaccount = driver.findElement(uilocator.getLocator("my_account_menu"));
		myaccount.click();
		
		WebElement register_link=driver.findElement(uilocator.getLocator("register_link"));
		register_link.click(); 
	//step 3, fill details
		driver.findElement(uilocator.getLocator("firstname")).sendKeys(firstName);
		driver.findElement(uilocator.getLocator("lastname")).sendKeys(lastName);
		driver.findElement(uilocator.getLocator("email")).sendKeys(email);
		driver.findElement(uilocator.getLocator("telephone")).sendKeys(telephone);
		driver.findElement(uilocator.getLocator("password")).sendKeys(password);
		driver.findElement(uilocator.getLocator("confirm_password")).sendKeys(confirm_password);
				
		//step 4 Verify privacy policy checkbox is checked or not
		WebElement checkbox = driver.findElement(uilocator.getLocator("policy_checkbox"));
		

		if( checkbox.isSelected()==true)
		{
			System.out.println("The checkbox is checked");
			
			String checkboxstatus="The checkbox is checked";
			
			try {
			FileWriter fr=new FileWriter("src\\test\\resources\\outputfiles\\status.txt",true);
			BufferedWriter br=new BufferedWriter(fr);
			br.newLine();
			br.write(checkboxstatus);
			br.newLine();
		
			br.close();
		
			}
			catch(FileNotFoundException e)
			{
				System.out.println("File missing ");
			}
		}
		else
		{
			System.out.println("The checkbox is UNchecked");
			
			String checkboxstatus="The checkbox is UNchecked";
			
			try {
			FileWriter fr=new FileWriter("src\\test\\resources\\outputfiles\\status.txt",true);
			BufferedWriter br=new BufferedWriter(fr);
			br.newLine();
			br.write(checkboxstatus);
			br.newLine();
		
			br.close();
		
			}
			catch(FileNotFoundException e)
			{
				System.out.println("File missing ");
			}
		}
		
		//step 5 Check the privacy checkbox and click on continue		
		checkbox.click();
		driver.findElement(uilocator.getLocator("register_button")).click();
		
		String msg=driver.findElement(uilocator.getLocator("success_message")).getText();
		
		try {
			FileWriter fr=new FileWriter("src\\test\\resources\\outputfiles\\status.txt",true);
			BufferedWriter br=new BufferedWriter(fr);
			br.newLine();
			br.write(msg);
			br.newLine();
			br.close();
					}
			catch(FileNotFoundException e)
			{
				System.out.println("File missing ");
			}
		
		Date currentDate=new Date();
		String uniquefilename=currentDate.toString().replace(" ", "_").replace(":","_");
		System.out.println(uniquefilename);
		File screnshotfile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screnshotfile, new File("src\\test\\resources\\screenshots\\"+uniquefilename+"TC_001_Reg.png"));
		
		
		driver.findElement(uilocator.getLocator("my_account_menu")).click();
	    driver.findElement(uilocator.getLocator("logout_link")).click();
	    String logoutmsg=driver.findElement(uilocator.getLocator("logout_message")).getText();
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

	 

	@AfterMethod
	public void afterMethod() throws Exception {
		System.out.println("inside child-@after method");
		//driver.quit();
	}

	
}
