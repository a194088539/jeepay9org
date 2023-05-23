package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WxUserExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public WxUserExample() {
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

        public Criteria andUserIdIsNull() {
            addCriterion("UserId is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("UserId is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("UserId =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("UserId <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("UserId >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("UserId >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("UserId <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("UserId <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("UserId in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("UserId not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("UserId between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("UserId not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andWxIdIsNull() {
            addCriterion("WxId is null");
            return (Criteria) this;
        }

        public Criteria andWxIdIsNotNull() {
            addCriterion("WxId is not null");
            return (Criteria) this;
        }

        public Criteria andWxIdEqualTo(String value) {
            addCriterion("WxId =", value, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdNotEqualTo(String value) {
            addCriterion("WxId <>", value, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdGreaterThan(String value) {
            addCriterion("WxId >", value, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdGreaterThanOrEqualTo(String value) {
            addCriterion("WxId >=", value, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdLessThan(String value) {
            addCriterion("WxId <", value, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdLessThanOrEqualTo(String value) {
            addCriterion("WxId <=", value, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdLike(String value) {
            addCriterion("WxId like", value, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdNotLike(String value) {
            addCriterion("WxId not like", value, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdIn(List<String> values) {
            addCriterion("WxId in", values, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdNotIn(List<String> values) {
            addCriterion("WxId not in", values, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdBetween(String value1, String value2) {
            addCriterion("WxId between", value1, value2, "wxId");
            return (Criteria) this;
        }

        public Criteria andWxIdNotBetween(String value1, String value2) {
            addCriterion("WxId not between", value1, value2, "wxId");
            return (Criteria) this;
        }

        public Criteria andAccountIsNull() {
            addCriterion("Account is null");
            return (Criteria) this;
        }

        public Criteria andAccountIsNotNull() {
            addCriterion("Account is not null");
            return (Criteria) this;
        }

        public Criteria andAccountEqualTo(String value) {
            addCriterion("Account =", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotEqualTo(String value) {
            addCriterion("Account <>", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThan(String value) {
            addCriterion("Account >", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThanOrEqualTo(String value) {
            addCriterion("Account >=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThan(String value) {
            addCriterion("Account <", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThanOrEqualTo(String value) {
            addCriterion("Account <=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLike(String value) {
            addCriterion("Account like", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotLike(String value) {
            addCriterion("Account not like", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountIn(List<String> values) {
            addCriterion("Account in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotIn(List<String> values) {
            addCriterion("Account not in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountBetween(String value1, String value2) {
            addCriterion("Account between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotBetween(String value1, String value2) {
            addCriterion("Account not between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("Password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("Password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("Password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("Password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("Password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("Password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("Password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("Password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("Password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("Password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("Password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("Password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("Password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("Password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andNickNameIsNull() {
            addCriterion("NickName is null");
            return (Criteria) this;
        }

        public Criteria andNickNameIsNotNull() {
            addCriterion("NickName is not null");
            return (Criteria) this;
        }

        public Criteria andNickNameEqualTo(String value) {
            addCriterion("NickName =", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotEqualTo(String value) {
            addCriterion("NickName <>", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameGreaterThan(String value) {
            addCriterion("NickName >", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameGreaterThanOrEqualTo(String value) {
            addCriterion("NickName >=", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLessThan(String value) {
            addCriterion("NickName <", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLessThanOrEqualTo(String value) {
            addCriterion("NickName <=", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLike(String value) {
            addCriterion("NickName like", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotLike(String value) {
            addCriterion("NickName not like", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameIn(List<String> values) {
            addCriterion("NickName in", values, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotIn(List<String> values) {
            addCriterion("NickName not in", values, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameBetween(String value1, String value2) {
            addCriterion("NickName between", value1, value2, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotBetween(String value1, String value2) {
            addCriterion("NickName not between", value1, value2, "nickName");
            return (Criteria) this;
        }

        public Criteria andServerIdIsNull() {
            addCriterion("ServerId is null");
            return (Criteria) this;
        }

        public Criteria andServerIdIsNotNull() {
            addCriterion("ServerId is not null");
            return (Criteria) this;
        }

        public Criteria andServerIdEqualTo(String value) {
            addCriterion("ServerId =", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotEqualTo(String value) {
            addCriterion("ServerId <>", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdGreaterThan(String value) {
            addCriterion("ServerId >", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdGreaterThanOrEqualTo(String value) {
            addCriterion("ServerId >=", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLessThan(String value) {
            addCriterion("ServerId <", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLessThanOrEqualTo(String value) {
            addCriterion("ServerId <=", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLike(String value) {
            addCriterion("ServerId like", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotLike(String value) {
            addCriterion("ServerId not like", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdIn(List<String> values) {
            addCriterion("ServerId in", values, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotIn(List<String> values) {
            addCriterion("ServerId not in", values, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdBetween(String value1, String value2) {
            addCriterion("ServerId between", value1, value2, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotBetween(String value1, String value2) {
            addCriterion("ServerId not between", value1, value2, "serverId");
            return (Criteria) this;
        }

        public Criteria andRandomIdIsNull() {
            addCriterion("RandomId is null");
            return (Criteria) this;
        }

        public Criteria andRandomIdIsNotNull() {
            addCriterion("RandomId is not null");
            return (Criteria) this;
        }

        public Criteria andRandomIdEqualTo(String value) {
            addCriterion("RandomId =", value, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdNotEqualTo(String value) {
            addCriterion("RandomId <>", value, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdGreaterThan(String value) {
            addCriterion("RandomId >", value, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdGreaterThanOrEqualTo(String value) {
            addCriterion("RandomId >=", value, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdLessThan(String value) {
            addCriterion("RandomId <", value, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdLessThanOrEqualTo(String value) {
            addCriterion("RandomId <=", value, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdLike(String value) {
            addCriterion("RandomId like", value, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdNotLike(String value) {
            addCriterion("RandomId not like", value, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdIn(List<String> values) {
            addCriterion("RandomId in", values, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdNotIn(List<String> values) {
            addCriterion("RandomId not in", values, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdBetween(String value1, String value2) {
            addCriterion("RandomId between", value1, value2, "randomId");
            return (Criteria) this;
        }

        public Criteria andRandomIdNotBetween(String value1, String value2) {
            addCriterion("RandomId not between", value1, value2, "randomId");
            return (Criteria) this;
        }

        public Criteria andDayInAmountIsNull() {
            addCriterion("DayInAmount is null");
            return (Criteria) this;
        }

        public Criteria andDayInAmountIsNotNull() {
            addCriterion("DayInAmount is not null");
            return (Criteria) this;
        }

        public Criteria andDayInAmountEqualTo(Long value) {
            addCriterion("DayInAmount =", value, "dayInAmount");
            return (Criteria) this;
        }

        public Criteria andDayInAmountNotEqualTo(Long value) {
            addCriterion("DayInAmount <>", value, "dayInAmount");
            return (Criteria) this;
        }

        public Criteria andDayInAmountGreaterThan(Long value) {
            addCriterion("DayInAmount >", value, "dayInAmount");
            return (Criteria) this;
        }

        public Criteria andDayInAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("DayInAmount >=", value, "dayInAmount");
            return (Criteria) this;
        }

        public Criteria andDayInAmountLessThan(Long value) {
            addCriterion("DayInAmount <", value, "dayInAmount");
            return (Criteria) this;
        }

        public Criteria andDayInAmountLessThanOrEqualTo(Long value) {
            addCriterion("DayInAmount <=", value, "dayInAmount");
            return (Criteria) this;
        }

        public Criteria andDayInAmountIn(List<Long> values) {
            addCriterion("DayInAmount in", values, "dayInAmount");
            return (Criteria) this;
        }

        public Criteria andDayInAmountNotIn(List<Long> values) {
            addCriterion("DayInAmount not in", values, "dayInAmount");
            return (Criteria) this;
        }

        public Criteria andDayInAmountBetween(Long value1, Long value2) {
            addCriterion("DayInAmount between", value1, value2, "dayInAmount");
            return (Criteria) this;
        }

        public Criteria andDayInAmountNotBetween(Long value1, Long value2) {
            addCriterion("DayInAmount not between", value1, value2, "dayInAmount");
            return (Criteria) this;
        }

        public Criteria andDayInNumberIsNull() {
            addCriterion("DayInNumber is null");
            return (Criteria) this;
        }

        public Criteria andDayInNumberIsNotNull() {
            addCriterion("DayInNumber is not null");
            return (Criteria) this;
        }

        public Criteria andDayInNumberEqualTo(Long value) {
            addCriterion("DayInNumber =", value, "dayInNumber");
            return (Criteria) this;
        }

        public Criteria andDayInNumberNotEqualTo(Long value) {
            addCriterion("DayInNumber <>", value, "dayInNumber");
            return (Criteria) this;
        }

        public Criteria andDayInNumberGreaterThan(Long value) {
            addCriterion("DayInNumber >", value, "dayInNumber");
            return (Criteria) this;
        }

        public Criteria andDayInNumberGreaterThanOrEqualTo(Long value) {
            addCriterion("DayInNumber >=", value, "dayInNumber");
            return (Criteria) this;
        }

        public Criteria andDayInNumberLessThan(Long value) {
            addCriterion("DayInNumber <", value, "dayInNumber");
            return (Criteria) this;
        }

        public Criteria andDayInNumberLessThanOrEqualTo(Long value) {
            addCriterion("DayInNumber <=", value, "dayInNumber");
            return (Criteria) this;
        }

        public Criteria andDayInNumberIn(List<Long> values) {
            addCriterion("DayInNumber in", values, "dayInNumber");
            return (Criteria) this;
        }

        public Criteria andDayInNumberNotIn(List<Long> values) {
            addCriterion("DayInNumber not in", values, "dayInNumber");
            return (Criteria) this;
        }

        public Criteria andDayInNumberBetween(Long value1, Long value2) {
            addCriterion("DayInNumber between", value1, value2, "dayInNumber");
            return (Criteria) this;
        }

        public Criteria andDayInNumberNotBetween(Long value1, Long value2) {
            addCriterion("DayInNumber not between", value1, value2, "dayInNumber");
            return (Criteria) this;
        }

        public Criteria andWeightIsNull() {
            addCriterion("Weight is null");
            return (Criteria) this;
        }

        public Criteria andWeightIsNotNull() {
            addCriterion("Weight is not null");
            return (Criteria) this;
        }

        public Criteria andWeightEqualTo(BigDecimal value) {
            addCriterion("Weight =", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotEqualTo(BigDecimal value) {
            addCriterion("Weight <>", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThan(BigDecimal value) {
            addCriterion("Weight >", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("Weight >=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThan(BigDecimal value) {
            addCriterion("Weight <", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThanOrEqualTo(BigDecimal value) {
            addCriterion("Weight <=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightIn(List<BigDecimal> values) {
            addCriterion("Weight in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotIn(List<BigDecimal> values) {
            addCriterion("Weight not in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("Weight between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("Weight not between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andInStatusIsNull() {
            addCriterion("InStatus is null");
            return (Criteria) this;
        }

        public Criteria andInStatusIsNotNull() {
            addCriterion("InStatus is not null");
            return (Criteria) this;
        }

        public Criteria andInStatusEqualTo(Byte value) {
            addCriterion("InStatus =", value, "inStatus");
            return (Criteria) this;
        }

        public Criteria andInStatusNotEqualTo(Byte value) {
            addCriterion("InStatus <>", value, "inStatus");
            return (Criteria) this;
        }

        public Criteria andInStatusGreaterThan(Byte value) {
            addCriterion("InStatus >", value, "inStatus");
            return (Criteria) this;
        }

        public Criteria andInStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("InStatus >=", value, "inStatus");
            return (Criteria) this;
        }

        public Criteria andInStatusLessThan(Byte value) {
            addCriterion("InStatus <", value, "inStatus");
            return (Criteria) this;
        }

        public Criteria andInStatusLessThanOrEqualTo(Byte value) {
            addCriterion("InStatus <=", value, "inStatus");
            return (Criteria) this;
        }

        public Criteria andInStatusIn(List<Byte> values) {
            addCriterion("InStatus in", values, "inStatus");
            return (Criteria) this;
        }

        public Criteria andInStatusNotIn(List<Byte> values) {
            addCriterion("InStatus not in", values, "inStatus");
            return (Criteria) this;
        }

        public Criteria andInStatusBetween(Byte value1, Byte value2) {
            addCriterion("InStatus between", value1, value2, "inStatus");
            return (Criteria) this;
        }

        public Criteria andInStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("InStatus not between", value1, value2, "inStatus");
            return (Criteria) this;
        }

        public Criteria andStartPayUserIsNull() {
            addCriterion("StartPayUser is null");
            return (Criteria) this;
        }

        public Criteria andStartPayUserIsNotNull() {
            addCriterion("StartPayUser is not null");
            return (Criteria) this;
        }

        public Criteria andStartPayUserEqualTo(String value) {
            addCriterion("StartPayUser =", value, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserNotEqualTo(String value) {
            addCriterion("StartPayUser <>", value, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserGreaterThan(String value) {
            addCriterion("StartPayUser >", value, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserGreaterThanOrEqualTo(String value) {
            addCriterion("StartPayUser >=", value, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserLessThan(String value) {
            addCriterion("StartPayUser <", value, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserLessThanOrEqualTo(String value) {
            addCriterion("StartPayUser <=", value, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserLike(String value) {
            addCriterion("StartPayUser like", value, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserNotLike(String value) {
            addCriterion("StartPayUser not like", value, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserIn(List<String> values) {
            addCriterion("StartPayUser in", values, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserNotIn(List<String> values) {
            addCriterion("StartPayUser not in", values, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserBetween(String value1, String value2) {
            addCriterion("StartPayUser between", value1, value2, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayUserNotBetween(String value1, String value2) {
            addCriterion("StartPayUser not between", value1, value2, "startPayUser");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeIsNull() {
            addCriterion("StartPayTime is null");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeIsNotNull() {
            addCriterion("StartPayTime is not null");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeEqualTo(Date value) {
            addCriterion("StartPayTime =", value, "startPayTime");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeNotEqualTo(Date value) {
            addCriterion("StartPayTime <>", value, "startPayTime");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeGreaterThan(Date value) {
            addCriterion("StartPayTime >", value, "startPayTime");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("StartPayTime >=", value, "startPayTime");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeLessThan(Date value) {
            addCriterion("StartPayTime <", value, "startPayTime");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeLessThanOrEqualTo(Date value) {
            addCriterion("StartPayTime <=", value, "startPayTime");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeIn(List<Date> values) {
            addCriterion("StartPayTime in", values, "startPayTime");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeNotIn(List<Date> values) {
            addCriterion("StartPayTime not in", values, "startPayTime");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeBetween(Date value1, Date value2) {
            addCriterion("StartPayTime between", value1, value2, "startPayTime");
            return (Criteria) this;
        }

        public Criteria andStartPayTimeNotBetween(Date value1, Date value2) {
            addCriterion("StartPayTime not between", value1, value2, "startPayTime");
            return (Criteria) this;
        }

        public Criteria andLoginStatusIsNull() {
            addCriterion("LoginStatus is null");
            return (Criteria) this;
        }

        public Criteria andLoginStatusIsNotNull() {
            addCriterion("LoginStatus is not null");
            return (Criteria) this;
        }

        public Criteria andLoginStatusEqualTo(Integer value) {
            addCriterion("LoginStatus =", value, "loginStatus");
            return (Criteria) this;
        }

        public Criteria andLoginStatusNotEqualTo(Integer value) {
            addCriterion("LoginStatus <>", value, "loginStatus");
            return (Criteria) this;
        }

        public Criteria andLoginStatusGreaterThan(Integer value) {
            addCriterion("LoginStatus >", value, "loginStatus");
            return (Criteria) this;
        }

        public Criteria andLoginStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("LoginStatus >=", value, "loginStatus");
            return (Criteria) this;
        }

        public Criteria andLoginStatusLessThan(Integer value) {
            addCriterion("LoginStatus <", value, "loginStatus");
            return (Criteria) this;
        }

        public Criteria andLoginStatusLessThanOrEqualTo(Integer value) {
            addCriterion("LoginStatus <=", value, "loginStatus");
            return (Criteria) this;
        }

        public Criteria andLoginStatusIn(List<Integer> values) {
            addCriterion("LoginStatus in", values, "loginStatus");
            return (Criteria) this;
        }

        public Criteria andLoginStatusNotIn(List<Integer> values) {
            addCriterion("LoginStatus not in", values, "loginStatus");
            return (Criteria) this;
        }

        public Criteria andLoginStatusBetween(Integer value1, Integer value2) {
            addCriterion("LoginStatus between", value1, value2, "loginStatus");
            return (Criteria) this;
        }

        public Criteria andLoginStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("LoginStatus not between", value1, value2, "loginStatus");
            return (Criteria) this;
        }

        public Criteria andLoginResultIsNull() {
            addCriterion("LoginResult is null");
            return (Criteria) this;
        }

        public Criteria andLoginResultIsNotNull() {
            addCriterion("LoginResult is not null");
            return (Criteria) this;
        }

        public Criteria andLoginResultEqualTo(String value) {
            addCriterion("LoginResult =", value, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultNotEqualTo(String value) {
            addCriterion("LoginResult <>", value, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultGreaterThan(String value) {
            addCriterion("LoginResult >", value, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultGreaterThanOrEqualTo(String value) {
            addCriterion("LoginResult >=", value, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultLessThan(String value) {
            addCriterion("LoginResult <", value, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultLessThanOrEqualTo(String value) {
            addCriterion("LoginResult <=", value, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultLike(String value) {
            addCriterion("LoginResult like", value, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultNotLike(String value) {
            addCriterion("LoginResult not like", value, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultIn(List<String> values) {
            addCriterion("LoginResult in", values, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultNotIn(List<String> values) {
            addCriterion("LoginResult not in", values, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultBetween(String value1, String value2) {
            addCriterion("LoginResult between", value1, value2, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginResultNotBetween(String value1, String value2) {
            addCriterion("LoginResult not between", value1, value2, "loginResult");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeIsNull() {
            addCriterion("LoginSyncTime is null");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeIsNotNull() {
            addCriterion("LoginSyncTime is not null");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeEqualTo(Date value) {
            addCriterion("LoginSyncTime =", value, "loginSyncTime");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeNotEqualTo(Date value) {
            addCriterion("LoginSyncTime <>", value, "loginSyncTime");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeGreaterThan(Date value) {
            addCriterion("LoginSyncTime >", value, "loginSyncTime");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LoginSyncTime >=", value, "loginSyncTime");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeLessThan(Date value) {
            addCriterion("LoginSyncTime <", value, "loginSyncTime");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeLessThanOrEqualTo(Date value) {
            addCriterion("LoginSyncTime <=", value, "loginSyncTime");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeIn(List<Date> values) {
            addCriterion("LoginSyncTime in", values, "loginSyncTime");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeNotIn(List<Date> values) {
            addCriterion("LoginSyncTime not in", values, "loginSyncTime");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeBetween(Date value1, Date value2) {
            addCriterion("LoginSyncTime between", value1, value2, "loginSyncTime");
            return (Criteria) this;
        }

        public Criteria andLoginSyncTimeNotBetween(Date value1, Date value2) {
            addCriterion("LoginSyncTime not between", value1, value2, "loginSyncTime");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeIsNull() {
            addCriterion("DayUpdateTime is null");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeIsNotNull() {
            addCriterion("DayUpdateTime is not null");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeEqualTo(Date value) {
            addCriterion("DayUpdateTime =", value, "dayUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeNotEqualTo(Date value) {
            addCriterion("DayUpdateTime <>", value, "dayUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeGreaterThan(Date value) {
            addCriterion("DayUpdateTime >", value, "dayUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("DayUpdateTime >=", value, "dayUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeLessThan(Date value) {
            addCriterion("DayUpdateTime <", value, "dayUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("DayUpdateTime <=", value, "dayUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeIn(List<Date> values) {
            addCriterion("DayUpdateTime in", values, "dayUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeNotIn(List<Date> values) {
            addCriterion("DayUpdateTime not in", values, "dayUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("DayUpdateTime between", value1, value2, "dayUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDayUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("DayUpdateTime not between", value1, value2, "dayUpdateTime");
            return (Criteria) this;
        }

        public Criteria andLastInTimeIsNull() {
            addCriterion("LastInTime is null");
            return (Criteria) this;
        }

        public Criteria andLastInTimeIsNotNull() {
            addCriterion("LastInTime is not null");
            return (Criteria) this;
        }

        public Criteria andLastInTimeEqualTo(Date value) {
            addCriterion("LastInTime =", value, "lastInTime");
            return (Criteria) this;
        }

        public Criteria andLastInTimeNotEqualTo(Date value) {
            addCriterion("LastInTime <>", value, "lastInTime");
            return (Criteria) this;
        }

        public Criteria andLastInTimeGreaterThan(Date value) {
            addCriterion("LastInTime >", value, "lastInTime");
            return (Criteria) this;
        }

        public Criteria andLastInTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LastInTime >=", value, "lastInTime");
            return (Criteria) this;
        }

        public Criteria andLastInTimeLessThan(Date value) {
            addCriterion("LastInTime <", value, "lastInTime");
            return (Criteria) this;
        }

        public Criteria andLastInTimeLessThanOrEqualTo(Date value) {
            addCriterion("LastInTime <=", value, "lastInTime");
            return (Criteria) this;
        }

        public Criteria andLastInTimeIn(List<Date> values) {
            addCriterion("LastInTime in", values, "lastInTime");
            return (Criteria) this;
        }

        public Criteria andLastInTimeNotIn(List<Date> values) {
            addCriterion("LastInTime not in", values, "lastInTime");
            return (Criteria) this;
        }

        public Criteria andLastInTimeBetween(Date value1, Date value2) {
            addCriterion("LastInTime between", value1, value2, "lastInTime");
            return (Criteria) this;
        }

        public Criteria andLastInTimeNotBetween(Date value1, Date value2) {
            addCriterion("LastInTime not between", value1, value2, "lastInTime");
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