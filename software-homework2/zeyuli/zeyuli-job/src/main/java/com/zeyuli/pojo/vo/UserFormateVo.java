package com.zeyuli.pojo.vo;


import lombok.Data;

import java.time.LocalDate;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-11 16:14
 */
@Data
public class UserFormateVo {
    private String markdown;
    private String startCity;
    private String endCity;
    private LocalDate startDate;
    private LocalDate endDate;
    private String token;
}