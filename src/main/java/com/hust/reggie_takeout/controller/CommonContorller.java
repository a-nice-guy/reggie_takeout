package com.hust.reggie_takeout.controller;

import com.hust.reggie_takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonContorller {

    @Value("${reggie.uploadPath}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        //file是一个临时文件，需要转存至指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        //获取传入文件的原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //生成uuid
        String fileName = UUID.randomUUID().toString() + suffix;

        //如果保存目录不存在，则创建
        File dir = new File(basePath);
        if (!dir.exists()){
            //创建目录
            dir.mkdir();
        }

        //将文件转存至指定位置
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //使用输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //使用输出流输出文件内容
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];

            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            fileInputStream.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
