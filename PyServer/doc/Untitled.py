#!/usr/bin/env python
# coding: utf-8

# In[6]:


#模型的使用
from sklearn.externals import joblib 
#格式为[['Close', 'HL_PCT', 'PCT_change', 'Volumn','Change','Sentiment']]
X_lately = [[3.58800000e+02,5.57413601e-01,-2.77932185e-01,9.10092200e+06,-9.90000000e-01,5.00000000e-01]]

rfc2 = joblib.load('E:/linear.pkl')
print(rfc2.predict(X_lately))


# In[ ]:




