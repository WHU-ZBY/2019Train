#!/usr/bin/env python
# -*- coding:utf-8 -*-
#作者：张步云

from mySpider import Spider
from printWordCloud import PrintWordCloud
from forecast import  Forecast
import sys

def start(sharename,sharenum):


    a=Spider(sharename)
    a.writeDoc()
    print("调用spider结束")
    b=PrintWordCloud(sharename,sharenum)
    b.drawWordCloud()
    print("生成词云图结束")
    print("开始调用Forecast")
    c=Forecast(sharename,sharenum)
    c.useModel()


if __name__ == '__main__':

    start(sys.argv[1],sys.argv[2])
    start(sharename,sharenum)
