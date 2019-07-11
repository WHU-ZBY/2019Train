
//作者：张步云

package com.whu.data.pojo;


import lombok.Data;

@Data
public class IsPrintData {
    String sharenum;
    int isPrint;

    public String getSharenum() {
        return sharenum;
    }

    public void setSharenum(String sharenum) {
        this.sharenum = sharenum;
    }

    public int getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(int isPrint) {
        this.isPrint = isPrint;
    }
}
