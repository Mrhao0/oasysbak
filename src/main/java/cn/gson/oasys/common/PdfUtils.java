package cn.gson.oasys.common;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qiaoli
 * @date 2022/10/19
 */
public class PdfUtils {
    public static void main(String[] args) {
        File file = new File("D:\\test2");
        File filet = new File("D:\\test\\tt");

        File file1 = pdfToImageAll(file, filet, Type.JPG);
    }
    /**
     * 图片展示的百分比
     */
    private static int PERCENT = 33;

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
     * @return {@link File} 成功拆分后文件的的目录
     */
    public static File doSplitPdfFile(File pdfFile, File target) {
        try {
            return splitPdfFile(pdfFile, target);
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
    public static List<File> doImagesSplit(File image, Type type, File target) {
        try {
            return imagesSplit(image, type, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * pdf转化为图片（会报EOFException，正常，测试显示为绿色，不会阻碍程序的正常执行，）
     * <hr/>
     * 例如路径：目标路径/文件名字/文件名.jpg
     * <hr/>
     *
     * @param file   拆分后的单页文件
     * @param target 目标(目录)地址
     * @param type   图片类型
     * @return {@link File} 最后转化的图片的文件对象
     */
    public static File doPdfToImage(File file, File target, Type type) {
        try {
            return pdfToJpg(file, target, type);
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
    public static File doUserSplitImages(File image, int startX, int startY, int endX, int endY, Type type, File target) {
        try {
            return userSplitImages(image, startX, startY, endX, endY, type, target);
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
    public static int getPdfPage(File file) {
        try {
            return getPage(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 展示pdf图像
     *
     * @param imageParentPath 图像父路径
     * @param number          查看的页码（从1开始，在代码中已经默认减 1 了）
     * @param size            大小（显示几个页面）
     * @return {@link List}<{@link String}>
     */
    public static List<String> showNumberImage(String imageParentPath, int number, int size){
        //调用方法
        List<String> numberImage = getNumberImage(imageParentPath,number,size);
        //给可能提前返回的集合进行最后一次排序，收集，然后返回
        return numberImage.stream().sorted(
                        Comparator.comparingInt(o -> Integer.parseInt(
                                o.substring(
                                        (o.lastIndexOf("\\") + 1),
                                        o.lastIndexOf("."))))
                )
                .collect(Collectors.toList());
    }


    /**
     * 合并pdf
     *
     * @param files  文件地址的集合
     * @param target 合并后的pdf文件地址
     * @return
     */
    public static File mergePdfFiles(List<String> files, String target) {
        Document document = null;
        PdfCopy copy = null;
        PdfReader reader = null;
        try {
            document = new Document(new PdfReader(files.get(0)).getPageSize(1));
            copy = new PdfCopy(document, Files.newOutputStream(Paths.get(target)));
            document.open();
            for (String file : files) {
                reader = new PdfReader(file);
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (copy != null) {
                copy.close();
            }
            if (document != null) {
                document.close();
            }
        }
        return new File(target);
    }

    /**
     * 图片转pdf
     *
     * @param source 文件对象集合
     * @param target 目标地址
     */
    public static void imgChangePdf(List<String> source, String target) {
        //创建一个文档对象
        Document doc = new Document();
        try {
            //定义输出文件的位置
            PdfWriter.getInstance(doc, Files.newOutputStream(Paths.get(target)));
            //开启文档
            doc.open();
            // 循环获取图片文件夹内的图片
            for (String url : source) {
                if (url == null) {
                    continue;
                }
                //路径
                com.lowagie.text.Image img = com.lowagie.text.Image.getInstance(url);
                //图片居中
                img.setAlignment(com.lowagie.text.Image.MIDDLE);
                //百分比显示图
                img.scalePercent(PERCENT);
                //设置高和宽的比例
                doc.add(img);
            }
            // 关闭文档
            doc.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换全部的pdf
     *
     * @param type    图片类型
     * @param pdfFile pdf文件
     * @param target  目标地址（目录）
     */
    public static File pdfToImageAll(File pdfFile, File target, Type type) {
        //判断用户文件是否为空
        judgingInformation(pdfFile, target, new String[]{Type.PDF.getValue()});
        try {
            PDDocument doc = PDDocument.load(pdfFile);

            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                System.out.println(i);
                BufferedImage image = renderer.renderImageWithDPI(i, 144);
                // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                ImageIO.write(image, type.value, new File(target.getPath() + "\\" + (i + 1) + type.getValue()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return target;
    }

    private static List<String> getNumberImage(String imageParentPath, int number, int size) {
        //默认减一，页码为1，集合下标为0
        number -= 1;
        //对设置的路径进行判断
        File imageDirectory = new File(imageParentPath);
        if (!imageDirectory.exists()) {
            throw new RuntimeException("没有此目录");
        } else {
            if (!imageDirectory.isDirectory()) {
                throw new RuntimeException("此文件不为一个文件夹，输入正确的文件夹地址");
            }
        }
        File[] files = imageDirectory.listFiles();
        if (Objects.isNull(files)) {
            throw new RuntimeException("目录下文件为空");
        }

        //使用流的方式对不是图片的文件进行过滤，对文件名采取截取后数字化排序的方式进行排序然后收集
        List<String> collect =
                Arrays.stream(files)
                        .map(File::getPath)
                        .filter(filename ->
                                Arrays.asList(
                                                Type.JPG.getValue(), Type.PNG.getValue())
                                        .contains(filename.substring(filename.lastIndexOf(".")))
                        )
                        .sorted(
                                Comparator.comparingInt(o -> Integer.parseInt(
                                        o.substring(
                                                (o.lastIndexOf("\\") + 1),
                                                o.lastIndexOf("."))))
                        )
                        .collect(Collectors.toList());
        //判断需要获取的大小与拥有文件数进行比对
        if (size > collect.size()) {
            size = collect.size();
        }
        //初始化集合
        ArrayList<String> imagePathList = new ArrayList<>(size);
        //左个数
        int left = size / 2;
        //右个数
        int right = size / 2;
        //开始对左边进行获取，考虑到边界的问题
        for (int l = number; l >= number - left; l--) {
            //对其进行判断如果越界，把次数给到右边
            if (l < 0) {
                right += 1;
                continue;
            }
            imagePathList.add(collect.get(l));
        }
        //判断是奇数还是偶数
        // 举例   size：5                                                size：4
        // 奇数  左：3      右：2                                     偶数  左：3      右：1
        if (size % 2 == 0) {
            right -= 1;
        }
        //对右边的元素进行填充获取，也会存在越界问题
        for (int r = number + 1; r <= number + right; r++) {
            if (r >= collect.size()) {
                //如果大于拥有数则已经越界，直接退出循环
                break;
            }
            imagePathList.add(collect.get(r));
        }
        //判断是否已经足够，足够则返回
        if (size == imagePathList.size()) {
            return imagePathList;
        }
        //左边完成，右边越界，
        //剩下的缺口
        //再次回到左边
        //由左边的元素再次进行填充
        for (int rl = collect.size() - imagePathList.size() - 1; rl >= collect.size() - size; rl--) {
            imagePathList.add(collect.get(rl));
        }
        return imagePathList;
    }


    /**
     * 图片的展示的百分比大小
     */
    public static void setPercent(int newPercent) {
        PERCENT = newPercent;
    }


    private static File splitPdfFile(File pdfFile, File target) throws IOException {
        //判断信息
        String[] types = {Type.PDF.getValue()};
        judgingInformation(pdfFile, target, types);

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
                    .save(target.getPath() +
                            (i + 1) + Type.PDF.getValue());
        }
        doc.close();
        return new File(target.getPath());
    }


    private static List<File> imagesSplit(File image, Type type, File target) throws IOException {

        String[] types = {Type.JPG.getValue(), Type.PNG.getValue()};

        judgingInformation(image, target, types);

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
            String targetPath = target + "\\" + (i + 1) + type.getValue();
            targetPathList.add(new File(targetPath));
            ImageIO.write(imgs[i], type.value, new File(targetPath));
        }
        return targetPathList;

    }

    private static File pdfToJpg(File file, File target, Type type) throws IOException {

        String[] types = {Type.PDF.getValue()};

        judgingInformation(file, target, types);

        //读取pdf
        PDDocument doc = PDDocument.load(file);
        //实例化PDFRenderer渲染器
        PDFRenderer renderer = new PDFRenderer(doc);
        // Windows 的默认 DPI
        BufferedImage image = renderer.renderImageWithDPI(0, 144);
        // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
        File imageFile = new File(target.getPath() + "//" + getFileName(file) + type.getValue());
        ImageIO.write(image, type.value, imageFile);
        doc.close();
        return imageFile;

    }


    private static File userSplitImages(File image, int startX, int startY, int endX, int endY, Type type, File target) throws IOException {
        String[] types = {Type.JPG.getValue(), Type.PNG.getValue()};
        //判断传过来的文件是否存在
        judgingInformation(image, target, types);
        // 读取图片文件流
        FileInputStream imageFileStream = new FileInputStream(image);
        // 转为图片对象
        BufferedImage imageObject = ImageIO.read(imageFileStream);


        // 设置第count小图的大小和类型
        BufferedImage img = new BufferedImage(endX - startX, endY - startY, imageObject.getType());

        // 创建图样对象  获取配置  创建兼容图像  设置（宽|高|透明度）
        img = img.createGraphics().getDeviceConfiguration().createCompatibleImage(endX - startX, endY - startY,
                Transparency.TRANSLUCENT);

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
        String targetPath = target.getPath() + "\\" + UUID.randomUUID() + type.getValue();
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
     * 裁取文件名方法
     * 获取文件名称
     *
     * @param file 文件
     * @return {@link String}
     */
    public static String getFileName(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }

    /**
     * 内置封装
     * 判断是不是想要文件类型
     * 如果是返回false，不是返回true
     *
     * @param file  文件
     * @param types 类型数组
     * @return boolean
     */
    private static boolean isWantFileType(File file, String... types) {
        List<String> strings = Arrays.asList(types);
        return !strings.contains(file.getName().substring(file.getName().lastIndexOf(".")));
    }


    /**
     * 判断文件信息
     *
     * @param file   文件
     * @param target 目标
     * @param types  类型
     */
    private static void judgingInformation(File file, File target, String[] types) {
        //判断用户文件是否为空
        if (!file.exists()) {
            throw new RuntimeException("调用文件为空");
        }
        if (types != null) {
            String fileType = "";
            for (String type : types) {
                fileType = type + "、";
            }
            fileType = fileType.substring(0, fileType.lastIndexOf("、"));
            if (isWantFileType(file, types)) {
                throw new RuntimeException("文件类型错误，请传输" + fileType + "类型的文件");
            }
        }
        //创建分页目录
        if (!target.exists()) {
            if (!target.isDirectory()) {
                if (!target.mkdirs()) {
                    throw new RuntimeException("目录创建失败");
                }
            }
        }
    }


    /**
     * 文件类型枚举类
     */
    public enum Type {
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
            this.value = value;
        }

        String getValue() {
            return "." + value;
        }
    }

}
