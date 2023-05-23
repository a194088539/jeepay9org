package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MchAccountHistoryExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public MchAccountHistoryExample() {
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

        public Criteria andAmountIsNull() {
            addCriterion("Amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("Amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Long value) {
            addCriterion("Amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Long value) {
            addCriterion("Amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Long value) {
            addCriterion("Amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("Amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Long value) {
            addCriterion("Amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Long value) {
            addCriterion("Amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Long> values) {
            addCriterion("Amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Long> values) {
            addCriterion("Amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Long value1, Long value2) {
            addCriterion("Amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Long value1, Long value2) {
            addCriterion("Amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNull() {
            addCriterion("Balance is null");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNotNull() {
            addCriterion("Balance is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceEqualTo(Long value) {
            addCriterion("Balance =", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotEqualTo(Long value) {
            addCriterion("Balance <>", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThan(Long value) {
            addCriterion("Balance >", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThanOrEqualTo(Long value) {
            addCriterion("Balance >=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThan(Long value) {
            addCriterion("Balance <", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThanOrEqualTo(Long value) {
            addCriterion("Balance <=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceIn(List<Long> values) {
            addCriterion("Balance in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotIn(List<Long> values) {
            addCriterion("Balance not in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceBetween(Long value1, Long value2) {
            addCriterion("Balance between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotBetween(Long value1, Long value2) {
            addCriterion("Balance not between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceIsNull() {
            addCriterion("AfterBalance is null");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceIsNotNull() {
            addCriterion("AfterBalance is not null");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceEqualTo(Long value) {
            addCriterion("AfterBalance =", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceNotEqualTo(Long value) {
            addCriterion("AfterBalance <>", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceGreaterThan(Long value) {
            addCriterion("AfterBalance >", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceGreaterThanOrEqualTo(Long value) {
            addCriterion("AfterBalance >=", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceLessThan(Long value) {
            addCriterion("AfterBalance <", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceLessThanOrEqualTo(Long value) {
            addCriterion("AfterBalance <=", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceIn(List<Long> values) {
            addCriterion("AfterBalance in", values, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceNotIn(List<Long> values) {
            addCriterion("AfterBalance not in", values, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceBetween(Long value1, Long value2) {
            addCriterion("AfterBalance between", value1, value2, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceNotBetween(Long value1, Long value2) {
            addCriterion("AfterBalance not between", value1, value2, "afterBalance");
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

        public Criteria andParentAgentIdIsNull() {
            addCriterion("parentAgentId is null");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdIsNotNull() {
            addCriterion("parentAgentId is not null");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdEqualTo(Long value) {
            addCriterion("parentAgentId =", value, "parentAgentId");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdNotEqualTo(Long value) {
            addCriterion("parentAgentId <>", value, "parentAgentId");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdGreaterThan(Long value) {
            addCriterion("parentAgentId >", value, "parentAgentId");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdGreaterThanOrEqualTo(Long value) {
            addCriterion("parentAgentId >=", value, "parentAgentId");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdLessThan(Long value) {
            addCriterion("parentAgentId <", value, "parentAgentId");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdLessThanOrEqualTo(Long value) {
            addCriterion("parentAgentId <=", value, "parentAgentId");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdIn(List<Long> values) {
            addCriterion("parentAgentId in", values, "parentAgentId");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdNotIn(List<Long> values) {
            addCriterion("parentAgentId not in", values, "parentAgentId");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdBetween(Long value1, Long value2) {
            addCriterion("parentAgentId between", value1, value2, "parentAgentId");
            return (Criteria) this;
        }

        public Criteria andParentAgentIdNotBetween(Long value1, Long value2) {
            addCriterion("parentAgentId not between", value1, value2, "parentAgentId");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIsNull() {
            addCriterion("OrderAmount is null");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIsNotNull() {
            addCriterion("OrderAmount is not null");
            return (Criteria) this;
        }

        public Criteria andOrderAmountEqualTo(Long value) {
            addCriterion("OrderAmount =", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotEqualTo(Long value) {
            addCriterion("OrderAmount <>", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountGreaterThan(Long value) {
            addCriterion("OrderAmount >", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("OrderAmount >=", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountLessThan(Long value) {
            addCriterion("OrderAmount <", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountLessThanOrEqualTo(Long value) {
            addCriterion("OrderAmount <=", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIn(List<Long> values) {
            addCriterion("OrderAmount in", values, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotIn(List<Long> values) {
            addCriterion("OrderAmount not in", values, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountBetween(Long value1, Long value2) {
            addCriterion("OrderAmount between", value1, value2, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotBetween(Long value1, Long value2) {
            addCriterion("OrderAmount not between", value1, value2, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andFeeIsNull() {
            addCriterion("Fee is null");
            return (Criteria) this;
        }

        public Criteria andFeeIsNotNull() {
            addCriterion("Fee is not null");
            return (Criteria) this;
        }

        public Criteria andFeeEqualTo(Long value) {
            addCriterion("Fee =", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotEqualTo(Long value) {
            addCriterion("Fee <>", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThan(Long value) {
            addCriterion("Fee >", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThanOrEqualTo(Long value) {
            addCriterion("Fee >=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThan(Long value) {
            addCriterion("Fee <", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThanOrEqualTo(Long value) {
            addCriterion("Fee <=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeIn(List<Long> values) {
            addCriterion("Fee in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotIn(List<Long> values) {
            addCriterion("Fee not in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeBetween(Long value1, Long value2) {
            addCriterion("Fee between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotBetween(Long value1, Long value2) {
            addCriterion("Fee not between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andAgentProfitIsNull() {
            addCriterion("AgentProfit is null");
            return (Criteria) this;
        }

        public Criteria andAgentProfitIsNotNull() {
            addCriterion("AgentProfit is not null");
            return (Criteria) this;
        }

        public Criteria andAgentProfitEqualTo(Long value) {
            addCriterion("AgentProfit =", value, "agentProfit");
            return (Criteria) this;
        }

        public Criteria andAgentProfitNotEqualTo(Long value) {
            addCriterion("AgentProfit <>", value, "agentProfit");
            return (Criteria) this;
        }

        public Criteria andAgentProfitGreaterThan(Long value) {
            addCriterion("AgentProfit >", value, "agentProfit");
            return (Criteria) this;
        }

        public Criteria andAgentProfitGreaterThanOrEqualTo(Long value) {
            addCriterion("AgentProfit >=", value, "agentProfit");
            return (Criteria) this;
        }

        public Criteria andAgentProfitLessThan(Long value) {
            addCriterion("AgentProfit <", value, "agentProfit");
            return (Criteria) this;
        }

        public Criteria andAgentProfitLessThanOrEqualTo(Long value) {
            addCriterion("AgentProfit <=", value, "agentProfit");
            return (Criteria) this;
        }

        public Criteria andAgentProfitIn(List<Long> values) {
            addCriterion("AgentProfit in", values, "agentProfit");
            return (Criteria) this;
        }

        public Criteria andAgentProfitNotIn(List<Long> values) {
            addCriterion("AgentProfit not in", values, "agentProfit");
            return (Criteria) this;
        }

        public Criteria andAgentProfitBetween(Long value1, Long value2) {
            addCriterion("AgentProfit between", value1, value2, "agentProfit");
            return (Criteria) this;
        }

        public Criteria andAgentProfitNotBetween(Long value1, Long value2) {
            addCriterion("AgentProfit not between", value1, value2, "agentProfit");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitIsNull() {
            addCriterion("parentAgentProfit is null");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitIsNotNull() {
            addCriterion("parentAgentProfit is not null");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitEqualTo(Long value) {
            addCriterion("parentAgentProfit =", value, "parentAgentProfit");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitNotEqualTo(Long value) {
            addCriterion("parentAgentProfit <>", value, "parentAgentProfit");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitGreaterThan(Long value) {
            addCriterion("parentAgentProfit >", value, "parentAgentProfit");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitGreaterThanOrEqualTo(Long value) {
            addCriterion("parentAgentProfit >=", value, "parentAgentProfit");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitLessThan(Long value) {
            addCriterion("parentAgentProfit <", value, "parentAgentProfit");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitLessThanOrEqualTo(Long value) {
            addCriterion("parentAgentProfit <=", value, "parentAgentProfit");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitIn(List<Long> values) {
            addCriterion("parentAgentProfit in", values, "parentAgentProfit");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitNotIn(List<Long> values) {
            addCriterion("parentAgentProfit not in", values, "parentAgentProfit");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitBetween(Long value1, Long value2) {
            addCriterion("parentAgentProfit between", value1, value2, "parentAgentProfit");
            return (Criteria) this;
        }

        public Criteria andParentAgentProfitNotBetween(Long value1, Long value2) {
            addCriterion("parentAgentProfit not between", value1, value2, "parentAgentProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitIsNull() {
            addCriterion("PlatProfit is null");
            return (Criteria) this;
        }

        public Criteria andPlatProfitIsNotNull() {
            addCriterion("PlatProfit is not null");
            return (Criteria) this;
        }

        public Criteria andPlatProfitEqualTo(Long value) {
            addCriterion("PlatProfit =", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitNotEqualTo(Long value) {
            addCriterion("PlatProfit <>", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitGreaterThan(Long value) {
            addCriterion("PlatProfit >", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitGreaterThanOrEqualTo(Long value) {
            addCriterion("PlatProfit >=", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitLessThan(Long value) {
            addCriterion("PlatProfit <", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitLessThanOrEqualTo(Long value) {
            addCriterion("PlatProfit <=", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitIn(List<Long> values) {
            addCriterion("PlatProfit in", values, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitNotIn(List<Long> values) {
            addCriterion("PlatProfit not in", values, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitBetween(Long value1, Long value2) {
            addCriterion("PlatProfit between", value1, value2, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitNotBetween(Long value1, Long value2) {
            addCriterion("PlatProfit not between", value1, value2, "platProfit");
            return (Criteria) this;
        }

        public Criteria andChannelCostIsNull() {
            addCriterion("ChannelCost is null");
            return (Criteria) this;
        }

        public Criteria andChannelCostIsNotNull() {
            addCriterion("ChannelCost is not null");
            return (Criteria) this;
        }

        public Criteria andChannelCostEqualTo(Long value) {
            addCriterion("ChannelCost =", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostNotEqualTo(Long value) {
            addCriterion("ChannelCost <>", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostGreaterThan(Long value) {
            addCriterion("ChannelCost >", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostGreaterThanOrEqualTo(Long value) {
            addCriterion("ChannelCost >=", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostLessThan(Long value) {
            addCriterion("ChannelCost <", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostLessThanOrEqualTo(Long value) {
            addCriterion("ChannelCost <=", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostIn(List<Long> values) {
            addCriterion("ChannelCost in", values, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostNotIn(List<Long> values) {
            addCriterion("ChannelCost not in", values, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostBetween(Long value1, Long value2) {
            addCriterion("ChannelCost between", value1, value2, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostNotBetween(Long value1, Long value2) {
            addCriterion("ChannelCost not between", value1, value2, "channelCost");
            return (Criteria) this;
        }

        public Criteria andFundDirectionIsNull() {
            addCriterion("FundDirection is null");
            return (Criteria) this;
        }

        public Criteria andFundDirectionIsNotNull() {
            addCriterion("FundDirection is not null");
            return (Criteria) this;
        }

        public Criteria andFundDirectionEqualTo(Byte value) {
            addCriterion("FundDirection =", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionNotEqualTo(Byte value) {
            addCriterion("FundDirection <>", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionGreaterThan(Byte value) {
            addCriterion("FundDirection >", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionGreaterThanOrEqualTo(Byte value) {
            addCriterion("FundDirection >=", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionLessThan(Byte value) {
            addCriterion("FundDirection <", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionLessThanOrEqualTo(Byte value) {
            addCriterion("FundDirection <=", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionIn(List<Byte> values) {
            addCriterion("FundDirection in", values, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionNotIn(List<Byte> values) {
            addCriterion("FundDirection not in", values, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionBetween(Byte value1, Byte value2) {
            addCriterion("FundDirection between", value1, value2, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionNotBetween(Byte value1, Byte value2) {
            addCriterion("FundDirection not between", value1, value2, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettIsNull() {
            addCriterion("IsAllowSett is null");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettIsNotNull() {
            addCriterion("IsAllowSett is not null");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettEqualTo(Byte value) {
            addCriterion("IsAllowSett =", value, "isAllowSett");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettNotEqualTo(Byte value) {
            addCriterion("IsAllowSett <>", value, "isAllowSett");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettGreaterThan(Byte value) {
            addCriterion("IsAllowSett >", value, "isAllowSett");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettGreaterThanOrEqualTo(Byte value) {
            addCriterion("IsAllowSett >=", value, "isAllowSett");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettLessThan(Byte value) {
            addCriterion("IsAllowSett <", value, "isAllowSett");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettLessThanOrEqualTo(Byte value) {
            addCriterion("IsAllowSett <=", value, "isAllowSett");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettIn(List<Byte> values) {
            addCriterion("IsAllowSett in", values, "isAllowSett");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettNotIn(List<Byte> values) {
            addCriterion("IsAllowSett not in", values, "isAllowSett");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettBetween(Byte value1, Byte value2) {
            addCriterion("IsAllowSett between", value1, value2, "isAllowSett");
            return (Criteria) this;
        }

        public Criteria andIsAllowSettNotBetween(Byte value1, Byte value2) {
            addCriterion("IsAllowSett not between", value1, value2, "isAllowSett");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusIsNull() {
            addCriterion("MchSettStatus is null");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusIsNotNull() {
            addCriterion("MchSettStatus is not null");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusEqualTo(Byte value) {
            addCriterion("MchSettStatus =", value, "mchSettStatus");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusNotEqualTo(Byte value) {
            addCriterion("MchSettStatus <>", value, "mchSettStatus");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusGreaterThan(Byte value) {
            addCriterion("MchSettStatus >", value, "mchSettStatus");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("MchSettStatus >=", value, "mchSettStatus");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusLessThan(Byte value) {
            addCriterion("MchSettStatus <", value, "mchSettStatus");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusLessThanOrEqualTo(Byte value) {
            addCriterion("MchSettStatus <=", value, "mchSettStatus");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusIn(List<Byte> values) {
            addCriterion("MchSettStatus in", values, "mchSettStatus");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusNotIn(List<Byte> values) {
            addCriterion("MchSettStatus not in", values, "mchSettStatus");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusBetween(Byte value1, Byte value2) {
            addCriterion("MchSettStatus between", value1, value2, "mchSettStatus");
            return (Criteria) this;
        }

        public Criteria andMchSettStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("MchSettStatus not between", value1, value2, "mchSettStatus");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusIsNull() {
            addCriterion("AgentSettStatus is null");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusIsNotNull() {
            addCriterion("AgentSettStatus is not null");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusEqualTo(Byte value) {
            addCriterion("AgentSettStatus =", value, "agentSettStatus");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusNotEqualTo(Byte value) {
            addCriterion("AgentSettStatus <>", value, "agentSettStatus");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusGreaterThan(Byte value) {
            addCriterion("AgentSettStatus >", value, "agentSettStatus");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("AgentSettStatus >=", value, "agentSettStatus");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusLessThan(Byte value) {
            addCriterion("AgentSettStatus <", value, "agentSettStatus");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusLessThanOrEqualTo(Byte value) {
            addCriterion("AgentSettStatus <=", value, "agentSettStatus");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusIn(List<Byte> values) {
            addCriterion("AgentSettStatus in", values, "agentSettStatus");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusNotIn(List<Byte> values) {
            addCriterion("AgentSettStatus not in", values, "agentSettStatus");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusBetween(Byte value1, Byte value2) {
            addCriterion("AgentSettStatus between", value1, value2, "agentSettStatus");
            return (Criteria) this;
        }

        public Criteria andAgentSettStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("AgentSettStatus not between", value1, value2, "agentSettStatus");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusIsNull() {
            addCriterion("parentAgentSettStatus is null");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusIsNotNull() {
            addCriterion("parentAgentSettStatus is not null");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusEqualTo(Byte value) {
            addCriterion("parentAgentSettStatus =", value, "parentAgentSettStatus");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusNotEqualTo(Byte value) {
            addCriterion("parentAgentSettStatus <>", value, "parentAgentSettStatus");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusGreaterThan(Byte value) {
            addCriterion("parentAgentSettStatus >", value, "parentAgentSettStatus");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("parentAgentSettStatus >=", value, "parentAgentSettStatus");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusLessThan(Byte value) {
            addCriterion("parentAgentSettStatus <", value, "parentAgentSettStatus");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusLessThanOrEqualTo(Byte value) {
            addCriterion("parentAgentSettStatus <=", value, "parentAgentSettStatus");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusIn(List<Byte> values) {
            addCriterion("parentAgentSettStatus in", values, "parentAgentSettStatus");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusNotIn(List<Byte> values) {
            addCriterion("parentAgentSettStatus not in", values, "parentAgentSettStatus");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusBetween(Byte value1, Byte value2) {
            addCriterion("parentAgentSettStatus between", value1, value2, "parentAgentSettStatus");
            return (Criteria) this;
        }

        public Criteria andParentAgentSettStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("parentAgentSettStatus not between", value1, value2, "parentAgentSettStatus");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNull() {
            addCriterion("OrderId is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("OrderId is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(String value) {
            addCriterion("OrderId =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(String value) {
            addCriterion("OrderId <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(String value) {
            addCriterion("OrderId >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("OrderId >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(String value) {
            addCriterion("OrderId <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(String value) {
            addCriterion("OrderId <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLike(String value) {
            addCriterion("OrderId like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotLike(String value) {
            addCriterion("OrderId not like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<String> values) {
            addCriterion("OrderId in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<String> values) {
            addCriterion("OrderId not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(String value1, String value2) {
            addCriterion("OrderId between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(String value1, String value2) {
            addCriterion("OrderId not between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoIsNull() {
            addCriterion("ChannelOrderNo is null");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoIsNotNull() {
            addCriterion("ChannelOrderNo is not null");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoEqualTo(String value) {
            addCriterion("ChannelOrderNo =", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoNotEqualTo(String value) {
            addCriterion("ChannelOrderNo <>", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoGreaterThan(String value) {
            addCriterion("ChannelOrderNo >", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("ChannelOrderNo >=", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoLessThan(String value) {
            addCriterion("ChannelOrderNo <", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoLessThanOrEqualTo(String value) {
            addCriterion("ChannelOrderNo <=", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoLike(String value) {
            addCriterion("ChannelOrderNo like", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoNotLike(String value) {
            addCriterion("ChannelOrderNo not like", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoIn(List<String> values) {
            addCriterion("ChannelOrderNo in", values, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoNotIn(List<String> values) {
            addCriterion("ChannelOrderNo not in", values, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoBetween(String value1, String value2) {
            addCriterion("ChannelOrderNo between", value1, value2, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoNotBetween(String value1, String value2) {
            addCriterion("ChannelOrderNo not between", value1, value2, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizTypeIsNull() {
            addCriterion("BizType is null");
            return (Criteria) this;
        }

        public Criteria andBizTypeIsNotNull() {
            addCriterion("BizType is not null");
            return (Criteria) this;
        }

        public Criteria andBizTypeEqualTo(Byte value) {
            addCriterion("BizType =", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeNotEqualTo(Byte value) {
            addCriterion("BizType <>", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeGreaterThan(Byte value) {
            addCriterion("BizType >", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("BizType >=", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeLessThan(Byte value) {
            addCriterion("BizType <", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeLessThanOrEqualTo(Byte value) {
            addCriterion("BizType <=", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeIn(List<Byte> values) {
            addCriterion("BizType in", values, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeNotIn(List<Byte> values) {
            addCriterion("BizType not in", values, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeBetween(Byte value1, Byte value2) {
            addCriterion("BizType between", value1, value2, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("BizType not between", value1, value2, "bizType");
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

        public Criteria andBizItemIsNull() {
            addCriterion("BizItem is null");
            return (Criteria) this;
        }

        public Criteria andBizItemIsNotNull() {
            addCriterion("BizItem is not null");
            return (Criteria) this;
        }

        public Criteria andBizItemEqualTo(String value) {
            addCriterion("BizItem =", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemNotEqualTo(String value) {
            addCriterion("BizItem <>", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemGreaterThan(String value) {
            addCriterion("BizItem >", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemGreaterThanOrEqualTo(String value) {
            addCriterion("BizItem >=", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemLessThan(String value) {
            addCriterion("BizItem <", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemLessThanOrEqualTo(String value) {
            addCriterion("BizItem <=", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemLike(String value) {
            addCriterion("BizItem like", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemNotLike(String value) {
            addCriterion("BizItem not like", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemIn(List<String> values) {
            addCriterion("BizItem in", values, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemNotIn(List<String> values) {
            addCriterion("BizItem not in", values, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemBetween(String value1, String value2) {
            addCriterion("BizItem between", value1, value2, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemNotBetween(String value1, String value2) {
            addCriterion("BizItem not between", value1, value2, "bizItem");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayIsNull() {
            addCriterion("AgentRiskDay is null");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayIsNotNull() {
            addCriterion("AgentRiskDay is not null");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayEqualTo(Integer value) {
            addCriterion("AgentRiskDay =", value, "agentRiskDay");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayNotEqualTo(Integer value) {
            addCriterion("AgentRiskDay <>", value, "agentRiskDay");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayGreaterThan(Integer value) {
            addCriterion("AgentRiskDay >", value, "agentRiskDay");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayGreaterThanOrEqualTo(Integer value) {
            addCriterion("AgentRiskDay >=", value, "agentRiskDay");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayLessThan(Integer value) {
            addCriterion("AgentRiskDay <", value, "agentRiskDay");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayLessThanOrEqualTo(Integer value) {
            addCriterion("AgentRiskDay <=", value, "agentRiskDay");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayIn(List<Integer> values) {
            addCriterion("AgentRiskDay in", values, "agentRiskDay");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayNotIn(List<Integer> values) {
            addCriterion("AgentRiskDay not in", values, "agentRiskDay");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayBetween(Integer value1, Integer value2) {
            addCriterion("AgentRiskDay between", value1, value2, "agentRiskDay");
            return (Criteria) this;
        }

        public Criteria andAgentRiskDayNotBetween(Integer value1, Integer value2) {
            addCriterion("AgentRiskDay not between", value1, value2, "agentRiskDay");
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