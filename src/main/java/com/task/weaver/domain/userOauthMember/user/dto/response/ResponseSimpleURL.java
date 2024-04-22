package com.task.weaver.domain.userOauthMember.user.dto.response;

import java.net.URL;
import lombok.Getter;

@Getter
public class ResponseSimpleURL {

    private URL updateURL;

    public ResponseSimpleURL(URL updateURL) {
        this.updateURL = updateURL;
    }
}
