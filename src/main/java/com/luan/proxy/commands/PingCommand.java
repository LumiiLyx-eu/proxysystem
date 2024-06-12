package com.luan.proxy.commands;

import com.luan.proxy.ProxySystem;
import com.luan.proxy.utlity.Properties;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCommand extends Command {

    private ProxySystem plugin;

    public PingCommand(String name, ProxySystem plugin) {
        super("ping");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            player.sendMessage(this.plugin.getColorAPI().process(Properties.prefix + "<color:#A5A5A5>Dein Ping</color>§8: <color:#8538EA>" + player.getPing() + "</color>"));
        }
    }
}
