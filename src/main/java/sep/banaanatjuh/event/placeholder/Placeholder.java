package sep.banaanatjuh.event.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sep.banaanatjuh.event.Main;

public class Placeholder extends PlaceholderExpansion {
   @Override
   @NotNull
   public String getAuthor() {
      return "Banaanatjuh";
   }

   @Override
   @NotNull
   public String getIdentifier() {
      return "minigame";
   }

   @Override
   @NotNull
   public String getVersion() {
      return "1.0.0";
   }

   @Override
   public boolean persist() {
      return true;
   }

   @Override
   public String onPlaceholderRequest(Player player, @NotNull String params) {
      String currentMinigame = Main.getMinigame();
      
      switch (currentMinigame) {
         case "build_battle":
            return "Build Battle";
         case "chicken_shooter":
            return "Chicken Shooter";
         case "hide_and_seek":
            return "Hide And Seek";
         case "tnt_run":
            return "TNT Run";
         case "spleef":
            return "Spleef";
         case "sumo":
            return "Sumo";
         case "parkour":
            return "Parkour";
         case "maze":
            return "Maze";
         case "none":
            return "Geen Minigame Bezig";
         default:
            return "No Minigame";
      }
   }
}
