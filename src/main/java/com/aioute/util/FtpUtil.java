package com.aioute.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * ftp操作类
 * <p>需创建实体对象。</p>
 *
 * @author 郑冉
 * @dependence org.apache.commons.net
 * @maven commons-net commons-net 3.3
 * @date 2016-02-24
 */
public class FtpUtil {
    private Logger logger = Logger.getLogger(FtpUtil.class.getName());

    private FTPClient ftpClient = null;
    private String url = null;
    private int port = -1;
    private String username = null;
    private String password = null;
    public ArrayList<String> arFiles;

    /**
     * 构造函数
     */
    public FtpUtil() {
        if (this.url == null) {
            this.url = "192.168.17.106";
            this.username = "ftpname";
            this.password = "ftpname";
            this.port = 21;
        }
        ftpClient = new FTPClient();
        ftpClient.enterLocalPassiveMode();
    }

    /**
     * 连接ftp服务器
     *
     * @return Boolean 连接是否成功，<b>null</b> - 异常
     */
    public Boolean conn() {
        try {
            ftpClient.connect(url, port);
//			ftpClient.setControlEncoding("GBK");
//			FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
//			conf.setServerLanguageCode("zh");
            ftpClient.login(username, password);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.debug("ftp连接失败，错误代码：" + ftpClient.getReplyCode());
                close();
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.debug("FtpUtil.conn 出现异常：" + e.getMessage());
            close();
            return null;
        }
    }

