package com.luhong.locwithlibrary.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;


import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.luhong.locwithlibrary.listener.IDownLoadListener;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * 文件处理类
 * Created by ITMG on 2019-12-18.
 */
public class FileUtils {
    private int fileSize = 1024;
    private static String rootDir = getBaseDir() + "/locwith_library";//文件根目录
    private static String fileDir = "/fileDir_library";//文件目录
    private static String picDir = "/picDir_library";//图片目录
    private static String picDirImg = "/picsDirImg_library";//图片目录
    private static String cacheDir = "/cacheDir_library";//缓存临时目录
    private static String ScreenRecord = "/ScreenRecord_library/";//缓存临时目录

    /**
     * 获取应用的根目录
     *
     * @return
     */
    public static String getBaseDir() {
        if (isSDCardAvailable()) {
            return getSDCardPath();
        } else {
            Logger.error("手机内存卡不存在");
        }
        return "";
    }

    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取App文件根路径
     *
     * @return
     */
    public static String getFileDir() {
        return createDir(rootDir + fileDir).getAbsolutePath();
    }

    /**
     * 获取App文件根路径
     *
     * @return
     */
    public static String getScreenRecordFileDir() {
        return createDir(rootDir + ScreenRecord).getAbsolutePath();
    }

    /**
     * 获取图片根目录
     *
     * @return
     */
    public static String getRootPicDir() {
        return createDir(rootDir + picDir).getAbsolutePath();
    }

    /**
     * 获取图片根目录
     *
     * @return
     */
    public static String getRootPicDirImg() {
        return createDir(rootDir + picDirImg).getAbsolutePath();
    }

    /**
     * 获取图片目录
     *
     * @return
     */
    public static String getPicDir() {
        return picDir;
    }

    /**
     * 获取缓存根目录
     *
     * @return
     */
    public static String getRootCacheDir() {
        return createDir(rootDir + cacheDir).getAbsolutePath();
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardAvailable()) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();
            Logger.error("block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
            Logger.error("可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB");

            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 将输入流写入本地
     *
     * @param targetDir        文件目录
     * @param fileName         文件名
     * @param responseBody     获取输入流
     * @param downLoadListener 下载情况回调
     */
    public static void writeFileByResponseBody(String targetDir, String fileName, ResponseBody responseBody, IDownLoadListener downLoadListener) {
        if (responseBody == null) return;
        downLoadListener.downloadStart();
        byte[] buffer = new byte[1024];
        int length = 0;
        long downloadedSize = 0;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            File file = createSDFile(targetDir, fileName);
            fileOutputStream = new FileOutputStream(file);
            long fileSize = responseBody.contentLength();
            inputStream = responseBody.byteStream();
            while ((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, length);
                downloadedSize += length;
                downLoadListener.downloadProgress(fileSize, downloadedSize);
            }
            fileOutputStream.flush();
            downLoadListener.downloadFinish(file.getAbsolutePath());
        } catch (Exception e) {
            Logger.error("文件下载流处理出错= " + e);
            downLoadListener.downloadError(e.toString());
        } finally {
            closeStream(inputStream);
            closeStream(fileOutputStream);
        }
    }

    /**
     * InputStream数据写入SD卡中
     *
     * @param targetDir   要写到的目录
     * @param fileName    文件名
     * @param inputStream 输入流
     * @return
     */
    public static File writeFileByStream(String targetDir, String fileName, InputStream inputStream) {
        File file = null;
        OutputStream outputStream = null;
        try {
            file = createSDFile(targetDir, fileName);
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];

            int length;
            while ((length = (inputStream.read(buffer))) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            Logger.error("文件写入完成");
        } catch (Exception e) {
            Logger.error("文件写入SD卡出错 = " + e);
        } finally {
            closeStream(inputStream);
            closeStream(outputStream);
        }
        return file;
    }

