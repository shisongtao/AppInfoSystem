package cn.appsys.dao.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

/**
 * APP版本Dao接口
 * @author Administrator
 *
 */
public interface AppVersionMapper {

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
	 * 修改版本地址为Null
	 * @return
	 */
	public int updateAppVersionURL(@Param("id") Integer id);
	/**
	 * 根据AppId获取最近版本Id
	 * @param appId
	 * @return
	 */
	public AppVersion getAppVersionId(@Param("appId") Integer appId);
	/**
	 * 根据条件获取版本数量
	 * @param appId
	 * @return
	 */
	public int count(@Param("appId") Integer appId);
	
}
