import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

public class FindAndClickRandomLink {
    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", "/Users/ericyap/Desktop/geckodriver");
        WebDriver driver = new FirefoxDriver();

        driver.get("https://istd.sutd.edu.sg/");

        // get all the links
        List<WebElement> links = driver.findElements(By.tagName("a"));
        driver.manage().window().maximize();

        while (true) {

            int linkIndex = new Random().nextInt(links.size());
            String linkHref = links.get(linkIndex).getAttribute("href");
            if (linkHref == null || !linkHref.substring(0, 4).equals("http"))
                continue;

            System.out.println("*** Navigating to " + links.get(linkIndex).getAttribute("href"));
            boolean staleElementLoaded = true;
            while (staleElementLoaded) {
                try {
                    driver.navigate().to(links.get(linkIndex).getAttribute("href"));
                    WebDriverWait wait = new WebDriverWait(driver, 10);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("html")));

                    driver.navigate().back();
                    links = driver.findElements(By.tagName("a"));
                    System.out.println("*** Back from "
                            + links.get(linkIndex).getAttribute("href"));

                    staleElementLoaded = false;
                } catch (StaleElementReferenceException ex) {
                    staleElementLoaded = true;
                }
            }
        }
    }
}