    /**
     * 复制文件
     *
     * @param srcFile     源文件
     * @param destFile    目标文件
     * @param isDeleteSrc 是否删除源文件
     * @return
     */
    public static boolean copyFile(File srcFile, File destFile, boolean isDeleteSrc) {
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = in.read(buffer)) > 0) {
                out.write(buffer, 0, i);
                out.flush();
            }
            if (isDeleteSrc) {
                srcFile.delete();
            }
        } catch (Exception e) {
            Logger.error("复制文件出错=" + e);
            return false;
        } finally {
            closeStream(out);
            closeStream(in);
        }
        return true;
    }

    /**
     * 创建文件
     *
     * @param targetPath 创建文件的根目录
     * @param fileName   文件名称
     * @return
     * @throws IOException
     */
    public static File createSDFile(String targetPath, String fileName) {
        File file = null;
        createDir(targetPath);
        try {
            file = new File(targetPath, fileName);
            if (file.exists()) {//临时TXT文件删除再新建，zip文件因文件名不同则直接新建
                boolean isDel = file.delete();
                Logger.error("删除文件= " + isDel);
            }
            boolean isCreateNewFile = file.createNewFile();
        } catch (Exception e) {
            Logger.error("创建新文件失败 = " + e);
        }
        return file;
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public static File createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 判断SD卡上的文件是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isFileExist(String targetPath, String fileName) {
        File file = new File(targetPath, fileName);
        if (file == null) return false;
        return file.exists();
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        if (file == null) return false;
        return file.exists();
    }

    /**
     * 写入内容到SD卡中的txt文本中
     * strText
     */
    public static void writeTextToFile(File fileName, String strText) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(fileName, true);// 创建FileWriter对象，用来写入字符流
            bufferedWriter = new BufferedWriter(fileWriter); // 将缓冲对文件的输出
            bufferedWriter.write(strText + "\n"); // 写入文件
            bufferedWriter.newLine();
            bufferedWriter.flush(); // 刷新该流的缓冲
            bufferedWriter.close();
            fileWriter.close();
            Logger.error("向文件写入数据");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Logger.error("向文件写入数据出错=" + e);
            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
            }
        }
    }

    /**
     * 遍历接收一个文件路径，然后把文件子目录中的所有文件添加到上传请求的参数集合中(没有子目录)
     */
    public static void getAllFiles(Map<String, String> fileParams, File rootDir) {
        File files[] = rootDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                } else {
                    fileParams.put("", file.getAbsolutePath());//请求字段参数
                }
            }
        }
    }

    /**
     * 文件重命名
     *
     * @param sourcePath  源文件路径
     * @param newFileDir  新文件目录
     * @param fileNewName 带后缀的新文件名
     * @return
     */
    public static String renameFile(String sourcePath, String newFileDir, String fileNewName) {
        if (TextUtils.isEmpty(sourcePath)) {
            ToastUtil.show("文件不存在");
            return null;
        }
        File file = new File(sourcePath);
        if (file != null && file.exists()) {
            File newFile = new File(newFileDir, fileNewName);
            file.renameTo(newFile);
            return newFile.getAbsolutePath();
        } else {
            ToastUtil.show("文件不存在");
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        if (TextUtils.isEmpty(path)) return;
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /**
     * 删除文件夹
     *
     * @param file 文件夹目录
     */
    public static void deleteAllFiles(File file) {
        Logger.error("删除文件夹");
        File files[] = file.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                        Logger.error("删除文件夹出错 = " + e);
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                            Logger.error("删除文件出错 = " + e);
                        }
                    }
                }
            }
    }

    /**
     * 生成随机文件名
     *
     * @return
     */
    public static String getFileName() {
        int numCode = (int) ((Math.random() * 9 + 1) * 100000);
        return DateUtils.formatCurrentMillisecond() + numCode;
    }

    /**
     * @param fileName     文件名
     * @param isContainDot 是否包含.点
     * @return
     */
    public static String getFileSuffix(String fileName, boolean isContainDot) {
        int lastIndex = fileName.lastIndexOf(".");
        if (!isContainDot) lastIndex = lastIndex + 1;
        return fileName.substring(lastIndex);
    }

    /**
     * 获取URI真实路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static String getUriPath(Context context, Uri uri) {//腾讯云储存
        String path = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.MediaColumns.DATA};
            String colum_name = "_data";
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                Logger.error("count =" + cursor.getCount());
                if (cursor != null && cursor.moveToFirst()) {
                    int colum_index = cursor.getColumnIndex(colum_name);
                    path = cursor.getString(colum_index);
                }
            } catch (Exception e) {
                Logger.error(e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        } else {
            Logger.error("选择文件路径为空");
        }
        return path;
    }

    /**
     * 打开相机
     * 兼容10.0
     *
     * @param activity    Activity
     * @param file        File
     * @param requestCode result requestCode
     */
    public static void startActionCapture(@NonNull Activity activity, File file, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(activity, file));
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startPictureCut(@NonNull Activity activity, File imageFile/*,File outputFile*/, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        Uri imageUri = getUriForFile(activity, imageFile);
        //  Uri outputUri = getUriForFile(activity_feedback, outputFile);
        intent.setDataAndType(imageUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //  intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        activity.startActivityForResult(intent, requestCode);
    }

    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri fileUri;
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//安卓10
            fileUri = getImageContentUri(context, file.getPath());
        } else */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N /*&& Build.VERSION.SDK_INT < Build.VERSION_CODES.Q*/) {//24
            fileUri = FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName() + ".provider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * 安卓10.0获取文件uri
     *
     * @param context
     * @param path
     * @return
     */
    public static Uri getImageContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * @param fileDirs
     * @param fileName
     * @return true代表该文件存在  false代表该文件不存在
     */
    public static boolean isFiel(String fileDirs, String fileName) {
        try {
            //传入指定文件夹的路径
            File file = new File(fileDirs);
            File[] files = file.listFiles();
            for (File file1 : files) {
                if (file1.getPath().contains(fileName)) {
                  /*  File photoFile = new File(file1.getPath());
                    photoFile.delete();*/
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    /**
     * 保存图片&插入系统相册
     *
     * @param context
     * @param bitmap
     * @param fileDir
     * @param fileName
     * @return
     */
    public static boolean saveBitmap(Context context, Bitmap bitmap, String fileDir, String fileName) {
        // 首先保存图片
        File appDir = createDir(fileDir);
        File file = new File(appDir, fileName);
        try {
          /* if (isFiel(fileDir, fileName)) {//存在
                return false;
            }*/
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // 把文件插入到系统图库
        try {
//            这样写可以更新到系统相册 但是查看详情时 图片显示1970年1月1日
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//            正确修改图片详情显示时间
            String insertImage = MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            File file1 = new File(getRealPathFromURI(Uri.parse(insertImage), context));
            updatePhotoMedia(file, context);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        // 通知图库更新
        //    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/namecard/")));
        return true;
    }

    /**
     * 保存图片&插入系统相册
     *
     * @param context
     * @param bitmap
     * @param fileDir
     * @param fileName
     * @return
     */
    public static boolean saveBitmapTow(Context context, Bitmap bitmap, String fileDir, String fileName) {
        // 首先保存图片
        File appDir = createDir(fileDir);
        File file = new File(appDir, fileName);
        try {
          /* if (isFiel(fileDir, fileName)) {//存在
                return false;
            }*/
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 把文件插入到系统图库
        try {
//            这样写可以更新到系统相册 但是查看详情时 图片显示1970年1月1日
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//            正确修改图片详情显示时间
            String insertImage = MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            File file1 = new File(getRealPathFromURI(Uri.parse(insertImage), context));
            updatePhotoMedia(file1, context);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        // 通知图库更新
        //    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/namecard/")));
        return true;
    }

    /**
     * public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
     * public void scanDirAsync(Context ctx, String dir) {
     * Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
     * scanIntent.setData(Uri.fromFile(new File(dir)));
     * ctx.sendBroadcast(scanIntent);
     * }
     *
     * @param file
     * @param context
     */
    //更新图库
    public static void updatePhotoMedia(File file, Context context) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
    }

    //得到绝对地址
    private static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String fileStr = cursor.getString(column_index);
        cursor.close();
        return fileStr;
    }

    /**
     * 文件转化为字节数组
     */
    public static byte[] getByteByInStream(FileInputStream inputStream, int size) {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream(size);
            byte[] b = new byte[size];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, n);
            }
            inputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            Logger.error("文件转化为字节数组异常" + e);
            closeStream(inputStream);
            closeStream(outputStream);
        }
        return null;
    }

    public static byte[] getByteByPath(/*String filePath*/File file) throws IOException {
//        File file = new File(filePath);
        long fileSize = file.length();
        Logger.error("fileSize=" + fileSize);
        if (fileSize > Integer.MAX_VALUE) {
            Logger.error("file too big...");
            return null;
        }
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = inputStream.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely getByteByInStream file " + file.getName());
        }
        closeStream(inputStream);
        return buffer;
    }

    /**
     * 关闭流
     */
    public static void closeStream(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                Logger.error("关闭流出错=" + e);
            }
        }
    }

    /**
     * 文件读取
     *
     * @param bytes
     * @return
     */
    public static Object byteToObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream oInputStream = new ObjectInputStream(bInputStream);

            obj = oInputStream.readObject();

            closeStream(bInputStream);
            closeStream(oInputStream);
        } catch (Exception e) {
            Logger.error("文件写入出错=" + e);
        }
        return obj;
    }


    /**
     * 文件写入
     *
     * @param obj 对象
     * @return
     */
    public static byte[] objectToByte(Object obj) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bInputStream = new ByteArrayOutputStream();
            ObjectOutputStream oInputStream = new ObjectOutputStream(bInputStream);
            oInputStream.writeObject(obj);

            bytes = bInputStream.toByteArray();

            closeStream(bInputStream);
            closeStream(oInputStream);
        } catch (Exception e) {
            Logger.error("文件写入出错=" + e);
        }
        return bytes;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {// 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();// 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);// 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);// 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

}
