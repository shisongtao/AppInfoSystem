package cn.appsys.dao.appcategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryMapper {
	/**
	 * 查询分类列表
	 * @param parentId
	 * @return
	 */
	public List<AppCategory> getCategoryLevelList(@Param("parentId") Integer parentId);
	
}
