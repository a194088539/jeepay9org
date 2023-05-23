package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MchInfoExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public MchInfoExample() {
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

        public Criteria andMobileIsNull() {
            addCriterion("Mobile is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("Mobile is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(Long value) {
            addCriterion("Mobile =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(Long value) {
            addCriterion("Mobile <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(Long value) {
            addCriterion("Mobile >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(Long value) {
            addCriterion("Mobile >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(Long value) {
            addCriterion("Mobile <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(Long value) {
            addCriterion("Mobile <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<Long> values) {
            addCriterion("Mobile in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<Long> values) {
            addCriterion("Mobile not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(Long value1, Long value2) {
            addCriterion("Mobile between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(Long value1, Long value2) {
            addCriterion("Mobile not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("Email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("Email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("Email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("Email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("Email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("Email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("Email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("Email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("Email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("Email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("Email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("Email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("Email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("Email not between", value1, value2, "email");
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

        public Criteria andPayPasswordIsNull() {
            addCriterion("PayPassword is null");
            return (Criteria) this;
        }

        public Criteria andPayPasswordIsNotNull() {
            addCriterion("PayPassword is not null");
            return (Criteria) this;
        }

        public Criteria andPayPasswordEqualTo(String value) {
            addCriterion("PayPassword =", value, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordNotEqualTo(String value) {
            addCriterion("PayPassword <>", value, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordGreaterThan(String value) {
            addCriterion("PayPassword >", value, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("PayPassword >=", value, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordLessThan(String value) {
            addCriterion("PayPassword <", value, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordLessThanOrEqualTo(String value) {
            addCriterion("PayPassword <=", value, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordLike(String value) {
            addCriterion("PayPassword like", value, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordNotLike(String value) {
            addCriterion("PayPassword not like", value, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordIn(List<String> values) {
            addCriterion("PayPassword in", values, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordNotIn(List<String> values) {
            addCriterion("PayPassword not in", values, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordBetween(String value1, String value2) {
            addCriterion("PayPassword between", value1, value2, "payPassword");
            return (Criteria) this;
        }

        public Criteria andPayPasswordNotBetween(String value1, String value2) {
            addCriterion("PayPassword not between", value1, value2, "payPassword");
            return (Criteria) this;
        }

        public Criteria andRoleIsNull() {
            addCriterion("Role is null");
            return (Criteria) this;
        }

        public Criteria andRoleIsNotNull() {
            addCriterion("Role is not null");
            return (Criteria) this;
        }

        public Criteria andRoleEqualTo(String value) {
            addCriterion("Role =", value, "role");
            return (Criteria) this;
        }

        public Criteria andRoleNotEqualTo(String value) {
            addCriterion("Role <>", value, "role");
            return (Criteria) this;
        }

        public Criteria andRoleGreaterThan(String value) {
            addCriterion("Role >", value, "role");
            return (Criteria) this;
        }

        public Criteria andRoleGreaterThanOrEqualTo(String value) {
            addCriterion("Role >=", value, "role");
            return (Criteria) this;
        }

        public Criteria andRoleLessThan(String value) {
            addCriterion("Role <", value, "role");
            return (Criteria) this;
        }

        public Criteria andRoleLessThanOrEqualTo(String value) {
            addCriterion("Role <=", value, "role");
            return (Criteria) this;
        }

        public Criteria andRoleLike(String value) {
            addCriterion("Role like", value, "role");
            return (Criteria) this;
        }

        public Criteria andRoleNotLike(String value) {
            addCriterion("Role not like", value, "role");
            return (Criteria) this;
        }

        public Criteria andRoleIn(List<String> values) {
            addCriterion("Role in", values, "role");
            return (Criteria) this;
        }

        public Criteria andRoleNotIn(List<String> values) {
            addCriterion("Role not in", values, "role");
            return (Criteria) this;
        }

        public Criteria andRoleBetween(String value1, String value2) {
            addCriterion("Role between", value1, value2, "role");
            return (Criteria) this;
        }

        public Criteria andRoleNotBetween(String value1, String value2) {
            addCriterion("Role not between", value1, value2, "role");
            return (Criteria) this;
        }

        public Criteria andMchRateIsNull() {
            addCriterion("MchRate is null");
            return (Criteria) this;
        }

        public Criteria andMchRateIsNotNull() {
            addCriterion("MchRate is not null");
            return (Criteria) this;
        }

        public Criteria andMchRateEqualTo(BigDecimal value) {
            addCriterion("MchRate =", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateNotEqualTo(BigDecimal value) {
            addCriterion("MchRate <>", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateGreaterThan(BigDecimal value) {
            addCriterion("MchRate >", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("MchRate >=", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateLessThan(BigDecimal value) {
            addCriterion("MchRate <", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("MchRate <=", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateIn(List<BigDecimal> values) {
            addCriterion("MchRate in", values, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateNotIn(List<BigDecimal> values) {
            addCriterion("MchRate not in", values, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MchRate between", value1, value2, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MchRate not between", value1, value2, "mchRate");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyIsNull() {
            addCriterion("PrivateKey is null");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyIsNotNull() {
            addCriterion("PrivateKey is not null");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyEqualTo(String value) {
            addCriterion("PrivateKey =", value, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyNotEqualTo(String value) {
            addCriterion("PrivateKey <>", value, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyGreaterThan(String value) {
            addCriterion("PrivateKey >", value, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyGreaterThanOrEqualTo(String value) {
            addCriterion("PrivateKey >=", value, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyLessThan(String value) {
            addCriterion("PrivateKey <", value, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyLessThanOrEqualTo(String value) {
            addCriterion("PrivateKey <=", value, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyLike(String value) {
            addCriterion("PrivateKey like", value, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyNotLike(String value) {
            addCriterion("PrivateKey not like", value, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyIn(List<String> values) {
            addCriterion("PrivateKey in", values, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyNotIn(List<String> values) {
            addCriterion("PrivateKey not in", values, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyBetween(String value1, String value2) {
            addCriterion("PrivateKey between", value1, value2, "privateKey");
            return (Criteria) this;
        }

        public Criteria andPrivateKeyNotBetween(String value1, String value2) {
            addCriterion("PrivateKey not between", value1, value2, "privateKey");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("Address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("Address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("Address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("Address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("Address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("Address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("Address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("Address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("Address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("Address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("Address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("Address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("Address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("Address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAccountAttrIsNull() {
            addCriterion("AccountAttr is null");
            return (Criteria) this;
        }

        public Criteria andAccountAttrIsNotNull() {
            addCriterion("AccountAttr is not null");
            return (Criteria) this;
        }

        public Criteria andAccountAttrEqualTo(Byte value) {
            addCriterion("AccountAttr =", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrNotEqualTo(Byte value) {
            addCriterion("AccountAttr <>", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrGreaterThan(Byte value) {
            addCriterion("AccountAttr >", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrGreaterThanOrEqualTo(Byte value) {
            addCriterion("AccountAttr >=", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrLessThan(Byte value) {
            addCriterion("AccountAttr <", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrLessThanOrEqualTo(Byte value) {
            addCriterion("AccountAttr <=", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrIn(List<Byte> values) {
            addCriterion("AccountAttr in", values, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrNotIn(List<Byte> values) {
            addCriterion("AccountAttr not in", values, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrBetween(Byte value1, Byte value2) {
            addCriterion("AccountAttr between", value1, value2, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrNotBetween(Byte value1, Byte value2) {
            addCriterion("AccountAttr not between", value1, value2, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIsNull() {
            addCriterion("AccountType is null");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIsNotNull() {
            addCriterion("AccountType is not null");
            return (Criteria) this;
        }

        public Criteria andAccountTypeEqualTo(Byte value) {
            addCriterion("AccountType =", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotEqualTo(Byte value) {
            addCriterion("AccountType <>", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThan(Byte value) {
            addCriterion("AccountType >", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("AccountType >=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThan(Byte value) {
            addCriterion("AccountType <", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThanOrEqualTo(Byte value) {
            addCriterion("AccountType <=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIn(List<Byte> values) {
            addCriterion("AccountType in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotIn(List<Byte> values) {
            addCriterion("AccountType not in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeBetween(Byte value1, Byte value2) {
            addCriterion("AccountType between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("AccountType not between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNull() {
            addCriterion("BankName is null");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNotNull() {
            addCriterion("BankName is not null");
            return (Criteria) this;
        }

        public Criteria andBankNameEqualTo(String value) {
            addCriterion("BankName =", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotEqualTo(String value) {
            addCriterion("BankName <>", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThan(String value) {
            addCriterion("BankName >", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThanOrEqualTo(String value) {
            addCriterion("BankName >=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThan(String value) {
            addCriterion("BankName <", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThanOrEqualTo(String value) {
            addCriterion("BankName <=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLike(String value) {
            addCriterion("BankName like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotLike(String value) {
            addCriterion("BankName not like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameIn(List<String> values) {
            addCriterion("BankName in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotIn(List<String> values) {
            addCriterion("BankName not in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameBetween(String value1, String value2) {
            addCriterion("BankName between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotBetween(String value1, String value2) {
            addCriterion("BankName not between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameIsNull() {
            addCriterion("BankNetName is null");
            return (Criteria) this;
        }

        public Criteria andBankNetNameIsNotNull() {
            addCriterion("BankNetName is not null");
            return (Criteria) this;
        }

        public Criteria andBankNetNameEqualTo(String value) {
            addCriterion("BankNetName =", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotEqualTo(String value) {
            addCriterion("BankNetName <>", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameGreaterThan(String value) {
            addCriterion("BankNetName >", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameGreaterThanOrEqualTo(String value) {
            addCriterion("BankNetName >=", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameLessThan(String value) {
            addCriterion("BankNetName <", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameLessThanOrEqualTo(String value) {
            addCriterion("BankNetName <=", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameLike(String value) {
            addCriterion("BankNetName like", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotLike(String value) {
            addCriterion("BankNetName not like", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameIn(List<String> values) {
            addCriterion("BankNetName in", values, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotIn(List<String> values) {
            addCriterion("BankNetName not in", values, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameBetween(String value1, String value2) {
            addCriterion("BankNetName between", value1, value2, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotBetween(String value1, String value2) {
            addCriterion("BankNetName not between", value1, value2, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andAccountNameIsNull() {
            addCriterion("AccountName is null");
            return (Criteria) this;
        }

        public Criteria andAccountNameIsNotNull() {
            addCriterion("AccountName is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNameEqualTo(String value) {
            addCriterion("AccountName =", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotEqualTo(String value) {
            addCriterion("AccountName <>", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameGreaterThan(String value) {
            addCriterion("AccountName >", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameGreaterThanOrEqualTo(String value) {
            addCriterion("AccountName >=", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameLessThan(String value) {
            addCriterion("AccountName <", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameLessThanOrEqualTo(String value) {
            addCriterion("AccountName <=", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameLike(String value) {
            addCriterion("AccountName like", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotLike(String value) {
            addCriterion("AccountName not like", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameIn(List<String> values) {
            addCriterion("AccountName in", values, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotIn(List<String> values) {
            addCriterion("AccountName not in", values, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameBetween(String value1, String value2) {
            addCriterion("AccountName between", value1, value2, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotBetween(String value1, String value2) {
            addCriterion("AccountName not between", value1, value2, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNoIsNull() {
            addCriterion("AccountNo is null");
            return (Criteria) this;
        }

        public Criteria andAccountNoIsNotNull() {
            addCriterion("AccountNo is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNoEqualTo(String value) {
            addCriterion("AccountNo =", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotEqualTo(String value) {
            addCriterion("AccountNo <>", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoGreaterThan(String value) {
            addCriterion("AccountNo >", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoGreaterThanOrEqualTo(String value) {
            addCriterion("AccountNo >=", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoLessThan(String value) {
            addCriterion("AccountNo <", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoLessThanOrEqualTo(String value) {
            addCriterion("AccountNo <=", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoLike(String value) {
            addCriterion("AccountNo like", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotLike(String value) {
            addCriterion("AccountNo not like", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoIn(List<String> values) {
            addCriterion("AccountNo in", values, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotIn(List<String> values) {
            addCriterion("AccountNo not in", values, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoBetween(String value1, String value2) {
            addCriterion("AccountNo between", value1, value2, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotBetween(String value1, String value2) {
            addCriterion("AccountNo not between", value1, value2, "accountNo");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNull() {
            addCriterion("Province is null");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNotNull() {
            addCriterion("Province is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceEqualTo(String value) {
            addCriterion("Province =", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotEqualTo(String value) {
            addCriterion("Province <>", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThan(String value) {
            addCriterion("Province >", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("Province >=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThan(String value) {
            addCriterion("Province <", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThanOrEqualTo(String value) {
            addCriterion("Province <=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLike(String value) {
            addCriterion("Province like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotLike(String value) {
            addCriterion("Province not like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceIn(List<String> values) {
            addCriterion("Province in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotIn(List<String> values) {
            addCriterion("Province not in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceBetween(String value1, String value2) {
            addCriterion("Province between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotBetween(String value1, String value2) {
            addCriterion("Province not between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andCityIsNull() {
            addCriterion("City is null");
            return (Criteria) this;
        }

        public Criteria andCityIsNotNull() {
            addCriterion("City is not null");
            return (Criteria) this;
        }

        public Criteria andCityEqualTo(String value) {
            addCriterion("City =", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotEqualTo(String value) {
            addCriterion("City <>", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThan(String value) {
            addCriterion("City >", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThanOrEqualTo(String value) {
            addCriterion("City >=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThan(String value) {
            addCriterion("City <", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThanOrEqualTo(String value) {
            addCriterion("City <=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("City like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotLike(String value) {
            addCriterion("City not like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityIn(List<String> values) {
            addCriterion("City in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotIn(List<String> values) {
            addCriterion("City not in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityBetween(String value1, String value2) {
            addCriterion("City between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotBetween(String value1, String value2) {
            addCriterion("City not between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andFeeTypeIsNull() {
            addCriterion("FeeType is null");
            return (Criteria) this;
        }

        public Criteria andFeeTypeIsNotNull() {
            addCriterion("FeeType is not null");
            return (Criteria) this;
        }

        public Criteria andFeeTypeEqualTo(Byte value) {
            addCriterion("FeeType =", value, "feeType");
            return (Criteria) this;
        }

        public Criteria andFeeTypeNotEqualTo(Byte value) {
            addCriterion("FeeType <>", value, "feeType");
            return (Criteria) this;
        }

        public Criteria andFeeTypeGreaterThan(Byte value) {
            addCriterion("FeeType >", value, "feeType");
            return (Criteria) this;
        }

        public Criteria andFeeTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("FeeType >=", value, "feeType");
            return (Criteria) this;
        }

        public Criteria andFeeTypeLessThan(Byte value) {
            addCriterion("FeeType <", value, "feeType");
            return (Criteria) this;
        }

        public Criteria andFeeTypeLessThanOrEqualTo(Byte value) {
            addCriterion("FeeType <=", value, "feeType");
            return (Criteria) this;
        }

        public Criteria andFeeTypeIn(List<Byte> values) {
            addCriterion("FeeType in", values, "feeType");
            return (Criteria) this;
        }

        public Criteria andFeeTypeNotIn(List<Byte> values) {
            addCriterion("FeeType not in", values, "feeType");
            return (Criteria) this;
        }

        public Criteria andFeeTypeBetween(Byte value1, Byte value2) {
            addCriterion("FeeType between", value1, value2, "feeType");
            return (Criteria) this;
        }

        public Criteria andFeeTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("FeeType not between", value1, value2, "feeType");
            return (Criteria) this;
        }

        public Criteria andFeeRateIsNull() {
            addCriterion("FeeRate is null");
            return (Criteria) this;
        }

        public Criteria andFeeRateIsNotNull() {
            addCriterion("FeeRate is not null");
            return (Criteria) this;
        }

        public Criteria andFeeRateEqualTo(BigDecimal value) {
            addCriterion("FeeRate =", value, "feeRate");
            return (Criteria) this;
        }

        public Criteria andFeeRateNotEqualTo(BigDecimal value) {
            addCriterion("FeeRate <>", value, "feeRate");
            return (Criteria) this;
        }

        public Criteria andFeeRateGreaterThan(BigDecimal value) {
            addCriterion("FeeRate >", value, "feeRate");
            return (Criteria) this;
        }

        public Criteria andFeeRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("FeeRate >=", value, "feeRate");
            return (Criteria) this;
        }

        public Criteria andFeeRateLessThan(BigDecimal value) {
            addCriterion("FeeRate <", value, "feeRate");
            return (Criteria) this;
        }

        public Criteria andFeeRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("FeeRate <=", value, "feeRate");
            return (Criteria) this;
        }

        public Criteria andFeeRateIn(List<BigDecimal> values) {
            addCriterion("FeeRate in", values, "feeRate");
            return (Criteria) this;
        }

        public Criteria andFeeRateNotIn(List<BigDecimal> values) {
            addCriterion("FeeRate not in", values, "feeRate");
            return (Criteria) this;
        }

        public Criteria andFeeRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("FeeRate between", value1, value2, "feeRate");
            return (Criteria) this;
        }

        public Criteria andFeeRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("FeeRate not between", value1, value2, "feeRate");
            return (Criteria) this;
        }

        public Criteria andFeeLevelIsNull() {
            addCriterion("FeeLevel is null");
            return (Criteria) this;
        }

        public Criteria andFeeLevelIsNotNull() {
            addCriterion("FeeLevel is not null");
            return (Criteria) this;
        }

        public Criteria andFeeLevelEqualTo(String value) {
            addCriterion("FeeLevel =", value, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelNotEqualTo(String value) {
            addCriterion("FeeLevel <>", value, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelGreaterThan(String value) {
            addCriterion("FeeLevel >", value, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelGreaterThanOrEqualTo(String value) {
            addCriterion("FeeLevel >=", value, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelLessThan(String value) {
            addCriterion("FeeLevel <", value, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelLessThanOrEqualTo(String value) {
            addCriterion("FeeLevel <=", value, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelLike(String value) {
            addCriterion("FeeLevel like", value, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelNotLike(String value) {
            addCriterion("FeeLevel not like", value, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelIn(List<String> values) {
            addCriterion("FeeLevel in", values, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelNotIn(List<String> values) {
            addCriterion("FeeLevel not in", values, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelBetween(String value1, String value2) {
            addCriterion("FeeLevel between", value1, value2, "feeLevel");
            return (Criteria) this;
        }

        public Criteria andFeeLevelNotBetween(String value1, String value2) {
            addCriterion("FeeLevel not between", value1, value2, "feeLevel");
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

        public Criteria andMaxDrawAmountIsNull() {
            addCriterion("MaxDrawAmount is null");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountIsNotNull() {
            addCriterion("MaxDrawAmount is not null");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountEqualTo(Long value) {
            addCriterion("MaxDrawAmount =", value, "maxDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountNotEqualTo(Long value) {
            addCriterion("MaxDrawAmount <>", value, "maxDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountGreaterThan(Long value) {
            addCriterion("MaxDrawAmount >", value, "maxDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("MaxDrawAmount >=", value, "maxDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountLessThan(Long value) {
            addCriterion("MaxDrawAmount <", value, "maxDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountLessThanOrEqualTo(Long value) {
            addCriterion("MaxDrawAmount <=", value, "maxDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountIn(List<Long> values) {
            addCriterion("MaxDrawAmount in", values, "maxDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountNotIn(List<Long> values) {
            addCriterion("MaxDrawAmount not in", values, "maxDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountBetween(Long value1, Long value2) {
            addCriterion("MaxDrawAmount between", value1, value2, "maxDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDrawAmountNotBetween(Long value1, Long value2) {
            addCriterion("MaxDrawAmount not between", value1, value2, "maxDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountIsNull() {
            addCriterion("MinDrawAmount is null");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountIsNotNull() {
            addCriterion("MinDrawAmount is not null");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountEqualTo(Long value) {
            addCriterion("MinDrawAmount =", value, "minDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountNotEqualTo(Long value) {
            addCriterion("MinDrawAmount <>", value, "minDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountGreaterThan(Long value) {
            addCriterion("MinDrawAmount >", value, "minDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("MinDrawAmount >=", value, "minDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountLessThan(Long value) {
            addCriterion("MinDrawAmount <", value, "minDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountLessThanOrEqualTo(Long value) {
            addCriterion("MinDrawAmount <=", value, "minDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountIn(List<Long> values) {
            addCriterion("MinDrawAmount in", values, "minDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountNotIn(List<Long> values) {
            addCriterion("MinDrawAmount not in", values, "minDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountBetween(Long value1, Long value2) {
            addCriterion("MinDrawAmount between", value1, value2, "minDrawAmount");
            return (Criteria) this;
        }

        public Criteria andMinDrawAmountNotBetween(Long value1, Long value2) {
            addCriterion("MinDrawAmount not between", value1, value2, "minDrawAmount");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesIsNull() {
            addCriterion("DayDrawTimes is null");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesIsNotNull() {
            addCriterion("DayDrawTimes is not null");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesEqualTo(Integer value) {
            addCriterion("DayDrawTimes =", value, "dayDrawTimes");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesNotEqualTo(Integer value) {
            addCriterion("DayDrawTimes <>", value, "dayDrawTimes");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesGreaterThan(Integer value) {
            addCriterion("DayDrawTimes >", value, "dayDrawTimes");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("DayDrawTimes >=", value, "dayDrawTimes");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesLessThan(Integer value) {
            addCriterion("DayDrawTimes <", value, "dayDrawTimes");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesLessThanOrEqualTo(Integer value) {
            addCriterion("DayDrawTimes <=", value, "dayDrawTimes");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesIn(List<Integer> values) {
            addCriterion("DayDrawTimes in", values, "dayDrawTimes");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesNotIn(List<Integer> values) {
            addCriterion("DayDrawTimes not in", values, "dayDrawTimes");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesBetween(Integer value1, Integer value2) {
            addCriterion("DayDrawTimes between", value1, value2, "dayDrawTimes");
            return (Criteria) this;
        }

        public Criteria andDayDrawTimesNotBetween(Integer value1, Integer value2) {
            addCriterion("DayDrawTimes not between", value1, value2, "dayDrawTimes");
            return (Criteria) this;
        }

        public Criteria andSettTypeIsNull() {
            addCriterion("SettType is null");
            return (Criteria) this;
        }

        public Criteria andSettTypeIsNotNull() {
            addCriterion("SettType is not null");
            return (Criteria) this;
        }

        public Criteria andSettTypeEqualTo(Byte value) {
            addCriterion("SettType =", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeNotEqualTo(Byte value) {
            addCriterion("SettType <>", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeGreaterThan(Byte value) {
            addCriterion("SettType >", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("SettType >=", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeLessThan(Byte value) {
            addCriterion("SettType <", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeLessThanOrEqualTo(Byte value) {
            addCriterion("SettType <=", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeIn(List<Byte> values) {
            addCriterion("SettType in", values, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeNotIn(List<Byte> values) {
            addCriterion("SettType not in", values, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeBetween(Byte value1, Byte value2) {
            addCriterion("SettType between", value1, value2, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("SettType not between", value1, value2, "settType");
            return (Criteria) this;
        }

        public Criteria andSettModeIsNull() {
            addCriterion("SettMode is null");
            return (Criteria) this;
        }

        public Criteria andSettModeIsNotNull() {
            addCriterion("SettMode is not null");
            return (Criteria) this;
        }

        public Criteria andSettModeEqualTo(Byte value) {
            addCriterion("SettMode =", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeNotEqualTo(Byte value) {
            addCriterion("SettMode <>", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeGreaterThan(Byte value) {
            addCriterion("SettMode >", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeGreaterThanOrEqualTo(Byte value) {
            addCriterion("SettMode >=", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeLessThan(Byte value) {
            addCriterion("SettMode <", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeLessThanOrEqualTo(Byte value) {
            addCriterion("SettMode <=", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeIn(List<Byte> values) {
            addCriterion("SettMode in", values, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeNotIn(List<Byte> values) {
            addCriterion("SettMode not in", values, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeBetween(Byte value1, Byte value2) {
            addCriterion("SettMode between", value1, value2, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeNotBetween(Byte value1, Byte value2) {
            addCriterion("SettMode not between", value1, value2, "settMode");
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

        public Criteria andLastPasswordResetTimeIsNull() {
            addCriterion("LastPasswordResetTime is null");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeIsNotNull() {
            addCriterion("LastPasswordResetTime is not null");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeEqualTo(Date value) {
            addCriterion("LastPasswordResetTime =", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeNotEqualTo(Date value) {
            addCriterion("LastPasswordResetTime <>", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeGreaterThan(Date value) {
            addCriterion("LastPasswordResetTime >", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LastPasswordResetTime >=", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeLessThan(Date value) {
            addCriterion("LastPasswordResetTime <", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeLessThanOrEqualTo(Date value) {
            addCriterion("LastPasswordResetTime <=", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeIn(List<Date> values) {
            addCriterion("LastPasswordResetTime in", values, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeNotIn(List<Date> values) {
            addCriterion("LastPasswordResetTime not in", values, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeBetween(Date value1, Date value2) {
            addCriterion("LastPasswordResetTime between", value1, value2, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeNotBetween(Date value1, Date value2) {
            addCriterion("LastPasswordResetTime not between", value1, value2, "lastPasswordResetTime");
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

        public Criteria andTagIsNull() {
            addCriterion("Tag is null");
            return (Criteria) this;
        }

        public Criteria andTagIsNotNull() {
            addCriterion("Tag is not null");
            return (Criteria) this;
        }

        public Criteria andTagEqualTo(String value) {
            addCriterion("Tag =", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotEqualTo(String value) {
            addCriterion("Tag <>", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagGreaterThan(String value) {
            addCriterion("Tag >", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagGreaterThanOrEqualTo(String value) {
            addCriterion("Tag >=", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLessThan(String value) {
            addCriterion("Tag <", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLessThanOrEqualTo(String value) {
            addCriterion("Tag <=", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLike(String value) {
            addCriterion("Tag like", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotLike(String value) {
            addCriterion("Tag not like", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagIn(List<String> values) {
            addCriterion("Tag in", values, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotIn(List<String> values) {
            addCriterion("Tag not in", values, "tag");
            return (Criteria) this;
        }

        public Criteria andTagBetween(String value1, String value2) {
            addCriterion("Tag between", value1, value2, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotBetween(String value1, String value2) {
            addCriterion("Tag not between", value1, value2, "tag");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeIsNull() {
            addCriterion("LoginSecurityType is null");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeIsNotNull() {
            addCriterion("LoginSecurityType is not null");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeEqualTo(Byte value) {
            addCriterion("LoginSecurityType =", value, "loginSecurityType");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeNotEqualTo(Byte value) {
            addCriterion("LoginSecurityType <>", value, "loginSecurityType");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeGreaterThan(Byte value) {
            addCriterion("LoginSecurityType >", value, "loginSecurityType");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("LoginSecurityType >=", value, "loginSecurityType");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeLessThan(Byte value) {
            addCriterion("LoginSecurityType <", value, "loginSecurityType");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeLessThanOrEqualTo(Byte value) {
            addCriterion("LoginSecurityType <=", value, "loginSecurityType");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeIn(List<Byte> values) {
            addCriterion("LoginSecurityType in", values, "loginSecurityType");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeNotIn(List<Byte> values) {
            addCriterion("LoginSecurityType not in", values, "loginSecurityType");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeBetween(Byte value1, Byte value2) {
            addCriterion("LoginSecurityType between", value1, value2, "loginSecurityType");
            return (Criteria) this;
        }

        public Criteria andLoginSecurityTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("LoginSecurityType not between", value1, value2, "loginSecurityType");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeIsNull() {
            addCriterion("PaySecurityType is null");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeIsNotNull() {
            addCriterion("PaySecurityType is not null");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeEqualTo(Byte value) {
            addCriterion("PaySecurityType =", value, "paySecurityType");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeNotEqualTo(Byte value) {
            addCriterion("PaySecurityType <>", value, "paySecurityType");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeGreaterThan(Byte value) {
            addCriterion("PaySecurityType >", value, "paySecurityType");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("PaySecurityType >=", value, "paySecurityType");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeLessThan(Byte value) {
            addCriterion("PaySecurityType <", value, "paySecurityType");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeLessThanOrEqualTo(Byte value) {
            addCriterion("PaySecurityType <=", value, "paySecurityType");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeIn(List<Byte> values) {
            addCriterion("PaySecurityType in", values, "paySecurityType");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeNotIn(List<Byte> values) {
            addCriterion("PaySecurityType not in", values, "paySecurityType");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeBetween(Byte value1, Byte value2) {
            addCriterion("PaySecurityType between", value1, value2, "paySecurityType");
            return (Criteria) this;
        }

        public Criteria andPaySecurityTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("PaySecurityType not between", value1, value2, "paySecurityType");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusIsNull() {
            addCriterion("GoogleAuthStatus is null");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusIsNotNull() {
            addCriterion("GoogleAuthStatus is not null");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusEqualTo(Byte value) {
            addCriterion("GoogleAuthStatus =", value, "googleAuthStatus");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusNotEqualTo(Byte value) {
            addCriterion("GoogleAuthStatus <>", value, "googleAuthStatus");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusGreaterThan(Byte value) {
            addCriterion("GoogleAuthStatus >", value, "googleAuthStatus");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("GoogleAuthStatus >=", value, "googleAuthStatus");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusLessThan(Byte value) {
            addCriterion("GoogleAuthStatus <", value, "googleAuthStatus");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusLessThanOrEqualTo(Byte value) {
            addCriterion("GoogleAuthStatus <=", value, "googleAuthStatus");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusIn(List<Byte> values) {
            addCriterion("GoogleAuthStatus in", values, "googleAuthStatus");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusNotIn(List<Byte> values) {
            addCriterion("GoogleAuthStatus not in", values, "googleAuthStatus");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusBetween(Byte value1, Byte value2) {
            addCriterion("GoogleAuthStatus between", value1, value2, "googleAuthStatus");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("GoogleAuthStatus not between", value1, value2, "googleAuthStatus");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyIsNull() {
            addCriterion("GoogleAuthSecretKey is null");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyIsNotNull() {
            addCriterion("GoogleAuthSecretKey is not null");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyEqualTo(String value) {
            addCriterion("GoogleAuthSecretKey =", value, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyNotEqualTo(String value) {
            addCriterion("GoogleAuthSecretKey <>", value, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyGreaterThan(String value) {
            addCriterion("GoogleAuthSecretKey >", value, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyGreaterThanOrEqualTo(String value) {
            addCriterion("GoogleAuthSecretKey >=", value, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyLessThan(String value) {
            addCriterion("GoogleAuthSecretKey <", value, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyLessThanOrEqualTo(String value) {
            addCriterion("GoogleAuthSecretKey <=", value, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyLike(String value) {
            addCriterion("GoogleAuthSecretKey like", value, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyNotLike(String value) {
            addCriterion("GoogleAuthSecretKey not like", value, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyIn(List<String> values) {
            addCriterion("GoogleAuthSecretKey in", values, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyNotIn(List<String> values) {
            addCriterion("GoogleAuthSecretKey not in", values, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyBetween(String value1, String value2) {
            addCriterion("GoogleAuthSecretKey between", value1, value2, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andGoogleAuthSecretKeyNotBetween(String value1, String value2) {
            addCriterion("GoogleAuthSecretKey not between", value1, value2, "googleAuthSecretKey");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("UserName is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("UserName is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("UserName =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("UserName <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("UserName >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("UserName >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("UserName <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("UserName <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("UserName like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("UserName not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("UserName in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("UserName not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("UserName between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("UserName not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andRealNameIsNull() {
            addCriterion("RealName is null");
            return (Criteria) this;
        }

        public Criteria andRealNameIsNotNull() {
            addCriterion("RealName is not null");
            return (Criteria) this;
        }

        public Criteria andRealNameEqualTo(String value) {
            addCriterion("RealName =", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotEqualTo(String value) {
            addCriterion("RealName <>", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameGreaterThan(String value) {
            addCriterion("RealName >", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameGreaterThanOrEqualTo(String value) {
            addCriterion("RealName >=", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLessThan(String value) {
            addCriterion("RealName <", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLessThanOrEqualTo(String value) {
            addCriterion("RealName <=", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLike(String value) {
            addCriterion("RealName like", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotLike(String value) {
            addCriterion("RealName not like", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameIn(List<String> values) {
            addCriterion("RealName in", values, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotIn(List<String> values) {
            addCriterion("RealName not in", values, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameBetween(String value1, String value2) {
            addCriterion("RealName between", value1, value2, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotBetween(String value1, String value2) {
            addCriterion("RealName not between", value1, value2, "realName");
            return (Criteria) this;
        }

        public Criteria andIdCardIsNull() {
            addCriterion("IdCard is null");
            return (Criteria) this;
        }

        public Criteria andIdCardIsNotNull() {
            addCriterion("IdCard is not null");
            return (Criteria) this;
        }

        public Criteria andIdCardEqualTo(String value) {
            addCriterion("IdCard =", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardNotEqualTo(String value) {
            addCriterion("IdCard <>", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardGreaterThan(String value) {
            addCriterion("IdCard >", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardGreaterThanOrEqualTo(String value) {
            addCriterion("IdCard >=", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardLessThan(String value) {
            addCriterion("IdCard <", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardLessThanOrEqualTo(String value) {
            addCriterion("IdCard <=", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardLike(String value) {
            addCriterion("IdCard like", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardNotLike(String value) {
            addCriterion("IdCard not like", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardIn(List<String> values) {
            addCriterion("IdCard in", values, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardNotIn(List<String> values) {
            addCriterion("IdCard not in", values, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardBetween(String value1, String value2) {
            addCriterion("IdCard between", value1, value2, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardNotBetween(String value1, String value2) {
            addCriterion("IdCard not between", value1, value2, "idCard");
            return (Criteria) this;
        }

        public Criteria andQqIsNull() {
            addCriterion("Qq is null");
            return (Criteria) this;
        }

        public Criteria andQqIsNotNull() {
            addCriterion("Qq is not null");
            return (Criteria) this;
        }

        public Criteria andQqEqualTo(String value) {
            addCriterion("Qq =", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqNotEqualTo(String value) {
            addCriterion("Qq <>", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqGreaterThan(String value) {
            addCriterion("Qq >", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqGreaterThanOrEqualTo(String value) {
            addCriterion("Qq >=", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqLessThan(String value) {
            addCriterion("Qq <", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqLessThanOrEqualTo(String value) {
            addCriterion("Qq <=", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqLike(String value) {
            addCriterion("Qq like", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqNotLike(String value) {
            addCriterion("Qq not like", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqIn(List<String> values) {
            addCriterion("Qq in", values, "qq");
            return (Criteria) this;
        }

        public Criteria andQqNotIn(List<String> values) {
            addCriterion("Qq not in", values, "qq");
            return (Criteria) this;
        }

        public Criteria andQqBetween(String value1, String value2) {
            addCriterion("Qq between", value1, value2, "qq");
            return (Criteria) this;
        }

        public Criteria andQqNotBetween(String value1, String value2) {
            addCriterion("Qq not between", value1, value2, "qq");
            return (Criteria) this;
        }

        public Criteria andSiteNameIsNull() {
            addCriterion("SiteName is null");
            return (Criteria) this;
        }

        public Criteria andSiteNameIsNotNull() {
            addCriterion("SiteName is not null");
            return (Criteria) this;
        }

        public Criteria andSiteNameEqualTo(String value) {
            addCriterion("SiteName =", value, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameNotEqualTo(String value) {
            addCriterion("SiteName <>", value, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameGreaterThan(String value) {
            addCriterion("SiteName >", value, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameGreaterThanOrEqualTo(String value) {
            addCriterion("SiteName >=", value, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameLessThan(String value) {
            addCriterion("SiteName <", value, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameLessThanOrEqualTo(String value) {
            addCriterion("SiteName <=", value, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameLike(String value) {
            addCriterion("SiteName like", value, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameNotLike(String value) {
            addCriterion("SiteName not like", value, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameIn(List<String> values) {
            addCriterion("SiteName in", values, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameNotIn(List<String> values) {
            addCriterion("SiteName not in", values, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameBetween(String value1, String value2) {
            addCriterion("SiteName between", value1, value2, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteNameNotBetween(String value1, String value2) {
            addCriterion("SiteName not between", value1, value2, "siteName");
            return (Criteria) this;
        }

        public Criteria andSiteUrlIsNull() {
            addCriterion("SiteUrl is null");
            return (Criteria) this;
        }

        public Criteria andSiteUrlIsNotNull() {
            addCriterion("SiteUrl is not null");
            return (Criteria) this;
        }

        public Criteria andSiteUrlEqualTo(String value) {
            addCriterion("SiteUrl =", value, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlNotEqualTo(String value) {
            addCriterion("SiteUrl <>", value, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlGreaterThan(String value) {
            addCriterion("SiteUrl >", value, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlGreaterThanOrEqualTo(String value) {
            addCriterion("SiteUrl >=", value, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlLessThan(String value) {
            addCriterion("SiteUrl <", value, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlLessThanOrEqualTo(String value) {
            addCriterion("SiteUrl <=", value, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlLike(String value) {
            addCriterion("SiteUrl like", value, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlNotLike(String value) {
            addCriterion("SiteUrl not like", value, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlIn(List<String> values) {
            addCriterion("SiteUrl in", values, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlNotIn(List<String> values) {
            addCriterion("SiteUrl not in", values, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlBetween(String value1, String value2) {
            addCriterion("SiteUrl between", value1, value2, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andSiteUrlNotBetween(String value1, String value2) {
            addCriterion("SiteUrl not between", value1, value2, "siteUrl");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpIsNull() {
            addCriterion("LoginWhiteIp is null");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpIsNotNull() {
            addCriterion("LoginWhiteIp is not null");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpEqualTo(String value) {
            addCriterion("LoginWhiteIp =", value, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpNotEqualTo(String value) {
            addCriterion("LoginWhiteIp <>", value, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpGreaterThan(String value) {
            addCriterion("LoginWhiteIp >", value, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpGreaterThanOrEqualTo(String value) {
            addCriterion("LoginWhiteIp >=", value, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpLessThan(String value) {
            addCriterion("LoginWhiteIp <", value, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpLessThanOrEqualTo(String value) {
            addCriterion("LoginWhiteIp <=", value, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpLike(String value) {
            addCriterion("LoginWhiteIp like", value, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpNotLike(String value) {
            addCriterion("LoginWhiteIp not like", value, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpIn(List<String> values) {
            addCriterion("LoginWhiteIp in", values, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpNotIn(List<String> values) {
            addCriterion("LoginWhiteIp not in", values, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpBetween(String value1, String value2) {
            addCriterion("LoginWhiteIp between", value1, value2, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginWhiteIpNotBetween(String value1, String value2) {
            addCriterion("LoginWhiteIp not between", value1, value2, "loginWhiteIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpIsNull() {
            addCriterion("LoginBlackIp is null");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpIsNotNull() {
            addCriterion("LoginBlackIp is not null");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpEqualTo(String value) {
            addCriterion("LoginBlackIp =", value, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpNotEqualTo(String value) {
            addCriterion("LoginBlackIp <>", value, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpGreaterThan(String value) {
            addCriterion("LoginBlackIp >", value, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpGreaterThanOrEqualTo(String value) {
            addCriterion("LoginBlackIp >=", value, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpLessThan(String value) {
            addCriterion("LoginBlackIp <", value, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpLessThanOrEqualTo(String value) {
            addCriterion("LoginBlackIp <=", value, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpLike(String value) {
            addCriterion("LoginBlackIp like", value, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpNotLike(String value) {
            addCriterion("LoginBlackIp not like", value, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpIn(List<String> values) {
            addCriterion("LoginBlackIp in", values, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpNotIn(List<String> values) {
            addCriterion("LoginBlackIp not in", values, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpBetween(String value1, String value2) {
            addCriterion("LoginBlackIp between", value1, value2, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andLoginBlackIpNotBetween(String value1, String value2) {
            addCriterion("LoginBlackIp not between", value1, value2, "loginBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpIsNull() {
            addCriterion("PayWhiteIp is null");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpIsNotNull() {
            addCriterion("PayWhiteIp is not null");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpEqualTo(String value) {
            addCriterion("PayWhiteIp =", value, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpNotEqualTo(String value) {
            addCriterion("PayWhiteIp <>", value, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpGreaterThan(String value) {
            addCriterion("PayWhiteIp >", value, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpGreaterThanOrEqualTo(String value) {
            addCriterion("PayWhiteIp >=", value, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpLessThan(String value) {
            addCriterion("PayWhiteIp <", value, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpLessThanOrEqualTo(String value) {
            addCriterion("PayWhiteIp <=", value, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpLike(String value) {
            addCriterion("PayWhiteIp like", value, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpNotLike(String value) {
            addCriterion("PayWhiteIp not like", value, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpIn(List<String> values) {
            addCriterion("PayWhiteIp in", values, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpNotIn(List<String> values) {
            addCriterion("PayWhiteIp not in", values, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpBetween(String value1, String value2) {
            addCriterion("PayWhiteIp between", value1, value2, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayWhiteIpNotBetween(String value1, String value2) {
            addCriterion("PayWhiteIp not between", value1, value2, "payWhiteIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpIsNull() {
            addCriterion("PayBlackIp is null");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpIsNotNull() {
            addCriterion("PayBlackIp is not null");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpEqualTo(String value) {
            addCriterion("PayBlackIp =", value, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpNotEqualTo(String value) {
            addCriterion("PayBlackIp <>", value, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpGreaterThan(String value) {
            addCriterion("PayBlackIp >", value, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpGreaterThanOrEqualTo(String value) {
            addCriterion("PayBlackIp >=", value, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpLessThan(String value) {
            addCriterion("PayBlackIp <", value, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpLessThanOrEqualTo(String value) {
            addCriterion("PayBlackIp <=", value, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpLike(String value) {
            addCriterion("PayBlackIp like", value, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpNotLike(String value) {
            addCriterion("PayBlackIp not like", value, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpIn(List<String> values) {
            addCriterion("PayBlackIp in", values, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpNotIn(List<String> values) {
            addCriterion("PayBlackIp not in", values, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpBetween(String value1, String value2) {
            addCriterion("PayBlackIp between", value1, value2, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andPayBlackIpNotBetween(String value1, String value2) {
            addCriterion("PayBlackIp not between", value1, value2, "payBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpIsNull() {
            addCriterion("AgentpayWhiteIp is null");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpIsNotNull() {
            addCriterion("AgentpayWhiteIp is not null");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpEqualTo(String value) {
            addCriterion("AgentpayWhiteIp =", value, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpNotEqualTo(String value) {
            addCriterion("AgentpayWhiteIp <>", value, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpGreaterThan(String value) {
            addCriterion("AgentpayWhiteIp >", value, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpGreaterThanOrEqualTo(String value) {
            addCriterion("AgentpayWhiteIp >=", value, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpLessThan(String value) {
            addCriterion("AgentpayWhiteIp <", value, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpLessThanOrEqualTo(String value) {
            addCriterion("AgentpayWhiteIp <=", value, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpLike(String value) {
            addCriterion("AgentpayWhiteIp like", value, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpNotLike(String value) {
            addCriterion("AgentpayWhiteIp not like", value, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpIn(List<String> values) {
            addCriterion("AgentpayWhiteIp in", values, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpNotIn(List<String> values) {
            addCriterion("AgentpayWhiteIp not in", values, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpBetween(String value1, String value2) {
            addCriterion("AgentpayWhiteIp between", value1, value2, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayWhiteIpNotBetween(String value1, String value2) {
            addCriterion("AgentpayWhiteIp not between", value1, value2, "agentpayWhiteIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpIsNull() {
            addCriterion("AgentpayBlackIp is null");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpIsNotNull() {
            addCriterion("AgentpayBlackIp is not null");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpEqualTo(String value) {
            addCriterion("AgentpayBlackIp =", value, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpNotEqualTo(String value) {
            addCriterion("AgentpayBlackIp <>", value, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpGreaterThan(String value) {
            addCriterion("AgentpayBlackIp >", value, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpGreaterThanOrEqualTo(String value) {
            addCriterion("AgentpayBlackIp >=", value, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpLessThan(String value) {
            addCriterion("AgentpayBlackIp <", value, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpLessThanOrEqualTo(String value) {
            addCriterion("AgentpayBlackIp <=", value, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpLike(String value) {
            addCriterion("AgentpayBlackIp like", value, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpNotLike(String value) {
            addCriterion("AgentpayBlackIp not like", value, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpIn(List<String> values) {
            addCriterion("AgentpayBlackIp in", values, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpNotIn(List<String> values) {
            addCriterion("AgentpayBlackIp not in", values, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpBetween(String value1, String value2) {
            addCriterion("AgentpayBlackIp between", value1, value2, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andAgentpayBlackIpNotBetween(String value1, String value2) {
            addCriterion("AgentpayBlackIp not between", value1, value2, "agentpayBlackIp");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeIsNull() {
            addCriterion("SettConfigMode is null");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeIsNotNull() {
            addCriterion("SettConfigMode is not null");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeEqualTo(Byte value) {
            addCriterion("SettConfigMode =", value, "settConfigMode");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeNotEqualTo(Byte value) {
            addCriterion("SettConfigMode <>", value, "settConfigMode");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeGreaterThan(Byte value) {
            addCriterion("SettConfigMode >", value, "settConfigMode");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeGreaterThanOrEqualTo(Byte value) {
            addCriterion("SettConfigMode >=", value, "settConfigMode");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeLessThan(Byte value) {
            addCriterion("SettConfigMode <", value, "settConfigMode");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeLessThanOrEqualTo(Byte value) {
            addCriterion("SettConfigMode <=", value, "settConfigMode");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeIn(List<Byte> values) {
            addCriterion("SettConfigMode in", values, "settConfigMode");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeNotIn(List<Byte> values) {
            addCriterion("SettConfigMode not in", values, "settConfigMode");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeBetween(Byte value1, Byte value2) {
            addCriterion("SettConfigMode between", value1, value2, "settConfigMode");
            return (Criteria) this;
        }

        public Criteria andSettConfigModeNotBetween(Byte value1, Byte value2) {
            addCriterion("SettConfigMode not between", value1, value2, "settConfigMode");
            return (Criteria) this;
        }

        public Criteria andDrawFlagIsNull() {
            addCriterion("DrawFlag is null");
            return (Criteria) this;
        }

        public Criteria andDrawFlagIsNotNull() {
            addCriterion("DrawFlag is not null");
            return (Criteria) this;
        }

        public Criteria andDrawFlagEqualTo(Byte value) {
            addCriterion("DrawFlag =", value, "drawFlag");
            return (Criteria) this;
        }

        public Criteria andDrawFlagNotEqualTo(Byte value) {
            addCriterion("DrawFlag <>", value, "drawFlag");
            return (Criteria) this;
        }

        public Criteria andDrawFlagGreaterThan(Byte value) {
            addCriterion("DrawFlag >", value, "drawFlag");
            return (Criteria) this;
        }

        public Criteria andDrawFlagGreaterThanOrEqualTo(Byte value) {
            addCriterion("DrawFlag >=", value, "drawFlag");
            return (Criteria) this;
        }

        public Criteria andDrawFlagLessThan(Byte value) {
            addCriterion("DrawFlag <", value, "drawFlag");
            return (Criteria) this;
        }

        public Criteria andDrawFlagLessThanOrEqualTo(Byte value) {
            addCriterion("DrawFlag <=", value, "drawFlag");
            return (Criteria) this;
        }

        public Criteria andDrawFlagIn(List<Byte> values) {
            addCriterion("DrawFlag in", values, "drawFlag");
            return (Criteria) this;
        }

        public Criteria andDrawFlagNotIn(List<Byte> values) {
            addCriterion("DrawFlag not in", values, "drawFlag");
            return (Criteria) this;
        }

        public Criteria andDrawFlagBetween(Byte value1, Byte value2) {
            addCriterion("DrawFlag between", value1, value2, "drawFlag");
            return (Criteria) this;
        }

        public Criteria andDrawFlagNotBetween(Byte value1, Byte value2) {
            addCriterion("DrawFlag not between", value1, value2, "drawFlag");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayIsNull() {
            addCriterion("AllowDrawWeekDay is null");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayIsNotNull() {
            addCriterion("AllowDrawWeekDay is not null");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayEqualTo(String value) {
            addCriterion("AllowDrawWeekDay =", value, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayNotEqualTo(String value) {
            addCriterion("AllowDrawWeekDay <>", value, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayGreaterThan(String value) {
            addCriterion("AllowDrawWeekDay >", value, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayGreaterThanOrEqualTo(String value) {
            addCriterion("AllowDrawWeekDay >=", value, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayLessThan(String value) {
            addCriterion("AllowDrawWeekDay <", value, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayLessThanOrEqualTo(String value) {
            addCriterion("AllowDrawWeekDay <=", value, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayLike(String value) {
            addCriterion("AllowDrawWeekDay like", value, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayNotLike(String value) {
            addCriterion("AllowDrawWeekDay not like", value, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayIn(List<String> values) {
            addCriterion("AllowDrawWeekDay in", values, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayNotIn(List<String> values) {
            addCriterion("AllowDrawWeekDay not in", values, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayBetween(String value1, String value2) {
            addCriterion("AllowDrawWeekDay between", value1, value2, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andAllowDrawWeekDayNotBetween(String value1, String value2) {
            addCriterion("AllowDrawWeekDay not between", value1, value2, "allowDrawWeekDay");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeIsNull() {
            addCriterion("DrawDayStartTime is null");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeIsNotNull() {
            addCriterion("DrawDayStartTime is not null");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeEqualTo(String value) {
            addCriterion("DrawDayStartTime =", value, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeNotEqualTo(String value) {
            addCriterion("DrawDayStartTime <>", value, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeGreaterThan(String value) {
            addCriterion("DrawDayStartTime >", value, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeGreaterThanOrEqualTo(String value) {
            addCriterion("DrawDayStartTime >=", value, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeLessThan(String value) {
            addCriterion("DrawDayStartTime <", value, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeLessThanOrEqualTo(String value) {
            addCriterion("DrawDayStartTime <=", value, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeLike(String value) {
            addCriterion("DrawDayStartTime like", value, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeNotLike(String value) {
            addCriterion("DrawDayStartTime not like", value, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeIn(List<String> values) {
            addCriterion("DrawDayStartTime in", values, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeNotIn(List<String> values) {
            addCriterion("DrawDayStartTime not in", values, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeBetween(String value1, String value2) {
            addCriterion("DrawDayStartTime between", value1, value2, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayStartTimeNotBetween(String value1, String value2) {
            addCriterion("DrawDayStartTime not between", value1, value2, "drawDayStartTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeIsNull() {
            addCriterion("DrawDayEndTime is null");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeIsNotNull() {
            addCriterion("DrawDayEndTime is not null");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeEqualTo(String value) {
            addCriterion("DrawDayEndTime =", value, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeNotEqualTo(String value) {
            addCriterion("DrawDayEndTime <>", value, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeGreaterThan(String value) {
            addCriterion("DrawDayEndTime >", value, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeGreaterThanOrEqualTo(String value) {
            addCriterion("DrawDayEndTime >=", value, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeLessThan(String value) {
            addCriterion("DrawDayEndTime <", value, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeLessThanOrEqualTo(String value) {
            addCriterion("DrawDayEndTime <=", value, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeLike(String value) {
            addCriterion("DrawDayEndTime like", value, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeNotLike(String value) {
            addCriterion("DrawDayEndTime not like", value, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeIn(List<String> values) {
            addCriterion("DrawDayEndTime in", values, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeNotIn(List<String> values) {
            addCriterion("DrawDayEndTime not in", values, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeBetween(String value1, String value2) {
            addCriterion("DrawDayEndTime between", value1, value2, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawDayEndTimeNotBetween(String value1, String value2) {
            addCriterion("DrawDayEndTime not between", value1, value2, "drawDayEndTime");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountIsNull() {
            addCriterion("DrawMaxDayAmount is null");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountIsNotNull() {
            addCriterion("DrawMaxDayAmount is not null");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountEqualTo(Long value) {
            addCriterion("DrawMaxDayAmount =", value, "drawMaxDayAmount");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountNotEqualTo(Long value) {
            addCriterion("DrawMaxDayAmount <>", value, "drawMaxDayAmount");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountGreaterThan(Long value) {
            addCriterion("DrawMaxDayAmount >", value, "drawMaxDayAmount");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("DrawMaxDayAmount >=", value, "drawMaxDayAmount");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountLessThan(Long value) {
            addCriterion("DrawMaxDayAmount <", value, "drawMaxDayAmount");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountLessThanOrEqualTo(Long value) {
            addCriterion("DrawMaxDayAmount <=", value, "drawMaxDayAmount");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountIn(List<Long> values) {
            addCriterion("DrawMaxDayAmount in", values, "drawMaxDayAmount");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountNotIn(List<Long> values) {
            addCriterion("DrawMaxDayAmount not in", values, "drawMaxDayAmount");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountBetween(Long value1, Long value2) {
            addCriterion("DrawMaxDayAmount between", value1, value2, "drawMaxDayAmount");
            return (Criteria) this;
        }

        public Criteria andDrawMaxDayAmountNotBetween(Long value1, Long value2) {
            addCriterion("DrawMaxDayAmount not between", value1, value2, "drawMaxDayAmount");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitIsNull() {
            addCriterion("DrawFeeLimit is null");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitIsNotNull() {
            addCriterion("DrawFeeLimit is not null");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitEqualTo(Long value) {
            addCriterion("DrawFeeLimit =", value, "drawFeeLimit");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitNotEqualTo(Long value) {
            addCriterion("DrawFeeLimit <>", value, "drawFeeLimit");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitGreaterThan(Long value) {
            addCriterion("DrawFeeLimit >", value, "drawFeeLimit");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitGreaterThanOrEqualTo(Long value) {
            addCriterion("DrawFeeLimit >=", value, "drawFeeLimit");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitLessThan(Long value) {
            addCriterion("DrawFeeLimit <", value, "drawFeeLimit");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitLessThanOrEqualTo(Long value) {
            addCriterion("DrawFeeLimit <=", value, "drawFeeLimit");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitIn(List<Long> values) {
            addCriterion("DrawFeeLimit in", values, "drawFeeLimit");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitNotIn(List<Long> values) {
            addCriterion("DrawFeeLimit not in", values, "drawFeeLimit");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitBetween(Long value1, Long value2) {
            addCriterion("DrawFeeLimit between", value1, value2, "drawFeeLimit");
            return (Criteria) this;
        }

        public Criteria andDrawFeeLimitNotBetween(Long value1, Long value2) {
            addCriterion("DrawFeeLimit not between", value1, value2, "drawFeeLimit");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIsNull() {
            addCriterion("LastLoginIp is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIsNotNull() {
            addCriterion("LastLoginIp is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpEqualTo(String value) {
            addCriterion("LastLoginIp =", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotEqualTo(String value) {
            addCriterion("LastLoginIp <>", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpGreaterThan(String value) {
            addCriterion("LastLoginIp >", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpGreaterThanOrEqualTo(String value) {
            addCriterion("LastLoginIp >=", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLessThan(String value) {
            addCriterion("LastLoginIp <", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLessThanOrEqualTo(String value) {
            addCriterion("LastLoginIp <=", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLike(String value) {
            addCriterion("LastLoginIp like", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotLike(String value) {
            addCriterion("LastLoginIp not like", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIn(List<String> values) {
            addCriterion("LastLoginIp in", values, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotIn(List<String> values) {
            addCriterion("LastLoginIp not in", values, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpBetween(String value1, String value2) {
            addCriterion("LastLoginIp between", value1, value2, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotBetween(String value1, String value2) {
            addCriterion("LastLoginIp not between", value1, value2, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNull() {
            addCriterion("LastLoginTime is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNotNull() {
            addCriterion("LastLoginTime is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeEqualTo(Date value) {
            addCriterion("LastLoginTime =", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotEqualTo(Date value) {
            addCriterion("LastLoginTime <>", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThan(Date value) {
            addCriterion("LastLoginTime >", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LastLoginTime >=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThan(Date value) {
            addCriterion("LastLoginTime <", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThanOrEqualTo(Date value) {
            addCriterion("LastLoginTime <=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIn(List<Date> values) {
            addCriterion("LastLoginTime in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotIn(List<Date> values) {
            addCriterion("LastLoginTime not in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeBetween(Date value1, Date value2) {
            addCriterion("LastLoginTime between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotBetween(Date value1, Date value2) {
            addCriterion("LastLoginTime not between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateIsNull() {
            addCriterion("offRechargeRate is null");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateIsNotNull() {
            addCriterion("offRechargeRate is not null");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateEqualTo(BigDecimal value) {
            addCriterion("offRechargeRate =", value, "offRechargeRate");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateNotEqualTo(BigDecimal value) {
            addCriterion("offRechargeRate <>", value, "offRechargeRate");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateGreaterThan(BigDecimal value) {
            addCriterion("offRechargeRate >", value, "offRechargeRate");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("offRechargeRate >=", value, "offRechargeRate");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateLessThan(BigDecimal value) {
            addCriterion("offRechargeRate <", value, "offRechargeRate");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("offRechargeRate <=", value, "offRechargeRate");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateIn(List<BigDecimal> values) {
            addCriterion("offRechargeRate in", values, "offRechargeRate");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateNotIn(List<BigDecimal> values) {
            addCriterion("offRechargeRate not in", values, "offRechargeRate");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("offRechargeRate between", value1, value2, "offRechargeRate");
            return (Criteria) this;
        }

        public Criteria andOffRechargeRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("offRechargeRate not between", value1, value2, "offRechargeRate");
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