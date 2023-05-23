package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AgentSettDailyCollectExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public AgentSettDailyCollectExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria implements Serializable {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("Id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("Id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("Id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("Id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("Id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("Id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("Id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("Id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("Id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("Id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("Id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("Id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andAgentIdIsNull() {
            addCriterion("AgentId is null");
            return (Criteria) this;
        }

        public Criteria andAgentIdIsNotNull() {
            addCriterion("AgentId is not null");
            return (Criteria) this;
        }

        public Criteria andAgentIdEqualTo(Long value) {
            addCriterion("AgentId =", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotEqualTo(Long value) {
            addCriterion("AgentId <>", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdGreaterThan(Long value) {
            addCriterion("AgentId >", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdGreaterThanOrEqualTo(Long value) {
            addCriterion("AgentId >=", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdLessThan(Long value) {
            addCriterion("AgentId <", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdLessThanOrEqualTo(Long value) {
            addCriterion("AgentId <=", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdIn(List<Long> values) {
            addCriterion("AgentId in", values, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotIn(List<Long> values) {
            addCriterion("AgentId not in", values, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdBetween(Long value1, Long value2) {
            addCriterion("AgentId between", value1, value2, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotBetween(Long value1, Long value2) {
            addCriterion("AgentId not between", value1, value2, "agentId");
            return (Criteria) this;
        }

        public Criteria andCollectDateIsNull() {
            addCriterion("CollectDate is null");
            return (Criteria) this;
        }

        public Criteria andCollectDateIsNotNull() {
            addCriterion("CollectDate is not null");
            return (Criteria) this;
        }

        public Criteria andCollectDateEqualTo(Date value) {
            addCriterionForJDBCDate("CollectDate =", value, "collectDate");
            return (Criteria) this;
        }

        public Criteria andCollectDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("CollectDate <>", value, "collectDate");
            return (Criteria) this;
        }

        public Criteria andCollectDateGreaterThan(Date value) {
            addCriterionForJDBCDate("CollectDate >", value, "collectDate");
            return (Criteria) this;
        }

        public Criteria andCollectDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("CollectDate >=", value, "collectDate");
            return (Criteria) this;
        }

        public Criteria andCollectDateLessThan(Date value) {
            addCriterionForJDBCDate("CollectDate <", value, "collectDate");
            return (Criteria) this;
        }

        public Criteria andCollectDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("CollectDate <=", value, "collectDate");
            return (Criteria) this;
        }

        public Criteria andCollectDateIn(List<Date> values) {
            addCriterionForJDBCDate("CollectDate in", values, "collectDate");
            return (Criteria) this;
        }

        public Criteria andCollectDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("CollectDate not in", values, "collectDate");
            return (Criteria) this;
        }

        public Criteria andCollectDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("CollectDate between", value1, value2, "collectDate");
            return (Criteria) this;
        }

        public Criteria andCollectDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("CollectDate not between", value1, value2, "collectDate");
            return (Criteria) this;
        }

        public Criteria andCollectTypeIsNull() {
            addCriterion("CollectType is null");
            return (Criteria) this;
        }

        public Criteria andCollectTypeIsNotNull() {
            addCriterion("CollectType is not null");
            return (Criteria) this;
        }

        public Criteria andCollectTypeEqualTo(Byte value) {
            addCriterion("CollectType =", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeNotEqualTo(Byte value) {
            addCriterion("CollectType <>", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeGreaterThan(Byte value) {
            addCriterion("CollectType >", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("CollectType >=", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeLessThan(Byte value) {
            addCriterion("CollectType <", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeLessThanOrEqualTo(Byte value) {
            addCriterion("CollectType <=", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeIn(List<Byte> values) {
            addCriterion("CollectType in", values, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeNotIn(List<Byte> values) {
            addCriterion("CollectType not in", values, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeBetween(Byte value1, Byte value2) {
            addCriterion("CollectType between", value1, value2, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("CollectType not between", value1, value2, "collectType");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNull() {
            addCriterion("TotalAmount is null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNotNull() {
            addCriterion("TotalAmount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountEqualTo(Long value) {
            addCriterion("TotalAmount =", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotEqualTo(Long value) {
            addCriterion("TotalAmount <>", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThan(Long value) {
            addCriterion("TotalAmount >", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("TotalAmount >=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThan(Long value) {
            addCriterion("TotalAmount <", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThanOrEqualTo(Long value) {
            addCriterion("TotalAmount <=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIn(List<Long> values) {
            addCriterion("TotalAmount in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotIn(List<Long> values) {
            addCriterion("TotalAmount not in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountBetween(Long value1, Long value2) {
            addCriterion("TotalAmount between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotBetween(Long value1, Long value2) {
            addCriterion("TotalAmount not between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalCountIsNull() {
            addCriterion("TotalCount is null");
            return (Criteria) this;
        }

        public Criteria andTotalCountIsNotNull() {
            addCriterion("TotalCount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalCountEqualTo(Integer value) {
            addCriterion("TotalCount =", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountNotEqualTo(Integer value) {
            addCriterion("TotalCount <>", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountGreaterThan(Integer value) {
            addCriterion("TotalCount >", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("TotalCount >=", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountLessThan(Integer value) {
            addCriterion("TotalCount <", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountLessThanOrEqualTo(Integer value) {
            addCriterion("TotalCount <=", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountIn(List<Integer> values) {
            addCriterion("TotalCount in", values, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountNotIn(List<Integer> values) {
            addCriterion("TotalCount not in", values, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountBetween(Integer value1, Integer value2) {
            addCriterion("TotalCount between", value1, value2, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountNotBetween(Integer value1, Integer value2) {
            addCriterion("TotalCount not between", value1, value2, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitIsNull() {
            addCriterion("TotalAgentProfit is null");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitIsNotNull() {
            addCriterion("TotalAgentProfit is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitEqualTo(Long value) {
            addCriterion("TotalAgentProfit =", value, "totalAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitNotEqualTo(Long value) {
            addCriterion("TotalAgentProfit <>", value, "totalAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitGreaterThan(Long value) {
            addCriterion("TotalAgentProfit >", value, "totalAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitGreaterThanOrEqualTo(Long value) {
            addCriterion("TotalAgentProfit >=", value, "totalAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitLessThan(Long value) {
            addCriterion("TotalAgentProfit <", value, "totalAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitLessThanOrEqualTo(Long value) {
            addCriterion("TotalAgentProfit <=", value, "totalAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitIn(List<Long> values) {
            addCriterion("TotalAgentProfit in", values, "totalAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitNotIn(List<Long> values) {
            addCriterion("TotalAgentProfit not in", values, "totalAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitBetween(Long value1, Long value2) {
            addCriterion("TotalAgentProfit between", value1, value2, "totalAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTotalAgentProfitNotBetween(Long value1, Long value2) {
            addCriterion("TotalAgentProfit not between", value1, value2, "totalAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSettStatusIsNull() {
            addCriterion("SettStatus is null");
            return (Criteria) this;
        }

        public Criteria andSettStatusIsNotNull() {
            addCriterion("SettStatus is not null");
            return (Criteria) this;
        }

        public Criteria andSettStatusEqualTo(Byte value) {
            addCriterion("SettStatus =", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusNotEqualTo(Byte value) {
            addCriterion("SettStatus <>", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusGreaterThan(Byte value) {
            addCriterion("SettStatus >", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("SettStatus >=", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusLessThan(Byte value) {
            addCriterion("SettStatus <", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusLessThanOrEqualTo(Byte value) {
            addCriterion("SettStatus <=", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusIn(List<Byte> values) {
            addCriterion("SettStatus in", values, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusNotIn(List<Byte> values) {
            addCriterion("SettStatus not in", values, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusBetween(Byte value1, Byte value2) {
            addCriterion("SettStatus between", value1, value2, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("SettStatus not between", value1, value2, "settStatus");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("Remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("Remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("Remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("Remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("Remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("Remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("Remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("Remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("Remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("Remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("Remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("Remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("Remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("Remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRiskDayIsNull() {
            addCriterion("RiskDay is null");
            return (Criteria) this;
        }

        public Criteria andRiskDayIsNotNull() {
            addCriterion("RiskDay is not null");
            return (Criteria) this;
        }

        public Criteria andRiskDayEqualTo(Integer value) {
            addCriterion("RiskDay =", value, "riskDay");
            return (Criteria) this;
        }

        public Criteria andRiskDayNotEqualTo(Integer value) {
            addCriterion("RiskDay <>", value, "riskDay");
            return (Criteria) this;
        }

        public Criteria andRiskDayGreaterThan(Integer value) {
            addCriterion("RiskDay >", value, "riskDay");
            return (Criteria) this;
        }

        public Criteria andRiskDayGreaterThanOrEqualTo(Integer value) {
            addCriterion("RiskDay >=", value, "riskDay");
            return (Criteria) this;
        }

        public Criteria andRiskDayLessThan(Integer value) {
            addCriterion("RiskDay <", value, "riskDay");
            return (Criteria) this;
        }

        public Criteria andRiskDayLessThanOrEqualTo(Integer value) {
            addCriterion("RiskDay <=", value, "riskDay");
            return (Criteria) this;
        }

        public Criteria andRiskDayIn(List<Integer> values) {
            addCriterion("RiskDay in", values, "riskDay");
            return (Criteria) this;
        }

        public Criteria andRiskDayNotIn(List<Integer> values) {
            addCriterion("RiskDay not in", values, "riskDay");
            return (Criteria) this;
        }

        public Criteria andRiskDayBetween(Integer value1, Integer value2) {
            addCriterion("RiskDay between", value1, value2, "riskDay");
            return (Criteria) this;
        }

        public Criteria andRiskDayNotBetween(Integer value1, Integer value2) {
            addCriterion("RiskDay not between", value1, value2, "riskDay");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("CreateTime is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("CreateTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("CreateTime =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("CreateTime <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andCollectItemIsNull() {
            addCriterion("CollectItem is null");
            return (Criteria) this;
        }

        public Criteria andCollectItemIsNotNull() {
            addCriterion("CollectItem is not null");
            return (Criteria) this;
        }

        public Criteria andCollectItemEqualTo(String value) {
            addCriterion("CollectItem =", value, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemNotEqualTo(String value) {
            addCriterion("CollectItem <>", value, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemGreaterThan(String value) {
            addCriterion("CollectItem >", value, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemGreaterThanOrEqualTo(String value) {
            addCriterion("CollectItem >=", value, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemLessThan(String value) {
            addCriterion("CollectItem <", value, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemLessThanOrEqualTo(String value) {
            addCriterion("CollectItem <=", value, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemLike(String value) {
            addCriterion("CollectItem like", value, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemNotLike(String value) {
            addCriterion("CollectItem not like", value, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemIn(List<String> values) {
            addCriterion("CollectItem in", values, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemNotIn(List<String> values) {
            addCriterion("CollectItem not in", values, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemBetween(String value1, String value2) {
            addCriterion("CollectItem between", value1, value2, "collectItem");
            return (Criteria) this;
        }

        public Criteria andCollectItemNotBetween(String value1, String value2) {
            addCriterion("CollectItem not between", value1, value2, "collectItem");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion implements Serializable {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}