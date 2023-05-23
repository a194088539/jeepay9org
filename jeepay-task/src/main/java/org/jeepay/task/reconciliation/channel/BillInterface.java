package org.jeepay.task.reconciliation.channel;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.entity.CheckBatch;

/**
 * @author: aragom
 * @date: 18/1/18
 * @description:
 */
public interface BillInterface {

    JSONObject downloadBill(JSONObject param, CheckBatch batch);

}
