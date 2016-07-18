package com.it.controller;

import com.google.common.collect.Maps;
import com.it.dto.DataTablesResult;
import com.it.pojo.Sales;
import com.it.service.CustomerService;
import com.it.service.SalesService;
import com.it.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sales")
public class SalesController {
    @Inject
    private SalesService salesService;
    @Inject
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("customerList",customerService.findAllCustomer());
        return "sales/list";
    }
    @RequestMapping(value = "/{id:\\d+}",method = RequestMethod.GET)
    public String viewSales(@PathVariable Integer id){
        return "sales/view";

    }
    @RequestMapping(value = "/load",method = RequestMethod.GET)
    @ResponseBody
    public DataTablesResult<Sales> load(HttpServletRequest request){
        String draw = request.getParameter("draw");
        String start =request.getParameter("start") ;
        String length =request.getParameter("length");
        //query param
        String name =request.getParameter("name");
        name = Strings.toUTF8(name);
        String progress =request.getParameter("progress");
        progress =Strings.toUTF8(progress);
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        Map<String,Object> params = Maps.newHashMap();
        params.put("start",start);
        params.put("length",length);
        params.put("name",name);
        params.put("progress",progress);
        params.put("startdate",startDate);
        params.put("enddate",endDate);

        List<Sales> salesList =salesService.findByParam(params);
        Long count = salesService.count();
        Long countParam = salesService.countByParam(params);
        return new DataTablesResult<>(draw,salesList,count,countParam);
    }

    /**
     * 新增销售机会
     * @param sales
     * @return
     */
    @RequestMapping(value ="/new", method = RequestMethod.POST)
    @ResponseBody
    public String save(Sales sales){
        salesService.saveSales(sales);
        return "success";



    }


}
