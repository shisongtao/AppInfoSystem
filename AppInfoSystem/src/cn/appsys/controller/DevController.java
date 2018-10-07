package cn.appsys.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.appcategory.AppCategoryService;
import cn.appsys.service.appinfo.AppInfoService;
import cn.appsys.service.appversion.AppVersionService;
import cn.appsys.service.datadictionary.DataDictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping(value = "/dev/flatform/app")
public class DevController {
	private Logger logger = Logger.getLogger(DevController.class);
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private DataDictionaryService dataDictionaryService;
	@Resource
	private AppCategoryService appCategoryService;
	@Resource
	private AppVersionService appVersionService;
	
	//App基础信息列表
	@RequestMapping(value = "/list")
	public String getAppInfoList(Model model,
			@RequestParam(value = "querySoftwareName",required = false) String querySoftwareName,
			@RequestParam(value = "queryStatus",required = false) String queryStatus,
			@RequestParam(value = "queryFlatformId",required = false) String queryFlatformId,
			@RequestParam(value = "queryCategoryLevel1",required = false) String queryCategoryLevel1,
			@RequestParam(value = "queryCategoryLevel2",required = false) String queryCategoryLevel2,
			@RequestParam(value = "queryCategoryLevel3",required = false) String queryCategoryLevel3,
			@RequestParam(value = "pageIndex",required = false) String pageIndex) {
		
		List<AppInfo> appList = new ArrayList<AppInfo>();
		
		AppInfo appInfo = new AppInfo();
		int _queryStatus = 0;
		int _queryFlatformId = 0;
		int _queryCategoryLevel1 = 0;
		int _queryCategoryLevel2 = 0;
		int _queryCategoryLevel3 = 0;
		
		
		if(querySoftwareName == null) {
			querySoftwareName = "";
		}
		if(queryStatus != null && !queryStatus.equals("")) {
			_queryStatus = Integer.parseInt(queryStatus);
		}
		if(queryFlatformId != null && !queryFlatformId.equals("")) {
			_queryFlatformId = Integer.parseInt(queryFlatformId);
		}
		if(queryCategoryLevel1 != null && !queryCategoryLevel1.equals("")) {
			_queryCategoryLevel1 = Integer.parseInt(queryCategoryLevel1);
		}
		if(queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")) {
			_queryCategoryLevel2 = Integer.parseInt(queryCategoryLevel2);
		}
		if(queryCategoryLevel3 != null && !queryCategoryLevel3.equals("")) {
			_queryCategoryLevel3 = Integer.parseInt(queryCategoryLevel3);
		}
		
		//设置页面容量
		int pageSize = Constants.pageSize;
		//当前页码
		int currentPageNo = 1;
		if(pageIndex != null) {	
			currentPageNo = Integer.valueOf(pageIndex);	
		}
		//总数量
		appInfo.setSoftwareName(querySoftwareName);
		appInfo.setStatus(_queryStatus);
		appInfo.setFlatformId(_queryFlatformId);
		appInfo.setCategoryLevel1(_queryCategoryLevel1);
		appInfo.setCategoryLevel2(_queryCategoryLevel2);
		appInfo.setCategoryLevel3(_queryCategoryLevel3);
		int totalCount = appInfoService.count(appInfo);
		logger.debug("totalCount================>"+totalCount);
		PageSupport pages = new PageSupport();
		//当前页
		pages.setCurrentPageNo(currentPageNo);
		//每页条数
		pages.setPageSize(pageSize);
		//总数量
		pages.setTotalCount(totalCount);
		//总页数
		int totalPageCount = pages.getTotalPageCount();
		//控制首页和尾页
		if(currentPageNo < 1) {
			currentPageNo = 1;
		}else if(currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}
		appList = appInfoService.getAppInfoList(querySoftwareName, _queryStatus, _queryFlatformId, _queryCategoryLevel1, _queryCategoryLevel2, _queryCategoryLevel3, currentPageNo, pageSize);
		logger.debug("appList     size:"+appList.size() );
		
		List<DataDictionary> statusList = dataDictionaryService.getStatusList();
		List<DataDictionary> flatFormList = dataDictionaryService.getFlatFormList();
		List<AppCategory> categoryLevel1List = appCategoryService.getCategoryLevelList(0); 
		List<AppCategory> categoryLevel2List = appCategoryService.getCategoryLevelList(_queryCategoryLevel1); 
		List<AppCategory> categoryLevel3List = appCategoryService.getCategoryLevelList(_queryCategoryLevel2); 
		
		
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("categoryLevel2List", categoryLevel2List);
		model.addAttribute("categoryLevel3List", categoryLevel3List);
		
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("appInfoList",appList);
		model.addAttribute("querySoftwareName",querySoftwareName);
		model.addAttribute("queryStatus", _queryStatus);
		model.addAttribute("queryFlatformId", _queryFlatformId);
		model.addAttribute("queryCategoryLevel1", _queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", _queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", _queryCategoryLevel3);	
		model.addAttribute("pages", pages);
		return "developer/appinfolist";
	}
	//一二三级分类ajax
	@RequestMapping(value ="/categorylevellist", method = RequestMethod.GET)
	@ResponseBody
	public Object getlevelList(@RequestParam("pid")String pid) {
		List<AppCategory> appcateList = new ArrayList<AppCategory>();
		int parentId = 0;
		if(pid != null && pid != "") {
			parentId =Integer.parseInt(pid);
		}
		appcateList = appCategoryService.getCategoryLevelList(parentId);
		return appcateList;
	}
	
