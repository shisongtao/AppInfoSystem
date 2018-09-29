package cn.appsys.service.datadictionary;

import java.util.List;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryService {
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
