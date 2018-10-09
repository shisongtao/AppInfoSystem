package cn.appsys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.BackEndUser;
import cn.appsys.tools.Constants;

public class ManagerInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = Logger.getLogger(DevInterceptor.class);
	
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
		logger.debug("SysInterceptor preHandle ==========================");
		HttpSession session = request.getSession();
		boolean flag = false;
		BackEndUser backEndUser = (BackEndUser)session.getAttribute(Constants.USER_SESSION);
		if(backEndUser == null) {
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			flag = false;
		}else if(backEndUser != null) {
			flag = true;
		}
		return flag;
	}
}
