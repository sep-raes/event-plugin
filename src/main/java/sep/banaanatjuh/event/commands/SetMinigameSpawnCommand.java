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
import sep.banaanatjuh.event.storage.StorageManager;

public class SetMinigameSpawnCommand implements CommandExecutor, TabCompleter {

    private StorageManager storageManager;
    private static final List<String> KNOWN_MINIGAMES = Arrays.asList("chicken_shooter", "build_battle", "hide_and_seek", "tnt_run", "spleef", "sumo", "parkour", "maze", "main");
    private static final List<String> SPAWN_TYPES = Arrays.asList("main", "extra");


    public SetMinigameSpawnCommand(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return false;
        }
        Player player = (Player) sender;
        return onSetMinigameSpawnCommand(player, args);
    }

    private boolean onSetMinigameSpawnCommand(@NotNull Player player, @NotNull String[] args) {
        if (args.length != 3) {
            player.sendMessage("Usage: /setminigamespawn <minigame> <main|extra> <world_name>");
            return false;
        }

        String minigameName = args[0].toLowerCase();
        String spawnType = args[1].toLowerCase();
        String worldName = args[2];

        if (!KNOWN_MINIGAMES.contains(minigameName)) {
            player.sendMessage("Unknown minigame: " + minigameName);
            return false;
        }

        if (!SPAWN_TYPES.contains(spawnType)) {
            player.sendMessage("Invalid spawn type. Use 'main' or 'extra'.");
            return false;
        }

        World world = player.getServer().getWorld(worldName);
        if (world == null) {
            player.sendMessage("World not found: " + worldName);
            return false;
        }

        Location location = player.getLocation();



        if (spawnType.equals("main")) {

            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            double pitch = location.getPitch();
            double yaw = location.getYaw();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvsetspawn " + worldName + ":" + x + "," + y + "," + z + "," + pitch + "," + yaw + " --unsafe");

        }


        String key = "minigame." + minigameName + ".spawn." + spawnType;

        storageManager.saveLocation(key, location);
        player.sendMessage("Set " + spawnType + " spawnpoint for " + minigameName + " to your current location in world " + worldName + ".");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player player = (Player) sender;
        return tabCompleteSetMinigameSpawn(args, player);
    }

    private List<String> tabCompleteSetMinigameSpawn(String[] args, Player player) {
        if (args.length == 1) {
            return KNOWN_MINIGAMES;
        } else if (args.length == 2) {
            return SPAWN_TYPES;
        } else if (args.length == 3) {
            return player.getServer().getWorlds().stream().map(World::getName).toList();
        }
        return null;
    }
}
