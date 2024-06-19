package me.koba1.gaborteam.coimmands.subcommands;

import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.coimmands.SubCommand;
import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PlaceEntityCommand implements SubCommand {
    @Override
    public String getName() {
        return "placeentity";
    }

    @Override
    public String getDescription() {
        return "Entity lerakása";
    }

    @Override
    public String getSyntax() {
        return "/team placeentity <team>";
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
        if(!(sender instanceof Player player)) return;
        Team team = Utils.getTeam(args[0]);
        if(team == null) {
            player.sendMessage("§cCsapat nem található.");
            return;
        }

        team.getEntity().setLocation(player.getLocation());
        player.sendMessage("§aEntitás sikeresen lerakva vagy elteleportálva.");
    }
}