	//跳转页面，进入新增App基础信息页面
	@RequestMapping(value = "/appinfoadd")
	public String addAppInfo() {
		return "developer/appinfoadd";	
	}
	//平台列表
	@RequestMapping(value = "/datadictionarylist")
	@ResponseBody
	public Object dataDictionaryList() {
		List<DataDictionary> flatFormList = dataDictionaryService.getFlatFormList();
		return flatFormList;	
	}
	//新增App基础信息
	@RequestMapping(value = "/appinfoaddsave", method = RequestMethod.POST)
	public String addAppInfoSave(AppInfo appInfo,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_logoPicPath",required = false) MultipartFile attach) {
		
		//LOGO图片url路径
		String logoPicPath = null;
		//LOGO图片的服务器存储路径
		String logoLocPath = null;
		
		//判断文件是否为空
		if(!attach.isEmpty()) {
			//服务器存储路径
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path=============>"+path);
			
			//url本地路径
			String picPath = request.getServletContext().getContextPath() + "/statics/uploadfiles";
			logger.info("uploadFile picPath=============>"+picPath);
			
			//原文件名
			String oldFileName = attach.getOriginalFilename(); 
			logger.info("uploadFile oldFileName==============>"+oldFileName);
			
			//原文件后缀
			String prefix = FilenameUtils.getExtension(oldFileName); 
			logger.info("uploadFile prefix==================>"+prefix);
			
			//文件大小
			int filesize = 500000;
			logger.debug("uploadFile size=================>"+attach.getSize());
			
			//上传大小不得大于500KB
			if(attach.getSize() > filesize) {
				request.setAttribute("uploadFileError", "*上传大小不得超过500KB");
				return "developer/appinfoadd";
			}else if(prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")
					|| prefix.equalsIgnoreCase("pneg")) {
				
				//图片名称
				String fileName = appInfo.getAPKName() + ".jpg";
				logger.debug("new fileName========>"+attach.getName());
				
				//LOGO图片的服务器存储文件夹
				File targetFile = new File(path, fileName);
				if(!targetFile.exists()) {
					targetFile.mkdirs();
				}
					
				//保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "*上传失败");
					return "developer/appinfoadd";
				}
				
				//LOGO图片的服务器存储路径
				logoLocPath = path + File.separator + fileName;
				
				//LOGO图片url路径
				logoPicPath = picPath + "/"+ fileName;
				
			}else {
				request.setAttribute("uploadFileError", "*上传图片格式不正确");
				return "developer/appinfoadd";
			}
		}
		appInfo.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setCreationDate(new Date());
		appInfo.setDevId(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		if(appInfoService.addAppInfo(appInfo)) {
			return "redirect:/dev/flatform/app/list";
		}
		return "developer/appinfoadd";
		
	}
	
