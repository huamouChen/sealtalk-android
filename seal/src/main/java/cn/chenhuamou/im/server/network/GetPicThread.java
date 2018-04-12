package cn.chenhuamou.im.server.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Rex on 2018/3/21.
 * Email chenhm4444@gmail.com
 */

public class GetPicThread implements Runnable {
    private String path = null;
    //handler --用于将数据放送到主线程中
    private Handler hander = null;

    public GetPicThread(String path, Handler hander) {
        super();
        this.path = path;
        this.hander = hander;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader istr = null;
        BufferedReader br = null;
        try {
            //1.定义一个URL对象
            URL url = new URL(path);
            //2.通过url获取一个HttpURLConnection对象
            conn = (HttpURLConnection) url.openConnection();
            //3.设置请求方式
            conn.setRequestMethod("GET");
            //4.设置超时时间
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            //5.判断响应码200
            if (conn.getResponseCode() ==
                    HttpURLConnection.HTTP_OK) {
                //6.获取到网络输入的字符流
                is = conn.getInputStream();
                //7.将字节流中获取的图片数据获取出来
                //借助位图工厂类的方法,将流转换为图
                Bitmap bm = BitmapFactory.decodeStream(is);
                //8.将获取的数据发送到主线程中去
                Message msg = hander.obtainMessage();
                msg.what = 0x0001;
                msg.obj = bm;
                hander.sendMessage(msg);
            } else {
                int responseCode = conn.getResponseCode();
            }

            //9.释放资源
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {

                    br.close();
                }
                if (istr != null) {

                    istr.close();
                }
                if (is != null) {

                    is.close();
                }
                if (conn != null) {

                    conn.disconnect();
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
