package cn.appsys.dao.datadictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryMapper {
	/**
	 * 查询App状态列表
	 * @return
	 */
	public List<DataDictionary> getStatusList();
	/**
	 * 查询所属平台列表
	 * @return
	 */
	public List<DataDictionary> getFlatFormList();
}
