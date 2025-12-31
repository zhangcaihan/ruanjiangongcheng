package com.zeyuli.pojo.bo;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 学生专属线路实体类
 * 用于存储学生专属旅行线路的相关信息
 * 
 * @author 李泽聿
 * @since 2025-11-01
 */
@Data
public class StudentRoute {
    
    /**
     * 线路唯一标识
     */
    private String routeId;
    
    /**
     * 线路名称
     */
    private String routeName;
    
    /**
     * 线路描述
     */
    private String description;
    
    /**
     * 所在城市
     */
    private String city;
    
    /**
     * 行程天数
     */
    private int days;
    
    /**
     * 适合学生类型（大学生、中学生等）
     */
    private String studentType;
    
    /**
     * 预算范围（最低价）
     */
    private double minBudget;
    
    /**
     * 预算范围（最高价）
     */
    private double maxBudget;
    
    /**
     * 平均费用
     */
    private double averageCost;
    
    /**
     * 景点列表
     */
    private List<POI> attractions;
    
    /**
     * 交通信息
     */
    private List<Route> routes;
    
    /**
     * 推荐理由
     */
    private List<String> recommendedReasons;
    
    /**
     * 学生专属优惠信息
     */
    private List<DiscountInfo> studentDiscounts;
    
    /**
     * 线路特点标签
     */
    private List<String> tags;
    
    /**
     * 推荐季节
     */
    private List<String> recommendedSeasons;
    
    /**
     * 线路类型（文化、自然、美食等）
     */
    private String routeType;
    
    /**
     * 适合群体大小（个人、小团体、班级等）
     */
    private String groupSize;
    
    /**
     * 累计浏览次数
     */
    private int viewCount;
    
    /**
     * 收藏次数
     */
    private int favoriteCount;
    
    /**
     * 评分
     */
    private double rating;
    
    /**
     * 评分人数
     */
    private int ratingCount;
    
    /**
     * 是否热门
     */
    private boolean isHot;
    
    /**
     * 是否推荐
     */
    private boolean isRecommended;
    
    /**
     * 每日行程安排
     */
    private List<DailySchedule> dailySchedules;
    
    /**
     * 住宿建议
     */
    private AccommodationInfo accommodationInfo;
    
    /**
     * 创建时间
     */
    private long createTime;
    
    /**
     * 更新时间
     */
    private long updateTime;
    
    /**
     * 额外信息
     */
    private Map<String, Object> additionalInfo;
    
    /**
     * 学生专属优惠信息实体类
     */
    @Data
    public static class DiscountInfo {
        
        /**
         * 优惠名称
         */
        private String discountName;
        
        /**
         * 优惠描述
         */
        private String description;
        
        /**
         * 折扣力度（如 8.5折）
         */
        private double discountValue;
        
        /**
         * 适用景点/服务
         */
        private String applicableTo;
        
        /**
         * 所需凭证
         */
        private String requiredDocuments;
        
        /**
         * 有效期开始
         */
        private long validFrom;
        
        /**
         * 有效期结束
         */
        private long validTo;
        
        /**
         * 是否可用
         */
        private boolean isValid;
    }
    
    /**
     * 每日行程安排实体类
     */
    @Data
    public static class DailySchedule {
        
        /**
         * 第几天
         */
        private int day;
        
        /**
         * 上午行程
         */
        private SchedulePart morning;
        
        /**
         * 下午行程
         */
        private SchedulePart afternoon;
        
        /**
         * 晚上行程
         */
        private SchedulePart evening;
        
        /**
         * 全天推荐景点
         */
        private List<POI> recommendedAttractions;
        
        /**
         * 当日交通建议
         */
        private String transportationSuggestion;
        
        /**
         * 当日预算
         */
        private double dailyBudget;
        
        /**
         * 注意事项
         */
        private List<String> notices;
    }
    
    /**
     * 行程时段实体类
     */
    @Data
    public static class SchedulePart {
        
        /**
         * 时间段描述
         */
        private String timeRange;
        
        /**
         * 活动内容
         */
        private String activity;
        
        /**
         * 相关景点
         */
        private List<POI> relatedAttractions;
        
        /**
         * 预计时长（分钟）
         */
        private int estimatedDuration;
    }
    
    /**
     * 住宿信息实体类
     */
    @Data
    public static class AccommodationInfo {
        
        /**
         * 推荐住宿类型
         */
        private String accommodationType;
        
        /**
         * 住宿位置建议
         */
        private String locationSuggestion;
        
        /**
         * 价格区间（最低）
         */
        private double minPrice;
        
        /**
         * 价格区间（最高）
         */
        private double maxPrice;
        
        /**
         * 学生优惠住宿推荐
         */
        private List<StudentFriendlyHotel> studentFriendlyHotels;
        
        /**
         * 住宿注意事项
         */
        private List<String> notices;
    }
    
    /**
     * 学生友好型酒店实体类
     */
    @Data
    public static class StudentFriendlyHotel {
        
        /**
         * 酒店名称
         */
        private String hotelName;
        
        /**
         * 酒店地址
         */
        private String address;
        
        /**
         * 学生价格
         */
        private double studentPrice;
        
        /**
         * 原价
         */
        private double originalPrice;
        
        /**
         * 折扣力度
         */
        private double discount;
        
        /**
         * 距离市中心距离
         */
        private double distanceToCityCenter;
        
        /**
         * 设施描述
         */
        private List<String> facilities;
        
        /**
         * 预订链接
         */
        private String bookingLink;
    }
}