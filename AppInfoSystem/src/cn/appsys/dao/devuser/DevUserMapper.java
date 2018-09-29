package cn.appsys.dao.devuser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DevUser;

public interface DevUserMapper {
	/**
	 * 通过devCode获取DevUser
	 * @param devCode
	 * @return
	 */
	public DevUser getLoginDevUser(@Param("devCode")String devCode);
}
