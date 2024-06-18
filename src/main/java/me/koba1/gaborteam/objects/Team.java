package me.koba1.gaborteam.objects;

import lombok.Getter;
import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.files.PlayerDataFile;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Team {

    private String teamName;
    private String color;
    private final List<UUID> players;
    private int maxMembers;
    private TeamEntity entity;

    public Team(String teamName) {
        this.teamName = teamName;
        this.players = new ArrayList<>();
        this.maxMembers = 0;
        this.color = null;
        this.entity = null;

        load();
    }

    public void load() {
        String path = "teams." + this.teamName + ".";
        this.color = Main.getInstance().getConfig().getString(path + "color");
        this.maxMembers = Main.getInstance().getConfig().getInt(path + "maximum_members");

        this.entity = new TeamEntity(
                this,
                Main.getInstance().getConfig().getString(path + "entity.name"),
                Utils.getLocation(Main.getInstance().getConfig().getString(path + "entity.location")),
                Main.getInstance().getConfig().getDouble(path + "entity.health")

        );
    }

    public void join(Player player) {
        PlayerDataFile.get().set(player.getUniqueId().toString(), this.teamName);
        PlayerDataFile.getConfig().save();
    }

    public void leave(UUID uuid) {
        PlayerDataFile.get().set(uuid.toString(), null);
        PlayerDataFile.getConfig().save();
    }

    public void save() {
//        for (UUID player : this.players) {
//            PlayerDataFile.get().set(player.toString(), this.teamName);
//        }
//        PlayerDataFile.getConfig().save();
    }
}
