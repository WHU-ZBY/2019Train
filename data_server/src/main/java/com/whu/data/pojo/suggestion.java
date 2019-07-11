
//作者：张步云

package com.whu.data.pojo;

import lombok.Data;

@Data
public class suggestion {
    String username;
    String content;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
