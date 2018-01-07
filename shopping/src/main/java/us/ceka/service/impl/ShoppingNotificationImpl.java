package us.ceka.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.ceka.service.ShoppingNotification;


@Service("shoppingNotificationService")
@Transactional
public class ShoppingNotificationImpl extends BaseServiceImpl implements ShoppingNotification{

	@Autowired
	private WebDriver driver;

	public void scanPage() {
		//driver = new FirefoxDriver();
		String baseUrl = "http://bet.hkjc.com/football/default.aspx";
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(baseUrl + "/football/default.aspx");
		driver.switchTo().frame("betSlipFrame");
		driver.findElement(By.id("account")).clear();
		driver.findElement(By.id("account")).sendKeys("23401885");
		driver.findElement(By.id("passwordInput1")).clear();
		driver.findElement(By.id("password")).sendKeys(new String[]{"51824119a"});
		driver.findElement(By.id("divAccInfoDefaultLoginButton")).click();

		new WebDriverWait(driver, 10).until(
				ExpectedConditions.visibilityOfElementLocated(By.id("ekbaDivInput"))
				);

		Map<String, String> anwserPairs = new HashMap<String, String>();
		anwserPairs.put("你曾就讀哪一間中學?", "nthyksdss");
		anwserPairs.put("你第一部手提電話是甚麼品牌?", "ericsson");
		anwserPairs.put("你第一份工作的辦公室在哪個地區?", "fotan");


		String answer = anwserPairs.get(driver.findElement(By.id("ekbaSeqQuestion")).getText());
		log.info("answer: {}", answer);
		if(answer != null) {
			driver.findElement(By.id("ekbaDivInput")).sendKeys(answer);
			driver.findElement(By.id("pic_confirm")).click();
		}

		new WebDriverWait(driver, 5).until(
				ExpectedConditions.visibilityOfElementLocated(By.id("btn_enter"))
				).click();

		/////////////////////////////////////////////////////
		driver.switchTo().parentFrame();
		driver.switchTo().frame("info");
		int numWindow = driver.getWindowHandles().size();
		
		driver.findElement(By.id("114721_HAD_D")).click();
		new WebDriverWait(driver, 5).until(
				numberOfWindowsToBe(numWindow + 1)
		);
		String defaultWindow = driver.getWindowHandle();
		String popupWindow = driver.getWindowHandles().iterator().next();
		for(String window : driver.getWindowHandles()) {
			popupWindow = window;
			log.info("window: {}", window);	
		}		
		/*
	    "阿仙奴 對 曼聯"
	    "弗賴堡 對 史浩克04"
	    "賓福特 對 布力般流浪"
		 */

		//id + text
		driver.switchTo().window(defaultWindow);
		driver.findElement(By.id("114725_HAD_H")).click();
		driver.switchTo().window(popupWindow);
		//log.info("text: {}", driver.findElement(By.id("oddsTable")).getText());
		new WebDriverWait(driver, 5).until(
				ExpectedConditions.textToBePresentInElementLocated(By.id("oddsTable"), "弗賴堡 對 史浩克04")
		);
		/*
		driver.switchTo().window(defaultWindow);
		driver.findElement(By.id("114681_HAD_H")).click();
		driver.switchTo().window(popupWindow);
		new WebDriverWait(driver, 5).until(
				ExpectedConditions.textToBePresentInElementLocated(By.id("oddsTable"), "賓福特 對 布力般流浪")
		);
		*/

		driver.switchTo().window(popupWindow);
		driver.findElement(By.id("btn_addslip2")).click();
		
		driver.switchTo().window(defaultWindow);
		driver.switchTo().parentFrame();
		driver.switchTo().frame("betSlipFrame");
		driver.findElement(By.id("pic_preview")).click();



		/*
		webDriver.get("https://www.endclothing.com/hk/y-3-qasa-high-s82122.html");
		WebDriverWait wait = new WebDriverWait(webDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("attribute173_title")));

		WebElement dropdown = webDriver.findElement(By.id("attribute173_title"));
		dropdown.click();

		List<WebElement> options = webDriver.findElements(By.cssSelector("#attribute173_child li"));
		options.forEach(opt -> {
			if(opt.getText().contains("UK 9")) {
				opt.click();
				WebElement button = webDriver.findElement(By.cssSelector("button.form-button"));
				log.info(button.getText());
				if("Add to Cart".equalsIgnoreCase(button.getText())) {
					button.submit();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.messages")));
					//System.out.println(webDriver.findElement(By.cssSelector("ul.messages")).getText());
				}
			}
		});
		 */
		//webDriver.quit();
		/*
		// Check the title of the page
		log.info("Page title is: " + webDriver.getTitle());

		// Wait for the page to load, timeout after 10 seconds
		(new WebDriverWait(webDriver, 10)).until(
				new ExpectedCondition<Boolean>() {

					public Boolean apply(WebDriver d) {
						return d.getTitle().toLowerCase().startsWith("cheese!");
					}
				});

		// Should see: "cheese! - Google Search"
		log.info("Page title is: " + webDriver.getTitle());
		 */
	}

	public static ExpectedCondition<Boolean> numberOfWindowsToBe(final int numberOfWindows) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return driver.getWindowHandles().size() == numberOfWindows;
			}
		};
	}
}
