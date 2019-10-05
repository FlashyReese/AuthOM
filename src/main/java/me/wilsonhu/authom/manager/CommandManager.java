package me.wilsonhu.authom.manager;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import me.wilsonhu.authom.AuthOM;
import me.wilsonhu.authom.commands.*;

public class CommandManager {
	
	private ArrayList<Command> commands = new ArrayList<Command>();
	private AuthOM authom;

	public CommandManager(AuthOM authom) {
		this.authom = authom;
		loadCommands();
	}
	
	private void loadCommands() {
		this.getCommands().add(new ChangePassword(this.getAuthOM()));
		this.getCommands().add(new Login(this.getAuthOM()));
		this.getCommands().add(new Logout());
		this.getCommands().add(new Register(this.getAuthOM()));
		this.getCommands().add(new Unregister(this.getAuthOM()));
	}

	
	public boolean onCommand(AuthOM authom, CommandSender sender, org.bukkit.command.Command command, String label, String[] arg) throws Exception {
		for(Command cmd: this.getCommands()) {
			if (command.getName().equalsIgnoreCase(cmd.getName())) {
				cmd.onCommand(authom, sender, command, label, arg);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Command> getCommands() {
		return commands;
	}
	
	public AuthOM getAuthOM() {
		return authom;
	}
	
	public boolean isCommand(String cmd) {
		for(Command c: getCommands()) {
			if(cmd.toLowerCase().startsWith(c.getName().toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	public Command getCommand(Class<?extends Command> leCommandClass) {
		for (Command c: getCommands())
		{
			if (c.getClass() == leCommandClass)
			{
				return c;
			}
		}
		return null;
	}

}
