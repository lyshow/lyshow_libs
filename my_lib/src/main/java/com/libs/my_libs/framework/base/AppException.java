
package com.libs.my_libs.framework.base;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 描述:应用程序异常类：用于捕获异常和提示错误信息
 */
public class AppException extends Exception {

    private static final long serialVersionUID = -4644709214646468113L;

    /** 网络连接异常 */
    public final static byte TYPE_CONNECT = 0x01;

    /** 网络读取数据异常 */
    public final static byte TYPE_SOCKET = 0x02;

    /** 错误响应码 */
    public final static byte TYPE_HTTP_CODE = 0x03;

    /** 网络异常 */
    public final static byte TYPE_HTTP_ERROR = 0x04;

    /** XML解析出错 */
    public final static byte TYPE_XML = 0x05;

    /** io操作异常 */
    public final static byte TYPE_IO = 0x06;

    /** 运行时异常 */
    public final static byte TYPE_RUN = 0x07;

    private int mType;

    private int mCode;

    private AppException(int type, int code, Exception ex) {
        this.mType = type;
        this.mCode = code;
        if (LogUtil.isDebug()) {
            saveErrorLog(ex);
        }
    }

    public int getType() {
        return this.mType;
    }

    public int getCode() {
        return this.mCode;
    }

    /**
     * 保存异常日志
     * 
     * @param ex
     */
    @SuppressWarnings("deprecation")
	public static void saveErrorLog(Exception ex) {
        if (ex == null) {
            return;
        }
        ex.printStackTrace();
        String errorlog = "error.log";
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Kuyou/Log/";
        String logFilePath = "";
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            // 判断是否挂载了SD卡
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                logFilePath = savePath + errorlog;
            }
            // 没有挂载SD卡，无法写文件
            if (logFilePath == "") {
                return;
            }
            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            fw = new FileWriter(logFile, true);
            pw = new PrintWriter(fw);
            pw.println("--------------------" + (new Date().toLocaleString())
                    + "---------------------");
            ex.printStackTrace(pw);
            pw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 保存异常日志
     * 
     * @param ex
     */
    public static void saveErrorLog(String errorStr) {
        Exception ex = new Exception(errorStr);
        saveErrorLog(ex);
    }

    /**
     * HTTP错误响应码
     * 
     * @param code
     * @return
     */
    public static AppException http(int code) {
        return new AppException(TYPE_HTTP_CODE, code, new Exception("error http responsecode:"
                + code));
    }

    /**
     * 网络异常
     * 
     * @param e
     * @return
     */
    public static AppException http(Exception e) {
        return new AppException(TYPE_HTTP_ERROR, 0, e);
    }

    /**
     * 读取数据异常
     * 
     * @param e
     * @return
     */
    public static AppException socket(Exception e) {
        return new AppException(TYPE_SOCKET, 0, e);
    }

    /**
     * IO操作异常
     * 
     * @param e
     * @return
     */
    public static AppException io(Exception e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return new AppException(TYPE_CONNECT, 0, e);
        } else if (e instanceof IOException) {
            return new AppException(TYPE_IO, 0, e);
        }
        return run(e);
    }

    /**
     * XML解释异常
     * 
     * @param e
     * @return
     */
    public static AppException xml(Exception e) {
        return new AppException(TYPE_XML, 0, e);
    }

    /**
     * 网络异常
     * 
     * @param e
     * @return
     */
    public static AppException network(Exception e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return new AppException(TYPE_CONNECT, 0, e);
        } else if (e instanceof SocketException) {
            return socket(e);
        }
        return http(e);
    }

    /**
     * 运行时异常
     * 
     * @param e
     * @return
     */
    public static AppException run(Exception e) {
        return new AppException(TYPE_RUN, 0, e);
    }

}
