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
        if (args.length > 2) {
            System.out.println("Only a maximum of 2 arguments allowed.");
            return;
        }

        if (args.length == 0) {
            System.out.println("At least one argument for website required.");
            return;
        }

        // set driver path
        String driverPath = "/Users/sudiptac/sudiptac/teaching/SUTD/50.003@2018/Test/chromedriver";
        if (args.length == 2) {
            driverPath = args[1];
        }

        System.setProperty("webdriver.gecko.driver", driverPath);

        String siteUrl = args[0];
        List<String> linksWithEmptyTitlesHref = getEmptyTitleLinks(siteUrl);
        if (linksWithEmptyTitlesHref.isEmpty()) {
            System.out.println("\nAll webpages directly reachable from "
                    + siteUrl + " have titles.");
        } else {
            System.out.println("\nThe webpages directly reachable from "
                    + siteUrl + " listed below do not have titles.");

            // print out all links with empty title
            for (int i = 0; i < linksWithEmptyTitlesHref.size(); i++) {
                System.out.println(i + ". " + linksWithEmptyTitlesHref.get(i));
            }
        }

    }

    // get links that have empty title
    private static List<String> getEmptyTitleLinks(String siteUrl) throws InterruptedException {
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

        // go through all links in page
        for (String aLinksHref : linksHref) {
            if (aLinksHref == null || !aLinksHref.substring(0, 4).equals("http"))
                continue;

            System.out.println("*** Checking " + aLinksHref);
            driver.navigate().to(aLinksHref);
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("html")));

            // check if title is empty
            if (driver.getTitle() == null || driver.getTitle().isEmpty())
                linksWithEmptyTitlesHref.add(aLinksHref);
        }

        return linksWithEmptyTitlesHref;
    }

}

