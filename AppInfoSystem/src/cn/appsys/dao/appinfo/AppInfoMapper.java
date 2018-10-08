package cn.appsys.dao.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoMapper {
	/**
	 * 根据条件查询App信息数量
	 * @param appInfo
	 * @return
	 */
	public int count(AppInfo appInfo);
	/**
	 * 根据条件分页显示App信息
	 * @param appInfo
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 */
	public List<AppInfo> getAppInfoList(@Param("softwareName")String softwareName,
			@Param("status")Integer status,
			@Param("flatformId")Integer flatformId,
			@Param("categoryLevel1")Integer categoryLevel1,
			@Param("categoryLevel2")Integer categoryLevel2,
			@Param("categoryLevel3")Integer categoryLevel3,
			@Param("from")Integer currentPageNo, 
			@Param("pageSize")Integer pageSize);
	/**
	 * 新增App基础信息
	 * @param appInfo
	 * @return
	 */
	public int addAppInfo(AppInfo appInfo);
	/**
	 * 修改App基础信息
	 * @param appInfo
	 * @return
	 */
	public int modifyAppInfo(AppInfo appInfo);
	/**
	 * 删除App
	 * @param id
	 * @return
	 */
	public int delAppInfo(@Param("id")Integer id);
	/**
	 * 查看App信息
	 * @param id
	 * @return
	 */
	public AppInfo getAppInfo(@Param("id")Integer id);
	/**
	 * 通过APKName查询App信息
	 * @param APKName
	 * @return
	 */
	public AppInfo getAppInfoByAPK(@Param("APKName")String APKName);
}
