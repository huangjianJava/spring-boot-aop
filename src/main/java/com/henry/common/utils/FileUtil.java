package com.henry.common.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FileUtil {
    public static void saveToFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static ByteArrayOutputStream getByteArrayOutputStream(InputStream inStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            try {
                if (inStream != null) {
                    while ((len = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {

        }
        return outStream;
    }

    public static ByteArrayOutputStream getByteArrayOutputStreamByFileName(String fileName) {
        if (isExistFile(fileName)) {
            File file = new File(fileName);
            InputStream inputObj = null;
            try {
                try {
                    inputObj = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    return null;
                }
                return getByteArrayOutputStream(inputObj);
            } finally {
                if (inputObj != null) {
                    try {
                        inputObj.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return null;
        }
    }

    public static void saveToFile(ByteArrayOutputStream inStream, String filePath,
            String fileName) {
        saveToFile(inStream.toByteArray(), filePath, fileName);
    }

    public static void saveToFile(InputStream inStream, String filePath, String fileName) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            try {
                if (inStream != null) {
                    while ((len = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            saveToFile(outStream.toByteArray(), filePath, fileName);

        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static boolean isExistFile(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 
     * @param 判断文件目录是否存在
     * @return
     */
    public static boolean isExistDir(String dirPath) {
        File file = new File(dirPath);
        // 如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
           return false;
        } else {
            return true;
        }
    }

    /**
     * 获取文件后缀名
     * 
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        if (fileName.lastIndexOf(".") >= 0) {
            fileName = fileName.substring(fileName.lastIndexOf("."));
        } else {
            fileName = null;
        }
        return fileName;
    }

    /**
     * 判断文件是否是图片*
     * 
     * @param imgFile 文件对象
     * @return
     */
    public static boolean isImage(File imgFile) {
        try {
            BufferedImage image = ImageIO.read(imgFile);
            return image != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断文件是否是视频*
     * 
     * @param videoFile 文件对象
     * @return
     */
    public static boolean isVideo(File videoFile) {
        try {

            FileType type = getType(videoFile);

            return type == FileType.AVI || type == FileType.RAM || type == FileType.RM
                    || type == FileType.MOV || type == FileType.ASF || type == FileType.MPG;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断文件类型
     *
     * @param file 文件
     * @return 文件类型
     */
    public static FileType getType(File file) throws IOException {

        String fileHead = getFileContent(file);

        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }

        fileHead = fileHead.toUpperCase();

        FileType[] fileTypes = FileType.values();

        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }

        return null;
    }

    /**
     * 得到文件头
     *
     * @param file 文件
     * @return 文件头
     * @throws IOException
     */
    private static String getFileContent(File file) throws IOException {

        byte[] b = new byte[28];

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            inputStream.read(b, 0, 28);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }
    
    public static String getFileString(File file, String encoding) throws IOException {
    	  Long filelength = file.length();  
          byte[] filecontent = new byte[filelength.intValue()];  
          try {  
              FileInputStream in = new FileInputStream(file);  
              in.read(filecontent);  
              in.close();  
          } catch (FileNotFoundException e) {  
              e.printStackTrace();  
          } 
          try {  
              return new String(filecontent, encoding);  
          } catch (UnsupportedEncodingException e) {  
              System.err.println("The OS does not support " + encoding);  
              e.printStackTrace();  
              return null;  
          }  
    }

    /**
     * 将文件头转换成16进制字符串
     *
     * @param src 原生byte
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
