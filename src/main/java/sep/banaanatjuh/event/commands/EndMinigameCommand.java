package sep.banaanatjuh.event.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sep.banaanatjuh.event.Main;
import sep.banaanatjuh.event.storage.StorageManager;

public class EndMinigameCommand implements CommandExecutor {
   private final StorageManager storageManager;

   public EndMinigameCommand(StorageManager storageManager) {
      this.storageManager = storageManager;
   }

   @Override
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("This command can only be used by players!");
         return true;
      } else {
         Player player = (Player)sender;
         String currentMinigame = Main.getMinigame();
         if (currentMinigame.equals("none")) {
            player.sendMessage("No minigame is currently active!");
            return true;
         } else {
            Location lobby = storageManager.loadLocation("minigame.main.spawn.extra");
            if (lobby != null) {
               for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                  onlinePlayer.teleport(lobby);
                  String points = storageManager.readData("points." + onlinePlayer.getUniqueId().toString());
                  onlinePlayer.sendMessage("The minigame has ended!\n you ender with " + points + " points!");
               }
            }

            player.sendMessage("Ended the current minigame (" + currentMinigame + ")!");
            Main.setMinigame("none");
            return true;
         }
      }
   }
}
