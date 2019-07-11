#!/usr/bin/env python
# coding: utf-8

# In[49]:


#百度api
import json
import urllib.request
import urllib.parse
import urllib3
import json
import time
import os
import codecs
import pandas
import re

# 禁用安全请求警告
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)  

#获取access_token的值
def getAccess_token():
    url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials"
    values = {
    'host':'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials',
    'client_id':'你的AK密钥',
    'client_secret' : '你的SK密钥'
    }
    host = 'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=6HiMTQ0tgFVYnGArVeNE5njq&client_secret=452BGOrxbVfpFlO8S2ZhEmV2hOVUYs6I'
    request = urllib.request.Request(host)
    request.add_header('Content-Type', 'application/json; charset=UTF-8')
    response = urllib.request.urlopen(request)
    content = response.read()
    rdata = json.loads(content)
    return rdata['access_token']

def getDigit(text):
    cop = re.compile("[^0-9]") # 匹配数字
    string1 = cop.sub('', text) #将string中匹配到的字符替换成空字符
    return string1
#得到文件夹中的所有文件的情感倾向
def getSentiments(path):
    title_list = []
    label_prediction =[]
    date = []
    files = os.listdir(path) 
    for file in files:
            #准确获取一个txt的位置，利用字符串的拼接
            txt_path =path+file
            #判断文件是否为空
            if(os.path.getsize(txt_path)!=0):
                #把结果保存了在contents中
                f = codecs.open(txt_path,'r',encoding='utf-8')
                content = f.read(1000)
                #print(content)
                f.close()
                date.append(getDigit(file))
                title_list.append(content)
    access_token= getAccess_token()
    http=urllib3.PoolManager()
    url='https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify?access_token='+access_token
    for i in range(len(title_list)):
        if (i+1)%5==0:
            time.sleep(1)
        params={'text':title_list[i]}
        #进行json转换的时候，encode编码格式不指定也不会出错
        encoded_data = json.dumps(params).encode('GBK')
        request=http.request('POST', 
                              url,
                              body=encoded_data,
                              headers={'Content-Type':'application/json'})
        #对返回的byte字节进行处理。Python3输出位串，而不是可读的字符串，需要进行转换 
        #注意编码格式
        result = str(request.data,'GBK')
        a =json.loads(result)
        #a2 =eval(result)
        a1 =a['items'][0]
        #labels.append(a1['sentiment'])#分类结果
        
        label_prediction.append(a1['positive_prob'])#展示的概率
        
    segmentDataFrame=pandas.DataFrame({
       'Sentiment':label_prediction,
       'Date':date
    })    
    return segmentDataFrame


sdf = getSentiments('E:/腾讯/')
#print(sdf)


# In[70]:


get_ipython().run_line_magic('matplotlib', 'inline')
import quandl
import math
import numpy as np
from sklearn import preprocessing, model_selection, svm
from sklearn.linear_model import LinearRegression
from matplotlib import style
import datetime
import pandas as pd
import json
from pandas import to_datetime
from datetime import datetime

from sklearn import datasets
from sklearn.externals import joblib

#数据预处理
datas = pd.read_json("http://img1.money.126.net/data/hk/kline/day/history/2018/00700.json",encoding="utf-8", orient='records')
#数据转换为list
a = datas.to_dict(orient="list")
#提取data成为dataframe格式
df = pd.DataFrame(a['data'],columns = ['Date','Open','Close','High','Low','Volumn','Change'])

datas = pd.read_json("http://img1.money.126.net/data/hk/kline/day/history/2019/00700.json?tdsourcetag=s_pctim_aiomsg",encoding="utf-8", orient='records')
#数据转换为list
b = datas.to_dict(orient="list")
#提取data成为dataframe格式
df1 = pd.DataFrame(b['data'],columns = ['Date','Open','Close','High','Low','Volumn','Change'])

df = pd.concat([df, df1])


# 修改matplotlib样式
style.use('ggplot')


#获取谷歌股票数据，以dataframe的类型保存
#df = quandl.get('WIKI/AAPL')
#print(df)


# 定义预测列变量，它存放研究对象的标签名

forecast_col = 'Close'

# 定义预测天数，这里设置为所有数据量长度的1%

#forecast_out = int(math.ceil(0.01*len(df)))
forecast_out = 1

# 除权后的开盘价、最高价、最低价、收盘价、成交量

#df = df[['Adj. Open', 'Adj. High', 'Adj. Low', 'Adj. Close', 'Adj. Volume']]


# 构造两个新的列

# HL_PCT为股票最高价与最低价的变化百分比

df['HL_PCT'] = (df['High'] - df['Close']) / df['Close'] * 100.0

# HL_PCT为股票收盘价与开盘价的变化百分比

df['PCT_change'] = (df['Close'] - df['Open']) / df['Open'] * 100.0


# 下面为真正用到的特征字段

#合并两表，左连接

df = pd.merge(df, sdf, how='left', on=['Date'])
df['Date'] = to_datetime(df['Date'],format="%Y/%m/%d")


df = df[['Close', 'HL_PCT', 'PCT_change', 'Volumn','Change','Sentiment']]

#print(df)


# 因为scikit-learn并不会处理空数据，需要把为空的数据都设置为一个比较难出现的值，这里取-9999，

df.fillna(0.5, inplace=True)

# 用label代表该字段，是预测结果

# 通过让与Adj. Close列的数据往前移动1%行来表示    只有预测后的

df['label'] = df[forecast_col].shift(-forecast_out)


#删除label列
X = np.array(df.drop(['label'], 1))

#标准化，使均值为0，方差为1
#X = preprocessing.scale(X)

#预测天数之后
X_lately = X[-forecast_out:]
print(len(X_lately))
print(X_lately)
#预测天数之前
X = X[:-forecast_out]

print(len(X))
print(X)
print()
#删除最后一行
df.dropna(inplace=True)

print(df)
y = np.array(df['label'])


#线性回归
#分为测试集和训练集
X_train, X_test, y_train, y_test = model_selection.train_test_split(X, y, test_size=0.2)


#计算时使用所有的cpu
clf = LinearRegression(n_jobs=-1) # LinearRegression 97.7%

#clf = svm.SVR() # Support vector machine 79.5%

#clf = svm.SVR(kernel='poly') # Support vector machine 68.5%

clf.fit(X_train, y_train) # Here, start training

accuracy = clf.score(X_test, y_test) # Test an get a score for the classfier

forecast_set = clf.predict(X_lately)
#save model
joblib.dump(clf, 'E:/linear.pkl')
#load model
rfc2 = joblib.load('E:/linear.pkl')
print(rfc2.predict(X_lately))

print(forecast_set, accuracy)


# In[ ]:




