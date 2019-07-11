
//作者：张步云

package com.whu.data.pojo;

import lombok.Data;

@Data
public class ForecastInfo {
    String sharenum;
    float closeprice;


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
}


