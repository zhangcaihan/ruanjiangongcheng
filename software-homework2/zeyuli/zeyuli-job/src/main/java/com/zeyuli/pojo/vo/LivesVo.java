package com.zeyuli.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-09 20:16
 */
@Getter
@Setter
public class LivesVo {

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("adcode")
    private String adcode;

    @JsonProperty("weather")
    private String weather;

    @JsonProperty("temperature")
    private String temperature;

    @JsonProperty("winddirection")
    private String winddirection;

    @JsonProperty("windpower")
    private String windpower;

    @JsonProperty("humidity")
    private String humidity;

    // 关键修改：将 LocalDate 改为 String
    @JsonProperty("reporttime")
    private String reporttime;

    @JsonProperty("temperature_float")
    private String temperature_float;

    @JsonProperty("humidity_float")
    private String humidity_float;

}