package com.theslof.jmdb;

import java.io.Serializable;

public class Person implements Serializable{
    private static Person none;
    private String name;

    public Person(String name){
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException();
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException();
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static Person none(){
        if(none == null){
            none = new Person("Unknown");
        }
        return none;
    }
}
