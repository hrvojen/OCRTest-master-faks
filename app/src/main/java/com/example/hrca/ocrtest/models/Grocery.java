package com.example.hrca.ocrtest.models;

import java.math.BigDecimal;

/**
 * Created by hrca on 3.10.2016..
 */

public class Grocery {

    private String name;
    private BigDecimal kilos;

    public Grocery(String name, BigDecimal kilos) {
        this.name = name;
        this.kilos = kilos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getKilos() {
        return kilos;
    }

    public void setKilos(BigDecimal kilos) {
        this.kilos = kilos;
    }
}
