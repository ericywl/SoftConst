import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

public class InvalidGoogleLogin {
    static String myUserName = "###sudiptaonline";
    static String myPassword = "thisisnotmypassword";

    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", "/Users/ericyap/Desktop/geckodriver");

        WebDriver driver = new FirefoxDriver();
        driver.get("https://accounts.google.com/" +
                "signin/v2/identifier?hl=EN&flowName=GlifWebSignIn&flowEntry=ServiceLogin");

        WebElement username = driver.findElement(By.name("identifier"));
        WebElement nextBtn = driver.findElement(By.id("identifierNext"));
        String[] usernameArr = genArrayOfUserNames(20, 20);

        // last string is myUserName
        for (String u: usernameArr) {
            username.clear();
            username.sendKeys(u);
            nextBtn.click();

            try {
                WebDriverWait wait = new WebDriverWait(driver, 3);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));

                WebElement password = driver.findElement(By.name("password"));
                password.sendKeys(myPassword);

                nextBtn = driver.findElement(By.id("passwordNext"));
                nextBtn.click();
            } catch (TimeoutException ex) {
                System.out.println("Invalid username.");
            }
        }
    }

    private static String[] genArrayOfUserNames(int num, int fuzzLength) {
        String[] array = new String[num + 1];
        for (int i = 0; i < num; i++) {
            array[i] = fuzz(fuzzLength);
        }

        array[array.length - 1] = myUserName;
        return array;
    }

    private static String fuzz(int fuzzLength) {
        String alphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz" +
                "0123456789";

        StringBuilder strBld = new StringBuilder();
        for (int i = 0, len = alphaNum.length(); i < fuzzLength; i++) {
            int index = new Random().nextInt(len);
            strBld.append(alphaNum.charAt(index));
        }

        return strBld.toString();
    }
}
