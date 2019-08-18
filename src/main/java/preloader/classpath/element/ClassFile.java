/* Copyright (c) 2014 Raymond Xu
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software.*/
package preloader.classpath.element;

import preloader.classpath.visitor.ClassPathVisitor;

/**
 * based on https://github.com/jermainexu/ClassPreloader
 */
public class ClassFile implements ClassPathNode{
    private String fileName;

    public ClassFile(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }

    public String getClassName(){
        return fileName.substring(0, fileName.lastIndexOf(".class")).replace("/", ".");
    }

    @Override
    public boolean accept(ClassPathVisitor visitor) {
        return visitor.visit(this);
    }
}