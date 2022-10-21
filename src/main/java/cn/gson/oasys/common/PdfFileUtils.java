package cn.gson.oasys.common;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author qiaoli
 * @date 2022/10/19
 */
public class PdfFileUtils {

    /**
     * 系统默认的图片前缀
     */
    private static String TARGET_FILE_PREFIX = "default_image_";
    /**
     * 拆分后的pdf归总地址
     */
    private final static String TARGET_PDF_DIRECTORY = "\\PDFPage\\";
    /**
     * 拆分后每个单页的pdf文件前缀
     */
    private final static String PDF_FILE_PREFIX = "_page_";
    /**
     * 系统自定义分割路径
     */
    private final static String DEFAULT_SPLIT_DIRECTORY = "\\systemCreate\\";
    /**
     * 用户自定义剪裁的文件目录
     */
    private final static String USER_CUSTOM_CLIPPING = "\\userCreate\\";


    /**
     * 对pdf文件进行单页拆分
     * 并在当前目录下新建
     * PDFPage/{@link File}.getName()（已自动去除后缀）目录
     * 用于存储分页后的pdf
     * <hr/>
     * 例如路径：pdf文件目录/PDFPage/文件名/所有单页pdf
     * <hr/>
     *
     * @param pdfFile pdf文件
     * @return {@link String} 成功拆分后文件的的目录
     */
    public static String doSplitPdfFile(File pdfFile) {
        try {
            return splitPdfFile(pdfFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 做图像分割
     * 对图片进行4等分拆分
     * 存储在与参数image同目录下
     * 并且使用默认的文件前缀
     * <hr/>
     * 例如路径：image图片目录/systemCreate/default_image_0...3.jpg
     * * <hr/>
     *
     * @param image image文件
     * @param type  图片类型
     * @return {@link List}<{@link File}> 拆分后的所有子图片集合
     */
    public static List<File> doImagesSplit(File image,Type type) {
        try {
            return imagesSplit(image,type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设定前缀后
     * 对图片进行4等分拆分
     * 存储在与参数image同目录下
     * <hr/>
     * 例如路径：image图片目录/systemCreate/自定义设置的前缀_0...3.jpg
     * <hr/>
     *
     * @param image      image文件地址
     * @param filePrefix 拆分后文件的前缀
     * @param type       图片类型
     * @return {@link List}<{@link File}> 拆分后的所有子图片集合
     */
    public static List<File> doImagesSplit(File image, String filePrefix,Type type) {
        TARGET_FILE_PREFIX = filePrefix + "_";
        try {
            return imagesSplit(image,type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * pdf转化为jpg（会报EOFException，正常，测试显示为绿色，不会阻碍程序的正常执行，）
     * <hr/>
     * 例如路径：目标路径/文件名字/文件名.jpg
     * <hr/>
     *
     * @param file   拆分后的单页文件
     * @param target 目标(目录)地址
     * @param type   图片类型
     * @return {@link File} 最后转化的图片的文件对象
     */
    public static File doPdfToJpg(File file, File target,Type type) {
        try {
            return pdfToJpg(file, target,type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户自定义分割图像
     *
     * @param image  图像
     * @param startX 开始x
     * @param startY 开始y
     * @param endX   结束x
     * @param endY   结束y
     * @param type   类型
     * @return {@link File} 用户自定义图片的文件对象
     */
    public static File doUserSplitImages(File image, int startX, int startY, int endX, int endY,Type type) {
        try {
            return userSplitImages(image, startX, startY, endX, endY,type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 得到pdf页数
     *
     * @param file 文件
     * @return int
     */
    public static int getPdfPage(File file){
        try {
            return getPage(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static String splitPdfFile(File pdfFile) throws IOException {
        //判断用户文件是否为空
        if (!pdfFile.exists()) {
            throw new RuntimeException("调用文件为空");
        }
        if (isWantFileType(pdfFile, Type.PDF.getValue())) {
            throw new RuntimeException("文件类型错误，请传输pdf文件");
        }
        //设置用户文件的分页目录
        String pagePath = pdfFile.getParent() + TARGET_PDF_DIRECTORY +
                getFileName(pdfFile) + "\\";
        File pageDirectory = new File(pagePath);
        //创建分页目录
        if (!pageDirectory.exists()) {
            if (!pageDirectory.mkdirs()) {
                throw new RuntimeException("目录创建失败");
            }
        }
        PDDocument doc = null;
        //拆分PDF文档的页面
        List<PDDocument> pages = null;
        try {
            //加载PDF文件
            doc = PDDocument.load(pdfFile);
            if (Objects.isNull(doc)) {
                throw new RuntimeException("加载PDF文件失败");
            }
            //将文件流拆分成一个单页的文件流集合
            pages = new Splitter().split(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < Objects.requireNonNull(pages).size(); i++) {
            //获取单页实例并进行IO持久化
            pages.get(i)
                    .save(pagePath + getFileName(pdfFile)
                            + PDF_FILE_PREFIX + (i+1) + Type.PDF.getValue());
        }
        doc.close();
        return pagePath;
    }


    private static List<File> imagesSplit(File image,Type type) throws IOException {
        //判断传过来的文件是否存在
        if (!image.exists()) {
            throw new RuntimeException("没有此文件");
        }
        File file = new File(image.getParent()+DEFAULT_SPLIT_DIRECTORY);
        if (!file.exists()){
            if (!file.mkdirs()) {
                throw new RuntimeException("目录创建失败");
            }
        }
        if (isWantFileType(image, Type.JPG.getValue(),Type.PNG.getValue())) {
            throw new RuntimeException("文件类型错误，请传输图片");
        }

        // 读取图片文件流
        FileInputStream imageFileStream = new FileInputStream(image);
        // 转为图片对象
        BufferedImage imageObject = ImageIO.read(imageFileStream);

        // 分割成2*2(4)个小图
        int rows = 2;
        int cols = 2;
        int chunks = rows * cols;

        // 计算每个小图的宽度和高度
        int chunkWidth = imageObject.getWidth() / cols;
        int chunkHeight = imageObject.getHeight() / rows;

        // 获取大图中的一部分
        int count = 0;

        BufferedImage[] imgs = new BufferedImage[chunks];

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                // 设置第count小图的大小和类型
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, imageObject.getType());
                // 创建图样对象
                Graphics2D gr = imgs[count++].createGraphics();
                // 进行图片的绘画
                gr.drawImage(imageObject,
                        0, 0,
                        chunkWidth, chunkHeight,
                        chunkWidth * y, chunkHeight * x,
                        chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight,
                        null);

                /*
                    参数：
                    Dx1 -目标矩形第一个角的x坐标。Dy1 -目标矩形第一个角的y坐标。
                    Dx2 -目标矩形第二个角的x坐标。Dy2 -目标矩形第二个角的y坐标。
                    Sx1 -源矩形第一个角的x坐标。Sy1 -源矩形第一个角的y坐标。
                    Sx2 -源矩形第二个角的x坐标。Sy2 -源矩形第二个角的y坐标。

                    测试图片为 1440 x 900
                    源矩阵第一个角的坐标0+0源矩阵第二个角的坐标720+450
                    源矩阵第一个角的坐标720+0源矩阵第二个角的坐标1440+450
                    源矩阵第一个角的坐标0+450源矩阵第二个角的坐标720+900
                    源矩阵第一个角的坐标720+450源矩阵第二个角的坐标1440+900
                 */

                // 释放资源
                gr.dispose();
            }
        }
        List<File> targetPathList = new ArrayList<>(imgs.length);
        // 输出小图
        for (int i = 0; i < imgs.length; i++) {
            String targetPath = image.getParent()+ DEFAULT_SPLIT_DIRECTORY + TARGET_FILE_PREFIX + i + type.getValue();
            targetPathList.add(new File(targetPath));
            ImageIO.write(imgs[i], type.value, new File(targetPath));
        }
        return targetPathList;

    }

    private static File pdfToJpg(File file, File target,Type type) throws IOException {
        //判断用户文件是否为空
        if (!file.exists()) {
            throw new RuntimeException("调用文件为空");
        }
        if (isWantFileType(file, Type.PDF.getValue())) {
            throw new RuntimeException("文件类型错误，请传输pdf文件");
        }
        target= new File(target.getPath()+"\\"+getFileName(file));
        // 判断目标文件是否为空
        if (!target.exists()) {
            if (!target.mkdirs()) {
                throw new RuntimeException("目录创建失败");
            }
        }
        //读取pdf
        PDDocument doc = PDDocument.load(file);
        //实例化PDFRenderer渲染器
        PDFRenderer renderer = new PDFRenderer(doc);
        // Windows 的默认 DPI
        BufferedImage image = renderer.renderImageWithDPI(0, 96);
        // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
        File imageFile = new File(target.getPath() + "//" + getFileName(file) +type.getValue());
        ImageIO.write(image, type.value, imageFile);
        doc.close();
        return imageFile;

    }


    private static File userSplitImages(File image, int startX, int startY, int endX, int endY,Type type) throws IOException {
        //判断传过来的文件是否存在
        if (!image.exists()) {
            throw new RuntimeException("没有此文件");
        }
        if (isWantFileType(image, Type.JPG.getValue(),Type.PNG.getValue())) {
            throw new RuntimeException("文件类型错误，请传输图片");
        }
        File file = new File(image.getParent() + USER_CUSTOM_CLIPPING);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException("目录创建失败");
            }
        }
        // 读取图片文件流
        FileInputStream imageFileStream = new FileInputStream(image);
        // 转为图片对象
        BufferedImage imageObject = ImageIO.read(imageFileStream);


        // 设置第count小图的大小和类型
        BufferedImage img = new BufferedImage(endX - startX, endY - startY, imageObject.getType());
        // 创建图样对象
        Graphics2D gr = img.createGraphics();
        // 进行图片的绘画
        gr.drawImage(imageObject,
                0, 0,
                endX - startX, endY - startY,
                startX, startY,
                endX, endY,
                null);

                /*
                    参数：
                    Dx1 -目标矩形第一个角的x坐标。Dy1 -目标矩形第一个角的y坐标。
                    Dx2 -目标矩形第二个角的x坐标。Dy2 -目标矩形第二个角的y坐标。
                    Sx1 -源矩形第一个角的x坐标。Sy1 -源矩形第一个角的y坐标。
                    Sx2 -源矩形第二个角的x坐标。Sy2 -源矩形第二个角的y坐标。

                    测试图片为 1440 x 900
                    源矩阵第一个角的坐标0+0源矩阵第二个角的坐标720+450
                    源矩阵第一个角的坐标720+0源矩阵第二个角的坐标1440+450
                    源矩阵第一个角的坐标0+450源矩阵第二个角的坐标720+900
                    源矩阵第一个角的坐标720+450源矩阵第二个角的坐标1440+900
                 */
        // 释放资源
        gr.dispose();
        // 输出小图
        String targetPath = file.getPath() + "\\" + UUID.randomUUID() + type.getValue();
        ImageIO.write(img, type.value, new File(targetPath));
        return new File(targetPath);
    }

    private static int getPage(File file) throws IOException {
        if (!file.exists()) {
            throw new RuntimeException("调用文件为空");
        }
        if (isWantFileType(file, Type.PDF.getValue())) {
            throw new RuntimeException("文件类型错误，请传输pdf文件");
        }
        return new Splitter().split(PDDocument.load(file)).size();
    }

    /**
     * 内置封装
     * 裁取文件名方法
     * 获取文件名称
     *
     * @param file 文件
     * @return {@link String}
     */
    private static String getFileName(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }

    /**
     * 内置封装
     * 判断是不是想要文件类型
     * 如果是返回false，不是返回true
     *
     * @param file 文件
     * @param types 类型数组
     * @return boolean
     */
    private static boolean isWantFileType(File file,String ... types){
        List<String> strings = Arrays.asList(types);
        return !strings.contains(file.getName().substring(file.getName().lastIndexOf(".")));
    }


    /**
     * 文件类型枚举类
     */
    public enum Type{
        /**
         * pdf
         */
        PDF("pdf"),
        /**
         * jpg
         */
        JPG("jpg"),
        /**
         * png
         */
        PNG("png");

        private final String value;

        Type(String value) {
            this.value=value;
        }
        String getValue(){
            return "."+value;
        }
    }


}
