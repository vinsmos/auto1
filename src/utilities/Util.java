package utilities;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
* Utils class 
* 
*
* @author  Vincenzo Moschese
* @version 1.0
*/
public class Util {
    public JavascriptExecutor js;
	
    public List<Integer> setPriceslist(List<Integer> priceList, String[] prices) {
	    for (String price: prices) {           
	        priceList.add(Integer.parseInt(price.replaceAll("\\D+","")));
	    }
	    return priceList;
    }
	
    public List<Integer> setYearsList(List<Integer> yearList, String[] years) {
        for (String year: years) {           
            if(year.indexOf("•")!=-1) {
                year = year.replaceAll("\\r|\\n", "");
                year = year.split("•")[1];
                yearList.add(Integer.parseInt(year.split("/")[1]));
            }
        }
        return yearList;
    }
	
    public void clickElement(String selector, WebDriver driver) {
        WebElement filter = driver.findElement(By.cssSelector(selector));
        filter.click();
    }

    public boolean elementIsPresent(String selector, WebDriver driver) {
        boolean isPresent = true;
        List<WebElement> elements = driver.findElements(By.cssSelector(selector));
        if (elements.size() == 0) {
            isPresent = false;
        }
        return isPresent;
    }
	
    public void setPaginationFilter(String selector, WebDriver driver) {
        Select pagination = new Select(driver.findElement(By.cssSelector(selector)));
        int selectOptions = pagination.getOptions().size();
        pagination.selectByIndex(selectOptions - 1);		
    }
	
    public Integer getSearchResultItemsNumber(String selector, WebDriver driver) {
        String total = driver.findElement(By.cssSelector(selector)).getText();
        Integer totalItems = Integer.parseInt(total.replaceAll("[^0-9]", ""));
        return totalItems;
    }
	
    public String[] executeScript(String selector, WebDriver driver) {
        js = (JavascriptExecutor)driver;
        String result = js.executeScript("return Array.from(document.querySelectorAll('" + selector + "')).map(i => i.innerText);").toString();
        return parseData(result);
    }

    public String[] parseData(String data) {
        String[] dataParsed = null;
        data = data.replaceAll("\\[", "");
        dataParsed = data.split(",");
        return dataParsed;
    }
	
    public boolean checkValueLessThan(List<Integer> list, Integer value) {
        boolean valueLessThan = false;
        for (Integer data : list ) {
            if (Integer.valueOf(data) < Integer.valueOf(value)) {
                valueLessThan = true;
                break;
            }
        } 		
        return valueLessThan;
    }
	
    public List<Integer> copyList(List<Integer> sourceList) {
        List<Integer> destinationlist = new ArrayList<Integer>();
        for (Integer data : sourceList ) {
            destinationlist.add(data);
        }
        return destinationlist;
    }
	
    public void checkPopUp(String selector, WebDriver driver) {
        if (elementIsPresent(selector, driver)) {
            clickElement(selector, driver);
        }		
    }
}
