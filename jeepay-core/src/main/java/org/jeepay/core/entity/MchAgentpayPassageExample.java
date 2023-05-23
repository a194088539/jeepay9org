package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MchAgentpayPassageExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public MchAgentpayPassageExample() {
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
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andAgentpayPassageIdIsNull() {
            addCriterion("AgentpayPassageId is null");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdIsNotNull() {
            addCriterion("AgentpayPassageId is not null");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdEqualTo(Integer value) {
            addCriterion("AgentpayPassageId =", value, "agentpayPassageId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdNotEqualTo(Integer value) {
            addCriterion("AgentpayPassageId <>", value, "agentpayPassageId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdGreaterThan(Integer value) {
            addCriterion("AgentpayPassageId >", value, "agentpayPassageId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("AgentpayPassageId >=", value, "agentpayPassageId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdLessThan(Integer value) {
            addCriterion("AgentpayPassageId <", value, "agentpayPassageId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdLessThanOrEqualTo(Integer value) {
            addCriterion("AgentpayPassageId <=", value, "agentpayPassageId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdIn(List<Integer> values) {
            addCriterion("AgentpayPassageId in", values, "agentpayPassageId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdNotIn(List<Integer> values) {
            addCriterion("AgentpayPassageId not in", values, "agentpayPassageId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdBetween(Integer value1, Integer value2) {
            addCriterion("AgentpayPassageId between", value1, value2, "agentpayPassageId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageIdNotBetween(Integer value1, Integer value2) {
            addCriterion("AgentpayPassageId not between", value1, value2, "agentpayPassageId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdIsNull() {
            addCriterion("AgentpayPassageAccountId is null");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdIsNotNull() {
            addCriterion("AgentpayPassageAccountId is not null");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdEqualTo(Integer value) {
            addCriterion("AgentpayPassageAccountId =", value, "agentpayPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdNotEqualTo(Integer value) {
            addCriterion("AgentpayPassageAccountId <>", value, "agentpayPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdGreaterThan(Integer value) {
            addCriterion("AgentpayPassageAccountId >", value, "agentpayPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("AgentpayPassageAccountId >=", value, "agentpayPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdLessThan(Integer value) {
            addCriterion("AgentpayPassageAccountId <", value, "agentpayPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdLessThanOrEqualTo(Integer value) {
            addCriterion("AgentpayPassageAccountId <=", value, "agentpayPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdIn(List<Integer> values) {
            addCriterion("AgentpayPassageAccountId in", values, "agentpayPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdNotIn(List<Integer> values) {
            addCriterion("AgentpayPassageAccountId not in", values, "agentpayPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdBetween(Integer value1, Integer value2) {
            addCriterion("AgentpayPassageAccountId between", value1, value2, "agentpayPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andAgentpayPassageAccountIdNotBetween(Integer value1, Integer value2) {
            addCriterion("AgentpayPassageAccountId not between", value1, value2, "agentpayPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeIsNull() {
            addCriterion("MchFeeType is null");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeIsNotNull() {
            addCriterion("MchFeeType is not null");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeEqualTo(Byte value) {
            addCriterion("MchFeeType =", value, "mchFeeType");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeNotEqualTo(Byte value) {
            addCriterion("MchFeeType <>", value, "mchFeeType");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeGreaterThan(Byte value) {
            addCriterion("MchFeeType >", value, "mchFeeType");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("MchFeeType >=", value, "mchFeeType");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeLessThan(Byte value) {
            addCriterion("MchFeeType <", value, "mchFeeType");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeLessThanOrEqualTo(Byte value) {
            addCriterion("MchFeeType <=", value, "mchFeeType");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeIn(List<Byte> values) {
            addCriterion("MchFeeType in", values, "mchFeeType");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeNotIn(List<Byte> values) {
            addCriterion("MchFeeType not in", values, "mchFeeType");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeBetween(Byte value1, Byte value2) {
            addCriterion("MchFeeType between", value1, value2, "mchFeeType");
            return (Criteria) this;
        }

        public Criteria andMchFeeTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("MchFeeType not between", value1, value2, "mchFeeType");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateIsNull() {
            addCriterion("MchFeeRate is null");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateIsNotNull() {
            addCriterion("MchFeeRate is not null");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateEqualTo(BigDecimal value) {
            addCriterion("MchFeeRate =", value, "mchFeeRate");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateNotEqualTo(BigDecimal value) {
            addCriterion("MchFeeRate <>", value, "mchFeeRate");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateGreaterThan(BigDecimal value) {
            addCriterion("MchFeeRate >", value, "mchFeeRate");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("MchFeeRate >=", value, "mchFeeRate");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateLessThan(BigDecimal value) {
            addCriterion("MchFeeRate <", value, "mchFeeRate");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("MchFeeRate <=", value, "mchFeeRate");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateIn(List<BigDecimal> values) {
            addCriterion("MchFeeRate in", values, "mchFeeRate");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateNotIn(List<BigDecimal> values) {
            addCriterion("MchFeeRate not in", values, "mchFeeRate");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MchFeeRate between", value1, value2, "mchFeeRate");
            return (Criteria) this;
        }

        public Criteria andMchFeeRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MchFeeRate not between", value1, value2, "mchFeeRate");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryIsNull() {
            addCriterion("MchFeeEvery is null");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryIsNotNull() {
            addCriterion("MchFeeEvery is not null");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryEqualTo(Long value) {
            addCriterion("MchFeeEvery =", value, "mchFeeEvery");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryNotEqualTo(Long value) {
            addCriterion("MchFeeEvery <>", value, "mchFeeEvery");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryGreaterThan(Long value) {
            addCriterion("MchFeeEvery >", value, "mchFeeEvery");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryGreaterThanOrEqualTo(Long value) {
            addCriterion("MchFeeEvery >=", value, "mchFeeEvery");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryLessThan(Long value) {
            addCriterion("MchFeeEvery <", value, "mchFeeEvery");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryLessThanOrEqualTo(Long value) {
            addCriterion("MchFeeEvery <=", value, "mchFeeEvery");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryIn(List<Long> values) {
            addCriterion("MchFeeEvery in", values, "mchFeeEvery");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryNotIn(List<Long> values) {
            addCriterion("MchFeeEvery not in", values, "mchFeeEvery");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryBetween(Long value1, Long value2) {
            addCriterion("MchFeeEvery between", value1, value2, "mchFeeEvery");
            return (Criteria) this;
        }

        public Criteria andMchFeeEveryNotBetween(Long value1, Long value2) {
            addCriterion("MchFeeEvery not between", value1, value2, "mchFeeEvery");
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

        public Criteria andIsDefaultIsNull() {
            addCriterion("IsDefault is null");
            return (Criteria) this;
        }

        public Criteria andIsDefaultIsNotNull() {
            addCriterion("IsDefault is not null");
            return (Criteria) this;
        }

        public Criteria andIsDefaultEqualTo(Byte value) {
            addCriterion("IsDefault =", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultNotEqualTo(Byte value) {
            addCriterion("IsDefault <>", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultGreaterThan(Byte value) {
            addCriterion("IsDefault >", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultGreaterThanOrEqualTo(Byte value) {
            addCriterion("IsDefault >=", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultLessThan(Byte value) {
            addCriterion("IsDefault <", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultLessThanOrEqualTo(Byte value) {
            addCriterion("IsDefault <=", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultIn(List<Byte> values) {
            addCriterion("IsDefault in", values, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultNotIn(List<Byte> values) {
            addCriterion("IsDefault not in", values, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultBetween(Byte value1, Byte value2) {
            addCriterion("IsDefault between", value1, value2, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultNotBetween(Byte value1, Byte value2) {
            addCriterion("IsDefault not between", value1, value2, "isDefault");
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

        public Criteria andMaxEveryAmountIsNull() {
            addCriterion("MaxEveryAmount is null");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountIsNotNull() {
            addCriterion("MaxEveryAmount is not null");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountEqualTo(Long value) {
            addCriterion("MaxEveryAmount =", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountNotEqualTo(Long value) {
            addCriterion("MaxEveryAmount <>", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountGreaterThan(Long value) {
            addCriterion("MaxEveryAmount >", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("MaxEveryAmount >=", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountLessThan(Long value) {
            addCriterion("MaxEveryAmount <", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountLessThanOrEqualTo(Long value) {
            addCriterion("MaxEveryAmount <=", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountIn(List<Long> values) {
            addCriterion("MaxEveryAmount in", values, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountNotIn(List<Long> values) {
            addCriterion("MaxEveryAmount not in", values, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountBetween(Long value1, Long value2) {
            addCriterion("MaxEveryAmount between", value1, value2, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountNotBetween(Long value1, Long value2) {
            addCriterion("MaxEveryAmount not between", value1, value2, "maxEveryAmount");
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