package com.it.controller;

import com.it.dto.FlashMessage;
import com.it.service.UserService;
import com.it.util.ServletUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {


    @Inject
    private UserService userService;

    /**
     * 去登录页面
     *
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String login(String username, String password,
                        RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated()) {
            //当前用户已经登录,则先退出之前的账号（选做）
            subject.logout();
        }
        try {
            UsernamePasswordToken usernamePasswordToken =
                    new UsernamePasswordToken(username, DigestUtils.md5Hex(password));
            subject.login(usernamePasswordToken);
            //获取登录的IP地址，并保存用户登录的日志
            userService.saveUserLogin(ServletUtil.getRemoteIp(request));
            return "redirect:/home";
        } catch (LockedAccountException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message",
                    new FlashMessage(FlashMessage.STATE_ERROR, "账号已被禁用"));
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message",
                    new FlashMessage(FlashMessage.STATE_ERROR, "账号或密码错误"));
        }
        return "redirect:/";
    }


    /**
     * 安全退出
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes) {
        SecurityUtils.getSubject().logout();

        redirectAttributes.addFlashAttribute("message", new FlashMessage("你已安全退出"));
        return "redirect:/";
    }


    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/403")
    public String error403() {
        return "error/403";
    }

}