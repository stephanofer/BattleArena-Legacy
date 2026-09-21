package mc.alk.arena.listeners;

import mc.alk.arena.BattleArena;
import mc.alk.arena.Defaults;
import mc.alk.arena.controllers.MoneyController;
import mc.alk.arena.controllers.plugins.EssentialsController;
import mc.alk.arena.controllers.plugins.TrackerController;
import mc.alk.arena.plugins.BAPlaceholderExtension;
import mc.alk.arena.util.Log;
import mc.alk.arena.util.PermissionsUtil;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

import org.battleplugins.arenaregenutil.ArenaRegenController;
import org.battleplugins.arenaregenutil.RegenPlugin;
import org.battleplugins.worldguardutil.controllers.WorldGuardController;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 *
 * @author alkarin
 *
 */
public class BAPluginListener implements Listener {

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        String pluginName = event.getPlugin().getName();
        if (pluginName.equalsIgnoreCase("BattleTracker")) {
            loadBattleTracker();
        } else if (pluginName.equalsIgnoreCase("Essentials")) {
            loadEssentials();
        } else if (pluginName.equalsIgnoreCase("MultiInv")) {
            loadMultiInv();
        } else if (pluginName.equalsIgnoreCase("Multiverse-Core")) {
            loadMultiverseCore();
        } else if (pluginName.equalsIgnoreCase("Multiverse-Inventories")) {
            loadMultiverseInventory();
        } else if (pluginName.equalsIgnoreCase("PlaceholderAPI")) {
            loadPlaceholderAPI();
        } else if (pluginName.equalsIgnoreCase("WorldEdit")) {
            loadWorldEdit();
        } else if (pluginName.equalsIgnoreCase("WorldGuard")) {
            loadWorldGuard();
        } else if (pluginName.equalsIgnoreCase("Vault")) {
            loadVault();
        } else {
            loadOthers();
        }
    }

    public void loadAll() {
        loadBattleTracker();
        loadEssentials();
        loadMultiInv();
        loadMultiverseCore();
        loadMultiverseInventory();
        loadPlaceholderAPI();
        loadWorldEdit();
        loadWorldGuard();
        loadVault();
        loadOthers();
    }

    public void loadBattleTracker() {
        if (!TrackerController.enabled()) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("BattleTracker");
            if (plugin != null) {
                TrackerController.setPlugin(plugin);
            } else {
                Log.info("[BattleArena] BattleTracker not detected, not tracking wins");
            }
        }
    }


    public void loadEssentials() {
        if (!EssentialsController.enabled()) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("Essentials");
            if (plugin != null) {
                if (EssentialsController.setPlugin(plugin)) {
                    Log.info("[BattleArena] Essentials detected. God mode handling activated");
                } else {
                    Log.info("[BattleArena] Essentials detected but could not hook properly");
                }
            }
        }
    }

    public void loadMultiInv() {
        if (!Defaults.PLUGIN_MULTI_INV) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("MultiInv");
            if (plugin != null) {
                Defaults.PLUGIN_MULTI_INV = true;
                Log.info("[BattleArena] MultiInv detected.  Implementing teleport/gamemode workarounds");
            }
        }
    }

    public void loadMultiverseCore() {
        if (!Defaults.PLUGIN_MULITVERSE_CORE) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("Multiverse-Core");
            if (plugin != null) {
                Defaults.PLUGIN_MULITVERSE_CORE = true;
                Log.info("[BattleArena] Multiverse-Core detected. Implementing teleport/gamemode workarounds");
            }
        }
    }

    public void loadMultiverseInventory() {
        if (!Defaults.PLUGIN_MULITVERSE_INV) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("Multiverse-Inventories");
            if (plugin != null) {
                Defaults.PLUGIN_MULITVERSE_INV = true;
                Log.info("[BattleArena] Multiverse-Inventories detected. Implementing teleport/gamemode workarounds");
            }
        }
    }

    public void loadPlaceholderAPI() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (plugin != null) {
            new BAPlaceholderExtension().register();
            Log.info(BattleArena.getPluginName() + " PlaceholderAPI detected. Implementing placeholder hook.");
        }
    }

    public void loadWorldEdit() {
        ArenaRegenController.setPlugin(BattleArena.getSelf());
        ArenaRegenController.initialize();

        if (ArenaRegenController.hasRegenPlugin(RegenPlugin.WORLDEDIT)) {
            ArenaRegenController.setDefaultRegenPlugin(RegenPlugin.WORLDEDIT);
            Log.info("[BattleArena] WorldEdit detected.");
        }
    }

    public void loadWorldGuard() {
        if (!WorldGuardController.hasWorldGuard()) {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
            if (plugin != null) {
                if (WorldGuardController.setWorldGuard(plugin)) {
                    Log.info("[BattleArena] WorldGuard detected. WorldGuard regions can now be used");
                }
            }
        }
    }


    public void loadVault() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Vault");
        if (plugin != null) {
            /// Load vault economy
            if (!MoneyController.hasEconomy()) {
                try {
                    RegisteredServiceProvider<Economy> provider = Bukkit.getServer().
                            getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
                    if (provider == null || provider.getProvider() == null) {
                        Log.warn(BattleArena.getPluginName() + " found no economy plugin. Attempts to use money in arenas might result in errors.");
                        return;
                    } else {
                        MoneyController.setEconomy(provider.getProvider());
                        Log.info(BattleArena.getPluginName() + " found economy plugin Vault. [Default]");
                    }
                } catch (Error e) {
                    Log.err(BattleArena.getPluginName() + " exception loading economy through Vault");
                    Log.printStackTrace(e);
                }
            }
            /// Load Vault chat
            if (AnnouncementOptions.chat == null) {
                try {
                    RegisteredServiceProvider<Chat> provider = Bukkit.getServer().
                            getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
                    if (provider != null && provider.getProvider() != null) {
                        AnnouncementOptions.setVaultChat(provider.getProvider());
                    } else if (AnnouncementOptions.chatPlugin == null) {
                        Log.info("[BattleArena] Vault chat not detected, ignoring channel options");
                    }
                } catch (Error e) {
                    Log.err(BattleArena.getPluginName() + " exception loading chat through Vault");
                    Log.printStackTrace(e);
                }
            }
            /// Load Vault Permissions
            PermissionsUtil.setPermission(plugin);
        }
    }

    private void loadOthers() {
        if (Bukkit.getPluginManager().getPlugin("AntiLootSteal") != null) {
            Defaults.PLUGIN_ANTILOOT = true;
        }

        if (ArenaRegenController.hasRegenPlugin(RegenPlugin.ROLLBACK_CORE)) {
            ArenaRegenController.setDefaultRegenPlugin(RegenPlugin.ROLLBACK_CORE);
            Log.info("[BattleArena] RollbackCore detected. Using it for pastes and schematic saving.");
        }
    }

}
