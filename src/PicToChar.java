import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PicToChar {
    // Application parameters
    private final String charPalette; // From non-filled (space) chars to filled ones (e.g. " ._^*O8#@$")
    private final boolean revert;
    private final int targetHeight;

    private final double segment; // Divide MAX_RGB into CHARS array equal segments

    // Magic numbers
    private static final int CHAR_WIDTH = 3; // Each char will be written CHAR_WIDTH times
    private static final int MAX_RGB = 255;

    public PicToChar(String charPalette, boolean revert, int targetHeight) {
        this.charPalette = charPalette;
        this.revert = revert;
        this.targetHeight = targetHeight;
        segment = (double)MAX_RGB / charPalette.length();
    }

    public void process(String file) {
        try {
            // Open image
            File input = new File(file);
            BufferedImage image = ImageIO.read(input);

            // Get image's size
            int width = image.getWidth();
            int height = image.getHeight();
            final int increment = height / targetHeight; // Pixel browsing step size

            // Browse all pixels
            for(int y = 0; y < height; y += increment) {
                for(int x = 0; x < width; x += increment) {

                    Color pixel = new Color(image.getRGB(x, y));
                    int grayScale = toGrayscale(pixel);

                    int index = (int)Math.round(grayScale / segment) - 1;
                    if(index < 0) index = 0; // Avoid negative values
                    if(revert) index = charPalette.length() - index - 1; // Reverse index

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
        PicToChar picToChar = new PicToChar(" .-+*s8#@$", false, 60);
        picToChar.process("party.jpg");

        // .-+*s8#@$
        // .:-=+*#%@
        // `.-:/+osyhdmNM
        // ░▒▓█
        // .'`^",:;Il!i><~+_-?][}{1)(|\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$
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
