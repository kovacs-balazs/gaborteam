package me.koba1.gaborteam.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.utils.Utils;
import me.koba1.gaborteam.utils.formatters.Formatter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ScoreboardManager {

    private String title;
    private List<String> lines;
    private String healthDesign;

    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();

        outer: for (String line : this.lines) {
            if(line.contains("_health%")) {
                for (Team value : Main.getTeams().values()) {
                    if(!line.contains("%" + value.getKey() + "_health%")) continue;

                    int maxHealth = Math.round(Math.round(value.getEntity().getMaxHealth()));
                    if(value.getEntity().getEntity() == null || value.getEntity().getEntity().isDead()) {
                        lines.add(line.replace("%" + value.getKey() + "_health%", "§8" + this.healthDesign.replace("%mob_health%",  "0")));
                        continue outer;
                    }

                    int health = Math.round(Math.round(value.getEntity().getEntity().getHealth()));

                    lines.add(line.replace("%" + value.getKey() + "_health%", Formatter.applyColor(getHealthDesign(health, maxHealth))));
                    continue outer;
                }
            }

            lines.add(Formatter.applyColor(line));
        }

        return lines;
    }

    private String getHealthDesign(int health, int maxHealth) {
        String healthDesign = this.healthDesign.replace("%mob_health%", health + "");
        float percent = (float) health / maxHealth;

        int progress = (int) (healthDesign.length() * percent);

        return "§c" + healthDesign.substring(0, progress) + "§7" + healthDesign.substring(progress);
    }
}
