package com.example.android.multmedia.player.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LyricsParser {
    /**
     * 从歌词文件解析出歌词数据列表
     */
    public static ArrayList<Lcr> parserFromFile(File lyricsFile) {
        ArrayList<Lcr> lyricsList = new ArrayList<>();
        // 数据可用性检查
        if (lyricsFile == null || !lyricsFile.exists()) {
            lyricsList.add(new Lcr("没有找到歌词文件。", 0));
            return lyricsList;
        }

        // 按行解析歌词
        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(lyricsFile), "GBK"));
            String line = buffer.readLine();
            while (line != null) {
                List<Lcr> lineList = parserLine(line);
                lyricsList.addAll(lineList);

                line = buffer.readLine();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 歌词排序  如果歌词是这种格式的[01:45.51][02:58.62]整理好心情再出发
        //如果解析之后不排序 歌词的顺序是乱的 sort方法 需要javabean 实现comparable接口 重写compareTo方法
        //返回负数排在前面
        Collections.sort(lyricsList);
        return lyricsList;
    }

    /**
     * 解析一行歌词  [01:45.51][02:58.62]整理好心情再出发
     */
    private static List<Lcr> parserLine(String line) {
        List<Lcr> lineList = new ArrayList<>();

        String[] arr = line.split("]");
        // [01:45.51 [02:58.62 整理好心情再出发
        String content = arr[arr.length - 1];

        // [01:45.51 [02:58.62
        for (int i = 0; i < arr.length - 1; i++) {
            int startPoint = parserStartPoint(arr[i]);
            lineList.add(new Lcr(content, startPoint));
        }

        return lineList;
    }

    /**
     * 解析出一行歌词的起始时间 [01:45.51
     */
    private static int parserStartPoint(String startPoint) {

        int time = 0;
        String[] arr = startPoint.split(":");
        // [01 45.51
        String minStr = arr[0].substring(1);

        // 45.51
        arr = arr[1].split("\\.");

        // 45 51
        String secStr = arr[0];
        String mSecStr = arr[1];

        time = Integer.parseInt(minStr) * 60 * 1000
                + Integer.parseInt(secStr) * 1000
                + Integer.parseInt(mSecStr) * 10;


        return time;
    }
}
