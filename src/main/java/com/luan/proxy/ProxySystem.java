package com.luan.proxy;

import com.luan.proxy.api.ColorAPI;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

public final class ProxySystem extends Plugin {

    @Getter
    private static ProxySystem instance;
    @Getter
    private static ColorAPI colorAPI;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
