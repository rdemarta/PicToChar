import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PicToChar {

    public static void main(String[] args) {
        // Application parameters
        final char[] CHARS = {'.', '_', '^', '*', 'O', '8', '#', '$', '@'};
        final boolean NEGATIVE = false;
        final int TARGET_HEIGHT = 60;

        // Magic numbers
        final int CHAR_WIDTH = 3; // Each char will be written CHAR_WIDTH times
        final int MAX_RGB = 255;
        final int SEGMENT = MAX_RGB / (CHARS.length - 1); // Divide MAX_RGB into CHARS array equal segments

        try {
            // Open image
            File input = new File(args[0]);
            BufferedImage image = ImageIO.read(input);

            // Get image's size
            int width = image.getWidth();
            int height = image.getHeight();
            final int INCREMENT = height / TARGET_HEIGHT;

            // Browse all pixels
            for(int y = 0; y < height; y += INCREMENT) {
                for(int x = 0; x < width; x += INCREMENT) {

                    Color pixel = new Color(image.getRGB(x, y));
                    int grayScale = toGrayscale(pixel);
                    int index = grayScale / SEGMENT;
                    if(NEGATIVE) index = CHARS.length - index - 1;

                    char c = CHARS[index];

                    for(int k = 0; k < CHAR_WIDTH; ++k) System.out.print(c); // Draw on console
                }
                System.out.println(); // Line return
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts an RGB color into one grayscaled value.
     * @param color RGB color to convert
     * @return int value [0;255]
     */
    private static int toGrayscale(Color color) {
        return (color.getRed() + color.getGreen() + color.getBlue()) / 3;
    }
}