	//APKName判断
	@RequestMapping(value = "/apkexist")
	@ResponseBody
	public Object apkName(@RequestParam("APKName")String APKName) {
		Map<String,String> resultMap = new HashMap<String,String>();
		if(APKName == null || APKName == "") {
			resultMap.put("APKName", "empty");
		}else {
			AppInfo appInfo = appInfoService.getAppInfoByAPK(APKName);
			if(appInfo != null) {
				resultMap.put("APKName", "exist");
			}else {
				resultMap.put("APKName", "noexist");
			}
			
		}
		return resultMap;
	}
	
	//查看App基础信息
	@RequestMapping(value = "/appview/{appinfoid}")
	public String viewAppInfo(@PathVariable String appinfoid, Model model) {
		AppInfo appInfo = appInfoService.getAppInfo(Integer.parseInt(appinfoid));
		List<AppVersion> appVersionList = appVersionService.getAppVersionListByAppInfo(appInfo.getId());
		model.addAttribute("appInfo", appInfo);
		model.addAttribute("appVersionList", appVersionList);
		return "/developer/appinfoview";
	}
	
	//页面跳转，进入修改App基础信息页面
	@RequestMapping(value = "/appinfomodify")
	public String updateAppInfo(@RequestParam(value ="id", required = false) String id,
			AppInfo appInfo, Model model) {
		appInfo = appInfoService.getAppInfo(Integer.parseInt(id));
		model.addAttribute("appInfo", appInfo);
		return "/developer/appinfomodify";	
	}
	
