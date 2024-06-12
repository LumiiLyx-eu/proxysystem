package com.luan.proxy.commands;

import com.luan.proxy.ProxySystem;
import com.luan.proxy.utlity.Properties;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ChatClearCommand extends Command {

    private ProxySystem plugin;


    public ChatClearCommand(String name, ProxySystem plugin) {
        super("chatclear", null, new String[] { "cc" });
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.hasPermission("proxy.chatclear")) {
                for (ProxiedPlayer all : player.getServer().getInfo().getPlayers()) {
                    for (int i = 0; i < 500; ++i) {
                        all.sendMessage(new TextComponent(" "));
                    }
                    all.sendMessage(this.plugin.getColorAPI().process(Properties.prefix + "<color:#A5A5A5>Der Chat wurde von </color> <color:#8538EA>" + player.getName() + "</color> <color:#A5A5A5>geleert</color>§8!"));
                }
            } else {
                player.sendMessage(Properties.noperms);
            }
        }
    }
}
