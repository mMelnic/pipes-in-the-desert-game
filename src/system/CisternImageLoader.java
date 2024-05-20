package system;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CisternImageLoader {
    private static BufferedImage cisternWithoutComponentImage;
    private static BufferedImage cisternWithPipeImage;
    private static BufferedImage cisternWithPumpImage;
    private static BufferedImage cisternFillingImage;
    private static BufferedImage cisternFullImage;

    static {
        loadImages();
    }

    private static void loadImages() {
        try {
            cisternWithoutComponentImage = loadImage("/resources/images/cisternView.png");
            cisternWithPipeImage = loadImage("/resources/images/cisternView_manufacturedPipe.png");
            cisternWithPumpImage = loadImage("/resources/images/cisternView_manufacturedPump.png");
            cisternFillingImage = loadImage("/resources/images/cisternView_filling.png");
            cisternFullImage = loadImage("/resources/images/cisternView_full.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(CisternImageLoader.class.getResource(path));
    }

    public static BufferedImage getCisternWithoutComponentImage() {
        return cisternWithoutComponentImage;
    }

    public static BufferedImage getCisternWithPipeImage() {
        return cisternWithPipeImage;
    }

    public static BufferedImage getCisternWithPumpImage() {
        return cisternWithPumpImage;
    }

    public static BufferedImage getCisternFillingImage() {
        return cisternFillingImage;
    }

    public static BufferedImage getCisternFullImage() {
        return cisternFullImage;
    }
}
