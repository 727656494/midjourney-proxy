package com.github.novicezk.midjourney.controller;


import com.github.novicezk.midjourney.support.Task;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Api(tags = "file")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    @GetMapping("/download")
    public void fileDownload(String url, HttpServletResponse response, HttpServletRequest request)
    {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            HttpURLConnection httpUrl = (HttpURLConnection) new URL(url).openConnection();
            httpUrl.connect();
            bufferedInputStream = new BufferedInputStream(httpUrl.getInputStream());
            //以上这样我们就得到了一个文件流；不管是视频，图片，音频，文本文件，都是可以的
            //返回文件流
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            //创建存放文件内容的数组
            byte[] buff =new byte[1024];
            //所读取的内容使用n来接收
            int n;
            //当没有读取完时,继续读取,循环
            while((n=bufferedInputStream.read(buff))!=-1){
                //将字节数组的数据全部写入到输出流中
                bufferedOutputStream.write(buff,0,n);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (bufferedInputStream!=null){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (bufferedOutputStream!=null){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
