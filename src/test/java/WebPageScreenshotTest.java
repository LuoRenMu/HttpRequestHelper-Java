import cn.luorenmu.web.WebPageScreenshot;

public class WebPageScreenshotTest {
    public static void main(String[] args) {
        WebPageScreenshot webPageScreenshot = new WebPageScreenshot("https://www.bilibili.com/", "E:\\code\\1.png");
        webPageScreenshot.screenshotAll(2000);
    }
}