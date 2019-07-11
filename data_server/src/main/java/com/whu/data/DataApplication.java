
//作者：张步云

package com.whu.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(DataApplication.class, args);
    }
}
