package com.liugs.tool.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MyClassLoader
 * @Description
 * @Author liugs
 * @Date 2021/9/3 15:52:50
 */
public class MyClassLoader extends ClassLoader {

    public static final String CLASS_SUFFIX = "class";
    public String rootPath;

    public List<String> clazzs;

    public MyClassLoader(String rootPath, String... classPaths) throws Exception {
        this.rootPath = rootPath;
        clazzs = new ArrayList<>();
        //扫描 并加载类 打破双亲委派
        for (String path : classPaths) {
            loadClassFromPath(new File(path));
        }
    }


    public void loadClassFromPath(File file) throws Exception {
        if (!file.isFile()) {
            for (File file1 : file.listFiles()) {
                loadClassFromPath(file1);
            }
        } else {
            String fileName = file.getName();
            String filePath = file.getPath();
            String endName = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (CLASS_SUFFIX.equals(endName)) {
                InputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[(int) file.length()];
                inputStream.read(bytes);
                //将路径转为className
                String className = getClassNameFromPath(filePath);
                //记录加载过的className
                clazzs.add(className);
                //class文件加载到JVM虚拟机
                defineClass(className, bytes, 0, bytes.length);
            }
        }

    }


    /**
     * 描述 从路径获取类名
     *
     * @param filePath
     * @return java.lang.String
     * @author liugs
     * @date 2021/9/3 17:12:22
     */
    public String getClassNameFromPath(String filePath) {
        String className = filePath.replace(rootPath, "").replaceAll("\\\\", ".");
        className = className.substring(0, className.lastIndexOf("."));
        className = className.substring(1);
        return className;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        synchronized (getClassLoadingLock(name)) {
            Class<?> clazz = findLoadedClass(name);
            //如果这个类没有被加载到
            if (null == clazz) {
                //如果不存在本加载器的集合中，说明，这个类不是本加载器加载的，调用系统的加载器加载
                if (!clazzs.contains(name)) {
                    clazz = getSystemClassLoader().loadClass(name);
                } else {
                    throw new ClassNotFoundException("加载不到类");
                }
            }

            return clazz;
//        }
    }

    public static void main(String[] args) throws Exception {
        String rootPath = MyClassLoader.class.getResource("/").getPath().replaceAll(File.pathSeparator, ".");
        rootPath = new File(rootPath).getPath();
        MyClassLoader myClassLoader = new MyClassLoader(rootPath, rootPath + "/test");

        Class<?> clazz = myClassLoader.loadClass("test.LoaderTest");
        Object obj = clazz.newInstance();
        clazz.getMethod("hello").invoke(obj);
    }
}