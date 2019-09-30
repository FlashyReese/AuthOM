package me.wilsonhu.authom.manager;

import org.bukkit.command.CommandSender;

import me.wilsonhu.authom.AuthOM;

public abstract class Command {
	private String name;
	private String syntax;

	public Command(String name, String syntax)
	{
		this.name = name;
		this.syntax = syntax;
	}
	
	public abstract void onCommand(AuthOM authom, CommandSender sender, org.bukkit.command.Command command, String label, String[] arg) throws Exception;
	
	public String getName()
	{
		return name;
	}
	
	public String getSyntax()
	{
		return syntax;
	}
	
}