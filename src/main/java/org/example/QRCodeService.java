package org.example;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;

public class QRCodeService {
    private static final Integer WIDTH = 200;
    private static final Integer HEIGHT = 200;

    private static BufferedImage getQrCodeImage(String content, int width, int height, Hashtable hints) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        if (hints == null) {
            hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        }
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage bi = toBufferedImage(matrix);
        return bi;
    }

    public static byte[] getQrCodeImageBytes(String content, int width, int height) throws
            Exception {
        Hashtable hints = new Hashtable();
        int level = 3;
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.forBits(3));

        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BufferedImage zoomImage = getQrCodeImage(content, width, height, hints);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (!ImageIO.write(zoomImage, "png", out)) {
            throw new IOException("Could not write an image of format png");
        }
        byte[] binaryData = out.toByteArray();
        return binaryData;
    }

    public static ByteArrayInputStream getQrCodeAsStream(String content) throws
            Exception {
        return new ByteArrayInputStream(getQrCodeImageBytes(content, WIDTH, HEIGHT));
    }
}