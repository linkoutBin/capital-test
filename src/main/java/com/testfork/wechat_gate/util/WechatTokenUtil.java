package com.testfork.wechat_gate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.testfork.wechat_gate.domain.AccessToken;

/**
 * description: 获取微信Token工具类
 * 
 * @author: xubin
 * @datatime: 2018年12月23日
 *
 */
public class WechatTokenUtil {
	private static Logger log = LoggerFactory.getLogger(WechatTokenUtil.class);

	@Value(value = "${wechat.api.access_token}")
	private static String TOKEN_URL;

	private static volatile boolean IS_TOKEN_INVALID;

	/**
	 * description: 根据appId获取access_token
	 * 
	 * @param appId
	 * @return
	 */
	public synchronized static String get(String appId, String secret, boolean refresh) {
		String token_cached = CacheManager.get(appId);

		if (null != token_cached && !refresh) {
			return token_cached;
		}
		IS_TOKEN_INVALID = true;
		return refresh(appId, secret);
	}

	/**
	 * description: 重新获取微信Token
	 * 
	 * @param appId
	 * @param secret
	 * @return
	 */
	private static String refresh(String appId, String secret) {
		if (IS_TOKEN_INVALID) {
			AccessToken token = RestTemplateUtil.getForObject(TOKEN_URL, AccessToken.class, appId, secret);
			String accessToken = token.getAccess_token();
			if (StringUtils.hasText(accessToken)) {
				CacheManager.set(appId, accessToken, token.getExpires_in());
				IS_TOKEN_INVALID = false;
				return accessToken;
			}
			log.error("未能更新微信token,errCode:{},errMsg:{}", token.getErrcode(), token.getErrmsg());
			return null;
		}
		return CacheManager.get(appId);

	}

	public static void main(String[] args) {
		log.error("未获取到{}", IS_TOKEN_INVALID);

		String st = "kdjskjf";
		log.info("开卷考试{}", st);
	}

}
