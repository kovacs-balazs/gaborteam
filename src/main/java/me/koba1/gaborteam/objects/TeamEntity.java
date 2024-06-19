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

import java.util.Objects;


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

    public TeamEntity(Team team, String name, Location location, double maxHealth, double lastHealth) {
        this.team = team;
        this.name = name;
        this.location = location;
        this.maxHealth = maxHealth;
        this.entity = null;

        if(lastHealth > 0) {
            spawn();
            this.entity.setHealth(lastHealth);
        }
    }

    public void reload() {
        if(this.entity != null) {
            setupEntity(this.entity, false);
        }
    }

    public void spawn() {
        if(this.entity != null) this.entity.remove();
        for (Skeleton skeleton : this.location.getNearbyEntitiesByType(Skeleton.class, 1)) {
            if(isTeamEntity(skeleton)) {
                System.out.println("Entity megtalálva!");
                setupEntity(skeleton, false);
                this.entity = skeleton;
                return;
            }
        }

        Skeleton skeleton = (Skeleton) this.location.getWorld().spawnEntity(this.location, EntityType.SKELETON);
        System.out.println("Spawned entity: " + this.location);
        setTeamEntity(skeleton);
        setupEntity(skeleton, true);

        this.entity = skeleton;
    }

    public void setupEntity(LivingEntity livingEntity, boolean setHealth) {
        if(setHealth) {
            livingEntity.setMaxHealth(this.maxHealth);
            livingEntity.setHealth(this.maxHealth);
        }
        livingEntity.setAI(true);
        if(livingEntity instanceof Skeleton skel)
            skel.setAggressive(false);
        livingEntity.setGravity(false);
        livingEntity.setJumping(false);
        livingEntity.setCustomName(ChatColor.translateAlternateColorCodes('&', this.name));
        livingEntity.setCustomNameVisible(true);
    }

    public void setLocation(Location location) {
        this.location = location;

        if(this.entity == null) {
            // spawn();
            return;
        }
        this.entity.teleport(location);
    }

    private void setTeamEntity(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        NamespacedKey teamEntity = new NamespacedKey(Main.getInstance(), "teamEntity");
        NamespacedKey team = new NamespacedKey(Main.getInstance(), "team");
        container.set(teamEntity, PersistentDataType.BOOLEAN, true);
        container.set(team, PersistentDataType.STRING, this.team.getKey());
    }

    public boolean isTeamEntity(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        NamespacedKey teamEntity = new NamespacedKey(Main.getInstance(), "teamEntity");
        NamespacedKey team = new NamespacedKey(Main.getInstance(), "team");
        if (container.has(teamEntity, PersistentDataType.BOOLEAN) && Boolean.TRUE.equals(container.get(teamEntity, PersistentDataType.BOOLEAN))) {
            if(container.has(team, PersistentDataType.STRING)) {
                return Objects.equals(container.get(team, PersistentDataType.STRING), this.team.getKey());
            }
        }
        return false;
    }

    public void save() {
        if(this.entity != null)
            Main.getInstance().getConfig().set("teams.%s.entity.last_health".formatted(this.team.getKey()), this.entity.getHealth());

        String loc = new StringBuilder()
                .append(this.location.getWorld().getName())
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
        Main.getInstance().saveConfig();
    }
}
