import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PicToChar {
    // Application parameters
    private final String charPalette;
    private final boolean negative;
    private final int targetHeight;

    private final int segment; // Divide MAX_RGB into CHARS array equal segments

    // Magic numbers
    private static final int CHAR_WIDTH = 3; // Each char will be written CHAR_WIDTH times
    private static final int MAX_RGB = 255;

    public PicToChar(String charPalette, boolean negative, int targetHeight) {
        this.charPalette = charPalette;
        this.negative = negative;
        this.targetHeight = targetHeight;
        segment = MAX_RGB / (charPalette.length() - 1);
    }

    public void process(String file) {
        try {
            // Open image
            File input = new File(file);
            BufferedImage image = ImageIO.read(input);

            // Get image's size
            int width = image.getWidth();
            int height = image.getHeight();
            final int INCREMENT = height / targetHeight; // Pixel browsing step size

            // Browse all pixels
            for(int y = 0; y < height; y += INCREMENT) {
                for(int x = 0; x < width; x += INCREMENT) {

                    Color pixel = new Color(image.getRGB(x, y));
                    int grayScale = toGrayscale(pixel);
                    int index = grayScale / segment;
                    if(negative) index = charPalette.length() - index - 1;

                    char c = charPalette.charAt(index);

                    for(int k = 0; k < CHAR_WIDTH; ++k) System.out.print(c); // Draw on console
                }
                System.out.println(); // Line return
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { //"._^*O8#$@"
        PicToChar picToChar = new PicToChar("$@B%8&WM#*oahkbdpqwmZOQLCJUYXzcvunxrjft", false, 50);
        picToChar.process("suchet2.jpg");
        //" ``.-:/+osyhdmNM"
        //$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\|()1{}[]?-_+~<>i!lI;:,"^`'.
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
