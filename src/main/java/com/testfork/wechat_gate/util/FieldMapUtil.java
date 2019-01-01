package com.testfork.wechat_gate.util;

import java.util.HashMap;
import java.util.Map;

public class FieldMapUtil {
	public static final Map<String,String> TEST_FIELD = new HashMap<>();
	static{
		TEST_FIELD.put("用户类型", "UserType");
		TEST_FIELD.put("手机号/商户编号", "Mobile");
		TEST_FIELD.put("交易类型", "TradeType");
		TEST_FIELD.put("加入原因", "Reason");
		TEST_FIELD.put("生效日期", "EffectDate");
		TEST_FIELD.put("失效日期", "ExpiryDate");
		TEST_FIELD.put("余额", "Balance");
	}
}
