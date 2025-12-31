package com.zeyuli.pojo.vo;


import lombok.Data;

import java.util.List;

/**
 * 逆编码地理位置
 *
 * @author 李泽聿
 * @since 2025-12-09 20:48
 */
@Data
public class ReverseCodingVo {

    private String status;
    private String info;
    private String infocode;
    private RegeocodeVo regeocode;
}

@Data
class StreetNumber {
    private String street;
    private String number;
    private String location;
    private String direction;
    private String distance;
}

@Data
class BusinessAreas {

    private String location;
    private String name;
    private String id;
}

@Data
class AddressComponent {
    private String country;
    private String province;
    private String city;
    private String citycode;
    private String district;
    private String adcode;
    private String township;
    private String towncode;
    private Neighborhood neighborhood;
    private Building building;
    private StreetNumber streetNumber;
    private List<BusinessAreas> businessAreas;
}

@Data
class Roads {
    private String id;
    private String name;
    private String direction;
    private String distance;
    private String location;
}

@Data
class Roadinters {
    private String direction;
    private String distance;
    private String location;
    private String first_id;
    private String first_name;
    private String second_id;
    private String second_name;
}

@Data
class Aois {

    private String id;
    private String name;
    private String adcode;
    private String location;
    private String area;
    private String distance;
    private String type;
}

