package ericminio.katas.mastermind.domain;

import java.awt.*;

public class Combination {

    public final Color[] colors;

    public Combination(Color[] colors) {
        this.colors = colors;
    }

    public static Combination from(String input) {
        String[] names = input.split(",");
        Color[] colors = new Color[names.length];
        int index = 0;
        for (String name: names) {
            try {
                Color color = (Color) Color.class.getField(name.trim().toUpperCase()).get(null);
                colors[index] = color;
                index +=1 ;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new Combination(colors);
    }
}
