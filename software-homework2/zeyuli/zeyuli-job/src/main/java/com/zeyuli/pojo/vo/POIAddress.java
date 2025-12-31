package com.zeyuli.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class POIAddress {

    private SuggestionVo suggestion;
    private String count;
    private String infocode;
    private List<Pois> pois;
    private String status;
    private String info;
}

