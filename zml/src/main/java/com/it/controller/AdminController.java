package com.it.controller;
import com.google.common.collect.Maps;
import com.it.dto.DataTablesResult;
import com.it.pojo.Role;
import com.it.pojo.User;
import com.it.service.UserService;
import com.it.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Inject
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String userList(Model model) {
        List<Role> roleList = userService.findAllRole();
        model.addAttribute("roleList",roleList);
        return "admin/userlist";
    }

    @RequestMapping(value = "/users/load", method = RequestMethod.GET)
    @ResponseBody
    public DataTablesResult<User> loadUser(HttpServletRequest request) {
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        String keyword = request.getParameter("search[value]");
        keyword = Strings.toUTF8(keyword);
        Map<String, Object> params = Maps.newHashMap();
        params.put("keyword", keyword);
        params.put("start", start);
        params.put("length", length);

        List<User> userList = userService.findUserListByParam(params);
        Long count = userService.findtUserCount();
        Long filterCount = userService.findUserCountByParam(params);
        return new DataTablesResult<>(draw, userList, count, filterCount);
    }
    /**
     * 验证用户名是否可用（Ajax调用）
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/checkusername" ,method = RequestMethod.GET)
    @ResponseBody
    public String checkUserName(String username){
        User user = userService.findUserByUserName(username);
        if(user == null){
            return "true";
        }
        return "false";
    }
    /**
     * 添加新用户
     * @Return
     *
     */
    @RequestMapping(value = "/users/new",method = RequestMethod.POST)
    @ResponseBody
    public String saveUser(User user){
        userService.saveUser(user);
        return "success";

    }
    /**
     *重置用户密码为000000
     * @return
     */
    @RequestMapping(value = "/users/resetpassword",method = RequestMethod.POST)
    @ResponseBody
    public String resetPassword(Integer id){
        userService.resetUserPassword(id);
        return "success";

    }


}
