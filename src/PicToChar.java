import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PicToChar {
    // Application parameters
    private final String charset; // From non-filled (space) chars to filled ones (e.g. " ._^*O8#@$")
    private final boolean revert;
    private final int targetHeight; // TODO percentage

    private final double segment; // Divide MAX_RGB into CHARS array equal segments

    // Magic numbers
    private static int CHAR_WIDTH = 3; // Each char will be written CHAR_WIDTH times
    private static final int MAX_RGB = 255;
    private static final String[] CHARSET_TYPES = {
            " .,-+*s0@$",
            " .'`^\",:;Il!i><~+_-?][}{1)(|\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$",
            " ░▒▓█",
            " .-+*"
    };

    public enum PALETTE {
        MINIMAL,
        EXTENDED,
        SQUARES,
    }

    public PicToChar(PALETTE charset, boolean revert, int targetHeight) {
        this.revert = revert;
        this.targetHeight = targetHeight;

        switch (charset) {
            case EXTENDED -> this.charset = CHARSET_TYPES[1];
            case SQUARES -> this.charset = CHARSET_TYPES[2];
            default -> this.charset = CHARSET_TYPES[0];
        }

        segment = (double)MAX_RGB / this.charset.length();
    }

    public PicToChar() {
        this(PALETTE.MINIMAL, false, 65);
    }

    public void process(String file) {
        try {
            // Open image
            File input = new File(file);
            BufferedImage image = ImageIO.read(input);

            // Get image's size
            int width = image.getWidth();
            int height = image.getHeight();
            final int increment = height / Math.min(targetHeight, height); // Pixel browsing step size

            // Browse all pixels
            for(int y = 0; y < height; y += increment) {
                for(int x = 0; x < width; x += increment) {

                    Color pixel = new Color(image.getRGB(x, y));
                    int grayScale = toGrayscale(pixel);

                    int index = (int)Math.round(grayScale / segment) - 1;
                    if(index < 0) index = 0; // Avoid negative values
                    if(revert) index = charset.length() - index - 1; // Reverse index

                    char c = charset.charAt(index);

                    for(int k = 0; k < CHAR_WIDTH; ++k) System.out.print(c); // Draw on console
                }
                System.out.println(); // Line return
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        PicToChar picToChar = new PicToChar(PALETTE.SQUARES, false, 200);
        picToChar.process("isaac.jpeg");
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
