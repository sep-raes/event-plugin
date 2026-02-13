package sep.banaanatjuh.event.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GivePointsCommand implements CommandExecutor, TabCompleter {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("event.command.givepointscommand")) {
            player.sendMessage("&4You don't have the permission to do this");
        }
        return GivePoints(player, args);
    }

    public boolean GivePoints(Player player,String[] args) {

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }
}
