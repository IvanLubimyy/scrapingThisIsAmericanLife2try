package com.company;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

import java.io.*;

//import org.junit.*;


public class Main {
        public WebDriver driver = new FirefoxDriver();

        private final Wait<WebDriver> wait = new WebDriverWait(driver, 20, 1000);
    /**
         * Open the test website.
         */
        public void openTestSite() { //view-source:
            driver.navigate().to("https://www.thisamericanlife.org/archive?year=2018");
        }

        /*
         * @param username
         * @param Password
         *            Logins into the website, by entering provided username and
         *            password
         */
        public void login(String username, String Password) {

            WebElement userName_editbox = driver.findElement(By.id("usr"));
            WebElement password_editbox = driver.findElement(By.id("pwd"));
            WebElement submit_button = driver.findElement(By.xpath("//input[@value='Login']"));

            userName_editbox.sendKeys(username);
            password_editbox.sendKeys(Password);
            submit_button.click();

        }

        public void closeCookieWindow() throws IOException {
            try {
            WebElement cookie = driver.findElement(By.className("cc-compliance"));
            cookie.click();}
            catch(Exception e){
               System.out.println("Куки не закрыл:"+ e);
            }
        }
        /*
        * find "thumbnail goto goto-episode" class on the page
        */
        public void findThumbnail() throws IOException {

            WebElement link = driver.findElement(By.cssSelector("a.thumbnail.goto.goto-episode"));
            closeCookieWindow();
            //WebElement cookie = driver.findElement(By.className("cc-compliance"));
            //cookie.click();
    //        WebElement element= driver.findElement(By."Your Locator"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", link);

            System.out.println("Нашел: "+link);
            //link.click();
    }
    /*
     * find all "thumbnail goto goto-episode" classes on the page
     */
    public void findAllThumbnails() throws IOException, InterruptedException {
        List<WebElement> links = driver.findElements(By.cssSelector("a.thumbnail.goto.goto-episode"));
        for (WebElement link : links) {
            // does this work? What about path?
            driver.navigate().to("https://www.thisamericanlife.org"+link.getAttribute("href")); //.to("https://www.thisamericanlife.org/archive?year=2018");
            playEpisode();
            //JavascriptExecutor executor = (JavascriptExecutor) driver;
            //executor.executeScript("arguments[0].click();", link);
        }
    }

        /*
        Find and play episode
        */
         public void playEpisode() throws IOException, InterruptedException {
            FirstPage episodePage = new FirstPage(driver);
            episodePage.waiter("//*[@id='playlist-data']");
             //List<WebElement> playEpisodes = driver.findElements(By.xpath("//script"));
             WebElement playEpisode = driver.findElement(By.xpath("//script[contains(@id,'playlist-data')]"));
             System.out.println("Нажал: tag:" + playEpisode.getTagName()+" id:"+playEpisode.getAttribute("id")+" text:"+playEpisode.getAttribute("text")+" title:"+playEpisode.getAttribute("title"));
//Json work
             String str = playEpisode.getAttribute("text");
             JSONObject obj = new JSONObject(str);
             String mp3File = obj.getString("audio");
             System.out.println("Start downloading:"+mp3File);
             //download mp3

             Thread audioFile = new Thread(new DownLoadURL(mp3File, "c:\\work\\1"));
             try {
                 audioFile.sleep(3000);
                 audioFile.start();
                 System.out.println("Download " + mp3File + " ends. Congrats!");
                 audioFile.sleep(180000); // wait 3 minutes to start new download tread
             } catch(InterruptedException e) {
                 e.printStackTrace();
             }
                 //}
             //}
            //playEpisod.click();
    }
        /**
         * grabs the status text and saves that into status.txt file
         *
         * @throws IOException
         */
        public void getText() throws IOException {
            String text = driver.findElement(By.xpath("//div[@id='case_login']/h3")).getText();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("status.txt"), "utf-8"));
            writer.write(text);
            writer.close();

        }

        /**
         * Saves the screenshot
         *
         * @throws IOException
         */
        public void saveScreenshot() throws IOException {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("screenshot.png"));
        }

        public void closeBrowser() {
            driver.close();
        }

        public static void main(String[] args) throws IOException, InterruptedException {
            Main webSrcapper = new Main();
            webSrcapper.openTestSite();
            webSrcapper.findAllThumbnails();

            //webSrcapper.findThumbnail();

            //webSrcapper.playEpisode();
            //webSrcapper.closeBrowser();
        }
}
