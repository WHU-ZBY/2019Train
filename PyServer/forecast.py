#!/usr/bin/env python
# -*- coding:utf-8 -*-
#作者：曾誉
import pandas_datareader  as data
import time
import json
import urllib.request
import urllib.parse
import urllib3
import pandas
import requests
import datetime
from pandas import to_datetime
from datetime import datetime
from sklearn.externals import joblib
import re
import os
import os.path
import codecs
import pymysql

class Forecast:
    def __init__(self,sharename,sharenum):
        self.sharename=sharename
        self.folderPath=filepath="C:/server/PyServer/data/"+self.sharename+'/'
        self.sharenum=sharenum
        self.closeprice=''
        self.num=1.111
        self.now_price=''
        urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

    def getAccess_token(self):
        url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials"
        values = {
            'host': 'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials',
            'client_id': '你的AK密钥',
            'client_secret': '你的SK密钥'
        }
        host = 'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=6HiMTQ0tgFVYnGArVeNE5njq&client_secret=452BGOrxbVfpFlO8S2ZhEmV2hOVUYs6I'
        request = urllib.request.Request(host)
        request.add_header('Content-Type', 'application/json; charset=UTF-8')
        response = urllib.request.urlopen(request)
        content = response.read()
        rdata = json.loads(content)
        return rdata['access_token']

    def getDigit(self,text):
        cop = re.compile("[^0-9]")  # 匹配数字
        string1 = cop.sub('', text)  # 将string中匹配到的字符替换成空字��?
        return string1

    # 得到文件夹中的所有文件的情感倾向
    def getSentiments(self,path):
        title_list = []
        label_prediction = []
        date = []
        files = os.listdir(path)
        for file in files:
            # 准确获取一个txt的位置，利用字符串的拼接
            txt_path = path + file
            # 判断文件是否为空
            if (os.path.getsize(txt_path) != 0):
                # 把结果保存了在contents��?
                f = codecs.open(txt_path, 'r', encoding='utf-8')
                content = f.read(1000)
                # print(content)
                f.close()
                date.append(self.getDigit(file))
                title_list.append(content)
        access_token = self.getAccess_token()
        http = urllib3.PoolManager()
        url = 'https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify?access_token=' + access_token
        for i in range(len(title_list)):
            if (i + 1) % 5 == 0:
                time.sleep(1)
            params = {'text': title_list[i]}
            # 进行json转换的时候，encode编码格式不指定也不会出错
            encoded_data = json.dumps(params).encode('GBK')
            request = http.request('POST',
                                   url,
                                   body=encoded_data,
                                   headers={'Content-Type': 'application/json'})
            # 对返回的byte字节进行处理。Python3输出位串，而不是可读的字符串，需要进行转��?
            # 注意编码格式
            result = str(request.data, 'GBK')
            a = json.loads(result)
            # a2 =eval(result)
            a1 = a['items'][0]
            # labels.append(a1['sentiment'])#分类结果

            label_prediction.append(a1['positive_prob'])  # 展示的概��?

        segmentDataFrame = pandas.DataFrame({
            'Sentiment': label_prediction,
            'CDate': date
        })
        return segmentDataFrame



    # 获取当天股票数据
    def getShareInfo(self,sharenum):
        market = sharenum[-2:]
        print(market)
        sh = 'sh'
        sz = 'sz'
        hk = 'hk'
        url = ''
        if market == 'sh':
            url = 'http://img1.money.126.net/data/hs/kline/day/history/2019/0' + sharenum[0:6] + '.json'
        elif market == 'sz':
            url = 'http://img1.money.126.net/data/hs/kline/day/history/2019/1' + sharenum[0:6] + '.json'
        elif market == 'hk':
            url= 'http://img1.money.126.net/data/hk/kline/day/history/2019/' + sharenum[0:5] + '.json'
        else:
            print("无法获取数据")

        req = urllib.request.Request(url, headers={
            'Connection': 'Keep-Alive',
            'Accept': 'text/html, application/xhtml+xml, */*',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko'
        })
        opener = urllib.request.urlopen(req)
        page = opener.read()
        c = page.decode(encoding='utf-8')
        c = json.loads(c)
        print(c)
        print(type(c))
        data_arry = c["data"]
        print(data_arry[-1])
        d=data_arry[-1]
        e=[]
        e.append(d)
        return e


    # 第一个参数为给出的数据，形式为[['Date','Open','Close','High','Low','Volumn','Change']]，格式为dataframe��?
    # 第二个参数为文件夹路��?
    def useModel(self):
        print("useModel方法开始执行")
        date = time.strftime("%Y-%m-%d", time.localtime())
        df = self.getShareInfo(self.sharenum)
        print(df)
        print(type(df))
        # dDf_dict = {'Date':[date]}
        #df.insert(0, 'CDate', [to_datetime(date, format="%Y/%m/%d")])
        print("转换")
        df = pandas.DataFrame(df,columns = ['Date','Open','Close','High','Low','Volumn','Change']) #list格式
        print(df)

        # 构造两个新的列

        # HL_PCT为股票最高价变化百分��?

        df['HL_PCT'] = (df['High'] - df['Close']) / df['Close'] * 100.0

        # HL_PCT为股票收盘价与开盘价的变化百分比

        df['PCT_change'] = (df['Close'] - df['Open']) / df['Open'] * 100.0

        # 获取情感分析结果��?
        print(df)
        sdf = self.getSentiments(self.folderPath)


        sdf['CDate'] = to_datetime(sdf['CDate'], format="%Y/%m/%d")

        # 下面为真正用到的特征字段

       # # 合并两表，左连接，以第一个表为基��?
        #添加情感特征值
        df.insert(0, 'Sentiment', sdf['Sentiment'][0])

        # df['Sentiment'] = sdf['Sentiment']
        # df = pandas.merge(df, sdf, how='left', on=['CDate'])

        # df['CDate'] = to_datetime(df['CDate'],format="%Y/%m/%d")
        print(df)
        df = df[['Close', 'HL_PCT', 'PCT_change', 'Volumn', 'Sentiment']]
        print(df)
        # 若无新闻，默认情感为中��?
        df.fillna(0.5, inplace=True)

        rfc2 = joblib.load('C:/server/PyServer/doc/linear.pkl')

        self.closeprice=float(rfc2.predict(df)[0])
        self.now_price=float(df["PCT_change"][0])
        print(self.now_price)
        self.writeMysql()
        return rfc2.predict(df)[0]



    def open_connect(self):
        try:

            # 创建数据库连��?
            dbconnect = pymysql.connect("localhost", "root","123456","summertrain",charset='utf8')

            return dbconnect

        except:

            print('打开连接异常')

            return None

    def insert_data(self,dbconnect, sql):

        # 使用cursor()方法获取操作游标

        cursor = dbconnect.cursor()

        try:

            # 执行sql语句

            cursor.execute(sql)

            # 提交到数据库执行

            dbconnect.commit()

            return 1

        except:

            # 如果发生错误则回��?

            dbconnect.rollback()

            print('请检查sql语法是否正确')

            return 0



    def close_connect(self,dbconnect):

        if dbconnect != None:
            dbconnect.close()

    def writeMysql(self):
        dbconnect = self.open_connect()
        print(self.closeprice)
        new_sharenum=self.sharenum[:-3]
        sql = "INSERT INTO forecast(sharenum,closeprice,nowprice) VALUES ('%s','%s','%s')"\
              "ON DUPLICATE KEY UPDATE closeprice='%s',nowprice='%s'" % (new_sharenum,self.closeprice,self.now_price,self.closeprice,self.now_price)

        if dbconnect != None:
            res =self.insert_data(dbconnect,sql)
            print(res)
            self.close_connect(dbconnect)

if __name__ == '__main__':
    a= Forecast("腾讯",'00700.hk')
    a.getShareInfo('00700.hk')
    a.useModel()
