package com.wipro.testcases;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wipro.testbase.TestBaseClass;
import com.wipro.utilities.ExcelUtils;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC_003_AddCart_Payment {
	

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
	 ExcelUtils.setExcelFile("src\\test\\resources\\testdata\\testdata.xlsx","TC_03_Data");
	 sTestCaseName = this.toString();
	 sTestCaseName = ExcelUtils.getTestCaseName(this.toString());
	 iTestCaseRow = ExcelUtils.getRowContains(sTestCaseName,0);
	 Object[][] testObjArray = ExcelUtils.getTableArray("src\\test\\resources\\testdata\\testdata.xlsx","TC_03_Data",iTestCaseRow);
	 return (testObjArray);
	 }

	 @Test(dataProvider = "Authentication")
	 public void login(String email,String password,String checkboxval,String textval,String selectboxval,String textareaval,String fileval,String date_val,String time_val,String date_time_val,String qtyval,String country,String state,String pin) throws Exception {
		
		 System.out.println("inside child-@Test");
		// Get object locator file
		uilocator = new TestBaseClass("src\\test\\resources\\config\\locator.properties");

		//step 2 Click on My Account and then click on login
		
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
		
		//5  Click on components menu and select Monitors
		driver.findElement(uilocator.getLocator("components_tab")).click();
		driver.findElement(uilocator.getLocator("monitors_link")).click();
		//Take screenshot of page and store in screenshot folder
		this.takeSnapShot(driver);
		
		//6 Fetch Apple Cinema 30" name and its prize and store in text file.
		String itemName=driver.findElement(uilocator.getLocator("apple_tv_name")).getText();
		String itemPrice=driver.findElement(uilocator.getLocator("apple_tv_price")).getText();
		 try {
				FileWriter fr=new FileWriter("src\\test\\resources\\outputfiles\\Prize.txt",true);
				BufferedWriter br=new BufferedWriter(fr);
				br.newLine();
				br.write(itemName);
				br.write(" ");
				br.write(itemPrice);
				br.close();
						}
				catch(FileNotFoundException e)
				{
					System.out.println("File missing ");
				}
		 //7 Click on add to cart button displayed under Apple Cinema 30"
		 driver.findElement(uilocator.getLocator("apple_add_cart_button")).click();
		 //8 Fill the product Available options like, radio, checkbox, Text , Select color, In text area add note, upload a sample img file, date, time and quantity
		
		 driver.findElement(uilocator.getLocator("checkbox_1")).click();
		 driver.findElement(uilocator.getLocator("text_input")).clear();
		 driver.findElement(uilocator.getLocator("text_input")).sendKeys(textval);
		
		 Select s1=new Select(driver.findElement(uilocator.getLocator("select_box")));
		 s1.selectByValue(selectboxval);
		 driver.findElement(uilocator.getLocator("text_area")).sendKeys(textareaval);
		 WebElement addFile = driver.findElement(uilocator.getLocator("file_upload"));
		 addFile.sendKeys(fileval);
		 driver.findElement(uilocator.getLocator("date_field")).clear();
		 driver.findElement(uilocator.getLocator("date_field")).sendKeys(date_val);
		 driver.findElement(uilocator.getLocator("time_field")).clear();
		 driver.findElement(uilocator.getLocator("time_field")).sendKeys(time_val);
		 driver.findElement(uilocator.getLocator("date_time")).clear();
		 driver.findElement(uilocator.getLocator("date_time")).sendKeys(date_time_val);
		 driver.findElement(uilocator.getLocator("qty")).clear();
		 driver.findElement(uilocator.getLocator("qty")).sendKeys(qtyval);
		 
		 //9 Click on add to cart button
		 driver.findElement(uilocator.getLocator("add_cart_item")).click();
   	     driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
   	     driver.findElement(uilocator.getLocator("add_cart_item")).click();
		 boolean error_msg_presence=driver.findElement(uilocator.getLocator("radio_error_msg")).isDisplayed();
          System.out.println(error_msg_presence);
          
          if(error_msg_presence==true)
          {
        	  this.takeSnapShot(driver);
        	  driver.navigate().back();
        	  driver.findElement(uilocator.getLocator("samsung_add_cart_btn")).click();
        	  driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        	  WebElement  cart_success_msg= driver.findElement(uilocator.getLocator("cart_success_msg"));
        	  driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        	  this.takeElementSnapShot(driver,cart_success_msg);
        	  //10 Click on Shopping Cart link available on the top right of page
        	  driver.findElement(uilocator.getLocator("shopping_cart_link")).click(); 
        	  
        	  //Save the Product Name and Total prize in prize.txt file
        	  String cart_item_1=driver.findElement(uilocator.getLocator("cart_item_1")).getText();
        	  String cart_item_1_price=driver.findElement(uilocator.getLocator("cart_item_1_price")).getText();
     		 try {
     				FileWriter fr=new FileWriter("src\\test\\resources\\outputfiles\\Prize.txt",true);
     				BufferedWriter br=new BufferedWriter(fr);
     				br.newLine();
     				br.write(cart_item_1);
     				br.write(" ");
     				br.write(cart_item_1_price);
     				br.close();
     						}
     				catch(FileNotFoundException e)
     				{
     					System.out.println("File missing ");
     				}
        	  int rowNum = driver.findElements(By.xpath("//div[@class='table-responsive']/table/tbody/tr")).size();
				String l="//tr[";
				String r="]/td[2]/span[contains(text(),'***')]";
				System.out.println("before for ");
								for(int m=1; m<=rowNum; m++){
									System.out.println(l+m+r);
									
									if(driver.findElement(By.xpath(l+m+r))!= null){
				
										System.out.println("span is Present");
										
				
										String left="//div[@class='table-responsive']/table/tbody/tr[";
										String right="]/td[4]/div/span/button[@data-original-title='Remove']";
										 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
										driver.findElement(By.xpath(left+m+right)).click(); 
										
										driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
										rowNum=rowNum-1;
										m=m-1;
										System.out.println(m+"removed"+rowNum);
										
										}else{
				
										System.out.println("span  is Absent");
				
										}	
									}
							
								System.out.println("11 Click on Estimate Shipping & Taxes");
								driver.findElement(uilocator.getLocator("estimate_tax")).click(); 	
								
								 Select estimate_country=new Select(driver.findElement(uilocator.getLocator("estimate_country")));
								 estimate_country.selectByValue(country);
								 
								 Select estimate_state=new Select(driver.findElement(uilocator.getLocator("estimate_state")));
								 estimate_state.selectByValue(state);
								 driver.findElement(uilocator.getLocator("estimate_pin")).sendKeys(pin); 	
						    	System.out.println("13 Click on Get Quotes button");
								 driver.findElement(uilocator.getLocator("get_quotes_btn")).click();
								 
//								 WebElement radiobtn= driver.findElement(uilocator.getLocator("popup_radio"));
//								 boolean radio_presence=radiobtn.isDisplayed();
//								
//								 if(radio_presence==true)
//								 {
//									 boolean radio_select=radiobtn.isSelected();
//									 System.out.println("Radio button status is "+radio_select);
//									 
//									 driver.findElement(uilocator.getLocator("button_shipping")).click();
//									 
//									 WebElement estimate_succ_msg= driver.findElement(uilocator.getLocator("estimate_succ_msg"));
//									 boolean msg_present=estimate_succ_msg.isDisplayed();
//									 
//									 if(msg_present==true)
//									 {
//										 System.out.println("success message displayed");
//										 
//									 }
//									 else
//									 {
//										 System.out.println("success message NOT displayed");
//									 }
//									 
//								 }
//								 else
//								 {
//									 System.out.println("pop up not displayed");
//								 }
//								
//								 
//				 // Step 15	 Click on Check out button. 
//								 driver.findElement(uilocator.getLocator("checkout_btn")).click();
//				//16 Fill the Billing details and click on continue button
//								 
//								 System.out.println("Filling Billing details");
//									
//								 driver.findElement(uilocator.getLocator("bfname")).sendKeys("Jilcy");
//								 driver.findElement(uilocator.getLocator("blname")).sendKeys("JA");
//								 driver.findElement(uilocator.getLocator("bcompany")).sendKeys("Wipro");
//								 driver.findElement(uilocator.getLocator("badrs1")).sendKeys("Marangattu");
//								 driver.findElement(uilocator.getLocator("badrs2")).sendKeys("Inchiany PO");
//								 driver.findElement(uilocator.getLocator("bcity")).sendKeys("Mundakayam");
//								 driver.findElement(uilocator.getLocator("bpostcode")).sendKeys(pin);
//								 
//								 WebElement bcountry=driver.findElement(uilocator.getLocator("bcountry"));
//									Select country_item=new Select(bcountry);
//									country_item.selectByVisibleText("India");
//									
//									WebElement zone=driver.findElement(uilocator.getLocator("bstate"));
//									Select zone_item=new Select(zone);
//									zone_item.selectByVisibleText("Kerala");
//								
//									WebDriverWait wait = new WebDriverWait(driver, 5);
//									 WebElement element = driver.findElement(By.xpath("//input[@type='button' and @id='button-payment-address']"));
//									 wait.until(ExpectedConditions.elementToBeClickable(element));
//									 element.click();
//									 
//									 //driver.findElement(By.xpath("//input[@type='button' and @id='button-payment-address']")).click();
//									 System.out.println(" clicked buttonn 2");
//									 
//									 WebDriverWait btn_3_wait = new WebDriverWait(driver, 5);
//									 WebElement btn_3_element = driver.findElement(By.xpath("//input[@type='button' and @id='button-shipping-address']"));
//									 btn_3_wait.until(ExpectedConditions.elementToBeClickable(btn_3_element ));
//									 btn_3_element .click();
//									 
//									 System.out.println(" clicked buttton 3");
//									 
//									 WebDriverWait btn_4_wait = new WebDriverWait(driver, 5);
//									 WebElement btn_4_element = driver.findElement(By.xpath("//input[@type='button' and @id='button-shipping-method']"));
//									 btn_4_wait.until(ExpectedConditions.elementToBeClickable(btn_4_element ));
//									 btn_4_element .click();
//									
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
			FileUtils.copyFile(screnshotfile, new File("src\\test\\resources\\screenshots\\"+uniquefilename+"_TC_003_Cart.png"));
		 
	 }
	 
	 //Common function to take screenshot of the Particular Element
	 public  void takeElementSnapShot(WebDriver webdriver,WebElement element) throws Exception{

		 	Date currentDate=new Date();
			String uniquefilename=currentDate.toString().replace(" ", "_").replace(":","_");
			System.out.println(uniquefilename);
		    File f = element.getScreenshotAs(OutputType.FILE);
		    FileUtils.copyFile(f, new File("src\\test\\resources\\screenshots\\"+uniquefilename+"_TC_003_Cart.png"));
	
	 }
	 
	 
	@AfterMethod
	public void afterMethod() throws Exception {
		System.out.println("inside child-@after method");
		//driver.quit();
	}

}
