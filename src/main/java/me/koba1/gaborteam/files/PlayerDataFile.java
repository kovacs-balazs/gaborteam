package me.koba1.gaborteam.files;

import lombok.Getter;
import me.koba1.gaborteam.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;

public class PlayerDataFile {

    private static Main m = Main.getPlugin(Main.class);
    private static File cfg;
    private static FileConfiguration file;

    @Getter
    private static PlayerDataFile config;
    private final File ymlFile;

    public PlayerDataFile(String ymlFile) {
        config = this;
        this.ymlFile = new File(m.getDataFolder(), ymlFile);
        setup();
    }

    public void setup() {
        cfg = ymlFile;
        if (!cfg.exists()) {
            try {
                ymlFile.getParentFile().mkdirs();
                ymlFile.createNewFile();
                InputStream in = m.getResource(ymlFile.getName());
                FileOutputStream out = new FileOutputStream(cfg);

                if(in == null) return;
                try {
                    int n;
                    while ((n = in.read()) != -1) {
                        out.write(n);
                    }
                }
                finally {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                }

            } catch (IOException e) {
            }
        }
        file = YamlConfiguration.loadConfiguration(cfg);
    }

    public FileConfiguration getFile() {
        return file;
    }

    public void save() {
        try {
            file.save(cfg);
        } catch (IOException e) {
            System.out.println("Can't save language file");
        }
    }

    public void reload() {
        file = YamlConfiguration.loadConfiguration(cfg);
    }

    public static FileConfiguration get() {
        return getConfig().getFile();
    }

}
