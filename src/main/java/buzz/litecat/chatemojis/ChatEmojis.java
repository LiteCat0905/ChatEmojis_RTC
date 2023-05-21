package buzz.litecat.chatemojis;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import buzz.litecat.chatemojis.exceptions.ConfigException;
import buzz.litecat.chatemojis.utils.BukkitUtils;
import buzz.litecat.chatemojis.utils.ComponentBuilder;
import buzz.litecat.chatemojis.utils.Config;
import buzz.litecat.chatemojis.utils.Version;
import buzz.litecat.chatemojis.utils.Config.ConfigurationReference;
import buzz.litecat.chatemojis.windows.SettingsWindow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class ChatEmojis extends JavaPlugin {

    static final List<String> RESERVED_NAMES = Arrays.asList("emoticon", "emoji", "regex", "enabled");
    static final Pattern NAME_PATTERN = Pattern.compile("(?<=\\.)?([^.]+?)$", Pattern.CASE_INSENSITIVE);

    private final Config config;
    EmojiGroup emojis = null;
    private static ChatEmojis plugin;
    boolean papiIsLoaded = false;
    public final ConfigurationReference<Boolean> useOnSigns, useInBooks;
    private SettingsWindow settingsWindow = null;

    public ChatEmojis() throws IOException, InvalidConfigurationException {
        plugin = this;

        BukkitUtils.init(this);

        config = Config.init(new File(getDataFolder(), "config.yml"), "config.yml");
        useInBooks = config.reference("settings.use.books", true);
        useOnSigns = config.reference("settings.use.signs", true);
    }

    @Override
    public void onLoad() {
        try {
            emojis = EmojiGroup.init(getConfig());
        } catch (ConfigException e) {
            e.printStackTrace();
        }
    }

    @Override
	public void onEnable() {
        settingsWindow = new SettingsWindow(this);
        papiIsLoaded = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        getServer().getPluginManager().registerEvents(new PluginListeners(), this);

        getCommand("emoji").setExecutor((sender, command, label, args) -> {
            if(sender.hasPermission("chatemojis.command") || sender.hasPermission("chatemojis.list")) {
                if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("list"))) {
                    if(sender instanceof Player) {
                        ComponentBuilder builder = new ComponentBuilder("&6RTChatEmojis &7(v"+getDescription().getVersion()+")\n");

                        BaseComponent[] hoverMessage = new ComponentBuilder("&6RTChatEmojis\n&7Version: &e"+getDescription().getVersion()+"\n&7Author: &eLiteCat").create();

                        // new Text(BaseComponent[]) is not added until 1.16
                        HoverEvent hoverEvent;
                        if(Version.getServerVersion().isOlderThan(Version.V1_16)) hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverMessage);
                        else hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverMessage));
                        builder.event(hoverEvent);

                        Player player = (Player) sender;
                        if(Version.getServerVersion().isOlderThan(Version.V1_8)) {
                            // idk new-lines dont work on 1.7
                            player.spigot().sendMessage(builder.create());
                            emojis.getComponents((Player) sender).forEach(baseComponents -> player.spigot().sendMessage(baseComponents));
                        } else {
                            List<BaseComponent[]> components = emojis.getComponents((Player) sender);
                            for(int i = 0; i < components.size(); i++) {
                                builder.append(components.get(i), ComponentBuilder.FormatRetention.NONE);
                                if(i != components.size() - 1) builder.append("\n", ComponentBuilder.FormatRetention.NONE);
                            }

                            player.spigot().sendMessage(builder.create());
                        }
                    } else sender.sendMessage(ChatColor.RED + "Emojis are only available to players.");
                } else if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("help")) {
                        List<String> lines = new ArrayList<>();
                        lines.add("&6RTChatEmojis &7- &f指令列表");
                        lines.add("&e/emoji [list] &e- &7展示emoji列表");
                        lines.add("&e/emoji help &e- &7Shows this list of commands");
                        lines.add("&e/emoji reload &e- &7Reloads all emojis");
                        lines.add("&e/emoji version &e- &7Shows the plugin version");
                        lines.add("&e/emoji settings &e- &7Toggle plugin settings");

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.join("\n", lines)));
                    } else if(args[0].equalsIgnoreCase("reload")) {
                        if(sender.hasPermission("chatemojis.reload")) {
                            long start = System.currentTimeMillis();
                            try {
                                reloadConfig();
                                emojis = EmojiGroup.init(getConfig());
                            } catch (ConfigException e) {
                                e.printStackTrace();
                            }
                            long interval = System.currentTimeMillis() - start;
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e所有表情符号和群组均已重新加载 &7("+Long.toString(interval)+"ms)"));
                        } else sender.sendMessage(ChatColor.RED + "您没有足够的权限来使用此命令。");
                    } else if(args[0].equalsIgnoreCase("settings")) {
                        if(sender instanceof Player) {
                            if(sender.hasPermission("chatemojis.admin")) ((Player) sender).openInventory(settingsWindow.getInventory());
                            else sender.sendMessage(ChatColor.RED + "You don't have enough permission to use this command.");
                        } else sender.sendMessage(ChatColor.RED + "Emojis are only available to players.");
                    } else if(args[0].toLowerCase().matches("^v(?:er(?:sion)?)?$")) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7服务器正在运行 &eRTChatEmojis &7(v"+getDescription().getVersion()+")"));
                    else sender.sendMessage(ChatColor.RED + "Unknown argument. Try \"/emoji help\" for a list of commands.");
                } else sender.sendMessage(ChatColor.RED + "Too many arguments. Try \"/emoji help\" for a list of commands.");
            } else sender.sendMessage(ChatColor.RED + "您没有足够的权限来使用此命令。");
            return true;
        });
    }

    @NotNull
    @Override
    public Config getConfig() {
        return config;
    }

    static ChatEmojis getInstance() {
        return plugin;
    }

}
