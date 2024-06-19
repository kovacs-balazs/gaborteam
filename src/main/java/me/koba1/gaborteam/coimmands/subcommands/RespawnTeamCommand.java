package me.koba1.gaborteam.coimmands.subcommands;

import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.coimmands.SubCommand;
import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
                List<String> asd = new ArrayList<>();
                asd.add("all");
                asd.addAll(Main.getTeams().keySet());
                return asd;
        }
        return List.of();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(args.length != 1) {
            sender.sendMessage("§c" + getSyntax());
            return;
        }

        Team team = Utils.getTeam(args[0]);
        if(team == null) {
            if(args[0].equalsIgnoreCase("all")) {
                for (Team value : Main.getTeams().values()) {
                    value.getEntity().spawn();
                    for (Player target : value.getPlayers()) {
                        if(target.getGameMode() == GameMode.SPECTATOR) {
                            target.setGameMode(GameMode.SURVIVAL);
                        }
                    }
                }
                return;
            }

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
