package me.koba1.gaborteam.coimmands.subcommands;

import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.coimmands.SubCommand;
import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetTeamCommand implements SubCommand {
    @Override
    public String getName() {
        return "setteam";
    }

    @Override
    public String getDescription() {
        return "Játékos csapatba sorolása";
    }

    @Override
    public String getSyntax() {
        return "/team setteam <name> <team>";
    }

    @Override
    public String getPermission() {
        return "gaborteam.admin";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        switch (index) {
            case 0:
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            case 1:
                return Main.getTeams().keySet().stream().toList();
        }
        return List.of();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(args.length != 2) {
            sender.sendMessage("§c" + getSyntax());
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage("§cJátékos nem található.");
            return;
        }

        Team team = Utils.getTeam(args[1]);
        if(team == null) {
            sender.sendMessage("§cCsapat nem található.");
            return;
        }

        Team targetTeam = Utils.getTeam(target);
        if(targetTeam != null) {
            targetTeam.leave(target.getUniqueId());
        }

        if(team.getMaxMembers() <= team.getPlayers().size() + 1) {
            sender.sendMessage("§cA csapat megtelt.");
            return;
        }

        team.join(target);
        sender.sendMessage("§a%s új csapata: %s".formatted(target.getName(), team.getName()));
        target.sendMessage("§aÚj csapatod: " + team.getName());
    }
}
