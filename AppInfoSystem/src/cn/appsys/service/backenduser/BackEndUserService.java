package cn.appsys.service.backenduser;

import cn.appsys.pojo.BackEndUser;

public interface BackEndUserService {
	/**
	 * 超级管理员登录
	 * @param userCode
	 * @param userPassword
	 * @return
	 */
	public BackEndUser login(String userCode, String userPassword);
}
