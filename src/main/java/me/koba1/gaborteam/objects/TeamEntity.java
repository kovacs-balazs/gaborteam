package me.koba1.gaborteam.objects;

import lombok.Getter;
import me.koba1.gaborteam.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;


@Getter
public class TeamEntity {
    private Team team;
    private Location location;
    private String name;
    private double maxHealth;
    private LivingEntity entity;

    public TeamEntity(Team team, String name, Location location, double maxHealth) {
        this.team = team;
        this.name = name;
        this.location = location;
        this.maxHealth = maxHealth;
        this.entity = null;

        spawn();
    }

    public void spawn() {
        if(this.entity != null) this.entity.remove();
        for (Skeleton skeleton : this.location.getNearbyEntitiesByType(Skeleton.class, 1)) {
            if(isTeamEntity(skeleton)) {
                System.out.println("Entity megtalálva!");
                this.entity = skeleton;
                return;
            }
        }

        Skeleton skeleton = (Skeleton) this.location.getWorld().spawnEntity(this.location, EntityType.SKELETON);
        setTeamEntity(skeleton);
        skeleton.setMaxHealth(this.maxHealth);
        skeleton.setHealth(this.maxHealth);
        skeleton.setAI(false);
        skeleton.setAggressive(false);
        skeleton.setGravity(false);
        skeleton.setJumping(false);
        skeleton.setFireTicks(0);
        skeleton.setVisualFire(false);
        skeleton.setCustomName(ChatColor.translateAlternateColorCodes('&', this.name));
        skeleton.setCustomNameVisible(true);
        this.entity = skeleton;

        System.out.println("Entity lespawnolva!");
    }

    public void setLocation(Location location) {
        this.location = location;

        if(this.entity == null) {
            spawn();
            return;
        }
        this.entity.teleport(location);
    }

    private void setTeamEntity(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "teamEntity");
        container.set(key, PersistentDataType.BOOLEAN, true);
    }

    private boolean isTeamEntity(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "teamEntity");
        return container.has(key, PersistentDataType.BOOLEAN) && container.get(key, PersistentDataType.BOOLEAN);
    }

    public void save() {
        Main.getInstance().getConfig().set("teams.%s.entity.last_health".formatted(this.team.getKey()), this.entity.getHealth());

        String loc = new StringBuilder()
                .append(this.location.getWorld())
                .append("; ")
                .append(this.location.getX())
                .append(", ")
                .append(this.location.getY())
                .append(", ")
                .append(this.location.getZ())
                .append(", ")
                .append(this.location.getYaw())
                .append(", ")
                .append(this.location.getPitch())
                .toString();
        Main.getInstance().getConfig().set("teams.%s.entity.location".formatted(this.team.getKey()), loc);
    }
}
