package mc.alk.arena.events;

import mc.alk.arena.Defaults;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Main event class for most all BattleArena events
 *
 */
public class BAEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public boolean callEvent() {
        if (Defaults.TESTSERVER)
            return false;

        return super.callEvent();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
