package cn.appsys.dao.backenduser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.BackEndUser;

public interface BackEndUserMapper {
	/**
	 * 通过userCode获取BackEndUser
	 * @param userCode
	 * @return
	 */
	public BackEndUser getLoginBackEndUser(@Param("userCode")String userCode);
}
