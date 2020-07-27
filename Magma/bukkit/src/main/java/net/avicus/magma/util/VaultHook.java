package net.avicus.magma.util;

import net.avicus.magma.Magma;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Optional;

public class VaultHook {

    private Magma magma;

    public VaultHook(Magma magma) {
        this.magma = magma;
    }

    public boolean hasVault() {
        return this.magma.getServer().getPluginManager().getPlugin("Vault") != null;
    }

    public Optional<Permission> getPermissions() {
        if(!this.hasVault()) {
            return Optional.empty();
        }

        RegisteredServiceProvider<Permission> registration = this.magma
                .getServer()
                .getServicesManager()
                .getRegistration(Permission.class);
        return registration.getProvider() != null
                ? Optional.of(registration.getProvider())
                : Optional.empty();
    }

    public Optional<Chat> getChat() {
        if(!this.hasVault()) {
            return Optional.empty();
        }

        RegisteredServiceProvider<Chat> registration = this.magma.
                getServer().
                getServicesManager().
                getRegistration(Chat.class);
        return registration.getProvider() != null
                ? Optional.of(registration.getProvider())
                : Optional.empty();
    }

}
