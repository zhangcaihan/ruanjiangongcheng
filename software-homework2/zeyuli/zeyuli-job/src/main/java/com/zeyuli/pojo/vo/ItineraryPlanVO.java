package com.zeyuli.pojo.vo;

import com.zeyuli.pojo.bo.POI;
import com.zeyuli.pojo.bo.Route;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 行程规划响应对象
 * 包含完整的行程信息，用于前端展示
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@Data
public class ItineraryPlanVO {
    private String planName; // 行程名称
    private String city; // 目的地城市
    private int days; // 旅行天数
    private double totalBudget; // 总预算
    private double estimatedCost; // 预估总费用
    private String accommodationSuggestion; // 住宿建议
    private List<DailyItinerary> dailyItineraries; // 每日行程
    private Map<String, Object> additionalInfo; // 附加信息（如天气提醒、注意事项等）
    private String planType; // 行程类型（价格锁定、人格化等）
    
    /**
     * 每日行程
     */
    @Data
    public static class DailyItinerary {
        private int day; // 第几天
        private List<POI> attractions; // 当天景点
        private List<Route> routes; // 路线
        private double dailyCost; // 当日费用
        private String weather; // 天气情况
        private List<String> suggestions; // 当日建议
    }
    
    /**
     * 费用明细
     */
    @Data
    public static class CostBreakdown {
        private double attractionTickets; // 门票费用
        private double transportation; // 交通费用
        private double accommodation; // 住宿费用
        private double food; // 餐饮费用
        private double other; // 其他费用
    }
}