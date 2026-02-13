package sep.banaanatjuh.event;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RoundPlaceholder extends PlaceholderExpansion {
    @Override
    @NotNull
    public String getAuthor() {
        return "Banaanatjuh";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "minigameround";
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
        if (Main.getRound().equals("1")) {
            return "1";
        }
        if (Main.getRound().equals("2")) {
            return "2";
        }
        if (Main.getRound().equals("3")) {
            return "3";
        }
        return "0";
    }
}
