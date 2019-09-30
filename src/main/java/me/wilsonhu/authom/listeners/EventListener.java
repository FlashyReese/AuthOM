package me.wilsonhu.authom.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.wilsonhu.authom.Account;
import me.wilsonhu.authom.AuthOM;

public class EventListener implements Listener{

	private AuthOM authom;
	
	public EventListener(AuthOM authom) {
		this.authom = authom;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(getAuthOM().getConfig().getBoolean("Settings.AutoLogin")) {
			if(getAuthOM().getPlayerHandler().isPlayer(p)) {
				Account a = getAuthOM().getPlayerHandler().getAccount(p.getName());
				if(a.getIp() != null) {
					if(a.getIp().equalsIgnoreCase(p.getAddress().getHostName())) {
						p.sendMessage(getAuthOM().getConfig().getString("Messages.AutoLogin"));
						return;
					}
				}
			}
		}
		getAuthOM().getPlayerHandler().addPlayer(p);
		getAuthOM().getPlayerHandler().addEffects(p);
		if(getAuthOM().getPlayerHandler().isRegistered(p.getName())) {
			p.sendMessage(getAuthOM().getConfig().getString("Messages.LoginSyntax"));
		}else {
			p.sendMessage(getAuthOM().getConfig().getString("Messages.RegisterSyntax"));
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (getAuthOM().getPlayerHandler().isPlayer(p)) {
			e.setCancelled(true);
			if(getAuthOM().getPlayerHandler().isRegistered(p.getName())) {
				p.sendMessage(getAuthOM().getConfig().getString("Messages.LoginSyntax"));
			}else {
				p.sendMessage(getAuthOM().getConfig().getString("Messages.RegisterSyntax"));
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e != null) {
			if(e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if(getAuthOM().getPlayerHandler().isPlayer(p)) {
					e.setCancelled(true);
				}
			}
			if(e.getDamager() instanceof Player) {
				Player p = (Player) e.getDamager();
				if(getAuthOM().getPlayerHandler().isPlayer(p)) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		getAuthOM().getPlayerHandler().removePlayer(p);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (getAuthOM().getPlayerHandler().isPlayer(p))
			e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (getAuthOM().getPlayerHandler().isPlayer(p))
			e.setCancelled(true);
	}

	@EventHandler
	public void onInventorySaver(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (getAuthOM().getPlayerHandler().isPlayer(p)) {
			e.setCancelled(true);
			e.getView().close();
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (getAuthOM().getPlayerHandler().isPlayer(p)) {
			e.setCancelled(true);
			e.getView().close();
		}
	}

	@EventHandler
	public void onInventoryDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (getAuthOM().getPlayerHandler().isPlayer(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		if(e instanceof Player) {
			Player p = (Player) e.getEntity();
			if(getAuthOM().getPlayerHandler().isPlayer(p)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockCmds(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (getAuthOM().getPlayerHandler().isPlayer(p)) {
			String msg = e.getMessage().substring(1);
			if(!this.getAuthOM().getCommandManager().isCommand(msg)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (getAuthOM().getPlayerHandler().isPlayer(p)) {
			Location to = e.getFrom();
			to.setPitch(e.getTo().getPitch());
			to.setYaw(e.getTo().getYaw());
			e.setTo(to);
		}
	}
	
	
	public AuthOM getAuthOM() {
		return authom;
	}
}
