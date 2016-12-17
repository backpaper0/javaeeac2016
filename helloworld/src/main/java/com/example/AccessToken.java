package com.example;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class AccessToken implements Serializable {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
