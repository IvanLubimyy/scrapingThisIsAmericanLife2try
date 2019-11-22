package com.company;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import static org.apache.commons.io.FilenameUtils.getBaseName;

//import org.junit.*;

/*
Main class where we can
1) go to targetPage
2) find, wait and collect linksOfEpisodes
3) download with workingStreams number of treads , wait and checkExisting mp3 files
 */
public class Main {
        public List<String> linksOfEpisodes= new ArrayList<String>(); //list of episodes we will download
        public WebDriver driver = new FirefoxDriver();
        public String targetPage = "https://www.thisamericanlife.org/archive?year=2019";
        public int workingStreams = 2;
        public String localPlace = "c:\\work\\1";

        private final Wait<WebDriver> wait = new WebDriverWait(driver, 20, 1000);
    /**
         * Open the test website.
         */
        public void openTestSite() { //view-source:
            driver.navigate().to(targetPage);
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
     * return true if this list not null
     */
    public boolean findAllThumbnails() throws IOException, InterruptedException {
        //FirstPage episodePage = new FirstPage(driver);
        waiter("//*[@class='thumbnail goto goto-episode']");

        List<WebElement> links = driver.findElements(By.cssSelector("a.thumbnail.goto.goto-episode"));
        // remember all links before we will update page
        if (links.isEmpty()){
            System.out.println("Empty list");
            return false;
        }

        for (WebElement link : links) {
           linksOfEpisodes.add(link.getAttribute("href").toString());
        }
        return true;
    }

    // download audio from all pages
    public void downloadList() throws IOException, InterruptedException {

        RunTasksList downloadsTasks = new RunTasksList();

        for (String Episode :linksOfEpisodes ) {
            driver.navigate().to(new URL(Episode));

            Thread taskToDownload = playEpisode();

            if(taskToDownload ==null){
                System.out.println("Episode "+Episode+" does not have audio content");
            }else{

                //counter of treads
                downloadsTasks.add(taskToDownload);

                //check limit of the number of sessions to downloading
                if(downloadsTasks.size() < workingStreams){
                    continue;    // add next in the loop
                }
                else { //wait 3 minutes
                    downloadsTasks.get(0).join(180000); // wait when first task ends
                }
            }
        }
    }

        /*
        Find and play episode
        */
         public Thread playEpisode() throws IOException, InterruptedException {

             waiter("//*[@id='playlist-data']");

             WebElement playEpisode = driver.findElement(By.xpath("//script[contains(@id,'playlist-data')]"));
             if (playEpisode==null){
                 return null;
             }
             System.out.println("Нажал: tag:" + playEpisode.getTagName()+" id:"+playEpisode.getAttribute("id")+" text:"+playEpisode.getAttribute("text")+" title:"+playEpisode.getAttribute("title"));
             //Json work
             String str = playEpisode.getAttribute("text");
             JSONObject obj = new JSONObject(str);
             String mp3File = obj.getString("audio");
             //System.out.println("Start downloading:"+mp3File);
             //download mp3

             Thread audioFile = new Thread(new DownLoadURL(mp3File, localPlace));
             try {
                 audioFile.sleep(3000);
                 audioFile.start();
                 audioFile.setName(mp3File);
                 //System.out.println("Download " + mp3File + " starts");

                 //audioFile.sleep(180000); // wait 3 minutes to start new download tread
             } catch(InterruptedException e) {
                 e.printStackTrace();
                 System.out.println("We have a problem with start downloading the file "+ mp3File);
                 return null;
             }
            return audioFile;
    }

    //  This method checks  linksOfEpisodes and removes exist (into target directory) files from it
        public void removeExistFiles(){
             LinkedHashSet<String> deleteOfEpisodes= new LinkedHashSet<>();

             //get list of mp3 files in target directory (localPlace)
             File dir = new File(localPlace);
             File[] files = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".mp3");
                }
            });
             //check and remove exist files
            for (File fileInList: files ) {
                String nameMp3File = getBaseName(fileInList.getName());

                for (String episode :linksOfEpisodes) {
                    if(episode.contains(nameMp3File)){
                        deleteOfEpisodes.add(episode);
                        System.out.println(episode+" have "+nameMp3File);
                    }
                }
            }
            Iterator<String> iterator = deleteOfEpisodes.iterator();

            while (iterator.hasNext()) {
                linksOfEpisodes.remove(iterator.next());
            }

        }
//todo add junit
//todo refactoring (think about architecture and UI)
//todo add Singleton for logging
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

        public void waiter(String SomeLocatorByXpath){
             WebDriverWait waitForOne = new WebDriverWait(driver, 10);
             waitForOne.until(ExpectedConditions.presenceOfElementLocated(By.xpath(SomeLocatorByXpath)));
          }

        public void closeBrowser() {
            driver.close();driver.close();
        }

        public static void main(String[] args) throws IOException, InterruptedException {
            Main webSrcapper = new Main();
            webSrcapper.openTestSite();
            // if we have not episodes then quit
            if (!webSrcapper.findAllThumbnails()) { return; }
            webSrcapper.removeExistFiles();
            webSrcapper.downloadList();
            webSrcapper.closeBrowser();

        }
}
