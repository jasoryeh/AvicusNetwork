package net.avicus.hook.gadgets.shop;

import lombok.Getter;
import lombok.Setter;
import net.avicus.compendium.menu.inventory.InventoryMenuItem;
import net.avicus.magma.Magma;
import net.avicus.magma.module.gadgets.GadgetContext;
import net.avicus.magma.module.gadgets.Gadgets;
import net.avicus.magma.util.menu.PaginatedInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ShopMenu extends PaginatedInventory {

    private static final int ROWS = 4;
    private static final int SIZE = ROWS * 9;

    private final Gadgets gadgets;

    @Getter
    @Setter
    private boolean trashEnabled;

    public ShopMenu(Player player) {
        super(player, "Shop", ROWS);
        this.gadgets = Magma.get().getMm().get(Gadgets.class);
        super.setPaginatedItems(generateGadgetItems());

        refreshPage();
    }

    public static ItemStack createShopOpener(Player player) {
        ItemStack stack = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "Shop");

        stack.setItemMeta(meta);
        return stack;
    }

    public static boolean isShopOpener(ItemStack stack) {
        return stack != null &&
                stack.hasItemMeta() &&
                stack.getItemMeta().hasDisplayName() &&
                stack.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Shop");
    }

    @Override
    public void refreshPage() {
        super.refreshPage();
    }

    public void refreshGadgetItems() {
        this.getPaginator().setCollection(generateGadgetItems());
        refreshPage();
    }

    private Collection<InventoryMenuItem> generateGadgetItems() {
        List<GadgetContext> contexts = Gadgets.getTransactables();

        return contexts.stream()
                .map(gadget -> new ShopItem(this, super.getPlayer(), gadget))
                .collect(Collectors.toList());
    }
}
