package base;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import clients.AuthClient;
public class BaseTest {
	protected static String accessToken;
	protected static ExtentReports extent;
	protected static ExtentTest test;
	
	
	@BeforeSuite
	public void setExtentReport()
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String timeStamp = formatter.format(LocalDateTime.now());
		
		String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport_" + timeStamp;
		ExtentSparkReporter esr = new ExtentSparkReporter(reportPath);
		esr.config().setTheme(Theme.STANDARD);
		esr.config().setReportName("Flight Booking API Test Report");
		esr.config().setDocumentTitle("Test Results");
		
		extent = new ExtentReports();
		extent.attachReporter(esr);
		extent.setSystemInfo("Tester", "Prayag Pachauri");
		extent.setSystemInfo("OS", "Windows 11");
		
	}
	
	@BeforeClass
	public void setToken() {
		accessToken = AuthClient.getAccessToken();
	}
	
	@AfterClass
	public void tearDown() {
		extent.flush();
	}
	
	
	
}
