package sep.banaanatjuh.event.commands;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import sep.banaanatjuh.event.Main; // Keep if Main is used for other things
import sep.banaanatjuh.event.storage.StorageManager;

public class MinigameCommand implements CommandExecutor, TabCompleter {

    private StorageManager storageManager;

    public MinigameCommand() {
        // no-op default constructor
    }

    public MinigameCommand(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        Player player = (Player) sender;

        if (!Main.getMinigame().equals("none")) {
            player.sendMessage(Component.text("A minigame is already active! You must use /endminigame first."));
            return true;
        }

        // Existing logic for /startminigame
        if (args.length != 1) {
            player.sendMessage("Usage: /startminigame <chicken_shooter|build_battle|hide_and_seek|tnt_run|spleef|sumo|parkour|maze>");
            return true;
        } else {
            String minigame = args[0].toLowerCase();
            
            switch(minigame) {
                case "build_battle":
                    this.startBuildBattle(player);
                    break;
                case "chicken_shooter":
                    this.startChickenShooter(player);
                    break;
                case "hide_and_seek":
                    this.startHideAndSeek(player);
                    break;
                case "tnt_run":
                    this.startTntRun(player);
                    break;
                case "spleef":
                    this.startSpleef(player);
                    break;
                case "sumo":
                    this.startSumo(player);
                    break;
                case "parkour":
                    this.startParkour(player);
                    break;
                case "maze":
                    this.startMaze(player);
                    break;
            }
        }
         return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        

        // Existing tab completion logic for /startminigame
        if (command.getName().equalsIgnoreCase("startminigame")) {
            return args.length == 1 ? Arrays.asList("chicken_shooter", "build_battle", "hide_and_seek", "tnt_run", "spleef", "sumo", "parkour", "maze") : null;
        }
        return null;
    }

    private void startMinigame(Player player, String minigameSlug, String minigameName) {
        Location mainSpawn = storageManager.loadLocation("minigame." + minigameSlug + ".spawn.main");
        Location extraSpawn = storageManager.loadLocation("minigame." + minigameSlug + ".spawn.extra");

        if (mainSpawn != null && extraSpawn != null) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.teleport(mainSpawn);
            }
            Main.setMinigame(minigameSlug);
            player.getWorld().sendMessage(Component.text(minigameName + " started"));
        } else {
            player.sendMessage("Could not start " + minigameName + ". Both 'main' and 'extra' spawn points must be set.");
        }
    }

    private void startBuildBattle(Player player) {
        startMinigame(player, "build_battle", "Build Battle");
    }

    private void startChickenShooter(Player player) {
        startMinigame(player, "chicken_shooter", "Chicken Shooter");
    }

    private void startHideAndSeek(Player player) {
        startMinigame(player, "hide_and_seek", "Hide And Seek");
    }

    private void startTntRun(Player player) {
        startMinigame(player, "tnt_run", "TNT Run");
    }

    private void startSpleef(Player player) {
        startMinigame(player, "spleef", "Spleef");
    }

    private void startSumo(Player player) {
        startMinigame(player, "sumo", "Sumo");
    }

    private void startParkour(Player player) {
        startMinigame(player, "parkour", "Parkour");
    }

    private void startMaze(Player player) {
        startMinigame(player, "maze", "Maze");
    }
}
