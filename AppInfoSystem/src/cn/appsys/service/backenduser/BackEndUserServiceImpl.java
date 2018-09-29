package cn.appsys.service.backenduser;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.backenduser.BackEndUserMapper;
import cn.appsys.pojo.BackEndUser;
@Service
public class BackEndUserServiceImpl implements BackEndUserService {
	@Resource
	private BackEndUserMapper backEndUserMapper;
	@Override
	public BackEndUser login(String userCode, String userPassword) {
		BackEndUser backEndUser = null;
		backEndUser = backEndUserMapper.getLoginBackEndUser(userCode);
		if(backEndUser != null) {
			if(!backEndUser.getUserPassword().equals(userPassword)) {
				backEndUser = null;
			}
		}
		return backEndUser;
	}

}
