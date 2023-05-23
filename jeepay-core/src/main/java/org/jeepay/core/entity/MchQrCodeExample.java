package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MchQrCodeExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public MchQrCodeExample() {
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

        public Criteria andMchIdIsNull() {
            addCriterion("MchId is null");
            return (Criteria) this;
        }

        public Criteria andMchIdIsNotNull() {
            addCriterion("MchId is not null");
            return (Criteria) this;
        }

        public Criteria andMchIdEqualTo(Long value) {
            addCriterion("MchId =", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdNotEqualTo(Long value) {
            addCriterion("MchId <>", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdGreaterThan(Long value) {
            addCriterion("MchId >", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MchId >=", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdLessThan(Long value) {
            addCriterion("MchId <", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdLessThanOrEqualTo(Long value) {
            addCriterion("MchId <=", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdIn(List<Long> values) {
            addCriterion("MchId in", values, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdNotIn(List<Long> values) {
            addCriterion("MchId not in", values, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdBetween(Long value1, Long value2) {
            addCriterion("MchId between", value1, value2, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdNotBetween(Long value1, Long value2) {
            addCriterion("MchId not between", value1, value2, "mchId");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNull() {
            addCriterion("AppId is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("AppId is not null");
            return (Criteria) this;
        }

        public Criteria andAppIdEqualTo(String value) {
            addCriterion("AppId =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotEqualTo(String value) {
            addCriterion("AppId <>", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThan(String value) {
            addCriterion("AppId >", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("AppId >=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThan(String value) {
            addCriterion("AppId <", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThanOrEqualTo(String value) {
            addCriterion("AppId <=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLike(String value) {
            addCriterion("AppId like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotLike(String value) {
            addCriterion("AppId not like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(List<String> values) {
            addCriterion("AppId in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(List<String> values) {
            addCriterion("AppId not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdBetween(String value1, String value2) {
            addCriterion("AppId between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotBetween(String value1, String value2) {
            addCriterion("AppId not between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("Status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("Status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("Status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("Status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("Status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("Status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("Status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("Status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("Status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("Status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("Status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("Status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andChannelsIsNull() {
            addCriterion("Channels is null");
            return (Criteria) this;
        }

        public Criteria andChannelsIsNotNull() {
            addCriterion("Channels is not null");
            return (Criteria) this;
        }

        public Criteria andChannelsEqualTo(String value) {
            addCriterion("Channels =", value, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsNotEqualTo(String value) {
            addCriterion("Channels <>", value, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsGreaterThan(String value) {
            addCriterion("Channels >", value, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsGreaterThanOrEqualTo(String value) {
            addCriterion("Channels >=", value, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsLessThan(String value) {
            addCriterion("Channels <", value, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsLessThanOrEqualTo(String value) {
            addCriterion("Channels <=", value, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsLike(String value) {
            addCriterion("Channels like", value, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsNotLike(String value) {
            addCriterion("Channels not like", value, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsIn(List<String> values) {
            addCriterion("Channels in", values, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsNotIn(List<String> values) {
            addCriterion("Channels not in", values, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsBetween(String value1, String value2) {
            addCriterion("Channels between", value1, value2, "channels");
            return (Criteria) this;
        }

        public Criteria andChannelsNotBetween(String value1, String value2) {
            addCriterion("Channels not between", value1, value2, "channels");
            return (Criteria) this;
        }

        public Criteria andCodeNameIsNull() {
            addCriterion("CodeName is null");
            return (Criteria) this;
        }

        public Criteria andCodeNameIsNotNull() {
            addCriterion("CodeName is not null");
            return (Criteria) this;
        }

        public Criteria andCodeNameEqualTo(String value) {
            addCriterion("CodeName =", value, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameNotEqualTo(String value) {
            addCriterion("CodeName <>", value, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameGreaterThan(String value) {
            addCriterion("CodeName >", value, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameGreaterThanOrEqualTo(String value) {
            addCriterion("CodeName >=", value, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameLessThan(String value) {
            addCriterion("CodeName <", value, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameLessThanOrEqualTo(String value) {
            addCriterion("CodeName <=", value, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameLike(String value) {
            addCriterion("CodeName like", value, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameNotLike(String value) {
            addCriterion("CodeName not like", value, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameIn(List<String> values) {
            addCriterion("CodeName in", values, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameNotIn(List<String> values) {
            addCriterion("CodeName not in", values, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameBetween(String value1, String value2) {
            addCriterion("CodeName between", value1, value2, "codeName");
            return (Criteria) this;
        }

        public Criteria andCodeNameNotBetween(String value1, String value2) {
            addCriterion("CodeName not between", value1, value2, "codeName");
            return (Criteria) this;
        }

        public Criteria andMinAmountIsNull() {
            addCriterion("MinAmount is null");
            return (Criteria) this;
        }

        public Criteria andMinAmountIsNotNull() {
            addCriterion("MinAmount is not null");
            return (Criteria) this;
        }

        public Criteria andMinAmountEqualTo(Long value) {
            addCriterion("MinAmount =", value, "minAmount");
            return (Criteria) this;
        }

        public Criteria andMinAmountNotEqualTo(Long value) {
            addCriterion("MinAmount <>", value, "minAmount");
            return (Criteria) this;
        }

        public Criteria andMinAmountGreaterThan(Long value) {
            addCriterion("MinAmount >", value, "minAmount");
            return (Criteria) this;
        }

        public Criteria andMinAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("MinAmount >=", value, "minAmount");
            return (Criteria) this;
        }

        public Criteria andMinAmountLessThan(Long value) {
            addCriterion("MinAmount <", value, "minAmount");
            return (Criteria) this;
        }

        public Criteria andMinAmountLessThanOrEqualTo(Long value) {
            addCriterion("MinAmount <=", value, "minAmount");
            return (Criteria) this;
        }

        public Criteria andMinAmountIn(List<Long> values) {
            addCriterion("MinAmount in", values, "minAmount");
            return (Criteria) this;
        }

        public Criteria andMinAmountNotIn(List<Long> values) {
            addCriterion("MinAmount not in", values, "minAmount");
            return (Criteria) this;
        }

        public Criteria andMinAmountBetween(Long value1, Long value2) {
            addCriterion("MinAmount between", value1, value2, "minAmount");
            return (Criteria) this;
        }

        public Criteria andMinAmountNotBetween(Long value1, Long value2) {
            addCriterion("MinAmount not between", value1, value2, "minAmount");
            return (Criteria) this;
        }

        public Criteria andMaxAmountIsNull() {
            addCriterion("MaxAmount is null");
            return (Criteria) this;
        }

        public Criteria andMaxAmountIsNotNull() {
            addCriterion("MaxAmount is not null");
            return (Criteria) this;
        }

        public Criteria andMaxAmountEqualTo(Long value) {
            addCriterion("MaxAmount =", value, "maxAmount");
            return (Criteria) this;
        }

        public Criteria andMaxAmountNotEqualTo(Long value) {
            addCriterion("MaxAmount <>", value, "maxAmount");
            return (Criteria) this;
        }

        public Criteria andMaxAmountGreaterThan(Long value) {
            addCriterion("MaxAmount >", value, "maxAmount");
            return (Criteria) this;
        }

        public Criteria andMaxAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("MaxAmount >=", value, "maxAmount");
            return (Criteria) this;
        }

        public Criteria andMaxAmountLessThan(Long value) {
            addCriterion("MaxAmount <", value, "maxAmount");
            return (Criteria) this;
        }

        public Criteria andMaxAmountLessThanOrEqualTo(Long value) {
            addCriterion("MaxAmount <=", value, "maxAmount");
            return (Criteria) this;
        }

        public Criteria andMaxAmountIn(List<Long> values) {
            addCriterion("MaxAmount in", values, "maxAmount");
            return (Criteria) this;
        }

        public Criteria andMaxAmountNotIn(List<Long> values) {
            addCriterion("MaxAmount not in", values, "maxAmount");
            return (Criteria) this;
        }

        public Criteria andMaxAmountBetween(Long value1, Long value2) {
            addCriterion("MaxAmount between", value1, value2, "maxAmount");
            return (Criteria) this;
        }

        public Criteria andMaxAmountNotBetween(Long value1, Long value2) {
            addCriterion("MaxAmount not between", value1, value2, "maxAmount");
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