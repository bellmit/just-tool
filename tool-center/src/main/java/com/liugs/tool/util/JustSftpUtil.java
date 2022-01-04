package com.liugs.tool.util;

import com.jcraft.jsch.*;
import com.liugs.tool.constants.RespConstants;
import com.liugs.tool.exception.ToolBusinessException;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * @ClassName JustSftpUtil
 * @Description SFTP工具
 * @Author liugs
 * @Date 2021/8/3 14:59:08
 */
public class JustSftpUtil {

    /**
     * 描述 获取sftp连接（通过用户名、密码获取）
     * @param host              地址
     * @param port              端口
     * @param userName          用户名
     * @param password          密码
     * @return com.jcraft.jsch.ChannelSftp
     * @author liugs
     * @date 2021/8/3 15:07:00
     */
    public static ChannelSftp openSftpChannel(String host, int port, String userName, String password) {
        try {
            JSch jsch = new JSch();
            Session session =jsch.getSession(userName, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            //打开sshSession通道
            session.connect();
            return (ChannelSftp) session.openChannel("sftp");
        } catch (JSchException e) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "获取sftp连接异常：", e);
        }
    }

    /**
     * 描述 获取sftp连接（通过证书）
     * @param host                  地址
     * @param port                  端口
     * @param userName              用户名
     * @param privateRsaPath        证书路径（一般是当前路径的.ssh下的rsa_id文件）
     * @param passphrase            证书密码
     * @return com.jcraft.jsch.ChannelSftp
     * @author liugs
     * @date 2021/8/3 15:12:45
     */
    public static ChannelSftp openSftpByRsa(String host, int port, String userName, String privateRsaPath, String passphrase) {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateRsaPath, passphrase);
            Session session = jsch.getSession(userName, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            return (ChannelSftp) session.openChannel("sftp");
        } catch (JSchException e) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "获取sftp连接异常：", e);
        }
    }

    /**
     * 描述 下载文件到输出流
     * @param ftpClient             sftp连接
     * @param targetFile            目标文件路径
     * @param outputStream          输出流
     * @return void
     * @author liugs
     * @date 2021/8/3 15:18:27
     */
    public static void downloadToStream(ChannelSftp ftpClient, String targetFile, OutputStream outputStream) {
        try {
            String tarPath = targetFile.substring(0, targetFile.lastIndexOf("/"));
            String tarFileName = targetFile.substring(targetFile.lastIndexOf("/") + 1);
            // 开启通道
            ftpClient.connect();
            ftpClient.cd(tarPath);
            ftpClient.get(tarFileName, outputStream);
        } catch (JSchException e) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "sftp服务器下载文件异常：", e);
        } catch (SftpException e) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "sftp服务器下载文件异常：", e);
        } finally {
            ftpClient.disconnect();
            try {
                ftpClient.getSession().disconnect();
            } catch (JSchException e) {
                throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "关闭sftp(session)连接异常", e);
            }
        }
    }

    /**
     * 描述 获取当前路径的文件名
     * @param ftpClient         sftp连接
     * @param dir               路径
     * @return java.util.List<java.lang.String>
     * @author liugs
     * @date 2021/8/3 15:21:57
     */
    public static List<String> lsDir(ChannelSftp ftpClient, String dir) {
        return lsDir(ftpClient, dir, true);
    }

    public static List<String> lsDir(ChannelSftp ftpClient, String dir, boolean filterSelf) {
        List<String> list = new LinkedList<>();
        try {
            ftpClient.connect();
            Vector<ChannelSftp.LsEntry> ls = ftpClient.ls(dir);
            for (ChannelSftp.LsEntry entry : ls) {
                // 如果不需要当前目录或父目录，可以将其过滤
                if (filterSelf) {
                    if (".".endsWith(entry.getFilename()) || "..".equals(entry.getFilename())) {
                        continue;
                    }
                }
                list.add(entry.getFilename());
            }
        } catch (JSchException e) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "连接sftp服务器异常：" + e.getMessage(), e);
        } catch (SftpException e) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "sftp服务器下载文件异常：" + e.getMessage(), e);
        } finally {
            ftpClient.disconnect();
            try {
                ftpClient.getSession().disconnect();
            } catch (JSchException e) {
                throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "关闭sftp(session)连接异常", e);
            }
        }

        return list;
    }




}
