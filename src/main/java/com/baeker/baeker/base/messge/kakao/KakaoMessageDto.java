package com.baeker.baeker.base.messge.kakao;

import lombok.Data;

@Data
public class KakaoMessageDto {

    private String objType;
    private String text;
    private String webUrl;
    private String mobileUrl;
    private String btnTitle;
}
