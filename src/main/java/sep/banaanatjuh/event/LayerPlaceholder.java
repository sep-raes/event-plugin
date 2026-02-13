package sep.banaanatjuh.event;

import
        me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LayerPlaceholder extends PlaceholderExpansion {
   @Override
   @NotNull
   public String getAuthor() {
      return "Banaanatjuh";
   }

   @Override
   @NotNull
   public String getIdentifier() {
      return "minigamelayer";
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
      double y = player.getY();
      String minigame = Main.getMinigame();

      if (!Objects.equals(minigame, "tnt_run")) {
         return "Error TNT_RUN not active";
      }
      if (y >= 94 && y <= 100) {
         return "1/5";
      }
      if (y >= 88 && y <= 94) {
         return "2/5";
      }
      if (y >= 82 && y <= 88) {
         return "3/5";
      }
      if (y >= 76 && y <= 82) {
         return "4/5";
      }
      if (y >= 70 && y <= 76) {
         return "5/5";
      }
      if (y < 70 || y > 100 ) {
         return "N.V.T.";
      }
      return "error";

      

   }
}
