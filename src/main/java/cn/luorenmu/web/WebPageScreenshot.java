package cn.luorenmu.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.function.Function;

/**
 * @author LoMu
 * Date 2024.07.24 17:43
 */
@Slf4j
@Setter
public class WebPageScreenshot {
    private static final ChromeOptions options = new ChromeOptions();

    static {
        // 设置 WebDriverManager 以管理驱动程序
        WebDriverManager.chromedriver().setup();

        // 配置 Chrome 浏览器选项
        options.addArguments("--start-maximized");
    }

    private String httpUrl;
    private int waitPage = 3;
    private String outPath;

    public WebPageScreenshot(String httpUrl, String outPath) {
        this.httpUrl = httpUrl;
        this.outPath = outPath;
    }


    private WebDriver stratWebPageDriver() {
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(waitPage));
        driver.get(httpUrl);
        return driver;
    }

    public void screenshotAll(int scrollTimeout) {
        screenshot(t -> {
            Screenshot screenshot = new AShot()
                    .coordsProvider(new WebDriverCoordsProvider())
                    .shootingStrategy(ShootingStrategies.viewportPasting(scrollTimeout))
                    .takeScreenshot(t);

            return screenshot.getImage();
        });
    }

    private BufferedImage imageCropLimit(int x, int y, int h, int w, BufferedImage image) {
        if (w < 0) {
            w = image.getWidth() + w;
        }
        if (h < 0) {
            h = image.getHeight() + h;
        }
        return image.getSubimage(x, y, w, h);
    }

    public void screenshotCrop(int x, int y, int w, int h) {
        screenshot(t -> {
            File screenshotAs = ((TakesScreenshot) t).getScreenshotAs(OutputType.FILE);
            try {
                BufferedImage image = ImageIO.read(screenshotAs);
                return imageCropLimit(x, y, w, h, image);
            } catch (IOException e) {
                log.error("image io read error", e);
                throw new RuntimeException(e);
            }
        });
    }

    private void screenshot(Function<WebDriver, BufferedImage> driverBufferedImageFunction) {
        WebDriver driver = stratWebPageDriver();
        try {
            BufferedImage bufferedImage = driverBufferedImageFunction.apply(driver);
            ImageIO.write(bufferedImage, "PNG", new File(outPath));
        } catch (IOException e) {
            log.error("image io write error", e);
        } finally {
            driver.quit();
        }
    }

    public void screenshotAllCrop(int x, int y, int w, int h, int scrollTimeout) {
        screenshot(t -> {
            Screenshot screenshot = new AShot()
                    .coordsProvider(new WebDriverCoordsProvider())
                    .shootingStrategy(ShootingStrategies.viewportPasting(scrollTimeout))
                    .takeScreenshot(t);
            BufferedImage image = screenshot.getImage();
            return imageCropLimit(x, y, w, h, image);
        });
    }

    public void screenshot() {
        screenshot(t -> {
            File screenshotAs = ((TakesScreenshot) t).getScreenshotAs(OutputType.FILE);
            try {
                return ImageIO.read(screenshotAs);
            } catch (IOException e) {
                log.error("image io read error", e);
                throw new RuntimeException(e);
            }
        });
    }
}
