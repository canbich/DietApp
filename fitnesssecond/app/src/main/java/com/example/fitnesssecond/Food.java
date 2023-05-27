package com.example.fitnesssecond;

import java.util.ArrayList;

public class Food {
    public String name;
    public int calories;

    public Food(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCalories() {
        return calories;
    }

    public Food() {
    }

    static public ArrayList<Food> getData() {
        ArrayList<Food> foodList = new ArrayList<>();
        String[] food = {"Hamburger", "Pizza", "Tavuklu Salata", "Ceviz 1 Kase", "Mantı", "Sucuklu Yumurta", "Kaşarlı Tost", "Menemen", "Tavuklu Pilav", "Köfte", "Ispanak Yemeği", "Yulaf 1 Kase"};
        int[] kalori = {1500, 2000, 700, 350, 1300, 1200, 1000, 800, 900, 1200, 500, 430};
        for (int i = 0; i < food.length; i++) {
            Food yemek = new Food();
            yemek.setName(food[i]);
            yemek.setCalories(kalori[i]);
            foodList.add(yemek);
        }


        return foodList;

    }

}

