package com.nishanth.dropbox.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class AboutController {
	
	public static String displayName = null;

	@RequestMapping(value = "about", method = RequestMethod.GET)
	public void intialPage( ModelMap model, HttpServletRequest httpReq) 
	{
		final HttpSession session = httpReq.getSession();
		model.addAttribute("name", session.getAttribute("displayName"));
	}
}
