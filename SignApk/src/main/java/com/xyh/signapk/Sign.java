package com.xyh.signapk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Sign {


    public static void main(String args[]) throws IOException {
        BufferedReader br = null;
        OutputStreamWriter osw = null;
        Process process = null;
        try {

            //1----解压
            //apktool路径
            String path = "D:\\My Documents\\Desktop\\apktool-install-windows-r04-brut1";
            //保存路径
            String appPath = "F:\\document\\APK\\new\\";
            File file = new File(path);

            process = Runtime.getRuntime().exec("cmd.exe /c apktool d " + appPath + "iGouShop.apk " + appPath + "app", null, file);
            if (process.waitFor() != 0) System.out.println("解压失败。。。");

            //2----内容修改
            String targetPath = appPath + "app\\res\\raw\\channel";
            String content = "TT201209291653";
            br = new BufferedReader(new InputStreamReader(new FileInputStream(targetPath)));
            while ((br.readLine()) != null) {
                osw = new OutputStreamWriter(new FileOutputStream(targetPath));
                osw.write(content, 0, content.length());
                osw.flush();
            }

            //3----打包
            process = Runtime.getRuntime().exec("cmd.exe /c apktool b " + appPath + "app " + appPath + "app.apk", null, file);
            if (process.waitFor() != 0) System.out.println("打包失败。。。");

            //4----签名 （文件名称中不能包含空格）
            String jdkBinPath = "C:\\Program Files\\Java\\jdk1.6.0_26\\bin";
            File bin = new File(jdkBinPath);
            String cmd = "cmd.exe /c jarsigner -keystore F:\\document\\APK\\sdk.keystore -storepass winadsdk -signedjar " + appPath + "appT.apk " + appPath + "\\app.apk" + " sdk.keystore";
            process = Runtime.getRuntime().exec(cmd, null, bin);
            if (process.waitFor() != 0) System.out.println("签名失败。。。");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            br.close();
            osw.close();
        }
    }
}
