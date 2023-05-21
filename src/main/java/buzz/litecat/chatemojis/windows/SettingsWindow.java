package buzz.litecat.chatemojis.windows;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import buzz.litecat.chatemojis.ChatEmojis;
import buzz.litecat.chatemojis.utils.Version;
import buzz.litecat.chatemojis.utils.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsWindow extends Window {

    private final ChatEmojis PLUGIN;

    private final ItemStack BOOK, SIGN, CLOSE;

    @Nullable
    private final Sound PLING_SOUND;

    public SettingsWindow(@NotNull ChatEmojis plugin) {
        super(null, InventoryType.HOPPER, "ChatEmojis Settings");
        PLUGIN = plugin;

        // never destroy the window
        this.TTL = -1;

        if(Version.getServerVersion().isNewerThan(Version.V1_7)) CLOSE = new ItemStack(Material.BARRIER);
        else CLOSE = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 14);
        ItemMeta barrierMeta = CLOSE.getItemMeta();
        assert barrierMeta != null;
        barrierMeta.setDisplayName(ChatColor.RED+"关闭");
        CLOSE.setItemMeta(barrierMeta);

        BOOK = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = BOOK.getItemMeta();
        assert bookMeta != null;
        bookMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l书籍"));
        List<String> bookLore = new ArrayList<>();
        Arrays.asList("&7单击此物品可切换是否","&7emojis可以在书籍中使用。").forEach(lore -> bookLore.add(ChatColor.translateAlternateColorCodes('&', lore)));
        bookMeta.setLore(bookLore);
        BOOK.setItemMeta(bookMeta);

        SIGN = new ItemStack(Version.getServerVersion().isNewerThan(Version.V1_13) ? Material.valueOf("木牌") : Material.valueOf("SIGN"));
        ItemMeta signMeta = SIGN.getItemMeta();
        assert signMeta != null;
        signMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l木牌"));
        List<String> signLore = new ArrayList<>();
        Arrays.asList("&7单击此物品可切换是否", "&7emojis可以在木牌中使用.").forEach(lore -> signLore.add(ChatColor.translateAlternateColorCodes('&', lore)));
        signMeta.setLore(signLore);
        SIGN.setItemMeta(signMeta);

        Sound plingSound = null;
        for(String s : new String[]{"NOTE_PLING", "BLOCK_NOTE_PLING", "BLOCK_NOTE_BLOCK_PLING"}) {
            try {
                plingSound = Sound.valueOf(s);
                break;
            } catch(Exception ignore) {}
        }
        PLING_SOUND = plingSound;
    }

    @Override
    public void build() {
        clear();

        final boolean
            useInBooks = Boolean.TRUE.equals(PLUGIN.useInBooks.getValue()),
            useOnSigns = Boolean.TRUE.equals(PLUGIN.useOnSigns.getValue());

        final ItemStack
            close = CLOSE.clone(),
            book = BOOK.clone(),
            sign = SIGN.clone();

        setItemEvents(4, close).onClick(e -> e.getWhoClicked().closeInventory());

        {
            ItemMeta bookMeta = book.getItemMeta();
            assert bookMeta != null;

            List<String> lores = bookMeta.hasLore() ? bookMeta.getLore() : new ArrayList<>();
            assert lores != null;
            lores.add("§7");
            lores.add(useInBooks ? "§a§l开启" : "§c§l关闭");
            bookMeta.setLore(lores);

            book.setItemMeta(bookMeta);

            setItemEvents(0, book).onClick(e -> {
                PLUGIN.useInBooks.setValue(!useInBooks);
                if(PLING_SOUND != null) ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), PLING_SOUND, 1f, 1f);
                build();
            });
        }

        {
            ItemMeta signMeta = sign.getItemMeta();
            assert signMeta != null;

            List<String> lores = signMeta.hasLore() ? signMeta.getLore() : new ArrayList<>();
            assert lores != null;
            lores.add("§7");
            lores.add(useOnSigns ? "§a§l开启" : "§c§l关闭");
            signMeta.setLore(lores);

            sign.setItemMeta(signMeta);

            setItemEvents(1, sign).onClick(e -> {
                PLUGIN.useOnSigns.setValue(!useOnSigns);
                if(PLING_SOUND != null) ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), PLING_SOUND, 1f, 1f);
                build();
            });
        }

    }

}
