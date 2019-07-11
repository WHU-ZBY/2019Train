
//作者：张步云

package com.whu.data.pojo;

import lombok.Data;

@Data
public class SaveShareInfo {
    public String shareNum;
    public String shareName;
    public String market;
    public float forecast;

    public String getShareNum() {
        return shareNum;
    }

    public void setShareNum(String shareNum) {
        this.shareNum = shareNum;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public float getForecast() {
        return forecast;
    }

    public void setForecast(float forecast) {
        this.forecast = forecast;
    }
}