    /**
     * 断开ftp服务器
     */
    public void close() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {

        }
    }

    /**
     * 文件是否存在
     *
     * @param srcName 文件名
     * @param path    文件路径
     * @return Boolean <b>true</b> - 存在，<b>false</b> - 不存在，<b>null</b> - 异常
     */
    public Boolean isExists(String srcName, String path) {
        Boolean isExists = null;
        try {
            srcName = new String(srcName.getBytes("GBK"), "ISO-8859-1");
            path = new String(path.getBytes("GBK"), "ISO-8859-1");
            ftpClient.changeWorkingDirectory(path);
            for (FTPFile file : ftpClient.listFiles()) {
                if (srcName.equals(new String(file.getName().getBytes("GBK"), "ISO-8859-1"))) {
                    isExists = true;
                    break;
                }
            }
            if (isExists == null)
                isExists = false;
        } catch (Exception e) {
            logger.error("FtpUtil.isExists 出现异常：" + e.getMessage());
        }
        return isExists;
    }

    /**
     * 删除文件
     *
     * @param path     文件所在路径
     * @param fileName 文件名
     * @return Boolean 是否删除成功，<b>null</b> - 异常
     */
    public Boolean deleteFile(String path, String fileName) {
        try {
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            path = new String(path.getBytes("GBK"), "ISO-8859-1");
            ftpClient.changeWorkingDirectory(path);
            ftpClient.deleteFile(fileName);
            return !isExists(fileName, ftpClient.listFiles());
        } catch (Exception e) {
            logger.error("FtpUtil.deleteFile 出现异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 上传文件
     *
     * @param path     文件保存路径
     * @param fileName 文件名
     * @param file     上传文件
     * @return Boolean 是否上传成功，<b>null</b> - 异常
     */
    public Boolean upload(String path, String fileName, File file) {
        try {
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            path = new String(path.getBytes("GBK"), "ISO-8859-1");
            ftpClient.changeWorkingDirectory(path);
            fileName = autoRename(fileName, ftpClient.listFiles());
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream is = new FileInputStream(file);
            ftpClient.storeFile(fileName, is);
            is.close();
            return isExists(fileName, ftpClient.listFiles());
        } catch (Exception e) {
            logger.error("FtpUtil.upload 出现异常：" + e.getMessage());
            return null;
        }
    }

    public Boolean upload(String path, String fileName, InputStream is) {
        try {
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            path = new String(path.getBytes("GBK"), "ISO-8859-1");
            ftpClient.changeWorkingDirectory(path);
            fileName = autoRename(fileName, ftpClient.listFiles());
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.storeFile(fileName, is);
            is.close();
            return isExists(fileName, ftpClient.listFiles());
        } catch (Exception e) {
            logger.error("FtpUtil.upload 出现异常：" + e.getMessage());
            return null;
        }
    }

    public OutputStream uploadStream(String path, String fileName) {
        try {
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            path = new String(path.getBytes("GBK"), "ISO-8859-1");
            ftpClient.changeWorkingDirectory(path);
            fileName = autoRename(fileName, ftpClient.listFiles());
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.storeFileStream(fileName);
        } catch (Exception e) {
            logger.error("FtpUtil.upload 出现异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 新建目录
     * <p>如目录已存在则返回<b>false</b></p>
     *
     * @param path    目录所在路径
     * @param dirName 目录名称
     * @return Boolean 是否新建成功，<b>null</b> - 异常
     */
    public Boolean createDirectory(String path, String dirName) {
        try {
            dirName = new String(dirName.getBytes("GBK"), "ISO-8859-1");
            path = new String(path.getBytes("GBK"), "ISO-8859-1");
            ftpClient.changeWorkingDirectory(path);
            return ftpClient.makeDirectory(dirName);
        } catch (Exception e) {
            logger.error("FtpUtil.createDirectory 出现异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 下载文件
     * <p>注意关闭返回的文件流</p>
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @return InputStream 文件流，<b>null</b> - 异常/文件不存在
     */
    public InputStream download(String path, String fileName) {
        InputStream is = null;
        try {
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            path = new String(path.getBytes("GBK"), "ISO-8859-1");
            ftpClient.changeWorkingDirectory(path);
//            if (isExists(fileName, ftpClient.listFiles()))
            is = ftpClient.retrieveFileStream(fileName);
        } catch (Exception e) {
            logger.error("FtpUtil.download 出现异常：" + e.getMessage());
        }
        return is;
    }

    /**
     * 自动重命名文件
     *
     * @param srcName    文件名
     * @param checkFiles 需要检测的文件列表
     * @return String 重命名完的文件名
     */
    private static String autoRename(String srcName, FTPFile[] checkFiles) throws UnsupportedEncodingException {
        String filename = null;
        String suffix = null;
        if (srcName.lastIndexOf(".") > -1) {
            filename = srcName.substring(0, srcName.lastIndexOf("."));
            suffix = srcName.substring(srcName.lastIndexOf(".") + 1);
        } else {
            filename = srcName;
        }
        int i = 1;
        String newName = srcName;
        while (isExists(newName, checkFiles)) {
            newName = filename + "(" + (i++) + ")"
                    + (suffix == null ? "" : "." + suffix);
        }
        return newName;
    }

    /**
     * 文件是否存在
     *
     * @param srcName    文件路径
     * @param checkFiles 需要检测的文件列表
     * @return Boolean 是否存在
     */
    private static boolean isExists(String srcName, FTPFile[] checkFiles) throws UnsupportedEncodingException {
        for (FTPFile file : checkFiles) {
            if (srcName.equals(new String(file.getName().getBytes("GBK"), "ISO-8859-1"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 递归遍历出目录下面所有文件
     *
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @throws IOException
     */
    public void List(String pathName) throws IOException {
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            String directory = pathName;
            //更换目录到当前目录
            ftpClient.changeWorkingDirectory(directory);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    arFiles.add(directory + file.getName());
                } else if (file.isDirectory()) {
                    List(directory + file.getName() + "/");
                }
            }
        }
    }

    public FTPFile[] filesList(String pathName) throws IOException {
        FTPFile[] files = null;
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            String directory = pathName;
            //更换目录到当前目
            ftpClient.changeWorkingDirectory(directory);
            files = ftpClient.listFiles();
        }
        return files;
    }

    /**
     * 递归遍历目录下面指定的文件名
     *
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @param ext      文件的扩展名
     * @throws IOException
     */
    public void List(String pathName, String ext) throws IOException {
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            String directory = pathName;
            //更换目录到当前目录
            ftpClient.changeWorkingDirectory(directory);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    if (file.getName().endsWith(ext)) {
                        arFiles.add(directory + file.getName());
                    }
                } else if (file.isDirectory()) {
                    List(directory + file.getName() + "/", ext);
                }
            }
        }
    }

    public String getXmlFile(String remoteFilePath, String fileName) {
        StringBuffer s = new StringBuffer();
        InputStream in = null;

        try {
            in = this.download(remoteFilePath, fileName);
//          ftp.downloadStream(remoteFilename.substring(1, remoteFilename.length()));
            int ch = 0;
            while ((ch = in.read()) >= 0) {
                s.append((char) ch);
            }
        } catch (Exception e) {
            logger.error(e);
            return "";
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }

        return s.toString();
    }

    /**
     * 递归创建远程服务器目录
     *
     * @param remote 远程服务器文件绝对路径
     * @return 目录创建是否成功
     * @throws IOException
     */
    public Boolean createDirecrotys(String remote)
            throws IOException {
        String[] directory = remote.split("/");
        ftpClient.changeWorkingDirectory("/");
        for (int i = 0; i < directory.length; i++) {
            boolean boo = ftpClient.changeWorkingDirectory(directory[i]);
            if (!boo) {
                ftpClient.makeDirectory(directory[i]);
                ftpClient.changeWorkingDirectory(directory[i]);
            }

        }

        return true;
    }

    // 检查路径是否存在，存在返回true，否则false
    public boolean existDirectory(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        for (FTPFile ftpFile : ftpFileArr) {
            if (ftpFile.isDirectory()
                    && ftpFile.getName().equalsIgnoreCase(path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static void main(String[] args) throws IOException {
       /* FtpUtil ftpUtil = new FtpUtil("192.168.101.172", 21, "ftp", "huitong123");
        Boolean connectstatus = ftpUtil.conn();
        System.out.print("-----------" + connectstatus);
        FTPFile[] listfile = ftpUtil.filesList("/out/");
        for (int i = 0; i < listfile.length; i++) {
            System.out.print("------------¥¥¥¥" + listfile[i].getName());
            String xmlFileStr = ftpUtil.getXmlFile("/out/", listfile[i].getName());
            System.out.print("-----------!!!!!" + xmlFileStr);
        }*/
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}