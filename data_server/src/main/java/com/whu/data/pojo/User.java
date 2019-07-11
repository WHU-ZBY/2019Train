
//作者：张步云

package com.whu.data.pojo;

import lombok.Data;

@Data
public class User {
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
