package cn.appsys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackEndUser;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.backenduser.BackEndUserService;
import cn.appsys.service.devuser.DevUserService;
import cn.appsys.tools.Constants;

@Controller
public class LoginController {
	@Resource
	private DevUserService devUserService;
	@Resource
	private BackEndUserService backEndUserService;
	
	@RequestMapping("/devlogin")
	public String devLogin() {
		return "devlogin";
	}
	@RequestMapping("/managerlogin")
	public String managerLogin() {
		return "backendlogin";
	}
	
	@RequestMapping(value = "/managerdologin", method = RequestMethod.POST)
	public String managerDoLogin(@RequestParam(value="userCode", required = false) String userCode,
			@RequestParam(value="userPassword", required = false) String userPassword,
			HttpServletRequest request,
			HttpSession session) {
		BackEndUser backEndUser = null; 	
		backEndUser = backEndUserService.login(userCode, userPassword);
		if(backEndUser != null) {
			session.setAttribute(Constants.USER_SESSION, backEndUser);
			return "redirect:/manager/main.html";
		}else {
			request.setAttribute("error", "用户名或密码不正确");
			return "backendlogin";
		}
	}

	@RequestMapping(value = "/devdologin", method = RequestMethod.POST)
	public String devDoLogin(@RequestParam(value="devCode", required = false) String devCode,
				@RequestParam(value="devPassword", required = false) String devPassword,
				HttpServletRequest request,
				HttpSession session) {
		DevUser devUser = null;
		devUser = devUserService.login(devCode, devPassword);
		if(devUser != null) {
			session.setAttribute(Constants.DEV_USER_SESSION, devUser);
			return "redirect:/dev/main.html";
		}else {
			request.setAttribute("error", "用户名或密码不正确");
			return "devlogin";
		}
	}
	
	@RequestMapping(value = "/manager/main.html")
	public String managerMain() {
		return "backend/main";
	}
	@RequestMapping(value = "/dev/main.html")
	public String devMain() {
		return "developer/main";
	}
	
	@RequestMapping(value = "/manager/logout")
	public String managerLogout(HttpSession session) {
		session.removeAttribute(Constants.USER_SESSION);
		return "backendlogin";
	}
	@RequestMapping(value = "/dev/logout")
	public String devLogout(HttpSession session) {
		session.removeAttribute(Constants.DEV_USER_SESSION);
		return "devlogin";
	}
}
