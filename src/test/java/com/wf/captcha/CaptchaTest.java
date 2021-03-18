package com.wf.captcha;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

/**
 * 测试类
 * Created by 王帆 on 2018-07-27 上午 10:08.
 */
public class CaptchaTest {

    private String prepOutputDir() {
        String directory = ".captchas";
        File folderFile = new File(directory);

        if (folderFile.exists()) {
            folderFile.delete();
        }

        folderFile.mkdirs();

        return folderFile.getAbsolutePath() + File.separator;
    }

    @Test
    public void test() throws Exception {
        for (int i = 0; i < 10; i++) {
            SpecCaptcha specCaptcha = new SpecCaptcha();
            specCaptcha.setLen(4);
            specCaptcha.setFont(i, 32f);
            System.out.println(specCaptcha.text());
            specCaptcha.out(new FileOutputStream(new File(prepOutputDir() + "spec_" + i + ".png")));
        }
    }

    @Test
    public void testGIf() throws Exception {
        for (int i = 0; i < 10; i++) {
            GifCaptcha gifCaptcha = new GifCaptcha();
            gifCaptcha.setLen(5);
            gifCaptcha.setFont(i, 32f);
            System.out.println(gifCaptcha.text());
            gifCaptcha.out(new FileOutputStream(new File(prepOutputDir() + "gif_" + i + ".gif")));
        }
    }

    @Test
    public void testHan() throws Exception {
        for (int i = 0; i < 10; i++) {
            ChineseCaptcha chineseCaptcha = new ChineseCaptcha();
            System.out.println(chineseCaptcha.text());
            String tempPath = System.getProperty("java.io.tmpdir") + File.separator;
            chineseCaptcha.out(new FileOutputStream(new File(prepOutputDir() + "han_" + i + ".png")));
        }
    }

    @Test
    public void testGifHan() throws Exception {
        for (int i = 0; i < 10; i++) {
            ChineseGifCaptcha chineseGifCaptcha = new ChineseGifCaptcha();
            System.out.println(chineseGifCaptcha.text());
            chineseGifCaptcha.out(new FileOutputStream(new File(prepOutputDir() + "gif_han_" + i + ".gif")));
        }
    }

    @Test
    public void testArit() throws Exception {
        for (int i = 0; i < 10; i++) {
            ArithmeticCaptcha specCaptcha = new ArithmeticCaptcha();
            specCaptcha.setLen(3);
            specCaptcha.setFont(i, 28f);
            specCaptcha.out(new FileOutputStream(new File(prepOutputDir() + "arit_" + i + ".png")));
        }
    }

    @Test
    public void testBase64() throws Exception {
        GifCaptcha specCaptcha = new GifCaptcha();
        System.out.println(specCaptcha.toBase64(""));
    }

}
