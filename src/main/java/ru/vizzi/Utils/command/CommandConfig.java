package ru.vizzi.Utils.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandConfig extends CommandBase {
    @Override
    public String getCommandName() {
        return "libconf";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "libconf";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] args) {

    }
}
