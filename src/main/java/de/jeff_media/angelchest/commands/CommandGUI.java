package de.jeff_media.angelchest.commands;

import de.jeff_media.angelchest.Main;
import de.jeff_media.angelchest.config.Permissions;
import de.jeff_media.angelchest.enums.Features;
import de.jeff_media.daddy.Daddy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CommandGUI implements CommandExecutor {

    final Main main;

    public CommandGUI() {
        this.main = Main.getInstance();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

        if(!Daddy.allows(Features.GUI)) {
            sender.sendMessage(main.messages.MSG_PREMIUMONLY);
            return true;
        }

        if(!sender.hasPermission(Permissions.ALLOW_USE)) {
            sender.sendMessage(main.messages.MSG_NO_PERMISSION);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(main.messages.MSG_PLAYERSONLY);
            return true;
        }

        Player player = (Player) sender;

        main.guiManager.showMainGUI(player);

        return true;
    }
}