package cn.appsys.service.devuser;

import cn.appsys.pojo.DevUser;

public interface DevUserService {
	/**
	 * 开发者登录
	 * @param devCode
	 * @param devPassword
	 * @return
	 */
	public DevUser login(String devCode, String devPassword);
}
