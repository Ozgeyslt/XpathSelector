import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class XpathTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp(){

        System.setProperty("webdriver.chrome.driver",
               System.getProperty("user.dir") + "\\src\\main\\java\\web\\test\\drivers\\chromedriver.exe");

        ChromeOptions options=new ChromeOptions();

        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--no-sandbox"); //
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");

        driver=new ChromeDriver(options);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait=new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillInTheBlank(WebElement element, String value){
        new Actions(driver).click(element).sendKeys(value).perform();
    }

    public void clickButton(WebElement element){
        new Actions(driver).click(element).perform();
    }

    public void scrollThePage(int number1, int number2){
        new Actions(driver).scrollByAmount(number1,number2).perform();
    }


    @Test(priority = 1)
    public void clickButton(){
        driver.get("https://demoqa.com/elements");

        //Elements sayfasında buttons elementi seçildi
        WebElement buttons=driver.findElement(By.xpath("//li/span[text()='Buttons']"));
        clickButton(buttons);

        scrollThePage(0,300);

        //Buttonların görünürlüğü kontrol edildi
        WebElement waitButtons=wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div/h1[@class=\"text-center\"]")));
        waitButtons.isDisplayed();

        //ClickMe butonuna tıklandi
        WebElement clickMe=driver.findElement(By.xpath("//div/button[text()='Click Me']"));
        clickButton(clickMe);

        //ClickMe butonunun, dinamik mesajinin doğrulamasi yapildi
        WebElement result=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/p[@id=\"dynamicClickMessage\"]")));
        result.isDisplayed();
        Assert.assertEquals(result.getText(), "You have done a dynamic click");

    }


    @Test(priority = 2)
    public void addRecord() throws InterruptedException {
        driver.get("https://demoqa.com/webtables");

        //WebTables sayfasinda add butonu seçildi
        WebElement add=driver.findElement(By.xpath("//button[@id='addNewRecordButton']"));
        clickButton(add);

        scrollThePage(0,300);

        //Form sayfasinda elementlerin xpath Locaterlari bulundu
        WebElement firstName=driver.findElement(By.xpath("//input[@id='firstName']"));
        WebElement lastName=driver.findElement(By.xpath ("//form[@id='userForm']//input[@id='lastName']"));
        WebElement email=driver.findElement(By.xpath ("//input[@id='userEmail']"));
        WebElement age=driver.findElement(By.xpath("//input[@id='age']"));
        WebElement salary=driver.findElement(By.xpath("//input[@id='salary']"));
        WebElement department=driver.findElement(By.xpath("//input[@id='department']"));
        WebElement submit=driver.findElement(By.xpath("//form[@id=\"userForm\"]//button[@id=\"submit\"]"));

        //FillInTheBlank metodu ile veriler girildi
        fillInTheBlank(firstName, "ayse");
        fillInTheBlank(lastName, "yilmaz");
        fillInTheBlank(email, "xxxx@xxx.com");
        fillInTheBlank(age, "25");
        fillInTheBlank(salary, "30000");
        fillInTheBlank(department, "IT");
        clickButton(submit);

        scrollThePage(0,300);

        //Form verilerini güncellemek için edit butonu seçildi
        WebElement editButton=wait.until(ExpectedConditions.
                visibilityOfElementLocated
                        (By.xpath("//div[@class='rt-tr-group'][4]//span[@title='Edit']")));
        clickButton(editButton);

        //Formda Last Name alani güncellendi
        WebElement edittedLastName=wait.until
                (ExpectedConditions.visibilityOfElementLocated(By.xpath
                        ("//form[@id=\"userForm\"]//div[@class=\"col-md-6 col-sm-6\"]//input[@id=\"lastName\"]")));
        edittedLastName.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
        fillInTheBlank(edittedLastName, "Kaya");

        //Form alani güncellenip submit edildi
        WebElement edittedSubmit=wait.until
                (ExpectedConditions.visibilityOfElementLocated(By.xpath
                        ("//form[@id=\"userForm\"]//button[@id=\"submit\"]")));
        clickButton(edittedSubmit);

        //Assertion ile guncel Last Name alaninin dogrulamasi yapildi
        WebElement waitNewLastName=wait.until(ExpectedConditions.
                visibilityOfElementLocated(By.xpath
                        ("//div[@class=\"web-tables-wrapper\"]//div[@class=\"rt-tr-group\"][4]//div[@class=\"rt-td\"][2]")));
        Assert.assertEquals(waitNewLastName.getText(), "Kaya");
    }

    @AfterTest
    public void tearDown(){
        if (driver!=null){
            driver.quit();
        }
    }

}
