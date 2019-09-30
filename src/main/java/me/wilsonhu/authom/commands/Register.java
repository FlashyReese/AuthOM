package me.wilsonhu.authom.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wilsonhu.authom.AuthOM;
import me.wilsonhu.authom.manager.Command;

public class Register extends Command{

	public Register(AuthOM authom) {
		super("register", authom.getConfig().getString("Messages.RegisterSyntax"));
	}

	@Override
	public void onCommand(AuthOM authom, CommandSender sender, org.bukkit.command.Command command, String label, String[] args) throws Exception {
		Player p = (Player)sender;
		if(authom.getPlayerHandler().isRegistered(p.getName())) {
			p.sendMessage(authom.getConfig().getString("Messages.RegisterationAlready"));
			return;
		}
		if (args.length == 0 && (args.length != 1 || args.length < 2)) {
			p.sendMessage(this.getSyntax());
		}
		if (args.length == 2) {
			String password1 = args[0];
			String password2 = args[1];
			if(password1.equals(password2)) {
				authom.getPlayerHandler().registerAccount(p, password1, null);
				authom.getPlayerHandler().removePlayer(p);
				authom.getPlayerHandler().removeEffects(p);
			}else {
				p.sendMessage(this.getSyntax());
				p.sendMessage(authom.getConfig().getString("Messages.RegisterationPassword"));
			}
		}
	}

}
