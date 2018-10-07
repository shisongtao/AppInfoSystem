package cn.appsys.service.appinfo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.AppInfoMapper;
import cn.appsys.pojo.AppInfo;
@Service
public class AppInfoServiceImpl implements AppInfoService {
	@Resource
	private AppInfoMapper appInfoMapper;
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
	public boolean updateAppInfoLoginPic(Integer id) {
		boolean flag = false;
		if(appInfoMapper.updateAppInfoLoginPic(id) > 0) {
			flag = true;
		}
		return flag;
	}

}
