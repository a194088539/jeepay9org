package org.jeepay.core.common.vo;

/**
 * <p><b>Title: </b>OrderCostFeeVO.java
 * <p><b>Description: </b>订单分润 对象
 * @author terrfly
 * @version V1.0
 * <p>
 */
public class OrderCostFeeVO {

	public OrderCostFeeVO() {
	}

	public OrderCostFeeVO(Long channelCostFee, Long agentCostFee, Long parentAgentCostFee, Long mchCostFee, Long platProfit, Long agentProfit, Long parentAgentProfit, Long mchIncome) {
		super();
		this.channelCostFee = channelCostFee;
		this.agentCostFee = agentCostFee;
		this.parentAgentCostFee = parentAgentCostFee;
		this.mchCostFee = mchCostFee;
		this.platProfit = platProfit;
		this.agentProfit = agentProfit;
		this.parentAgentProfit = parentAgentProfit;
		this.mchIncome = mchIncome;
	}


	/**
	 * 通道手续费
	 */
	private Long channelCostFee;

	/**
	 * 二级代理商成本费用   即  ：代理商需要支付给平台或一级代理的费用
	 */
	private Long agentCostFee;

	/**
	 * 一级代理商成本费用   即  ：代理商需要支付给平台的费用
	 */
	private Long parentAgentCostFee;

	/**
	 * 商家成本费用  即 ： 商家需要支付代理商的费用
	 */
	private Long mchCostFee;

	/**
	 * 平台利润
	 */
	private Long platProfit;

	/**
	 * 二级代理商利润
	 */
	private Long agentProfit;

	/**
	 * 一级代理商利润
	 */
	private Long parentAgentProfit;

	/**
	 * 商户入账金额
	 */
	private Long mchIncome;



	public Long getChannelCostFee() {
		return channelCostFee;
	}

	public void setChannelCostFee(Long channelCostFee) {
		this.channelCostFee = channelCostFee;
	}

	public Long getAgentCostFee() {
		return agentCostFee;
	}

	public void setAgentCostFee(Long agentCostFee) {
		this.agentCostFee = agentCostFee;
	}

	public Long getParentAgentCostFee() {
		return parentAgentCostFee;
	}

	public void setParentAgentCostFee(Long parentAgentCostFee) {
		this.parentAgentCostFee = parentAgentCostFee;
	}

	public Long getMchCostFee() {
		return mchCostFee;
	}

	public void setMchCostFee(Long mchCostFee) {
		this.mchCostFee = mchCostFee;
	}

	public Long getPlatProfit() {
		return platProfit;
	}

	public void setPlatProfit(Long platProfit) {
		this.platProfit = platProfit;
	}

	public Long getAgentProfit() {
		return agentProfit;
	}

	public void setAgentProfit(Long agentProfit) {
		this.agentProfit = agentProfit;
	}

	public Long getParentAgentProfit() {
		return parentAgentProfit;
	}

	public void setParentAgentProfit(Long parentAgentProfit) {
		this.parentAgentProfit = parentAgentProfit;
	}

	public Long getMchIncome() {
		return mchIncome;
	}

	public void setMchIncome(Long mchIncome) {
		this.mchIncome = mchIncome;
	}

}
