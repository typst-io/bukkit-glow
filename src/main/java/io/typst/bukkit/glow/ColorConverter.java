package io.typst.bukkit.glow;

import org.bukkit.ChatColor;

class ColorConverter {
    // Reference: https://wiki.vg/Text_formatting#Colors
    public static int getRGBFromChatColor(ChatColor color) {
        return switch (color) {
            case BLACK -> 0x000000;
            case DARK_BLUE -> 0x0000aa;
            case DARK_GREEN -> 0x00aa00;
            case DARK_AQUA -> 0x00aaaa;
            case DARK_RED -> 0xaa0000;
            case DARK_PURPLE -> 0xaa00aa;
            case GOLD -> 0xffaa00;
            case GRAY -> 0xaaaaaa;
            case DARK_GRAY -> 0x555555;
            case BLUE -> 0x5555ff;
            case GREEN -> 0x55ff55;
            case AQUA -> 0x55ffff;
            case RED -> 0xff5555;
            case LIGHT_PURPLE -> 0xff55ff;
            case YELLOW -> 0xffff55;
            default -> 0xffffff;
        };
    }
}
