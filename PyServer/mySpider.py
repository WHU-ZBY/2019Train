#!/usr/bin/env python
# -*- coding:utf-8 -*-

#作者：张步云、尚志强
import requests  # 用于获取网页
from bs4 import BeautifulSoup  # 用于分析网页
import re  # 用于在网页中搜索我们需要的关键字
import os
import os.path
import pandas
import time
import json
import urllib.request
import urllib.parse

class Spider:
    def __init__(self,mystr):
        self.mystr=mystr
        self.file_path = "C:/server/PyServer/data/" + mystr


    #创建文件夹
    def mkdir(self):

        filepath = self.file_path.strip()
        filepath = filepath.rstrip("\\")
        isExists = os.path.exists(filepath)
        if not isExists:
            # 如果不存在则创建目录
            # 创建目录操作函数
            os.makedirs(filepath)

            print(filepath + ' 创建成功')
            return True
        else:
            # 如果目录存在则不创建，并提示目录已存在
            print(filepath + ' 目录已存在')
            return False

    # 获取连接表
    def getLinkList(self,urls):
        new_list = []
        html = requests.get(urls)  # 这是使用requests库的get方法，获取网页元代码
        soup = BeautifulSoup(html.text, 'html.parser')
        newslist = soup.find_all(target="_blank")  # 注意find_all返回的是一个列表
        print(newslist)
        for news in newslist:  # 写一个循环
            news_link = news['href']  # a标签中包含一个字典，用['href']取出href对应的值
            new_list.append(news_link)
        s = pandas.Series(new_list)
        new_s = s.drop_duplicates()
        return new_s

    # 获取一个子连接中的数据
    def getTextOfSonLink(self,sonlink):
        re_str = re.compile('<[^>]*>')
        try:
            html_son = requests.get(sonlink)
            html_son.raise_for_status()
            html_son.encoding = "GB2312"
            html_text = html_son.text
            soup_son = BeautifulSoup(html_text, "html.parser")
            result = soup_son.select('.article-t p')
            result_title = soup_son.select('.article h1')
            re_result_title = re_str.sub('', str(result_title[0]))
            result_str = ''
            for item in result:
                re_item = re_str.sub('', str(item))
                # re_item = str(item).replace("<*>", '').replace("/n", '')
                result_str = result_str + re_item
            return result_str, re_result_title

        except:
            print("发生错误")

    def writeDoc(self):
        num=1
        date = time.strftime("%Y-%m-%d", time.localtime())
        self.mkdir()
        while (num < 2):
            baseurl = 'http://search.cs.com.cn/search?searchscope=DOCTITLE&channelid=215308&searchword=' + self.mystr + '&page=' + str(
                num)
            son_link_list = self.getLinkList(baseurl)
            print(son_link_list)
            num = num + 1
            output_file = ''
            try:
                for new_son in son_link_list:
                    print(new_son)
                    son_result = self.getTextOfSonLink(new_son)
                    output_file = "C:/server/PyServer/data/" + self.mystr + '/' + date + ".txt"
                    with open(output_file, 'a', encoding='utf-8') as f:
                        f.write(str(son_result))

            except:
                print("wrong")


if __name__ == '__main__':

    a=Spider("京蓝科技")
    a.writeDoc()
