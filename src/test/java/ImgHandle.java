import cn.hutool.core.img.ImgUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author LoMu
 * Date 2024.07.24 17:27
 */
public class ImgHandle {
    public static void main(String[] args) throws IOException {

        BufferedImage bufferedImage = ImgUtil.read("E:\\code\\sc.png");

        System.out.println(bufferedImage.getHeight());
        BufferedImage subimage = bufferedImage.getSubimage(381, 400, 1131, bufferedImage.getHeight() - 930);
        // 保存截图到指定路径
        ImageIO.write(subimage, "PNG", new File("E:\\code\\sc1.png"));
    }
}
