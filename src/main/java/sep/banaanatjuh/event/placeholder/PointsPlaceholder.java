package sep.banaanatjuh.event.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import sep.banaanatjuh.event.storage.StorageManager;

public class PointsPlaceholder extends PlaceholderExpansion {

    private final StorageManager storageManager;

    public PointsPlaceholder(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public @NotNull String getAuthor() {
        return "Banaanatjuh";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "minigamepoints";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        // Reads points from the database using the key "points.<uuid>"
        String points = storageManager.readData("points." + player.getUniqueId().toString());
        return points != null ? points : "0";
    }
}
