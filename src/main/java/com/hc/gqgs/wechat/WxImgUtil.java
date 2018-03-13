package com.hc.gqgs.wechat;

import com.hc.gqgs.json.ERRORDetail;
import com.hc.gqgs.json.WebResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WxImgUtil {

    //把从服务器获得图片的输入流InputStream写到本地磁盘
    public static WebResult saveImageToDisk(String token, String mediaId, String savePath) {
        return saveImageToDisk("https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + token + "&media_id=" + mediaId, savePath);
    }

    //把从服务器获得图片的输入流InputStream写到本地磁盘
    public static WebResult saveImageToDisk(String urlPath, String savePath) {
        InputStream inputStream = getInputStream(urlPath);
        if(inputStream == null){
            return WebResult.error(ERRORDetail.TFC_0101011);
        }
        byte[] data = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        try {
        	File destFile = new File(savePath);  
            if (!destFile.getParentFile().exists()) {  
                destFile.getParentFile().mkdirs();  
            } 
            fileOutputStream = new FileOutputStream(savePath);
            if(inputStream.available() == 0){
                return WebResult.error(ERRORDetail.TFC_0101011);
            }
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        return WebResult.success();
    }

    // 从服务器获得一个输入流(本例是指从服务器获得一个image输入流)
    public static InputStream getInputStream(String urlPath) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlPath);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设置网络连接超时时间
            httpURLConnection.setConnectTimeout(3000);
            // 设置应用程序要从网络连接读取数据
            httpURLConnection.setDoInput(true);

            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                // 从服务器返回一个输入流
                inputStream = httpURLConnection.getInputStream();

            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return inputStream;

    }

    public static void main(String[] args) {
        String urlPath = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=7_omLVab06gygpA6xyVsHtwWLGoCMzuyEuGgaWoRBkgkBrC-r0SGAShoRRmG8QI2ZIvDG-gkRLTTRSMmcn7xXm_ojRvfBDz9xtM0oXk5aETLJ_E8cLp8weAwUiYgMxRSrLkS6Af4ltNg3xdZ8OYALiAHASUF&media_id=3AhDzsia0Y0TyKN3yRm4Ve8a9n8th6KCOMvurm52jSUGOWY0dkZf3LTgQuquI-Qd";
        String token = "7_omLVab06gygpA6xyVsHtwWLGoCMzuyEuGgaWoRBkgkBrC-r0SGAShoRRmG8QI2ZIvDG-gkRLTTRSMmcn7xXm_ojRvfBDz9xtM0oXk5aETLJ_E8cLp8weAwUiYgMxRSrLkS6Af4ltNg3xdZ8OYALiAHASUF";
        String mediaId = "3AhDzsia0Y0TyKN3yRm4Ve8a9n8th6KCOMvurm52jSUGOWY0dkZf3LTgQuquI-Qd";
        saveImageToDisk(urlPath, "D://test1.jpg");
        saveImageToDisk(token, mediaId, "D://test2.jpg");
    }
}
