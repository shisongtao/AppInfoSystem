package cn.appsys.service.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

/**
 * 版本业务类
 * @author Administrator
 *
 */
public interface AppVersionService {

	/**
	 * 根据id获得版本信息
	 * @return
	 */
	public AppVersion getAppVersionById(@Param("id") Integer id);
	/**
	 * 修改版本信息
	 * @return
	 */
	public int updateAppVersion(AppVersion appVersion);
	
	/**
	 * 添加版本信息
	 * @return
	 */
	public int addAppVersion(AppVersion appVersion);
	
	/**
	 * 根据app的id删除版本信息
	 * @return
	 */
	public int deleteAppVersionByAppInfo(@Param("appId") Integer appId);
	
	/**
	 * 根据app的id获得该版本下面的所有版本
	 * @return
	 */
	public List<AppVersion> getAppVersionListByAppInfo(@Param("appId") Integer appId);

	/**
	 * 删除版本地址
	 * @return
	 */
	public int updateAppVersionURL(@Param("id") Integer id);
}
