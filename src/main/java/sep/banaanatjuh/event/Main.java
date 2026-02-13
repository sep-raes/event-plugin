package sep.banaanatjuh.event;

import java.time.Duration;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import sep.banaanatjuh.event.commands.EndMinigameCommand;
import sep.banaanatjuh.event.commands.MinigameCommand;
import sep.banaanatjuh.event.commands.SetMinigameSpawnCommand;
import sep.banaanatjuh.event.storage.StorageManager;


public final class Main extends JavaPlugin implements Listener {
   private static String minigame = "none";
   private static String round = "none";
   private StorageManager storageManager;
   private static Main instance;

   public void onEnable() {
      instance = this;
      this.getServer().getPluginManager().registerEvents(this, this);
      
      this.storageManager = new StorageManager(this);

      if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
         new Placeholder().register();
         new PointsPlaceholder(this.storageManager).register();
         new LayerPlaceholder().register();
         new RoundPlaceholder().register();
         new TimePlaceholder().register();

      }

      String savedMinigame = this.storageManager.readData("current_minigame");
      if (savedMinigame != null) {
         minigame = savedMinigame;
      }

      String savedRound = this.storageManager.readData("current_round");
      if (savedRound != null) {
         round = savedRound;
      }

      this.getCommand("startminigame").setExecutor(new MinigameCommand(storageManager));
      this.getCommand("endminigame").setExecutor(new EndMinigameCommand(storageManager));
      this.getCommand("setminigamespawn").setExecutor(new SetMinigameSpawnCommand(storageManager));
   }

   @EventHandler
   public void onPlayerJoinEvent(PlayerJoinEvent e) {
      Player player = e.getPlayer();

      Location hub = storageManager.loadLocation("minigame.main.spawn.main");
      if (hub != null) {
         player.teleport(hub);
      }

      Times times = Times.times(Duration.ofMillis(500L), Duration.ofSeconds(5L), Duration.ofMillis(500L));
      Component WelcomeCreator = MiniMessage.miniMessage().deserialize("<bold><gradient:#FF2D91:#7A00FF>Welcome Creator!</gradient></bold>");
      Component WelcomeCreatorSub = MiniMessage.miniMessage().deserialize("<gradient:#0202C4:#3939ED>Succes!</gradient>");
      Component WelcomeAdmin = MiniMessage.miniMessage().deserialize("<bold><gradient:#FF2D91:#7A00FF>Welcome Event Staff!</gradient></bold>");
      Component WelcomeAdminSub = MiniMessage.miniMessage().deserialize("<gradient:#0202C4:#3939ED>Zet OP meldingen uit als je streamt!</gradient>");
      Title title = Title.title(WelcomeCreator, WelcomeCreatorSub, times);
      Title admin = Title.title(WelcomeAdmin, WelcomeAdminSub, times);
      if (!player.isOp()) {
         player.showTitle(title);
      } else {
         player.showTitle(admin);
      }

   }

   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent event) {
      Player player = event.getEntity();
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove alive");
      World world = player.getWorld();
      if (Objects.equals(getMinigame(), "hide_and_seek")) {
         Component component = Component.text(player.getName() + " was found!").color(TextColor.color(104, 14, 237));
         world.sendMessage(component);
      }

   }

   @EventHandler
   public void onPlayerRespawn(PlayerRespawnEvent event) {
      if (!minigame.equals("none")) {
         Location extraSpawn = storageManager.loadLocation("minigame." + minigame + ".spawn.extra");
         if (extraSpawn != null) {
            event.setRespawnLocation(extraSpawn);
         }
      }
   }

   public void onDisable() {
   }

   public static String getMinigame() {
      return minigame;
   }

   public static String getRound() { return round; }

   public static void setMinigame(String newMinigame) {
      minigame = newMinigame;
      if (instance != null && instance.storageManager != null) {
         instance.storageManager.writeData("current_minigame", minigame);
      }
   }

   public static void setRound(String newRound) {
      round = newRound;

      if (instance != null && instance.storageManager != null) {
         instance.storageManager.writeData("current_round", String.valueOf(round));
      }
   }
}
