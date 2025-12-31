package com.zeyuli.pojo.vo;


import lombok.Data;

import java.util.List;

/**
 * 获取所在城市列表返回结果实体类
 *
 * @author 李泽聿
 * @since 2025-12-10 18:39
 */

@Data
public class AttractionsByCityResultVo {
    private SuggestionVo suggestion;
    private String count;
    private String infocode;
    private List<Pois> pois;
    private String status;
    private String info;
}
