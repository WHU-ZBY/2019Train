#!/usr/bin/env python
# -*- coding:utf-8 -*-

#作者：张步云
import os
import os.path
import  jieba
import numpy
from wordcloud import WordCloud
import matplotlib.pyplot as plt
import matplotlib.colors as colors
import requests  # 用于获取网页
from bs4 import BeautifulSoup  # 用于分析网页
import re  # 用于在网页中搜索我们需要的关键字
import pandas
import codecs

class PrintWordCloud:
    def __init__(self,mystr,sharenum):
        self.stop_words_file = "C:/server/PyServer/doc/stopwords.txt"
        self.mystr=mystr
        self.sharenum=sharenum[:-3]

    def get_custom_stopwords(self):
        with open(self.stop_words_file) as f:
            stopwords = f.read()
        stopwords_list = stopwords.split('\n')
        custom_stopwords_list = [i for i in stopwords_list]
        return custom_stopwords_list

    def drawWordCloud(self):
        filePaths = []
        fileContents = []
        # 输入名成
        dataUrl = 'C:/server/PyServer/data/' + self.mystr

        for root, dirs, files in os.walk(dataUrl):
            for name in files:
                filePath = os.path.join(root, name)
                filePaths.append(filePath)
                f = codecs.open(filePath, 'r', 'utf-8')
                fileContent = f.read()
                f.close()
                fileContents.append(fileContent)

        corpos = pandas.DataFrame({
            'filePath': filePaths,
            'fileContent': fileContents
        })



        segments = []
        filePaths = []

        for index, row in corpos.iterrows():
            filePath = row['filePath']
            fileContent = row['fileContent']
            # 接着调用cut方法，对文件内容进行分词
            segs = jieba.cut(fileContent)
            # 接着遍历每个分词，和分词对应的文件路径一起，把它加到两列中
            for seg in segs:
                segments.append(seg)
                filePaths.append(filePath)

        # 最后我们把得到的结果存在一个数据框中
        segmentDataFrame = pandas.DataFrame({
            'segment': segments,
            'filePath': filePaths
        })



        segStat = segmentDataFrame.groupby(
            by='segment'
        )['segment'].agg({'计数': numpy.size}).reset_index().sort_index(
            by=["计数"], ascending=False
        )


        stopwords = self.get_custom_stopwords()
        fSegStat = segStat[~segStat.segment.isin(stopwords)]

        color = ['#feca57', '#ff6b6b', '#0abde3', '#1dd1a1', '#00d2d3']
        colormap = colors.ListedColormap(color)
        wordcloud = WordCloud(
            font_path='./fonts/simhei.ttf',
            background_color='#333333',
            width=600,
            height=400,
            max_font_size=60,
            colormap=colormap,

        )
        # WordCloud方法接受一个字典结构的输入，我们前面整理出来的词频统计结果是数据框的形式
        # ，因此需要转换，转换的方法，首先把分词设置为数据框的索引，然后在调用一个to_dict()的方法，就可以转换为字典的机构
        words = fSegStat.set_index('segment').to_dict()
        # #接着调用fit_words方法来调用我们的词频
        wycloud = wordcloud.fit_words(words['计数'])
        # 绘图
        plt.imshow(wycloud)
        imgpath = "C:/server/PyServer/img/" + self.sharenum + '.png'
        wordcloud.to_file(imgpath)


#
# if __name__ == '__main__':
#
#     a=PrintWordCloud("腾讯")
#     a.drawWordCloud()
