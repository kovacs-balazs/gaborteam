package me.koba1.gaborteam.coimmands.subcommands;

import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.coimmands.SubCommand;
import me.koba1.gaborteam.objects.TeamItem;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class GiveItemCommand implements SubCommand {
    @Override
    public String getName() {
        return "giveitem";
    }

    @Override
    public String getDescription() {
        return "Tárgy lekérése";
    }

    @Override
    public String getSyntax() {
        return "/team giveitem <player> <item> [amount]";
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
                return Main.getItems().keySet().stream().toList();
        }
        return List.of();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        int amount = -1;
        if(args.length == 3) {
            if(!args[2].matches("^[0-9]+$")) {
                sender.sendMessage("§cÉrvénytelen mennyiség.");
                return;
            }
            amount = Integer.parseInt(args[2]);
            args = Arrays.copyOfRange(args, 0, args.length - 1);
        }

        if(args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage("§cJátékos nem található.");
                return;
            }

            TeamItem item = Utils.getItem(args[1]);
            if(item == null) {
                sender.sendMessage("§cEz az item nem található.");
                return;
            }

            ItemStack is = item.getItemStack();
            if(amount != -1)
                is.setAmount(amount);

            target.getInventory().addItem(is);
        }
    }
}
