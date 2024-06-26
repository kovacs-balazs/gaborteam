package me.koba1.gaborteam.coimmands;

import me.koba1.gaborteam.coimmands.argumentmatchers.ContainingAllCharsOfStringArgumentMatcher;
import me.koba1.gaborteam.coimmands.subcommands.*;

public class TeamCommand extends MainCommand {
    public TeamCommand () {
        /* Here you need to pass no permission message as the first argument. */
        /* And Argument matcher as second argument. */
        super("§cEhhez nincs jogod!", new ContainingAllCharsOfStringArgumentMatcher());
    }

    @Override
    protected void registerSubCommands() {
        subCommands.add(new SetTeamCommand());
        subCommands.add(new PlaceEntityCommand());
        subCommands.add(new RespawnTeamCommand());
        subCommands.add(new ReloadCommand());
        subCommands.add(new GiveItemCommand());
    }
}
