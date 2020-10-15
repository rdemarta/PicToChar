import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PicToChar {
    // Application parameters
    private final String charPalette;
    private final boolean NEGATIVE;
    private final int TARGET_HEIGHT;

    private final int SEGMENT; // Divide MAX_RGB into CHARS array equal segments

    // Magic numbers
    private static final int CHAR_WIDTH = 3; // Each char will be written CHAR_WIDTH times
    private static final int MAX_RGB = 255;

    public PicToChar(String charPalette, boolean NEGATIVE, int TARGET_HEIGHT) {
        this.charPalette = charPalette;
        this.NEGATIVE = NEGATIVE;
        this.TARGET_HEIGHT = TARGET_HEIGHT;
        SEGMENT = MAX_RGB / (charPalette.length() - 1);
    }

    public void process(String file) {
        try {
            // Open image
            File input = new File(file);
            BufferedImage image = ImageIO.read(input);

            // Get image's size
            int width = image.getWidth();
            int height = image.getHeight();
            final int INCREMENT = height / TARGET_HEIGHT; // Pixel browsing step size

            // Browse all pixels
            for(int y = 0; y < height; y += INCREMENT) {
                for(int x = 0; x < width; x += INCREMENT) {

                    Color pixel = new Color(image.getRGB(x, y));
                    int grayScale = toGrayscale(pixel);
                    int index = grayScale / SEGMENT;
                    if(NEGATIVE) index = charPalette.length() - index - 1;

                    char c = charPalette.charAt(index);

                    for(int k = 0; k < CHAR_WIDTH; ++k) System.out.print(c); // Draw on console
                }
                System.out.println(); // Line return
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PicToChar picToChar = new PicToChar("._^*O8#$@", false, 50);
        picToChar.process("");
    }

    /**
     * Converts an RGB color into one grayscaled value.
     * @param color RGB color to convert
     * @return int value [0;255]
     */
    private int toGrayscale(Color color) {
        return (color.getRed() + color.getGreen() + color.getBlue()) / 3;
    }
}
