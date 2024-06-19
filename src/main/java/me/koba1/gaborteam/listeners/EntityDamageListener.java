package me.koba1.gaborteam.listeners;

import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Skeleton skeleton) {
            if(e.getEntity() instanceof Player player) {
                Team playerTeam = Utils.getTeam(player);
                if(playerTeam == null) return;

                Team skeletonTeam = Utils.getTeam(skeleton);
                if(skeletonTeam == null) return;

                if(playerTeam.getKey().equals(skeletonTeam.getKey())) {
                    skeleton.setTarget(null);
                    e.setCancelled(true);
                }
            }
        }
    }
}
