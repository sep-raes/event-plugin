package sep.banaanatjuh.event.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class CheckpointPlaceholder extends PlaceholderExpansion {
    @Override
    @NotNull
    public String getAuthor() {
        return "Banaanatjuh";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "minigamecheckpoints";
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
        return "unimplemented";
    }
}
