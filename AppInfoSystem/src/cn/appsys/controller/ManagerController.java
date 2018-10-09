package cn.appsys.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.appcategory.AppCategoryService;
import cn.appsys.service.appinfo.AppInfoService;
import cn.appsys.service.appversion.AppVersionService;
import cn.appsys.service.datadictionary.DataDictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping(value = "/manager/backend/app")
public class ManagerController {
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private AppVersionService appVersionService;
	@Resource
	private DataDictionaryService dataDictionaryService;
	@Resource
	private AppCategoryService appCategoryService;
	
	//app列表
	@RequestMapping("/list")
	public String appList(Model model,@RequestParam(value="querySoftwareName",required=false)String querySoftwareName,
						@RequestParam(value="queryFlatformId",required=false)String queryFlatformId,
						@RequestParam(value="queryCategoryLevel1",required=false)String queryCategoryLevel1,	
						@RequestParam(value="queryCategoryLevel2",required=false)String queryCategoryLevel2,
						@RequestParam(value="queryCategoryLevel3",required=false)String queryCategoryLevel3,
						@RequestParam(value="pageIndex",required=false)String pageIndex){
		
		
		int _queryFlatformId = 0;
		int _queryCategoryLevel1= 0;
		int _queryCategoryLevel2 = 0;
		int _queryCategoryLevel3 = 0;
		
		//设置页面容量
		int pageSize = Constants.pageSize;
		//当前页码
		int currentPageNo = 1;
		if(querySoftwareName== null){
			querySoftwareName = "";
		}
		if(queryFlatformId!=null && !queryFlatformId.equals("")){
			_queryFlatformId = Integer.parseInt(queryFlatformId);
		}
		if(queryCategoryLevel1!=null && !queryCategoryLevel1.equals("")){
			_queryCategoryLevel1 = Integer.parseInt(queryCategoryLevel1);
		}
		if(queryCategoryLevel2!=null && !queryCategoryLevel2.equals("")){
			_queryCategoryLevel2 = Integer.parseInt(queryCategoryLevel2);
		}
		if(queryCategoryLevel3!=null && !queryCategoryLevel3.equals("")){
			_queryCategoryLevel3 = Integer.parseInt(queryCategoryLevel3);
		}
		if(pageIndex!=null){
			currentPageNo = Integer.valueOf(pageIndex);	
		}
		AppInfo appInfo = new AppInfo();
		appInfo.setAPKName(querySoftwareName);
		appInfo.setFlatformId(_queryFlatformId);
		appInfo.setCategoryLevel1(_queryCategoryLevel1);
		appInfo.setCategoryLevel2(_queryCategoryLevel2);
		appInfo.setCategoryLevel3(_queryCategoryLevel3);
		//总数量
//		int totalCount = appInfoService.getAppInfoNum(querySoftwareName, _queryFlatformId, _queryCategoryLevel1, _categorylevel2list, _queryCategoryLevel3);
		int totalCount = appInfoService.count(appInfo);
		System.out.println("totalCount>>>>>>>>>>>>>>>>>>"+totalCount);
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		if(currentPageNo<1){
			currentPageNo = 1;
		}else if(currentPageNo>totalPageCount){
			currentPageNo = totalPageCount;
		}
//		List<AppInfo> appInfoList = appInfoService.fiandAppInfoList(querySoftwareName, _queryFlatformId, _queryCategoryLevel1, _queryCategoryLevel2, _queryCategoryLevel3, currentPageNo, pageSize);
		List<AppInfo> appInfoList = appInfoService.getAppInfoList(querySoftwareName, 1, _queryFlatformId, _queryCategoryLevel1, _queryCategoryLevel2, _queryCategoryLevel3, currentPageNo, pageSize);
		List<DataDictionary> flatFormList = dataDictionaryService.getFlatFormList();
		List<AppCategory> categoryLevel1List = appCategoryService.getCategoryLevelList(0); 
		List<AppCategory> categoryLevel2List = appCategoryService.getCategoryLevelList(_queryCategoryLevel1); 
		List<AppCategory> categoryLevel3List = appCategoryService.getCategoryLevelList(_queryCategoryLevel2); 
		
		model.addAttribute("appInfoList", appInfoList);	
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("categoryLevel2List", categoryLevel2List);
		model.addAttribute("categoryLevel3List", categoryLevel3List);
		model.addAttribute("flatFormList", flatFormList);
		
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryFlatformId", _queryFlatformId);
		model.addAttribute("queryCategoryLevel1", _queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", _queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", _queryCategoryLevel3);	
		model.addAttribute("pages", pages);
		return "backend/applist";
	}
	//分类列表
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
	//平台列表
	@RequestMapping(value = "/datadictionarylist")
	@ResponseBody
	public Object dataDictionaryList() {
		List<DataDictionary> flatFormList = dataDictionaryService.getFlatFormList();
		return flatFormList;	
	}
	
	//审核app
	@RequestMapping("/check")
	public String check(String aid,String vid,Model model){
		AppInfo  appInfo = appInfoService.getAppInfo(Integer.parseInt(aid));
		AppVersion appVersion = appVersionService.getAppVersionById(Integer.parseInt(vid));				
		model.addAttribute("appInfo", appInfo);
		model.addAttribute("appVersion", appVersion);
		return "backend/appcheck";
		
	}
	
	@RequestMapping("/checksave")
	public String checksave(String id,String status){

		System.out.println("status>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+status);
		System.out.println("id>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+id);
	//	int  i = 0;
		if("2".equals(status)){
		//	i =  appInfoService.updateAppInfoByid(Integer.parseInt(id), Integer.parseInt(status));
			AppInfo appInfo = new AppInfo();
			appInfo.setId(Integer.parseInt(id));
			appInfo.setStatus(Integer.parseInt(status));
			appInfoService.modifyAppInfo(appInfo);
		}else if("3".equals(status)){
		//	i = appInfoService.updateAppInfoByid(Integer.parseInt(id), Integer.parseInt(status));
			AppInfo appInfo = new AppInfo();
			appInfo.setId(Integer.parseInt(id));
			appInfo.setStatus(Integer.parseInt(status));
		}
		//System.out.println("i>>>>>>>>>>>>>>>>>>>>>>>>>>>"+i);
		return "redirect:/manager/backend/app/list";
	}
}
