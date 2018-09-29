package cn.appsys.service.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoService {
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
	public List<AppInfo> getAppInfoList(String softwareName,
			Integer status,
			Integer flatformId,
			Integer categoryLevel1,
			Integer categoryLevel2,
			Integer categoryLevel3, 
			Integer currentPageNo, 
			Integer pageSize);
	
	/**
	 * 新增App基础信息
	 * @param appInfo
	 * @return
	 */
	public boolean addAppInfo(AppInfo appInfo);
	/**
	 * 修改App基础信息
	 * @param appInfo
	 * @return
	 */
	public boolean modifyAppInfo(AppInfo appInfo);
	/**
	 * 删除App
	 * @param id
	 * @return
	 */
	public boolean delAppInfo(Integer id);
	/**
	 * 查看App信息
	 * @param id
	 * @return
	 */
	public AppInfo getAppInfo(Integer id);
	/**
	 * 通过APKName查询App信息
	 * @param APKName
	 * @return
	 */
	public AppInfo getAppInfoByAPK(String APKName);
}
