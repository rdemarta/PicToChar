import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        final char[] CHARS = {'.', '_', '^', '*', 'O', '8', '#', '$', '@'};
        final int CHAR_WIDTH = 3;
        final int SCALE_X = 10;
        final int SCALE_Y = 10;
        final int MAX_RGB = 255;

        File input = new File(args[0]);
        BufferedImage image = null;
        int width;
        int height;

        try {
            image = ImageIO.read(input);
            width = image.getWidth();
            height = image.getHeight();

            for(int y = 0; y < height; y += SCALE_Y) {
                for(int x = 0; x < width; x += SCALE_X) {

                    Color pixel = new Color(image.getRGB(x, y));
                    int grayScale = toGray(pixel);
                    char c = CHARS[grayScale / (MAX_RGB / (CHARS.length - 1))];

                    for(int k = 0; k < CHAR_WIDTH; ++k) System.out.print(c);
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int toGray(Color color) {
        return (color.getRed() + color.getGreen() + color.getBlue()) / 3;
    }
}
