package com.zeyuli.pojo.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-10 19:38
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Paths {
    private int distance;
    private int duration;
    private List<Steps> steps;
}
