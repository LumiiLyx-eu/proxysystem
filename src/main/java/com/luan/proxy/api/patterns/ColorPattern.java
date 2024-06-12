package com.luan.proxy.api.patterns;

import com.luan.proxy.ProxySystem;

import java.awt.*;
import java.util.regex.Matcher;

public class ColorPattern implements Pattern {

    private final java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<color:(#[0-9A-Fa-f]{6})>(.*?)</color>");

    @Override
    public String process(String string) {
        Matcher matcher = pattern.matcher(string);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String color = matcher.group(1);
            String text = matcher.group(2);
            String coloredText = ProxySystem.getInstance().getColorAPI().color(text, Color.decode(color));
            matcher.appendReplacement(stringBuffer, coloredText);
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }
}