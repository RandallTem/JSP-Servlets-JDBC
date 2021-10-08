package ru.tacos.models;

public class Taco {
    long id;
    private boolean tortilla;
    private int meat;
    private boolean cucumber;
    private boolean tomato;
    private boolean salad;
    private boolean onion;
    private boolean pepper;
    private boolean beans;
    private boolean parsley;
    private boolean spices;
    int price;

    public Taco(boolean tortilla, int meat, boolean cucumber, boolean tomato, boolean salad, boolean onion, boolean pepper, boolean beans, boolean parsley, boolean spices) {
        this.id = System.currentTimeMillis();
        this.tortilla = tortilla;
        this.meat = meat;
        this.cucumber = cucumber;
        this.tomato = tomato;
        this.salad = salad;
        this.onion = onion;
        this.pepper = pepper;
        this.beans = beans;
        this.parsley = parsley;
        this.spices = spices;
        this.price = 30;
        this.price += cucumber ? 20 : 0;
        this.price += tomato ? 25 : 0;
        this.price += salad ? 25 : 0;
        this.price += onion ? 15 : 0;
        this.price += pepper ? 20 : 0;
        this.price += beans ? 20 : 0;
        this.price += parsley ? 10 : 0;
        this.price += spices ? 15 : 0;
        if (meat == 1)
            this.price += 50;
        else if (meat == 2)
            this.price += 40;
    }

    public boolean isTortilla() {
        return tortilla;
    }

    public void setTortilla(boolean tortilla) {
        this.tortilla = tortilla;
    }

    public int getMeat() {
        return meat;
    }

    public void setMeat(int meat) {
        this.meat = meat;
    }

    public boolean isCucumber() {
        return cucumber;
    }

    public void setCucumber(boolean cucumber) {
        this.cucumber = cucumber;
    }

    public boolean isTomato() {
        return tomato;
    }

    public void setTomato(boolean tomato) {
        this.tomato = tomato;
    }

    public boolean isSalad() {
        return salad;
    }

    public void setSalad(boolean salad) {
        this.salad = salad;
    }

    public boolean isOnion() {
        return onion;
    }

    public void setOnion(boolean onion) {
        this.onion = onion;
    }

    public boolean isPepper() {
        return pepper;
    }

    public void setPepper(boolean pepper) {
        this.pepper = pepper;
    }

    public boolean isBeans() {
        return beans;
    }

    public void setBeans(boolean beans) {
        this.beans = beans;
    }

    public boolean isParsley() {
        return parsley;
    }

    public void setParsley(boolean parsley) {
        this.parsley = parsley;
    }

    public boolean isSpices() {
        return spices;
    }

    public void setSpices(boolean spices) {
        this.spices = spices;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
