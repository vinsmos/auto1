package mainConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Base {			
    public static Map<String,String> testData=null;
    public WebDriver driver = null;
	
    private static Map<String, String> ReadPropertiesXmlFile() {
        testData = new HashMap();
        try {
            File file = new File("auto1TestConfig.xml");
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.loadFromXML(fileInput);
            fileInput.close();

            Enumeration enuKeys = properties.keys();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = properties.getProperty(key);
                testData.put(key, value)
            }
        } catch (FileNotFoundException eFn) {
            eFn.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	return testData;
    }
	
    public Map<String, String> getCampignData() {
        return ReadPropertiesXmlFile();
    }
	
    public WebDriver getDriver() {
        Map<String, String> testData = getCampignData();
        if (testData.get("systemSetProperty").toString().equals("true")) 
            System.setProperty(testData.get("webDriver").toString(),testData.get("webDriverPath").toString());
  
        WebDriver driver = testData.get("browserDriver").toLowerCase().equals("firefox") ? new FirefoxDriver():new ChromeDriver();
        return driver;
    }
}
