
//作者：张步云

package com.whu.data.pojo;

import lombok.Data;

@Data
public class SaveShare {
    String username;
    String sharenum;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSharenum() {
        return sharenum;
    }

    public void setSharenum(String sharenum) {
        this.sharenum = sharenum;
    }
}
