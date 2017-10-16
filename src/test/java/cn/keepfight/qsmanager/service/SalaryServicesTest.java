package cn.keepfight.qsmanager.service;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tom on 2017/10/15.
 */
public class SalaryServicesTest {
    @Test
    public void staticSalaryByYear() throws Exception {
        System.out.println(SalaryServices.staticSalaryByYear(2017L).size());;
        System.out.println(SalaryServices.staticSalaryByYear(2017L).get(0).getMonth());;
    }

}