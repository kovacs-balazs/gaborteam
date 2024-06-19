package me.koba1.gaborteam.coimmands.subcommands;

import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.coimmands.SubCommand;
import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class RespawnTeamCommand implements SubCommand {
    @Override
    public String getName() {
        return "respawnteam";
    }

    @Override
    public String getDescription() {
        return "Teljes team újraélesztése";
    }

    @Override
    public String getSyntax() {
        return "/team respawnteam <team>";
    }

    @Override
    public String getPermission() {
        return "gaborteam.admin";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        switch (index) {
            case 0:
                return Main.getTeams().keySet().stream().toList();
        }
        return List.of();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Team team = Utils.getTeam(args[0]);
        if(team == null) {
            sender.sendMessage("§cCsapat nem található.");
            return;
        }

        team.getEntity().spawn();
        for (Player target : team.getPlayers()) {

            if(target.getGameMode() == GameMode.SPECTATOR) {
                target.setGameMode(GameMode.SURVIVAL);
            }
        }
    }
}
