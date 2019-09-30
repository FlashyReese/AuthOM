package me.wilsonhu.authom.commands;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wilsonhu.authom.AuthOM;
import me.wilsonhu.authom.manager.Command;
import me.wilsonhu.authom.utils.JsonManager;

public class Logout extends Command{

	public Logout() {
		super("logout", "logout");
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
			authom.getPlayerHandler().addPlayer(p);
			authom.getPlayerHandler().addEffects(p);
			authom.getPlayerHandler().getAccount(p.getName()).setIp(null);
			new JsonManager().writeJson(new File("plugins/AuthOM"), "users", authom.getPlayerHandler().getAccounts());
			p.sendMessage(authom.getConfig().getString("Messages.LogoutSuccessful"));	
		}
	}

}
