package me.wilsonhu.authom.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wilsonhu.authom.AuthOM;
import me.wilsonhu.authom.manager.Command;

public class Login extends Command{

	public Login(AuthOM authom) {
		super("login", authom.getConfig().getString("Messages.LoginSyntax"));
	}

	@Override
	public void onCommand(AuthOM authom, CommandSender sender, org.bukkit.command.Command command, String label, String[] arg) throws Exception {
		Player p = (Player) sender;
		if(!authom.getPlayerHandler().isPlayer(p)) {
			p.sendMessage(authom.getConfig().getString("Messages.LoginAlready"));
			return;
		}
		if (arg.length == 0 || arg.length < 1) {
			p.sendMessage(this.getSyntax());
		}
		if (arg.length == 1) {
			String ip = null;
			if(authom.getConfig().getBoolean("Settings.AutoLogin")) {
				ip = p.getAddress().getHostName();
			}
			if(authom.getPlayerHandler().login(p, arg[0], ip)) {
				authom.getPlayerHandler().removePlayer(p);
			}
		}
	}

}
