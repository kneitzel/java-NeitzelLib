package de.kneitzel.images;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Helper class to scale images
 */
public class ImageScaler {

    public static byte[] createScaledImage(byte[] originalImageBytes, int width, int height) {

        try {
            // Create the image from a byte array.
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));

            // Get values required to scale the Image:
            double scaleWidth = (double) width / (double) originalImage.getWidth();
            double scaleHeight = (double) height / (double) originalImage.getHeight();

            double scaleFactor;
            if (scaleWidth > scaleHeight) {
                scaleFactor = scaleHeight;
            } else {
                scaleFactor = scaleWidth;
            }
            int newHeight = (int) (scaleFactor * originalImage.getHeight());
            int newWidth = (int) (scaleFactor * originalImage.getWidth());
            int x = (width - newWidth) / 2;
            int y = (height - newHeight) / 2;

            // Scale the image
            BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = scaledImage.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(originalImage, x, y, newWidth, newHeight, null);

            // Get the bytes of the image
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                ImageIO.write(scaledImage, "png", stream);
                return stream.toByteArray();
            }
        } catch (Exception ex) {
            return null;
        }
    }
}
