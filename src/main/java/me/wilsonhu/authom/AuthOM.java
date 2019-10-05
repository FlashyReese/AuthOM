package me.wilsonhu.authom;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.reflect.TypeToken;

import me.wilsonhu.authom.listeners.EventListener;
import me.wilsonhu.authom.manager.CommandManager;
import me.wilsonhu.authom.utils.JsonManager;
import me.wilsonhu.authom.utils.PlayerHandler;

public class AuthOM extends JavaPlugin implements Listener {

	private PlayerHandler playerHandler;
	private CommandManager commandManager = new CommandManager(this);

	public void onEnable() {
		loadConfig();
		for(Account a: this.getPlayerHandler().getAccounts()) {
			a.setIp(null);
		}
		for(Player p : Bukkit.getOnlinePlayers()) {
			this.getPlayerHandler().addPlayer(p);
			this.getPlayerHandler().addEffects(p);
			if(this.getPlayerHandler().isRegistered(p.getName())) {
				p.sendMessage(getConfig().getString("Messages.LoginSyntax"));
			}else {
				p.sendMessage(getConfig().getString("Messages.RegisterSyntax"));
			}
		}
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		if (!new File("plugins/AuthOM/users.json").exists()) {
			new JsonManager().writeJson(new File("plugins/AuthOM"), "users", this.getPlayerHandler().getAccounts());
		} else {
			this.getPlayerHandler().setAccounts(new JsonManager().readJson(new File("plugins/AuthOM"), "users",
					new TypeToken<ArrayList<Account>>() {
					}.getType()));
			for(Account a: this.getPlayerHandler().getAccounts()) {
				a.setIp(null);
			}
		}
	}

	public void onDisable() {
		new JsonManager().writeJson(new File("plugins/AuthOM"), "users", this.getPlayerHandler().getAccounts());
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			if (getCommandManager().onCommand(this, sender, command, label, args))
				
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void loadConfig() {
		FileConfiguration cfg = getConfig();

		cfg.options().copyDefaults(true);
		saveConfig();
	}

	public PlayerHandler getPlayerHandler() {
		if (playerHandler == null)
			playerHandler = new PlayerHandler(this);
		return playerHandler;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

}
