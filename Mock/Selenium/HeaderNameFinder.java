import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class HeaderNameFinder {
    public static void main(String[] args) throws InterruptedException {
        String siteUrl = "https://www.google.com.sg";
        List<String> linksWithEmptyTitlesHref = getEmptyTitleLinks(siteUrl);
        if (linksWithEmptyTitlesHref.isEmpty()) {
            System.out.println("\nAll webpages directly reachable from "
                    + siteUrl + " have titles.");
        } else {
            System.out.println("\nThe webpages directly reachable from "
                    + siteUrl + " listed below do not have titles.");

            for (int i = 0; i < linksWithEmptyTitlesHref.size(); i++) {
                System.out.println(i + ". " + linksWithEmptyTitlesHref.get(i));
            }
        }

    }

    private static List<String> getEmptyTitleLinks(String siteUrl) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "/Users/ericyap/Desktop/geckodriver");
        WebDriver driver = new FirefoxDriver();
        driver.get(siteUrl);

        List<WebElement> links = driver.findElements(By.tagName("a"));
        List<String> linksHref = new ArrayList<>();
        List<String> linksWithEmptyTitlesHref = new ArrayList<>();
        driver.manage().window().maximize();

        for (int i = 0; i < links.size(); i++) {
            System.out.println(i + ". " + links.get(i).getAttribute("href"));
            linksHref.add(links.get(i).getAttribute("href"));
        }

        for (String aLinksHref : linksHref) {
            if (aLinksHref == null || !aLinksHref.substring(0, 4).equals("http"))
                continue;

            System.out.println("*** Checking " + aLinksHref);
            driver.navigate().to(aLinksHref);
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("html")));

            if (driver.getTitle() == null || driver.getTitle().isEmpty())
                linksWithEmptyTitlesHref.add(aLinksHref);
        }

        return linksWithEmptyTitlesHref;
    }

}

