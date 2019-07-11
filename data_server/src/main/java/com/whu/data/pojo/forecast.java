
//作者：张步云

package com.whu.data.pojo;


import lombok.Data;

@Data
public class forecast {
    String sharenum;
    float closeprice;
    float nowprice;

    public String getSharenum() {
        return sharenum;
    }

    public void setSharenum(String sharenum) {
        this.sharenum = sharenum;
    }

    public float getCloseprice() {
        return closeprice;
    }

    public void setCloseprice(float closeprice) {
        this.closeprice = closeprice;
    }

    public float getNowprice() {
        return nowprice;
    }

    public void setNowprice(float nowprice) {
        this.nowprice = nowprice;
    }
}
