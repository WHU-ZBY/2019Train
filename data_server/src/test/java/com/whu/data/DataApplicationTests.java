package com.whu.data;

import com.whu.data.mapper.TeamMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataApplicationTests
{

    @Autowired
    private TeamMapper teamMapper;
    @Test
    public void contextLoads()
    {


    }

}
