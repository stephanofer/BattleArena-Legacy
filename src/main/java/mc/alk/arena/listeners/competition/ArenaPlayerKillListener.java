package mc.alk.arena.listeners.competition;

import mc.alk.arena.competition.Competition;
import mc.alk.arena.competition.TransitionController;
import mc.alk.arena.events.players.ArenaPlayerKillEvent;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.MatchState;
import mc.alk.arena.objects.arenas.ArenaListener;
import mc.alk.arena.objects.events.ArenaEventHandler;

public class ArenaPlayerKillListener implements ArenaListener {

    public ArenaPlayerKillListener(MatchParams params) {
    }

    @ArenaEventHandler
    public void onKill(ArenaPlayerKillEvent event) {
        ArenaPlayer ap = event.getPlayer();
        Competition comp = event.getPlayer().getCompetition();
        
        TransitionController.transition(comp, MatchState.ONKILL, ap, ap.getTeam(), true);
    }
}
