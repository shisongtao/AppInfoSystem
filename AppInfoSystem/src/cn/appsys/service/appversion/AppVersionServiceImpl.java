package cn.appsys.service.appversion;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appversion.AppVersionMapper;
import cn.appsys.pojo.AppVersion;
@Service
public class AppVersionServiceImpl implements AppVersionService {
	@Resource
	private AppVersionMapper appVersionMapper;
	//根据id获得版本信息
	@Override
	public AppVersion getAppVersionById(Integer id) {
		
		return appVersionMapper.getAppVersionById(id);
	}
	//修改版本信息
	@Override
	public int updateAppVersion(AppVersion appVersion) {
		// TODO Auto-generated method stub
		return appVersionMapper.updateAppVersion(appVersion);
	}
	//添加版本信息
	@Override
	public int addAppVersion(AppVersion appVersion) {
		// TODO Auto-generated method stub
		return appVersionMapper.addAppVersion(appVersion);
	}
	//根据app的id删除版本信息
	@Override
	public int deleteAppVersionByAppInfo(Integer appId) {
		// TODO Auto-generated method stub
		return appVersionMapper.deleteAppVersionByAppInfo(appId);
	}
	//根据app的id获得该版本下面的所有版本
	@Override
	public List<AppVersion> getAppVersionListByAppInfo(Integer appId) {
		// TODO Auto-generated method stub
		return appVersionMapper.getAppVersionListByAppInfo(appId);
	}
	@Override
	public int updateAppVersionURL(Integer id) {
		// TODO Auto-generated method stub
		return appVersionMapper.updateAppVersionURL(id);
	}

}
