package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MchAccountExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public MchAccountExample() {
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

        public Criteria andNameIsNull() {
            addCriterion("Name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("Name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("Name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("Name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("Name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("Name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("Name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("Name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("Name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("Name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("Name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("Name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("Name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("Name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("Type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("Type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Byte value) {
            addCriterion("Type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Byte value) {
            addCriterion("Type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Byte value) {
            addCriterion("Type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("Type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Byte value) {
            addCriterion("Type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Byte value) {
            addCriterion("Type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Byte> values) {
            addCriterion("Type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Byte> values) {
            addCriterion("Type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Byte value1, Byte value2) {
            addCriterion("Type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("Type not between", value1, value2, "type");
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

        public Criteria andUnBalanceIsNull() {
            addCriterion("UnBalance is null");
            return (Criteria) this;
        }

        public Criteria andUnBalanceIsNotNull() {
            addCriterion("UnBalance is not null");
            return (Criteria) this;
        }

        public Criteria andUnBalanceEqualTo(Long value) {
            addCriterion("UnBalance =", value, "unBalance");
            return (Criteria) this;
        }

        public Criteria andUnBalanceNotEqualTo(Long value) {
            addCriterion("UnBalance <>", value, "unBalance");
            return (Criteria) this;
        }

        public Criteria andUnBalanceGreaterThan(Long value) {
            addCriterion("UnBalance >", value, "unBalance");
            return (Criteria) this;
        }

        public Criteria andUnBalanceGreaterThanOrEqualTo(Long value) {
            addCriterion("UnBalance >=", value, "unBalance");
            return (Criteria) this;
        }

        public Criteria andUnBalanceLessThan(Long value) {
            addCriterion("UnBalance <", value, "unBalance");
            return (Criteria) this;
        }

        public Criteria andUnBalanceLessThanOrEqualTo(Long value) {
            addCriterion("UnBalance <=", value, "unBalance");
            return (Criteria) this;
        }

        public Criteria andUnBalanceIn(List<Long> values) {
            addCriterion("UnBalance in", values, "unBalance");
            return (Criteria) this;
        }

        public Criteria andUnBalanceNotIn(List<Long> values) {
            addCriterion("UnBalance not in", values, "unBalance");
            return (Criteria) this;
        }

        public Criteria andUnBalanceBetween(Long value1, Long value2) {
            addCriterion("UnBalance between", value1, value2, "unBalance");
            return (Criteria) this;
        }

        public Criteria andUnBalanceNotBetween(Long value1, Long value2) {
            addCriterion("UnBalance not between", value1, value2, "unBalance");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyIsNull() {
            addCriterion("SecurityMoney is null");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyIsNotNull() {
            addCriterion("SecurityMoney is not null");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyEqualTo(Long value) {
            addCriterion("SecurityMoney =", value, "securityMoney");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyNotEqualTo(Long value) {
            addCriterion("SecurityMoney <>", value, "securityMoney");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyGreaterThan(Long value) {
            addCriterion("SecurityMoney >", value, "securityMoney");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("SecurityMoney >=", value, "securityMoney");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyLessThan(Long value) {
            addCriterion("SecurityMoney <", value, "securityMoney");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyLessThanOrEqualTo(Long value) {
            addCriterion("SecurityMoney <=", value, "securityMoney");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyIn(List<Long> values) {
            addCriterion("SecurityMoney in", values, "securityMoney");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyNotIn(List<Long> values) {
            addCriterion("SecurityMoney not in", values, "securityMoney");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyBetween(Long value1, Long value2) {
            addCriterion("SecurityMoney between", value1, value2, "securityMoney");
            return (Criteria) this;
        }

        public Criteria andSecurityMoneyNotBetween(Long value1, Long value2) {
            addCriterion("SecurityMoney not between", value1, value2, "securityMoney");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeIsNull() {
            addCriterion("TotalIncome is null");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeIsNotNull() {
            addCriterion("TotalIncome is not null");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeEqualTo(Long value) {
            addCriterion("TotalIncome =", value, "totalIncome");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeNotEqualTo(Long value) {
            addCriterion("TotalIncome <>", value, "totalIncome");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeGreaterThan(Long value) {
            addCriterion("TotalIncome >", value, "totalIncome");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeGreaterThanOrEqualTo(Long value) {
            addCriterion("TotalIncome >=", value, "totalIncome");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeLessThan(Long value) {
            addCriterion("TotalIncome <", value, "totalIncome");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeLessThanOrEqualTo(Long value) {
            addCriterion("TotalIncome <=", value, "totalIncome");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeIn(List<Long> values) {
            addCriterion("TotalIncome in", values, "totalIncome");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeNotIn(List<Long> values) {
            addCriterion("TotalIncome not in", values, "totalIncome");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeBetween(Long value1, Long value2) {
            addCriterion("TotalIncome between", value1, value2, "totalIncome");
            return (Criteria) this;
        }

        public Criteria andTotalIncomeNotBetween(Long value1, Long value2) {
            addCriterion("TotalIncome not between", value1, value2, "totalIncome");
            return (Criteria) this;
        }

        public Criteria andTotalExpendIsNull() {
            addCriterion("TotalExpend is null");
            return (Criteria) this;
        }

        public Criteria andTotalExpendIsNotNull() {
            addCriterion("TotalExpend is not null");
            return (Criteria) this;
        }

        public Criteria andTotalExpendEqualTo(Long value) {
            addCriterion("TotalExpend =", value, "totalExpend");
            return (Criteria) this;
        }

        public Criteria andTotalExpendNotEqualTo(Long value) {
            addCriterion("TotalExpend <>", value, "totalExpend");
            return (Criteria) this;
        }

        public Criteria andTotalExpendGreaterThan(Long value) {
            addCriterion("TotalExpend >", value, "totalExpend");
            return (Criteria) this;
        }

        public Criteria andTotalExpendGreaterThanOrEqualTo(Long value) {
            addCriterion("TotalExpend >=", value, "totalExpend");
            return (Criteria) this;
        }

        public Criteria andTotalExpendLessThan(Long value) {
            addCriterion("TotalExpend <", value, "totalExpend");
            return (Criteria) this;
        }

        public Criteria andTotalExpendLessThanOrEqualTo(Long value) {
            addCriterion("TotalExpend <=", value, "totalExpend");
            return (Criteria) this;
        }

        public Criteria andTotalExpendIn(List<Long> values) {
            addCriterion("TotalExpend in", values, "totalExpend");
            return (Criteria) this;
        }

        public Criteria andTotalExpendNotIn(List<Long> values) {
            addCriterion("TotalExpend not in", values, "totalExpend");
            return (Criteria) this;
        }

        public Criteria andTotalExpendBetween(Long value1, Long value2) {
            addCriterion("TotalExpend between", value1, value2, "totalExpend");
            return (Criteria) this;
        }

        public Criteria andTotalExpendNotBetween(Long value1, Long value2) {
            addCriterion("TotalExpend not between", value1, value2, "totalExpend");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeIsNull() {
            addCriterion("TodayIncome is null");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeIsNotNull() {
            addCriterion("TodayIncome is not null");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeEqualTo(Long value) {
            addCriterion("TodayIncome =", value, "todayIncome");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeNotEqualTo(Long value) {
            addCriterion("TodayIncome <>", value, "todayIncome");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeGreaterThan(Long value) {
            addCriterion("TodayIncome >", value, "todayIncome");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeGreaterThanOrEqualTo(Long value) {
            addCriterion("TodayIncome >=", value, "todayIncome");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeLessThan(Long value) {
            addCriterion("TodayIncome <", value, "todayIncome");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeLessThanOrEqualTo(Long value) {
            addCriterion("TodayIncome <=", value, "todayIncome");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeIn(List<Long> values) {
            addCriterion("TodayIncome in", values, "todayIncome");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeNotIn(List<Long> values) {
            addCriterion("TodayIncome not in", values, "todayIncome");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeBetween(Long value1, Long value2) {
            addCriterion("TodayIncome between", value1, value2, "todayIncome");
            return (Criteria) this;
        }

        public Criteria andTodayIncomeNotBetween(Long value1, Long value2) {
            addCriterion("TodayIncome not between", value1, value2, "todayIncome");
            return (Criteria) this;
        }

        public Criteria andTodayExpendIsNull() {
            addCriterion("TodayExpend is null");
            return (Criteria) this;
        }

        public Criteria andTodayExpendIsNotNull() {
            addCriterion("TodayExpend is not null");
            return (Criteria) this;
        }

        public Criteria andTodayExpendEqualTo(Long value) {
            addCriterion("TodayExpend =", value, "todayExpend");
            return (Criteria) this;
        }

        public Criteria andTodayExpendNotEqualTo(Long value) {
            addCriterion("TodayExpend <>", value, "todayExpend");
            return (Criteria) this;
        }

        public Criteria andTodayExpendGreaterThan(Long value) {
            addCriterion("TodayExpend >", value, "todayExpend");
            return (Criteria) this;
        }

        public Criteria andTodayExpendGreaterThanOrEqualTo(Long value) {
            addCriterion("TodayExpend >=", value, "todayExpend");
            return (Criteria) this;
        }

        public Criteria andTodayExpendLessThan(Long value) {
            addCriterion("TodayExpend <", value, "todayExpend");
            return (Criteria) this;
        }

        public Criteria andTodayExpendLessThanOrEqualTo(Long value) {
            addCriterion("TodayExpend <=", value, "todayExpend");
            return (Criteria) this;
        }

        public Criteria andTodayExpendIn(List<Long> values) {
            addCriterion("TodayExpend in", values, "todayExpend");
            return (Criteria) this;
        }

        public Criteria andTodayExpendNotIn(List<Long> values) {
            addCriterion("TodayExpend not in", values, "todayExpend");
            return (Criteria) this;
        }

        public Criteria andTodayExpendBetween(Long value1, Long value2) {
            addCriterion("TodayExpend between", value1, value2, "todayExpend");
            return (Criteria) this;
        }

        public Criteria andTodayExpendNotBetween(Long value1, Long value2) {
            addCriterion("TodayExpend not between", value1, value2, "todayExpend");
            return (Criteria) this;
        }

        public Criteria andSettAmountIsNull() {
            addCriterion("SettAmount is null");
            return (Criteria) this;
        }

        public Criteria andSettAmountIsNotNull() {
            addCriterion("SettAmount is not null");
            return (Criteria) this;
        }

        public Criteria andSettAmountEqualTo(Long value) {
            addCriterion("SettAmount =", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountNotEqualTo(Long value) {
            addCriterion("SettAmount <>", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountGreaterThan(Long value) {
            addCriterion("SettAmount >", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("SettAmount >=", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountLessThan(Long value) {
            addCriterion("SettAmount <", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountLessThanOrEqualTo(Long value) {
            addCriterion("SettAmount <=", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountIn(List<Long> values) {
            addCriterion("SettAmount in", values, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountNotIn(List<Long> values) {
            addCriterion("SettAmount not in", values, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountBetween(Long value1, Long value2) {
            addCriterion("SettAmount between", value1, value2, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountNotBetween(Long value1, Long value2) {
            addCriterion("SettAmount not between", value1, value2, "settAmount");
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

        public Criteria andAccountUpdateTimeIsNull() {
            addCriterion("AccountUpdateTime is null");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeIsNotNull() {
            addCriterion("AccountUpdateTime is not null");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeEqualTo(Date value) {
            addCriterion("AccountUpdateTime =", value, "accountUpdateTime");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeNotEqualTo(Date value) {
            addCriterion("AccountUpdateTime <>", value, "accountUpdateTime");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeGreaterThan(Date value) {
            addCriterion("AccountUpdateTime >", value, "accountUpdateTime");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("AccountUpdateTime >=", value, "accountUpdateTime");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeLessThan(Date value) {
            addCriterion("AccountUpdateTime <", value, "accountUpdateTime");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("AccountUpdateTime <=", value, "accountUpdateTime");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeIn(List<Date> values) {
            addCriterion("AccountUpdateTime in", values, "accountUpdateTime");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeNotIn(List<Date> values) {
            addCriterion("AccountUpdateTime not in", values, "accountUpdateTime");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("AccountUpdateTime between", value1, value2, "accountUpdateTime");
            return (Criteria) this;
        }

        public Criteria andAccountUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("AccountUpdateTime not between", value1, value2, "accountUpdateTime");
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

        public Criteria andFrozenMoneyIsNull() {
            addCriterion("FrozenMoney is null");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyIsNotNull() {
            addCriterion("FrozenMoney is not null");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyEqualTo(Long value) {
            addCriterion("FrozenMoney =", value, "frozenMoney");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyNotEqualTo(Long value) {
            addCriterion("FrozenMoney <>", value, "frozenMoney");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyGreaterThan(Long value) {
            addCriterion("FrozenMoney >", value, "frozenMoney");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("FrozenMoney >=", value, "frozenMoney");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyLessThan(Long value) {
            addCriterion("FrozenMoney <", value, "frozenMoney");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyLessThanOrEqualTo(Long value) {
            addCriterion("FrozenMoney <=", value, "frozenMoney");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyIn(List<Long> values) {
            addCriterion("FrozenMoney in", values, "frozenMoney");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyNotIn(List<Long> values) {
            addCriterion("FrozenMoney not in", values, "frozenMoney");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyBetween(Long value1, Long value2) {
            addCriterion("FrozenMoney between", value1, value2, "frozenMoney");
            return (Criteria) this;
        }

        public Criteria andFrozenMoneyNotBetween(Long value1, Long value2) {
            addCriterion("FrozenMoney not between", value1, value2, "frozenMoney");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceIsNull() {
            addCriterion("AgentpayBalance is null");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceIsNotNull() {
            addCriterion("AgentpayBalance is not null");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceEqualTo(Long value) {
            addCriterion("AgentpayBalance =", value, "agentpayBalance");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceNotEqualTo(Long value) {
            addCriterion("AgentpayBalance <>", value, "agentpayBalance");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceGreaterThan(Long value) {
            addCriterion("AgentpayBalance >", value, "agentpayBalance");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceGreaterThanOrEqualTo(Long value) {
            addCriterion("AgentpayBalance >=", value, "agentpayBalance");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceLessThan(Long value) {
            addCriterion("AgentpayBalance <", value, "agentpayBalance");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceLessThanOrEqualTo(Long value) {
            addCriterion("AgentpayBalance <=", value, "agentpayBalance");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceIn(List<Long> values) {
            addCriterion("AgentpayBalance in", values, "agentpayBalance");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceNotIn(List<Long> values) {
            addCriterion("AgentpayBalance not in", values, "agentpayBalance");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceBetween(Long value1, Long value2) {
            addCriterion("AgentpayBalance between", value1, value2, "agentpayBalance");
            return (Criteria) this;
        }

        public Criteria andAgentpayBalanceNotBetween(Long value1, Long value2) {
            addCriterion("AgentpayBalance not between", value1, value2, "agentpayBalance");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceIsNull() {
            addCriterion("UnAgentpayBalance is null");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceIsNotNull() {
            addCriterion("UnAgentpayBalance is not null");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceEqualTo(Long value) {
            addCriterion("UnAgentpayBalance =", value, "unAgentpayBalance");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceNotEqualTo(Long value) {
            addCriterion("UnAgentpayBalance <>", value, "unAgentpayBalance");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceGreaterThan(Long value) {
            addCriterion("UnAgentpayBalance >", value, "unAgentpayBalance");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceGreaterThanOrEqualTo(Long value) {
            addCriterion("UnAgentpayBalance >=", value, "unAgentpayBalance");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceLessThan(Long value) {
            addCriterion("UnAgentpayBalance <", value, "unAgentpayBalance");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceLessThanOrEqualTo(Long value) {
            addCriterion("UnAgentpayBalance <=", value, "unAgentpayBalance");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceIn(List<Long> values) {
            addCriterion("UnAgentpayBalance in", values, "unAgentpayBalance");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceNotIn(List<Long> values) {
            addCriterion("UnAgentpayBalance not in", values, "unAgentpayBalance");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceBetween(Long value1, Long value2) {
            addCriterion("UnAgentpayBalance between", value1, value2, "unAgentpayBalance");
            return (Criteria) this;
        }

        public Criteria andUnAgentpayBalanceNotBetween(Long value1, Long value2) {
            addCriterion("UnAgentpayBalance not between", value1, value2, "unAgentpayBalance");
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