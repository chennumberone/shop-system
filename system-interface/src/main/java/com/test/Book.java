package com.test;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author chl
 * @date 2020/7/13 15:09
 */

/**
 * BeanNameAware设置baenName
 * BeanFactoryAware 获取Beanfactory，可读取spring中的bean
 * ApplicationContextAware 获取ApplicationContext上下文
 * InitializingBean 凡是继承该接口的类，在初始化bean的时候会执行该方法。
 * DisposableBean Bean生命周期结束前调用destory()方法做一些收尾工作
 */

public class Book implements BeanNameAware, BeanFactoryAware, ApplicationContextAware,
        InitializingBean, DisposableBean {

    private String name;

    public Book() {
        System.out.println("============book实列化");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("============BeanNameAware");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("==============BeanFactoryAware");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("================ApplicationContextAware");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("===============InitializingBean");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("==============DisposableBean");
    }

    //自定义初始化方法
    @PostConstruct
    public void springPostConstruct(){
        System.out.println("=====@PostConstruct");
    }

    public void myPostConstruct(){
        System.out.println("=======myPostConstruct invoke");
    }

    @PreDestroy
    public void springPreDesdroy(){
        System.out.println("==========@PreDestroy");
    }

    public void myPreDestroy(){
        System.out.println("==============myPreDestroy");
    }

    public static void main(String[] args) {
        // 为面试而准备的Bean生命周期加载过程
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:BeanTest.xml");
        Book book = (Book) context.getBean("book");
        System.out.println("========获取bean->Book name = " + book.getName());
        ((ClassPathXmlApplicationContext) context).destroy();
    }


}
