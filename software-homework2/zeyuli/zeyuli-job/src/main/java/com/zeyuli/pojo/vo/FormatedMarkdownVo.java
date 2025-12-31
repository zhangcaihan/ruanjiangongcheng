package com.zeyuli.pojo.vo;


import lombok.Data;

import java.util.List;

/**
 * 格式化markdown文档
 *
 * @author 李泽聿
 * @since 2025-12-11 15:57
 */
@Data
public class FormatedMarkdownVo {
    private List<Days> days;
}

@Data
class Items {
    private String time;
    private String title;
    private String description;
    private String cost;
    private String durationHours;
    private String attractions;
}

@Data
class Days {
    private int dayIndex;
    private String date;
    private String label;
    private List<Items> items;
}