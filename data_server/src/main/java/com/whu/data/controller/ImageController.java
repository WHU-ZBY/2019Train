
//作者：张步云

package com.whu.data.controller;

import org.springframework.http.MediaType;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;


@Controller

@RequestMapping(value = "/image")
public class ImageController {

    @RequestMapping(value = "/get",produces = MediaType.IMAGE_JPEG_VALUE)

    @ResponseBody

    public byte[] getImage(String imgname) throws IOException {

        System.out.println(imgname);
        String re_name=imgname;
        System.out.println(re_name);
        String tx="腾讯控股";
        if (imgname.equals(tx))
        {
            re_name="腾讯";
        }
        System.out.println(re_name);
        String imgpath = "C:\\server\\PyServer\\img\\"+re_name+".png";

        System.out.println(imgpath);

        File file = new File(imgpath);

        FileInputStream inputStream = new FileInputStream(file);

        byte[] bytes = new byte[inputStream.available()];

        inputStream.read(bytes, 0, inputStream.available());

        return bytes;

    }


}
