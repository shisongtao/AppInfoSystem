package cn.appsys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.DevUser;
import cn.appsys.tools.Constants;




public class DevInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = Logger.getLogger(DevInterceptor.class);
	
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
		logger.debug("SysInterceptor preHandle ==========================");
		HttpSession session = request.getSession();
		boolean flag = false;
		DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
		if(devUser == null) {
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			flag = false;
		}else if(devUser != null) {
			flag = true;
		}
		return flag;
	}
}
