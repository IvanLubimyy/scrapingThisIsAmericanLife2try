package com.company;
import org.json.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

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

        /**
         *
         * @param username
         * @param Password
         *
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
            //WebElement link = driver.findElement(By.className("thumbnail goto goto-episode"));


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

         public void playEpisode() throws IOException, InterruptedException {
            //WebElement playEpisod = driver.findElement(By.xpath("//div//main//div//div//div//article//header//div//div//a//span"));
             //WebElement playEpisod = driver.findElement(By.cssSelector("css=script[id='playlist-data']"));
            //WebElement playEpisod = driver.findElement(By.id("playlist-data"));
             //WebElement playEpisod = driver.findElement(By.cssSelector("script#playlist-data"));
            FirstPage episodePade = new FirstPage(driver);
            episodePade.waiter("//*[@id='playlist-data']");
             //List<WebElement> playEpisods = driver.findElements(By.xpath("//script"));
             WebElement playEpisod = driver.findElement(By.xpath("//script[contains(@id,'playlist-data')]"));
             //WebElement playEpisod = driver.findElement(By.tagName("script"));
             //playEpisod.getT
             //             #playlist-data
             //         html.js.svg.pointerevents.opacity.rgba.supports.no-touchevents.fontface.nthchild.objectfit.object-fit.cssanimations.backgroundsize.borderradius.csscolumns.csscolumns-width.no-csscolumns-span.csscolumns-fill.csscolumns-gap.csscolumns-rule.csscolumns-rulecolor.csscolumns-rulestyle.csscolumns-rulewidth.no-csscolumns-breakbefore.no-csscolumns-breakafter.no-csscolumns-breakinside.csstransforms.csstransforms3d.csstransitions.wf-maiola-n5-active.wf-maiola-i4-active.wf-maiola-n4-active.wf-maiola-n7-active.wf-maiola-i7-active.wf-maiola-i5-active.wf-active body.html.not-front.not-logged-in.no-sidebars.loaded.node-type-episode.page-682.page-episode-number-682 div#content main#main div.region.region-content div#block-system-main.block.block-system div.content article.node.node-episode.view-full.clearfix.episode-number-682.with-image.image-landscape.image-shifter.links-processed script#playlist-data
             //*[@id="playlist-data"]
             //for (WebElement playEpisod : playEpisods) {
                // if (playEpisod.getAttribute("type")=="application/json"){
                 System.out.println("Нажал: tag:" + playEpisod.getTagName()+" id:"+playEpisod.getAttribute("id")+" text:"+playEpisod.getAttribute("text")+" title:"+playEpisod.getAttribute("title"));
//Json work
             String str = playEpisod.getAttribute("text");
             JSONObject obj = new JSONObject(str);
             String mp3File = obj.getString("audio");
             System.out.println(mp3File);
             //download mp3


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
            //webSrcapper.login("admin", "12345");
            //webSrcapper.getText();
            //
            //webSrcapper.closeCookieWindow();
            webSrcapper.findThumbnail();
            //webSrcapper.closeCookieWindow();
            webSrcapper.playEpisode();
            //webSrcapper.closeBrowser();
        }
}
