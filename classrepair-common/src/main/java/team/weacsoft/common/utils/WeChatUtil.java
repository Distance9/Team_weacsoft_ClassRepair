package team.weacsoft.common.utils;

import com.alibaba.druid.sql.visitor.functions.Char;

import java.io.*;

/**
 * @Description 本类提供对推文地址的读写方法
 * @ClassName WeChatUtil
 * @Author 魔法はまだ解けない
 * @date 2020.02.26 14:13
 */
public class WeChatUtil {

    public static String getTweetsURL() throws IOException {
        FileReader fileReader = null;
        File file = new File("Tweets.txt");
        if(!file.exists()){
            file.createNewFile();
            String s1="https://mp.weixin.qq.com/s/ZGShHw6HI99IggrKgWyLaA";
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(s1);
            fileWriter.close();
            return s1;
        }else {
            fileReader = new FileReader("Tweets.txt");
            int i, j = 0;
            char a[] = new char[1000];
            i = fileReader.read();
            while (i != -1) {
                a[j] = (char) i;
                j++;
                i = fileReader.read();
            }
            String s = new String(a, 0, j);
            fileReader.close();
            return s;
        }
    }

    public static String setTweetsURL(String s) throws IOException {
        FileWriter fileWriter = null;
        String s1 = "true";
        fileWriter = new FileWriter("Tweets.txt");
        fileWriter.write(s);
        fileWriter.close();
        return s1;
    }
}
