package cn.appsys.service.appcategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryService {
	/**
	 * 查询分类列表
	 * @param parentId
	 * @return
	 */
	public List<AppCategory> getCategoryLevelList(Integer parentId);
	
}
