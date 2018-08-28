
package com.henry.common.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;

public class QRCodeUtil {
    private static final String CHARSET = "utf-8";
    public static final String FORMAT_NAME = "JPG";
    private static final int defaultSize = 640;
    private static final int defaultLogonSize = 64;
    
    private static Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);

    /**
     * 生成二维码的方法
     * 
     * @param content 目标URL
     * @param imgPath LOGO图片地址
     * @param needCompress 是否压缩LOGO
     * @return 二维码图片
     * @throws Exception
     */
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress)
            throws Exception {
        
        Image src = null;
        if (imgPath != null) {
            File file = new File(imgPath);
            if (file.exists()) {
                src = ImageIO.read(new File(imgPath));
            } else {
                logger.error("" + imgPath + "   该文件不存在！");
            }
        }

        return createImage(content, src, needCompress, defaultSize, defaultSize, defaultLogonSize,
                defaultLogonSize);
    }

    private static BufferedImage createImage(String content, Image src, boolean needCompress,
            int iW, int iH, int logonWidth, int logonHeight) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix =
                new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, iW, iH, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (src == null) {
            return image;
        }
        // 插入图片
        QRCodeUtil.insertImage(image, src, needCompress, iW, iH, logonWidth, logonHeight);
        return image;
    }


    /**
     * 插入LOGO
     * 
     * @param source 二维码图片
     * @param imgPath LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, Image src, boolean needCompress, int iW,
            int iH, int logonWidth, int logonHeight) throws Exception {
        if (src == null) {
            return;
        }
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > logonWidth) {
                width = logonWidth;
            }
            if (height > logonHeight) {
                height = logonHeight;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (iW - width) / 2;
        int y = (iH - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 15, 15);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成二维码(内嵌LOGO)
     * 
     * @param content 内容
     * @param imgPath LOGO地址
     * @param destPath 存放目录
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999) + ".jpg";
        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
    }

    /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
     * 
     * @param destPath 存放目录
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 生成二维码(内嵌LOGO)
     * 
     * @param content 内容
     * @param imgPath LOGO地址
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath) throws Exception {
        QRCodeUtil.encode(content, imgPath, destPath, false);
    }

    /**
     * 生成二维码
     * 
     * @param content 内容
     * @param destPath 存储地址
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String destPath, boolean needCompress)
            throws Exception {
        QRCodeUtil.encode(content, null, destPath, needCompress);
    }

    /**
     * 生成二维码
     * 
     * @param content 内容
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String destPath) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false);
    }

    /**
     * 生成二维码(内嵌LOGO)
     * 
     * @param content 内容
     * @param imgPath LOGO地址
     * @param output 输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */

    public static int getActualSize(Integer size) {
        if ((size == null) || (size <= 0)) {
            size = defaultSize;
        }
        if (size < 16) {
            size = 16;
        }
        if (size > 3000) {
            size = 3000;
        }
        return size;
    }

    public static void encode(String content, Image src, OutputStream output, boolean needCompress,
            Integer size) throws Exception {
        size = getActualSize(size);
        BufferedImage image = QRCodeUtil.createImage(content, src, needCompress, size, size,
                defaultLogonSize, defaultLogonSize);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码
     * 
     * @param content 内容
     * @param output 输出流
     * @throws Exception
     */
    public static void encode(String content, OutputStream output, Integer size) throws Exception {
        QRCodeUtil.encode(content, null, output, false, size);
    }

    /**
     * 解析二维码
     * 
     * @param file 二维码图片
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 解析二维码
     * 
     * @param path 二维码图片地址
     * @return 不是二维码的内容返回null,是二维码直接返回识别的结果
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }

    /**
     * 根据二维码的大小获取logon的大小.
     */
    public static int getLogoSize(Integer size) {
        size = getActualSize(size);
        int i = size / 10;
        if (i <= 0) {
            i = 1;
        }
        return i;
    }
}
