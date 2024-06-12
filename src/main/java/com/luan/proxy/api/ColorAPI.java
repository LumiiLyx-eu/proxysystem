package com.luan.proxy.api;

import com.luan.proxy.api.patterns.ColorPattern;
import com.luan.proxy.api.patterns.GradientPattern;
import com.luan.proxy.api.patterns.Pattern;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class ColorAPI {

    private final boolean SUPPORTS_RGB = true;
    private final List<String> SPECIAL_COLORS = Arrays.asList("&l", "&n", "&o", "&k", "&m", "§l", "§n", "§o", "§k", "§m");

    private final List<Pattern> PATTERNS = Arrays.asList(new ColorPattern(), new GradientPattern());

    public String process(String string) {
        for (Pattern pattern : PATTERNS) {
            string = pattern.process(string);
        }
        string = processHexColors(string);
        string = ChatColor.translateAlternateColorCodes('&', string);
        return string;
    }

    public List<String> process(List<String> strings) {
        return strings.stream()
                .map(this::process)
                .collect(Collectors.toList());
    }

    public String color(String string, Color color) {
        return (SUPPORTS_RGB ? ChatColor.of(color) : ChatColor.WHITE) + string;
    }

    public String color(String string, Color start, Color end) {
        String originalString = string;
        ChatColor[] colors = createGradient(start, end, withoutSpecialChar(string).length());
        return apply(originalString, colors);
    }

    public String boldColor(String string, Color color) {
        String formattedString = SUPPORTS_RGB ? ChatColor.of(color) + string : ChatColor.WHITE + string;
        return ChatColor.BOLD + formattedString + ChatColor.RESET;
    }

    public String rainbow(String string, float saturation) {
        String originalString = string;
        ChatColor[] colors = createRainbow(withoutSpecialChar(string).length(), saturation);
        return apply(originalString, colors);
    }

    public ChatColor getColor(String string) {
        return ChatColor.of(new Color(Integer.parseInt(string, 16)));
    }

    public String stripColorFormatting(String string) {
        return string.replaceAll("<#[0-9A-F]{6}>|[&§][a-f0-9lnokm]|<[/]?[A-Z]{5,8}(:[0-9A-F]{6})?[0-9]*>", "");
    }

    private String withoutSpecialChar(String source) {
        String workingString = source;
        for (String color : SPECIAL_COLORS) {
            if (workingString.contains(color)) {
                workingString = workingString.replace(color, "");
            }
        }
        return workingString;
    }

    private String processHexColors(String string) {
        Matcher matcher = HEX_COLOR_PATTERN.matcher(string);
        while (matcher.find()) {
            String hexColor = matcher.group(1);
            ChatColor color = ChatColor.of(new Color(
                    Integer.valueOf(hexColor.substring(1, 3), 16),
                    Integer.valueOf(hexColor.substring(3, 5), 16),
                    Integer.valueOf(hexColor.substring(5, 7), 16)
            ));
            string = string.replace("<color:" + hexColor + ">", color.toString());
        }
        return string;
    }

    private static final java.util.regex.Pattern HEX_COLOR_PATTERN = java.util.regex.Pattern.compile("<color:(#[0-9A-Fa-f]{6})>");

    private ChatColor[] createRainbow(int step, float saturation) {
        ChatColor[] colors = new ChatColor[step];
        double colorStep = (1.00 / step);

        for (int i = 0; i < step; i++) {
            Color color = Color.getHSBColor((float) (colorStep * i), saturation, saturation);
            colors[i] = ChatColor.of(color);
        }

        return colors;
    }

    private ChatColor[] createGradient(Color start, Color end, int step) {
        ChatColor[] colors = new ChatColor[step];
        int stepR = Math.abs(start.getRed() - end.getRed()) / (step - 1);
        int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step - 1);
        int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step - 1);
        int[] direction = new int[] {
                start.getRed() < end.getRed() ? +1 : -1,
                start.getGreen() < end.getGreen() ? +1 : -1,
                start.getBlue() < end.getBlue() ? +1 : -1
        };

        for (int i = 0; i < step; i++) {
            Color color = new Color(start.getRed() + ((stepR * i) * direction[0]), start.getGreen() + ((stepG * i) * direction[1]), start.getBlue() + ((stepB * i) * direction[2]));
            colors[i] = ChatColor.of(color);
        }

        return colors;
    }

    private String apply(String source, ChatColor[] colors) {
        StringBuilder specialColors = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        String[] characters = source.split("");
        int outIndex = 0;
        for (int i = 0; i < characters.length; i++) {
            if (characters[i].equals("&") || characters[i].equals("§")) {
                if (i + 1 < characters.length) {
                    if (characters[i + 1].equals("r")) {
                        specialColors.setLength(0);
                    } else {
                        specialColors.append(characters[i]);
                        specialColors.append(characters[i + 1]);
                    }
                    i++;
                } else
                    stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
            } else
                stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
        }
        return stringBuilder.toString();
    }
}