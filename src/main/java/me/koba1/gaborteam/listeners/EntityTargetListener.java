package me.koba1.gaborteam.listeners;

import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemFlag;

public class EntityTargetListener implements Listener {

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e) {
        if(e.getTarget() instanceof Player target && e.getEntity() instanceof Skeleton skeleton) {
            Team playerTeam = Utils.getTeam(target);
            if(playerTeam == null) return;

            Team skeletonTeam = Utils.getTeam(skeleton);
            if(skeletonTeam == null) return;

            if(playerTeam.getKey().equals(skeletonTeam.getKey())) {
                e.setCancelled(true);
            }
        }
    }
}
