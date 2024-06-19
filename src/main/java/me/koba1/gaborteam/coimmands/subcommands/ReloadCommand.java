package me.koba1.gaborteam.coimmands.subcommands;

import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.coimmands.SubCommand;
import me.koba1.gaborteam.files.PlayerDataFile;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand implements SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Config fileok újratöltése";
    }

    @Override
    public String getSyntax() {
        return "/team reload";
    }

    @Override
    public String getPermission() {
        return "gaborteam.admin";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return List.of();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Main.getInstance().reloadConfig();
        PlayerDataFile.getConfig().reload();

        Main.getInstance().reload();
    }
}
