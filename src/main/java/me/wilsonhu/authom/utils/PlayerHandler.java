package me.wilsonhu.authom.utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.wilsonhu.authom.Account;
import me.wilsonhu.authom.AuthOM;

public class PlayerHandler {

	private ArrayList<Account> accounts = new ArrayList<Account>();
	private ArrayList<Player> nonAuthenticatedPlayers = new ArrayList<Player>();
	
	private AuthOM authom;
	
	public PlayerHandler(AuthOM authom) {
		this.authom = authom;
	}

	public boolean isRegistered(String name) {
		for (Account a : getAccounts()) {
			if (a.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public Account getAccount(String name) {
		for (Account a : getAccounts()) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}

	public void registerAccount(Player p, String password, String ip) {
		if (isRegistered(p.getName())) {
			p.sendMessage(getAuthOM().getConfig().getString("Messages.RegisterationAlready"));
			return;
		}
		getAccounts().add(new Account(p.getName(), hash(getAuthOM().getConfig().getString("Settings.EncryptionMethod"), password), ip));
		p.sendMessage(getAuthOM().getConfig().getString("Messages.RegisterationSuccessful"));
		new JsonManager().writeJson(new File("plugins/AuthOM"), "users", this.getAccounts());
	}

	public boolean login(Player p, String password, String ip) {
		if (isRegistered(p.getName())) {
			Account account = getAccount(p.getName());
			if (account.getPassword().equals(hash(getAuthOM().getConfig().getString("Settings.EncryptionMethod"), password))) {
				if (account.getIp() == null || !account.getIp().equals(ip)) {
					account.setIp(ip);
				}
				p.sendMessage(getAuthOM().getConfig().getString("Messages.LoginSuccess"));
				removeEffects(p);
				successfulLogin(p);
				return true;
			}else {
				p.sendMessage(getAuthOM().getConfig().getString("Messages.LoginFail"));
				invalidLogin(p);
				return false;
			}
		} else {
			p.sendMessage(getAuthOM().getConfig().getString("Messages.RegisterSyntax"));
			return false;
		}
	}
	
	public void updateAccount(Player p, String oldpwd, String newpwd) {
		if(!isRegistered(p.getName())) {
			p.sendMessage(getAuthOM().getConfig().getString("Messages.RegisterSyntax"));
		}else {
			if(isPlayer(p)) {
				p.sendMessage(getAuthOM().getConfig().getString("Messages.LoginSyntax"));
			}else {
				Account account = getAccount(p.getName());
				if(account.getPassword().equals(hash(getAuthOM().getConfig().getString("Settings.EncryptionMethod"), oldpwd))) {
					account.setPassword(hash(getAuthOM().getConfig().getString("Settings.EncryptionMethod"), newpwd));
					p.sendMessage(getAuthOM().getConfig().getString("Messages.ChangePasswordSuccess"));
					new JsonManager().writeJson(new File("plugins/AuthOM"), "users", this.getAccounts());
				}else {
					p.sendMessage(getAuthOM().getConfig().getString("Messages.ChangePasswordSyntax"));
					p.sendMessage(getAuthOM().getConfig().getString("Messages.ChangePassword1"));
				}
			}
		}
	}

	public String hash(String method, String key) {
		method = method.trim().toLowerCase();
		if(method.equalsIgnoreCase("md5")) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] hashInBytes = md.digest(key.getBytes(StandardCharsets.UTF_8));
		        StringBuilder sb = new StringBuilder();
		        for (byte b : hashInBytes) {
		            sb.append(String.format("%02x", b));
		        }
		        return sb.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
	        
		}else if(method.equalsIgnoreCase("sha-512")) {
			try {
		        MessageDigest md = MessageDigest.getInstance("SHA-512");
		        md.update(key.getBytes(StandardCharsets.UTF_8));
		        byte[] bytes = md.digest(key.getBytes(StandardCharsets.UTF_8));
		        StringBuilder sb = new StringBuilder();
		        for(int i=0; i< bytes.length ;i++){
		            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		        }
		        return sb.toString();
		    } catch (NoSuchAlgorithmException e) {
		        e.printStackTrace();
		    }
		}else if(method.equalsIgnoreCase("sha-256")) {
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] encodedhash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
				StringBuffer hexString = new StringBuffer();
			    for (int i = 0; i < encodedhash.length; i++) {
			    String hex = Integer.toHexString(0xff & encodedhash[i]);
			    if(hex.length() == 1) hexString.append('0');
			        hexString.append(hex);
			    }
			    return hexString.toString();
			}catch(NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void addEffects(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2147483647, 5));
		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2147483647, 5));
	}

	public void removeEffects(Player p) {
		p.removePotionEffect(PotionEffectType.BLINDNESS);
		p.removePotionEffect(PotionEffectType.CONFUSION);
	}

	public void successfulLogin(Player p) {
		p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 5.0F, 12.0F);
	}

	public void invalidLogin(Player p) {
		p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 5.0F, 12.0F);
	}

	public void addPlayer(Player p) {
		getNonAuthPlayers().add(p);
	}

	public boolean isPlayer(Player p) {
		return getNonAuthPlayers().contains(p);
	}

	public void removePlayer(Player p) {
		getNonAuthPlayers().remove(p);
	}

	public ArrayList<Player> getNonAuthPlayers() {
		return nonAuthenticatedPlayers;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
	public AuthOM getAuthOM() {
		return authom;
	}
}
