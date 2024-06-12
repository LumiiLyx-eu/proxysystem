package com.luan.proxy;

import com.luan.proxy.api.ColorAPI;
import com.luan.proxy.commands.ChatClearCommand;
import com.luan.proxy.commands.PingCommand;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public final class ProxySystem extends Plugin {

    @Getter
    private static ProxySystem instance;
    @Getter
    private static ColorAPI colorAPI;
    @Getter
    private String prefix;

    @Override
    public void onEnable() {
        instance = this;
        colorAPI = new ColorAPI();

        loadCommands(this.getProxy().getPluginManager());
    }

    @Override
    public void onDisable() {
    }

    public void loadCommands(PluginManager pluginManager) {
        pluginManager.registerCommand(this, new PingCommand("ping", this));
        pluginManager.registerCommand(this, new ChatClearCommand("cc", this));
        pluginManager.registerCommand(this, new ChatClearCommand("chatclear", this));
    }
}
