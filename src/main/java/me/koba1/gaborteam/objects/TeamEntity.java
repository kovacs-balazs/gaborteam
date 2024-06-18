package me.koba1.gaborteam.objects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import lombok.Getter;
import me.koba1.gaborteam.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;


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
        for (Skeleton skeleton : this.location.getNearbyEntitiesByType(Skeleton.class, 1)) {
            if(isTeamEntity(skeleton)) {
                System.out.println("Entity megtalálva!");
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

        ArmorStand health = (ArmorStand) this.location.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);
        health.setVisible(false);
        health.setInvisible(true);
        health.setAI(false);
        health.setGravity(false);
        health.setInvulnerable(true);
        health.setCustomName("§c" + this.maxHealth + "❤");
        health.setCustomNameVisible(true);
        health.setVelocity(health.getVelocity().add(new Vector(0, 0.3, 0)));
        skeleton.addPassenger(health);

        ArmorStand name = (ArmorStand) this.location.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);
        name.setVisible(false);
        name.setInvisible(true);
        name.setAI(false);
        name.setGravity(false);
        name.setInvulnerable(true);
        name.setCustomName(ChatColor.translateAlternateColorCodes('&', this.name));
        name.setCustomNameVisible(true);
        name.setVelocity(name.getVelocity().add(new Vector(0, 1, 0)));
        skeleton.addPassenger(name);
        System.out.println("Entity lespawnolva!");
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
}
