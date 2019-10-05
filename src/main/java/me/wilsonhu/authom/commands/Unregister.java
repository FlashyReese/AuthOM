package me.wilsonhu.authom.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wilsonhu.authom.AuthOM;
import me.wilsonhu.authom.manager.Command;

public class Unregister extends Command{

	public Unregister(AuthOM authom) {
		super("unregister", authom.getConfig().getString("Messages.UnregisterSyntax"));
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
			return;
		}
		if (args.length > 3 || args.length < 3) {
			p.sendMessage(this.getSyntax());
		}
		if (args.length == 3) {
			String user = args[0];
			String password1 = args[1];
			String password2 = args[2];
			if(p.getName().equals(user)) {
				if(password1.equals(password2)) {
					authom.getPlayerHandler().unregisterAccount(p, password1);
					authom.getPlayerHandler().addEffects(p);
					authom.getPlayerHandler().addPlayer(p);
				}else {
					p.sendMessage(this.getSyntax());
					p.sendMessage(authom.getConfig().getString("Messages.RegisterationPassword"));
				}
			}else{
				Player p2 = Bukkit.getPlayerExact(user);
			}
			
		}
	}

}
