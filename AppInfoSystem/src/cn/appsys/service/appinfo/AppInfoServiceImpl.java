package cn.appsys.service.appinfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.AppInfoMapper;
import cn.appsys.dao.appversion.AppVersionMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
@Service
public class AppInfoServiceImpl implements AppInfoService {
	@Resource
	private AppInfoMapper appInfoMapper;
	@Resource
	private AppVersionMapper appVersionMapper;
	@Override
	public int count(AppInfo appInfo) {
		return appInfoMapper.count(appInfo);
	}

	@Override
	public List<AppInfo> getAppInfoList(String softwareName,
			Integer status,
			Integer flatformId,
			Integer categoryLevel1,
			Integer categoryLevel2,
			Integer categoryLevel3, 
			Integer currentPageNo, 
			Integer pageSize) {
		return appInfoMapper.getAppInfoList(softwareName, status, flatformId, categoryLevel1, categoryLevel2, categoryLevel3, (currentPageNo-1)*pageSize, pageSize);
		
	}

	@Override
	public boolean addAppInfo(AppInfo appInfo) {
		boolean flag = false;
		if(appInfoMapper.addAppInfo(appInfo) > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean modifyAppInfo(AppInfo appInfo) {
		boolean flag = false;
		if(appInfoMapper.modifyAppInfo(appInfo) > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean delAppInfo(Integer id) {
		boolean flag = false;
		
		//1.先删除版本
		int versionCount = appVersionMapper.count(id);
		List<AppVersion> appVersionList = new ArrayList<AppVersion>();
		if(versionCount > 0) {//版本数 >0
			//删除apk文件
			appVersionList = appVersionMapper.getAppVersionListByAppInfo(id);
			for(AppVersion av : appVersionList) {
				if(av.getApkLocPath() != null && !av.getApkLocPath().equals("")) {
					File file = new File(av.getApkLocPath());
					if(file.exists()){
						file.delete();		
					}
				}
			}
			//删除数据库版本记录
			appVersionMapper.deleteAppVersionByAppInfo(id);
		}
		//2.再删除app基础信息
		//删除图片
		AppInfo ai = appInfoMapper.getAppInfo(id);
		if(ai.getLogoLocPath() != null && !ai.getLogoLocPath().equals("")) {
			File file = new File(ai.getLogoLocPath());
			if(file.exists()) {
				file.delete();
			}
		}
		//删除数据库app基本信息记录
		if(appInfoMapper.delAppInfo(id) > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public AppInfo getAppInfo(Integer id) {
		
		return appInfoMapper.getAppInfo(id);
	}

	@Override
	public AppInfo getAppInfoByAPK(String APKName) {
		
		return appInfoMapper.getAppInfoByAPK(APKName);
	}

	@Override
	public boolean updatePath(Integer id) {
		boolean flag = false;
		if(appInfoMapper.updatePath(id) > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean updateSaleStatus(AppInfo appInfo) {
		Integer operator = appInfo.getModifyBy();
		AppInfo ai = appInfoMapper.getAppInfo(appInfo.getId());
		if(ai == null) {
			return false;
		}else {
			switch(ai.getStatus()) {
				case 2: //当状态为审核通过时，可以进行上架操作
					onSale(ai,operator,4,2);
					break;
				case 5://当状态为下架时，可以进行上架操作
					onSale(ai,operator,4,2);
					break;
				case 4://当状态为上架时，可以进行下架操作
					offSale(ai,operator,5);
					break;
	
				default:
					return false; 
			}
		}
		return true;
	}
	/**
	 * 上架
	 * @param appInfo
	 * @param operator
	 * @param appInfoStatus
	 * @param versionStatus
	 */
	private void onSale(AppInfo appInfo,Integer operator,Integer appInfoStatus,Integer versionStatus) {
		offSale(appInfo, operator, appInfoStatus);
		setSaleSwitchToAppVersion(appInfo, operator, versionStatus);
	}
	/**
	 * 下架
	 * @param appInfo
	 * @param operator
	 * @param appInfoStatus
	 * @return
	 */
	private boolean offSale(AppInfo appInfo,Integer operator,Integer appInfoStatus) {
		AppInfo _appInfo = new AppInfo();
		_appInfo.setId(appInfo.getId());
		_appInfo.setModifyBy(operator);
		_appInfo.setStatus(appInfoStatus);
		_appInfo.setOffSaleDate(new Date());
		appInfoMapper.modifyAppInfo(_appInfo);
		return true;
	}
	/**
	 * 修改版本状态
	 * @param appInfo
	 * @param operator
	 * @param saleStatus
	 * @return
	 */
	private boolean setSaleSwitchToAppVersion(AppInfo appInfo,Integer operator,Integer saleStatus) {
		System.out.println(appInfo.getId() +","+ appInfo.getVersionId());
		AppVersion appVersion = new AppVersion();
		appVersion.setId(appInfo.getVersionId());
		appVersion.setPublishStatus(saleStatus);
		appVersion.setModifyBy(operator);
		appVersion.setModifyDate(new Date());
		int result = appVersionMapper.updateAppVersion(appVersion);
		System.out.println(result);
		return false;
	}
}
