package com.zeyuli.pojo;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 用户行程计划
 *
 * @author 李泽聿
 * @since 2025-10-22 08:20
 */
@Data
public class UserItineraryPlan {
    @NotNull
    private String endCity;
    @NotNull
    private String startCity;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}
