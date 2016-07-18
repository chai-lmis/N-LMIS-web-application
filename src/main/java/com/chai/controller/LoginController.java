package com.chai.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chai.model.UserBean;
import com.chai.model.views.AdmUserV;
import com.chai.services.UserService;

@Controller
public class LoginController {
	@RequestMapping(value = "/loginPage", method = RequestMethod.GET)
	public ModelAndView getForm(@ModelAttribute("userBean") UserBean userBean, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("in LoginController action loginPage");
		return new ModelAndView("LoginPage");
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("userBean") UserBean userBean,HttpServletRequest request,HttpServletResponse respones,
			RedirectAttributes redirectAttributes) throws IOException {
		System.out.println("in LoginController action login");
		System.out.println("login Name:   "+userBean.getX_LOGIN_NAME());
		System.out.println("login Name:   "+userBean.getX_PASSWORD());
		String redirectPageUrl = "";
		ArrayList<Object> userdataList = UserService.validateUserLogin(userBean.getX_LOGIN_NAME(),
				userBean.getX_PASSWORD());
		try {
			if (userdataList.size() == 2) {
				AdmUserV userdata = (AdmUserV) userdataList.get(0);
				HttpSession session=request.getSession();
				session.setMaxInactiveInterval(1800);// half hour
				session.setAttribute("userBean", userdata);
				session.setAttribute("PREVIOUS_WEEK_OF_YEAR", userdataList.get(1));
				session.setAttribute("loadCount", 1);
				Date login_time=new Date();
				SimpleDateFormat ft =  new SimpleDateFormat ("E dd MM yyyy, hh:mm:ss a ");
				session.setAttribute("login_time",ft.format(login_time));
				redirectPageUrl = "redirect:homepage";
			}else{
				redirectAttributes.addFlashAttribute("errormessage", "Wrong UserName Or Password");
				redirectPageUrl = "redirect:loginPage";
			}
		} catch (Exception e) {
			redirectPageUrl = "redirect:loginPage";
			redirectAttributes.addFlashAttribute("errormessage", "Internet Connection Lost!");
			e.printStackTrace();
		}
		return redirectPageUrl;
    }	
}
