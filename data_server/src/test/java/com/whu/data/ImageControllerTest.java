
//作者：张步云

package com.whu.data;

import com.whu.data.controller.ImageController;
import com.whu.data.mapper.TeamMapper;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageControllerTest {

    @Autowired
    ImageController imageController;

    // 初始化
    @Before
    public void setUp() throws Exception {
        System.out.println("执行初始化");


    }

    @Test
    public void test() throws Exception {
        String sharename="jinglankeji";
        byte[] a =imageController.getImage(sharename);
        if (a!=null)
        {
            System.out.println("找到文件");
        }
        else
        {
            System.out.println("没有找到文件");
        }

    }



}
