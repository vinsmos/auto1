package auto1Test;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;

import mainConfig.Base;
import utilities.Util;

public class Auto1Test {
    public WebDriver driver;
    public Map<String, String> TestData;
    public Util u;
    public List<Integer> priceList = new ArrayList<Integer>();
    public List<Integer> priceListSorted = new ArrayList<Integer>();
    public List<Integer> yearList = new ArrayList<Integer>(); 
    public String[] paginationPricesData = null;
    public String[] paginationCarsData = null;
    public WebElement nextButton = null;
    public String modalPopUp;
  
    @Test(priority=0)
    public void search() throws InterruptedException {
        modalPopUp = TestData.get("css-popup-modal");  
        //Selecting filters conditions
        u.clickElement(TestData.get("css-select-filter-year"), driver);
        u.clickElement(TestData.get("css-select-filter-year-range"), driver);
        //Selecting and setting max pagination result
        u.setPaginationFilter(TestData.get("css-select-pagination-name"), driver);
        //Selecting "Sort" filter
	u.clickElement(TestData.get("css-select-filter-sort"), driver);
        //Waiting for page result loaded
        driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
        //Checking for modal pop up if appearing
        u.checkPopUp(modalPopUp, driver);
        //Checking total items result
        //Not expecting to have none search result
        Integer totalItems = u.getSearchResultItemsNumber(TestData.get("css-search-total-result"), driver);
        if (totalItems == 0) 
            Assert.assertEquals(totalItems, false);
	
        //Gathering search result data
        Boolean nextButtonClickable = true;
        nextButton = driver.findElement(By.cssSelector(TestData.get("css-pagination-next-link")));
        do {
            //Checking for modal pop up if appearing
            u.checkPopUp(modalPopUp, driver);
            paginationPricesData = u.executeScript(TestData.get("css-search-cars-price-result"), driver);
            paginationCarsData = u.executeScript(TestData.get("css-search-cars-data-result"), driver);
            u.setPriceslist(priceList, paginationPricesData);
            u.setYearsList(yearList, paginationCarsData);
            try {
                nextButton.click();
                //Waiting for page result loaded		
                driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
            }
            catch(Exception e) {
                nextButtonClickable = false;
            }
        } while (nextButtonClickable);
        //Assertion data
        //Price sort assertion
        priceListSorted = u.copyList(priceList);
        Collections.sort(priceListSorted, Collections.reverseOrder());
        boolean priceSorted = priceList.equals(priceListSorted);
        Assert.assertEquals(priceSorted, true);
        //Year assertion
        Assert.assertEquals(u.checkValueLessThan(yearList, Integer.parseInt(TestData.get("filter-year-value"))), false);
    }
  
    @BeforeTest
    public void beforeTest() {
        Base b = new Base();
        u  = new Util();
        driver = b.getDriver();
        TestData = Base.testData;
        driver.get(TestData.get("webappUrl"));
        driver.manage().window().maximize();		  
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }
}
