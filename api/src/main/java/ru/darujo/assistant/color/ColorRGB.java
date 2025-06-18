package ru.darujo.assistant.color;

import ru.darujo.dto.ColorDto;

public class ColorRGB extends ColorDto {
    private int red;
    private int green;
    private int blue;
    private int count = 1;

    public ColorRGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void addColor(ColorRGB colorAdd) {
        red = red + colorAdd.getRed();
        green = green + colorAdd.getGreen();
        blue = blue + colorAdd.getBlue();
        count++;
    }

    public void save() {
        red = red / count;
        green = green / count;
        blue = blue / count;
        count = 1;
    }

    @Override
    public String getColor() {
        return "#"
                + String.format("%02x", red / count)
                + String.format("%02x", green / count)
                + String.format("%02x", blue / count);
    }

    public int getRed() {
        return red / count;
    }

    public int getGreen() {
        return green / count;
    }

    public int getBlue() {
        return blue / count;
    }
}