	//修改App基础信息页面
	@RequestMapping(value = "/appinfomodifysave", method = RequestMethod.POST)
	public String updateAppInfoSave(AppInfo appInfo, 
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attach",required = false) MultipartFile attach) {
		logger.debug("updateAppInfoSave  id=============>"+appInfo.getId());
		
		//LOGO图片url路径
		String logoPicPath = null;
		//LOGO图片的服务器存储路径
		String logoLocPath = null;
		
		//判断文件是否为空
		if(!attach.isEmpty()) {
			//服务器存储路径
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path=============>"+path);
			
			//url本地路径
			String picPath = request.getServletContext().getContextPath() + "/statics/uploadfiles";
			logger.info("uploadFile picPath=============>"+picPath);
			
			//原文件名
			String oldFileName = attach.getOriginalFilename(); 
			logger.info("uploadFile oldFileName==============>"+oldFileName);
			
			//原文件后缀
			String prefix = FilenameUtils.getExtension(oldFileName); 
			logger.info("uploadFile prefix==================>"+prefix);
			
			//文件大小
			int filesize = 500000;
			logger.debug("uploadFile size=================>"+attach.getSize());
			
			//上传大小不得大于500KB
			if(attach.getSize() > filesize) {
				request.setAttribute("uploadFileError", "*上传大小不得超过500KB");
				return "developer/appinfoadd";
			}else if(prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")
					|| prefix.equalsIgnoreCase("pneg")) {
				
				//图片名称
				String fileName = appInfo.getAPKName() + ".jpg";
				logger.debug("new fileName========>"+attach.getName());
				
				//LOGO图片的服务器存储文件夹
				File targetFile = new File(path, fileName);
				if(!targetFile.exists()) {
					targetFile.mkdirs();
				}
					
				//保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "*上传失败");
					return "developer/appinfoadd";
				}
				
				//LOGO图片的服务器存储路径
				logoLocPath = path + File.separator + fileName;
				
				//LOGO图片url路径
				logoPicPath = picPath + "/"+ fileName;
				
			}else {
				request.setAttribute("uploadFileError", "*上传图片格式不正确");
				return "developer/appinfoadd";
			}
		}
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setModifyDate(new Date());
		if(appInfoService.modifyAppInfo(appInfo)) {
			return "redirect:/dev/flatform/app/list";
		}
		return "/developer/appinfomodify";	
	}
	
	//删除App及其版本信息
	@RequestMapping(value = "/delapp.json", method = RequestMethod.GET)
	@ResponseBody
	public Object delApp(@RequestParam(value = "id", required = false) String id) {
		Map<String,String> resultMap = new HashMap<String,String>();
		AppInfo appInfo = appInfoService.getAppInfo(Integer.parseInt(id));
		if(appInfo != null){
			appVersionService.deleteAppVersionByAppInfo(appInfo.getId());
			if(appInfoService.delAppInfo(Integer.parseInt(id))) {
				resultMap.put("delResult", "true");
			}else {
				resultMap.put("delResult", "false");
			}			
		}else {
			resultMap.put("delResult", "notexist");
		}
		return resultMap;
		
	}
	//页面跳转，进入App版本添加页面
	@RequestMapping(value = "/appversionadd")
	public String addAppVersion(@RequestParam(value = "id", required = false)String id, Model model) {
		List<AppVersion> appVersionList = appVersionService.getAppVersionListByAppInfo(Integer.parseInt(id));
		
		model.addAttribute("appVersionList", appVersionList);
		AppVersion appVersion = new AppVersion();
		appVersion.setAppId(Integer.parseInt(id));
		model.addAttribute("appVersion", appVersion);
		
		return "developer/appversionadd";	
	}
	
	//添加App版本
	@RequestMapping(value = "/addversionsave", method = RequestMethod.POST)
	public String addAppVersionSave(AppVersion appVersion, 
			HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "a_downloadLink",required = false) MultipartFile attach) {
		
		//下载路径
		String downloadLink = null;
		//apk文件的服务器存储路径
		String apkLocPath = null;
		//上传的apk文件名称
		String apkFileName = null;
		//判断文件是否为空
		if(!attach.isEmpty()) {
			//服务器存储路径
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path=============>"+path);
			
			//下载路径
			String picPath = request.getServletContext().getContextPath() + "/statics/uploadfiles";
			logger.info("uploadFile picPath=============>"+picPath);
			
			//原文件名
			String oldFileName = attach.getOriginalFilename(); 
			logger.info("uploadFile oldFileName==============>"+oldFileName);
			
			//原文件后缀
			String prefix = FilenameUtils.getExtension(oldFileName); 
			logger.info("uploadFile prefix==================>"+prefix);
			
			//文件大小
			int filesize = 500000;
			logger.debug("uploadFile size=================>"+attach.getSize());
			
			//上传大小不得大于500KB
			if(attach.getSize() > filesize) {
				request.setAttribute("uploadFileError", "*上传大小不得超过500KB");
				return "developer/appversionadd";
			}else if(prefix.equalsIgnoreCase("apk")
					|| prefix.equalsIgnoreCase("ios")) {
				//获取App信息
				AppInfo appInfo = appInfoService.getAppInfo(appVersion.getAppId());
			
				//版本名称 = App APKName - 版本号 .apk
				apkFileName = appInfo.getAPKName() + "-"+appVersion.getVersionNo() + ".apk";
				logger.debug("new apkFileName========>"+attach.getName());
				
				//App版本的服务器存储文件夹
				File targetFile = new File(path, apkFileName);
				if(!targetFile.exists()) {
					targetFile.mkdirs();
				}
					
				//保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "*上传失败");
					return "developer/appversionadd";
				}
				
				//apk文件的服务器存储路径
				apkLocPath = path + File.separator + apkFileName;
				
				//下载路径
				downloadLink = picPath + "/"+ apkFileName;
				
			}else {
				request.setAttribute("uploadFileError", "*上传App格式不正确");
				return "developer/appversionadd";
			}
		}
		logger.debug("appVersion ==================="+appVersion.getAppId());
		appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setApkFileName(apkFileName);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setDownloadLink(downloadLink);
		//添加版本信息
		int result = appVersionService.addAppVersion(appVersion);
		//判断是否添加
		if(result > 0) {
			//根据AppId获取最新版本信息
			AppVersion av = appVersionService.getAppVersion(appVersion.getAppId());
			
			logger.debug("av id================="+av.getId());
			//根据版本中的AppId查询App信息
			AppInfo appInfo = appInfoService.getAppInfo(av.getAppId());
			logger.debug("appInfo id===================="+appInfo.getId());
			//设置最新版本Id
			appInfo.setVersionId(av.getId());
			logger.debug("appInfo id===================="+appInfo.getVersionId());
			//修改app信息
			appInfoService.modifyAppInfo(appInfo);
			return "redirect:/dev/flatform/app/list";
		}
		return "developer/appversionadd";		
	}
	//页面跳转，进入版本修改页面
	@RequestMapping("/appversionmodify")
	public String updateAppVersion(@RequestParam(value = "vid", required = false) String vid,
			@RequestParam(value = "aid", required = false) String aid,
			Model model) {
		AppVersion appVersion = null;
		List<AppVersion> appVersionList = appVersionService.getAppVersionListByAppInfo(Integer.parseInt(aid));
		for(AppVersion av : appVersionList){
			if(av.getId() == Integer.parseInt(vid)) {
				appVersion = appVersionService.getAppVersionById(Integer.parseInt(vid));
			}
		}
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appVersionList", appVersionList);
		return "developer/appversionmodify";		
	}
	
	//版本修改页面
	@RequestMapping(value = "/appversionmodifysave", method = RequestMethod.POST)
	public String updateAppVeresionSave(AppVersion appVersion, 
			HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "attach",required = false) MultipartFile attach) {
		//下载路径
		String downloadLink = null;
		//apk文件的服务器存储路径
		String apkLocPath = null;
		//上传的apk文件名称
		String apkFileName = null;
		//判断文件是否为空
		if(!attach.isEmpty()) {
			//服务器存储路径
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path=============>"+path);
			
			//下载路径
			String picPath = request.getServletContext().getContextPath() + "/statics/uploadfiles";
			logger.info("uploadFile picPath=============>"+picPath);
			
			//原文件名
			String oldFileName = attach.getOriginalFilename(); 
			logger.info("uploadFile oldFileName==============>"+oldFileName);
			
			//原文件后缀
			String prefix = FilenameUtils.getExtension(oldFileName); 
			logger.info("uploadFile prefix==================>"+prefix);
			
			//文件大小
			int filesize = 500000;
			logger.debug("uploadFile size=================>"+attach.getSize());
			
			//上传大小不得大于500KB
			if(attach.getSize() > filesize) {
				request.setAttribute("uploadFileError", "*上传大小不得超过500KB");
				return "developer/appversionmodify";
			}else if(prefix.equalsIgnoreCase("apk")
					|| prefix.equalsIgnoreCase("ios")) {
				//获取App信息
				AppInfo appInfo = appInfoService.getAppInfo(appVersion.getAppId());
			
				//版本名称 = App APKName - 版本号 .apk
				apkFileName = appInfo.getAPKName() + "-"+appVersion.getVersionNo() + ".apk";
				logger.debug("new apkFileName========>"+attach.getName());
				
				//App版本的服务器存储文件夹
				File targetFile = new File(path, apkFileName);
				if(!targetFile.exists()) {
					targetFile.mkdirs();
				}
					
				//保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "*上传失败");
					return "developer/appversionmodify";
				}
				
				//apk文件的服务器存储路径
				apkLocPath = path + File.separator + apkFileName;
				
				//下载路径
				downloadLink = picPath + "/"+ apkFileName;
				
			}else {
				request.setAttribute("uploadFileError", "*上传App格式不正确");
				return "developer/appversionmodify";
			}	
		}
		appVersion.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setModifyDate(new Date());
		appVersion.setApkFileName(apkFileName);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setDownloadLink(downloadLink);
		
		int result = appVersionService.updateAppVersion(appVersion);
		if(result > 0) {
			return "redirect:/dev/flatform/app/list";
		}
		return "developer/appversionmodify";
	}
	
	@RequestMapping(value = "/delfile", method = RequestMethod.GET)
	@ResponseBody
	public Object delFile(@RequestParam(value = "id", required = false)String id,
			@RequestParam(value = "flag", required = false)String flag) {
		Map<String,String> resultMap = new HashMap<String,String>();
		
		if("logo".equals(flag)) {
			if(appInfoService.updateAppInfoLoginPic(Integer.parseInt(id))) {
				resultMap.put("result", "success");
			}else {
				resultMap.put("result", "failed");
			}
		}
		
		if("apk".equals(flag)) {
			if(appVersionService.updateAppVersionURL(Integer.parseInt(id)) > 0) {
				resultMap.put("result", "success");
			}else {
				resultMap.put("result", "failed");
			}
		}
		return resultMap;
	}
	
	
	//上架下架
	@RequestMapping(value = "/{appId}/sale")
	public Object saleSwitchAjax() {
		
		return null;
	}
}
