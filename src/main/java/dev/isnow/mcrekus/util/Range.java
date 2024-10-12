package dev.isnow.mcrekus.util;


import lombok.Data;

@Data
public class Range {
    int min;
    int max;

    public boolean isInRange(int value) {
        return value >= min && value <= max;
    }
}
