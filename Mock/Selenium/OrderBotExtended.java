import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class OrderBotExtended {
    static String myUserName = "sudiptacasual@gmail.com";
    static String myPassword = "thisisnotmypassword";

    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", "/Users/ericyap/Desktop/geckodriver");

        WebDriver driver = new FirefoxDriver();
        driver.get("https://deliveroo.com.sg/login");

        // get the username and password field of the account page
        WebElement username = driver.findElement(By.name("login_email"));
        WebElement password = driver.findElement(By.name("login_password"));
        // send username and password
        username.sendKeys(myUserName);
        password.sendKeys(myPassword);
        // submit
        username.submit();
        password.submit();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.get("https://deliveroo.com.sg");
        // get postcode field and send 485999
        WebElement postcode = driver.findElement(By.id("postcode"));
        postcode.sendKeys("485999");
        postcode.submit();
    }
}
