package me.wilsonhu.authom.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wilsonhu.authom.AuthOM;
import me.wilsonhu.authom.manager.Command;

public class ChangePassword extends Command{

	public ChangePassword(AuthOM authom) {
		super("changepassword", authom.getConfig().getString("Messages.ChangePasswordSyntax"));
	}

	@Override
	public void onCommand(AuthOM authom, CommandSender sender, org.bukkit.command.Command command, String label, String[] args) throws Exception {
		Player p = (Player)sender;
		if(!authom.getPlayerHandler().isRegistered(p.getName())) {
			p.sendMessage(authom.getConfig().getString("Messages.RegisterSyntax"));
			return;
		}
		if(authom.getPlayerHandler().isPlayer(p)) {
			p.sendMessage(authom.getConfig().getString("Messages.LogoutNotLogin"));	
		}else {
			if(args.length == 0 && (args.length > 3 || args.length < 3)) {
				p.sendMessage(this.getSyntax());
				return;
			}
			String oldpwd = args[0];
			String newpwd1 = args[1];
			String newpwd2 = args[2];
			if(newpwd1.equals(newpwd2)) {
				authom.getPlayerHandler().updateAccount(p, oldpwd, newpwd1);
			}else {
				p.sendMessage(authom.getConfig().getString("Messages.ChangePassword2"));
			}
		}
	}

}
