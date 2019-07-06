package net.avicus.hook.gadgets.shop;

import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.menu.inventory.ClickableInventoryMenuItem;
import net.avicus.compendium.menu.inventory.StaticInventoryMenuItem;
import net.avicus.hook.credits.Credits;
import net.avicus.hook.utils.ConfirmationDialog;
import net.avicus.hook.utils.Messages;
import net.avicus.magma.module.gadgets.GadgetContext;
import net.avicus.magma.module.gadgets.TransactibilityContext;
import net.avicus.magma.network.user.Users;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShopItem extends StaticInventoryMenuItem implements
        ClickableInventoryMenuItem {

    private final ShopMenu menu;
    private final Player player;
    private final GadgetContext gadget;

    public ShopItem(ShopMenu menu, Player player, GadgetContext gadget) {
        this.menu = menu;
        this.player = player;
        this.gadget = gadget;
    }

    @Override
    public ItemStack getItemStack() {
        Locale locale = this.player.getLocale();

        ItemStack icon = this.gadget.icon(locale);
        ItemMeta meta = icon.getItemMeta();

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

        lore.add(
                ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------");

        if (this.gadget.getTransactability().isTransactable()) {
            TransactibilityContext context = this.gadget.getTransactability();

            if (context.isPurchasable()) {
                lore.add(Messages.UI_SHOP_BUY.with(ChatColor.WHITE,
                        ChatColor.GREEN + ("" + context.getPurchaseCost()) + " credits")
                        .translate(locale).toLegacyText());
            }

            if (context.isSellable()) {
                lore.add(Messages.UI_SHOP_BUY.with(ChatColor.WHITE,
                        ChatColor.RED + ("-" + context.getPurchaseCost()) + " credits")
                        .translate(locale).toLegacyText());
            }
        } else {
            ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE);
            ItemMeta itemMeta = empty.getItemMeta();
            itemMeta.setDisplayName("");
            empty.setItemMeta(itemMeta);

            return empty;
        }

        meta.setLore(lore);
        icon.setItemMeta(meta);

        return icon;
    }

    @Override
    public void onClick(ClickType type) {
        TransactibilityContext context = this.gadget.getTransactability();
        if (type.isLeftClick()) {
            // Buy
            if (!context.isPurchasable()) {
                UnlocalizedText cannot = new UnlocalizedText("This item cannot be purchased!");
                cannot.style().color(ChatColor.RED);
                this.player.sendMessage(cannot);
                return;
            }
            int amount = Credits.getCredits(this.player);
            if (amount < context.getPurchaseCost()) {
                UnlocalizedText insufficient = new UnlocalizedText("Insufficient balance, required "
                        + context.getPurchaseCost() + " credits, you have " + amount + " credits.");
                insufficient.style().color(ChatColor.RED);
                this.player.sendMessage(insufficient);
                return;
            }
            new ConfirmationDialog(this.player, () -> {
                // Confirm
                Credits.take(this.player, (int) context.getPurchaseCost());
                this.gadget.getManager().getGadgets().createBackpackGadget(Users.user(this.player), this.gadget, true, new Date());
                UnlocalizedText success = new UnlocalizedText("Purchased! Check Backpack to see purchased items.");
                success.style().color(ChatColor.GREEN);
                this.player.sendMessage(success);

                this.menu.open();
            }, this.menu::open).open();
        } else if (type.isRightClick()) {
            // Sell
            UnlocalizedText unsupported = new UnlocalizedText("Unsupported. :(. Selling will be available in the future.");
            unsupported.style().color(ChatColor.DARK_PURPLE);
            this.player.sendMessage(unsupported);
        } else {
            UnlocalizedText info = new UnlocalizedText("Use right or left click to buy or sell!");
            info.style().color(ChatColor.AQUA);
            this.player.sendMessage(info);
        }
    }
}
