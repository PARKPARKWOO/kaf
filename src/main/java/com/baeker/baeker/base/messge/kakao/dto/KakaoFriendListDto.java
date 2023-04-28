package com.baeker.baeker.base.messge.kakao.dto;

import lombok.Data;

@Data
public class KakaoFriendListDto {

    private String id;
    private String uuid;
    private boolean favorite;
    private String profile_nickname;
    private String profile_thumbnail_image;

}
