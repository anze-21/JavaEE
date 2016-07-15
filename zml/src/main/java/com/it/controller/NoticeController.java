package com.it.controller;

import com.google.common.collect.Maps;
import com.it.dto.DataTablesResult;
import com.it.exception.NotFoundException;
import com.it.pojo.Notice;
import com.it.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notice")
public class NoticeController {
    @Inject
    private NoticeService noticeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "notice/list";

    }

    /**
     * 发布公告
     *
     * @return
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newNotice() {
        return "/notice/new";

    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newNotice(Notice notice, RedirectAttributes redirectAttributes) {
        noticeService.saveNotice(notice);
        redirectAttributes.addFlashAttribute("message", "发表成功");
        return "redirect:/notice";

    }

   @RequestMapping(value = "/load",method = RequestMethod.GET)
    @ResponseBody
    public DataTablesResult load(HttpServletRequest request) {
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        String draw = request.getParameter("draw");

        Map<String,Object> param = Maps.newHashMap();
        param.put("start",start);
        param.put("length",length);

        List<Notice> noticeList = noticeService.findByParam(param);
        Long count = noticeService.count();

        return new DataTablesResult(draw,noticeList,count,count);
    }

    /**
     * 根据ID显示具体的公告内容
     */
   @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
    public String viewNotice(@PathVariable Integer id, Model model) {
        Notice notice = noticeService.findNoticeById(id);
        if (notice == null) {
             throw new NotFoundException();
        }
        model.addAttribute("notice", notice);
        return "notice/view";
    }
    @RequestMapping(value = "/img/upload",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> upload(MultipartFile file) throws IOException {
        Map<String ,Object> result =Maps.newHashMap();
        if(!file.isEmpty()){
            String path =noticeService.saveImage(file.getInputStream(),file.getOriginalFilename());
            result.put("success",true);
            result.put("file_path",path);

        }else {
            result.put("success",false);
            result.put("msg","请选择文件");
        }
        return result;

    }

}
