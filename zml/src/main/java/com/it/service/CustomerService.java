package com.it.service;

import com.google.common.collect.Maps;
import com.it.mapper.CustomerMapper;
import com.it.pojo.Customer;
import com.it.util.ShiroUtil;
import com.it.util.Strings;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;
import java.util.Map;

@Named
public class CustomerService {
    @Inject
    private CustomerMapper customerMapper;

    public List<Customer> findCustomerByParams(Map<String, Object> params) {
        if (ShiroUtil.isEmployee()) {
            params.put("userid", ShiroUtil.getCurrentUserID());

        }
        return customerMapper.findByParam(params);
    }

    public Long count() {
        if (ShiroUtil.isEmployee()) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("userid", ShiroUtil.getCurrentUserID());
            return customerMapper.countByParam(params);
        }
        return customerMapper.count();
    }

    public Long countByParam(Map<String, Object> parms) {
        if (ShiroUtil.isEmployee()) {
            parms.put("userid", ShiroUtil.getCurrentUserID());
        }
        return customerMapper.countByParam(parms);
    }

    /**
     * 查询客户中所有的公司
     *
     * @return
     */
    public List<Customer> findAllCompany() {
        return customerMapper.findByType(Customer.CUSTOMER_TYPE_COMPANY);
    }

    /**
     * 保存新客户
     *
     * @param customer
     */

    public void saveCustomer(Customer customer) {
        if (customer.getCompanyid() != null) {
            Customer company = customerMapper.findById(customer.getCompanyid());
            customer.setCompanyname(company.getName());
        }
        customer.setUserid(ShiroUtil.getCurrentUserID());
        //TODO pinyin
        customer.setPinyin(Strings.toPinyin(customer.getName()));
        customerMapper.save(customer);
    }


    public List<Customer> findCOmpanyByKeyword(String keyword) {
        return customerMapper.findCompanyLikeName(keyword);
    }


    /**
     * 根据ID删除客户
     *
     * @param id
     */
    @Transactional
    public void delCustomer(Integer id) {
        Customer customer = customerMapper.findById(id);
        if (customer != null) {
            if (customer.getType().equals(Customer.CUSTOMER_TYPE_COMPANY)) {
                //被删除的用户是公司，查找是否有关联客户，如果有，则将公司的ID和名称设置为空
                List<Customer> customerList = customerMapper.findByCompanyId(id);
                for (Customer cust : customerList) {
                    cust.setCompanyname(null);
                    cust.setCompanyid(null);
                    customerMapper.update(cust);
                }
            }
            //TODO 删除关联的项目
            //TODO 删除关联的待办事项
            customerMapper.del(id);
        }
    }

    /**
     * 修改客户
     *
     * @param customer
     */
    @Transactional
    public void editCustomer(Customer customer) {
        if (customer.getType().equals(Customer.CUSTOMER_TYPE_COMPANY)) {
            //找到相关联的客户，并修改名字
            List<Customer> customerList = customerMapper.findByCompanyId(customer.getId());
            for (Customer cust : customerList) {
                cust.setCompanyid(customer.getId());
                cust.setCompanyname(customer.getName());
                customerMapper.update(cust);
            }
        } else {
            if(customer.getCompanyid() !=null){
                Customer company =customerMapper.findById(customer.getCompanyid());
                customer.setCompanyname(company.getName());
            }
        }




        customer.setPinyin(Strings.toPinyin(customer.getName()));
        customerMapper.update(customer);

    }

    /**
     * 根据客户ID查找客户
     *
     * @param id
     * @return
     */
    public Customer findCustomerById(Integer id) {
        return customerMapper.findById(id);
    }
}
