package com.liugs.tool.dao;


import com.liugs.tool.dao.po.TestPo;
import org.springframework.stereotype.Repository;

/**
 * TestMapper继承基类
 * @author Liuner
 */
@Repository
public interface TestMapper {

    TestPo selectByName(String name);
}