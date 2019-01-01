package com.testfork.wechat_gate.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.testfork.wechat_gate.util.EncodeUtil;

@RestController
@RequestMapping("wechat")
public class WechatMsgController {

	@Value("${wechat.app.token}")
	private String TOKEN;
	private static Logger LOG = LoggerFactory.getLogger(WechatMsgController.class);
	
	@RequestMapping("")
	public void excel(){
		
	}

	@RequestMapping(value = "sign", method = RequestMethod.GET)
	public String validate(HttpServletRequest request) throws NoSuchAlgorithmException {
		String sign = request.getParameter("signature");
		String echostr = request.getParameter("echostr");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		LOG.info("receive SIGN-MESSAGE from wechat-server: {}", request.getQueryString());
		if (checkParams(sign, echostr, timestamp, nonce)
				&& EncodeUtil.getSHA1(TOKEN, timestamp, nonce).equalsIgnoreCase(sign)) {
			LOG.info("wechat-server message verified passed!");
			return echostr;
		}
		LOG.error("wechat-server message verified failed!");
		return null;
	}

	@RequestMapping(value = "msg", method = RequestMethod.POST)
	public String receiveMsgFromWechatServer(HttpServletRequest request) throws IOException {
		LOG.info(request.getQueryString());
		InputStream is = request.getInputStream();
		StringBuffer sb = new StringBuffer();
		byte[] b = new byte[1024];
		while(is.read(b)>0){
			sb.append(new String(b));
		}
		LOG.info("接收消息处理:{}",sb.toString());
		return "success";
	}

	private boolean checkParams(String sign, String echostr, String timestamp, String nonce) {
		// TODO Auto-generated method stub
		return StringUtils.hasText(sign) && StringUtils.hasText(timestamp) && StringUtils.hasText(echostr)
				&& StringUtils.hasText(nonce);
	}

}
