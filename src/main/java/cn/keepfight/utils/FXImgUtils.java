package cn.keepfight.utils;

import javafx.scene.image.Image;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FXImgUtils {
    public static byte[] base64ToImage(String base64String){
        return Base64.decodeBase64(base64String);
    }

    public static String imgToBase64(File file) throws IOException {
        return Base64.encodeBase64String(IOUtils.toByteArray(new FileInputStream(file)));
    }

    public static void saveTo(String imgStr, File file) throws Exception {
        if (imgStr == null || file == null) {
            return;
        }
//        String[] bases = imgStr.split(",");
//        byte[] imageBytes = FXImgUtils.base64ToImage(bases[1]);
        byte[] imageBytes = FXImgUtils.base64ToImage(imgStr);
        //read image file
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
        ImageIO.write(newBufferedImage, "jpg", file);
    }

    public static Image baseToImage(String imgStr){
        if (imgStr == null) {
            return null;
        }
//        String[] bases = imgStr.split(",");
//        return new Image(new ByteArrayInputStream(FXImgUtils.base64ToImage(bases[1])));
        return new Image(new ByteArrayInputStream(FXImgUtils.base64ToImage(imgStr)));
    }
}
