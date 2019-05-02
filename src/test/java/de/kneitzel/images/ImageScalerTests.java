package de.kneitzel.images;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Tests the ImageScaler Class.
 */
public class ImageScalerTests {
    /**
     * Tests the scaling of images with ImageScaler.createScaledImage
     */
    @Test
    public void TestImageScaling() {
        try {
            InputStream imageStream = ImageScalerTests.class.getClassLoader().getResourceAsStream("images/testbild.jpg");
            Assert.assertNotNull(imageStream);
            int bytes = imageStream.available();
            byte[] imageBytes = new byte[bytes];
            int readBytes = imageStream.read(imageBytes);
            Assert.assertEquals(bytes, readBytes);

            try (FileOutputStream stream1 = new FileOutputStream("./temp/testHigh.png")) {
                stream1.write(ImageScaler.createScaledImage(imageBytes, 1000, 5000));
            }
            try (FileOutputStream stream2 = new FileOutputStream("./temp/testWide.png")) {
                stream2.write(ImageScaler.createScaledImage(imageBytes, 5000, 1000));
            }
        } catch (Exception ex) {
            Assert.fail("Exception thrown");
        }
    }
}
