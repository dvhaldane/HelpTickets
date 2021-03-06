package me.dotdash.helptickets.command;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import me.dotdash.helptickets.HelpTickets;

import java.util.UUID;

public class CommandTicketComplete implements CommandExecutor {

    private final HelpTickets plugin;

    public CommandTicketComplete(HelpTickets plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String id = args.<String>getOne("id").get();
        CommentedConfigurationNode ticket = plugin.getTickets().get(id);

        if (ticket.isVirtual()) {
            src.sendMessage(Text.of(TextColors.RED, "That ticket does not exist!"));
            return CommandResult.success();
        }

        if(ticket.getNode("completed").getBoolean()) {
            src.sendMessage(Text.of(TextColors.RED, "That ticket is already completed."));
            return CommandResult.success();
        } else {
            Sponge.getServer().getPlayer(UUID.fromString(ticket.getNode("player").getString()))
                    .ifPresent(player -> player.sendMessage(Text.of(TextColors.GREEN, "Your ticket has been completed by ",
                            TextColors.WHITE, src.getName()))
                    );
        }

        ticket.getNode("completed").setValue(true);
        plugin.getTickets().save();

        src.sendMessage(Text.of(TextColors.GREEN, "Ticket completed."));
        return CommandResult.success();
    }
}