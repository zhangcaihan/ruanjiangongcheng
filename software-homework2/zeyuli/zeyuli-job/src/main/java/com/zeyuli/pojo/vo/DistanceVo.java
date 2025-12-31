package com.zeyuli.pojo.vo;


/**
 * 获取距离
 *
 * @author 李泽聿
 * @since 2025-12-09 21:32
 */

import lombok.Data;
import org.apache.ibatis.annotations.Results;

import java.util.List;

@Data
public class DistanceVo {

    private String status;
    private String info;
    private String infocode;
    private String count;
    private List<DistanceResultsVo> results;
}
