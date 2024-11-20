package com.lubiekakao1212.util;

public class TextUtil {

    public static String amountBar(int amount) {
        var remainder = amount & 3;
        var fullCount = amount / 4;

        return "█".repeat(fullCount) + switch (remainder) {
            case 0 -> "";
            case 1 -> "░";
            case 2 -> "▒";
            case 3 -> "▓";
            default -> "Wrong";
        };
    }

}
