package com.warido.plugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.warido.plugin.Liberaries.CustomItem;

//import com.warido.plugin.NametagEdit.NameTagColor;

public class Listeners implements Listener {
	public static Plugin plugin;
	
	public Listeners(Plugin p) {
		plugin = p;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		ItemStack item = player.getItemInHand();
		if (item.getType().equals(Material.STICK) && CustomItem.isCustomItem(item, Main.specials[0].styledName)) {
			event.setCancelled(true);
		} else if (item.getType().equals(Material.GOLD_PICKAXE)
				&& CustomItem.isCustomItem(item, Main.specials[2].styledName)) {
//			float size = 5f;
			item.setDurability((short) 0);
			player.updateInventory();
			Location loc = block.getLocation();
			Vector v1 = block.getLocation().toVector();
			int radius = 8;
			int radiusY = 5;
			for (int x = (loc.getBlockX() - radius); x <= (loc.getBlockX() + radius); x++) {
				for (int y = (loc.getBlockY() - radiusY); y <= (loc.getBlockY() + radiusY); y++) {
					for (int z = (loc.getBlockZ() - radius); z <= (loc.getBlockZ() + radius); z++) {
						Location l = new Location(loc.getWorld(), x, y, z);
						Block b = event.getBlock().getWorld().getBlockAt(l);
						Boolean hard = true;
						float hardness = -1;
						Field field = null;
						try {
							field = net.minecraft.server.v1_8_R3.Block.class.getDeclaredField("strength");
							field.setAccessible(true);
							hardness = field.getFloat(net.minecraft.server.v1_8_R3.Block.getById(b.getTypeId()));
						} catch (NoSuchFieldException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						
						if(hardness < 0f | hardness == 50.0f) {
							hard = false;
						}
						
						if(!b.getType().equals(Material.AIR) & b.getType().isSolid() & hard) {
							double dist = v1.distance(b.getLocation().toVector());
							if(dist < radius) {
								double delay = Math.floor(Main.map((float) dist, 1f, (float) radius, 0f, 30f));
								Long delayL = Long.parseLong(((int) delay)+"");
								BukkitScheduler scheduler = plugin.getServer().getScheduler();
								scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {

									@Override
									public void run() {
										FallingBlock fb = block.getWorld().spawnFallingBlock(l, b.getType(),
												b.getData());
										Vector v2 = b.getLocation().toVector();
										v2 = v2.subtract(v1);
										Vector v = new Vector(v2.getX(),
												Main.map((float) dist, 1f, 4f, 10f, 1f), v2.getZ());
										v.add(new Vector(Math.random()*2-1, Math.random()*2, Math.random()*2-1));
										v = v.multiply(new Vector(0.15,0.13,0.15));
										fb.setVelocity(v);
										float pitch = Float.parseFloat(String.valueOf(Math.random()*1.2+0.3));
										b.getWorld().playSound(l, Sound.EXPLODE, 0.4f, pitch);
										b.setType(Material.AIR);
									}
								}, delayL);
								b.getWorld().loadChunk(b.getChunk());
							}
						}
					}
				}
			}
			Vector v2 = player.getLocation().toVector();
			v2 = v2.subtract(v1);
			Vector v = new Vector(v2.getX(), 1, v2.getZ());
			v.add(new Vector(Math.random()*2-1, 0, Math.random()*2-1));
			v = v.multiply(new Vector(0.15, 1,0.15));
			player.setVelocity(v);
			event.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlaced(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		if (item != null) {
			if (item.getType().equals(Material.SIGN) && CustomItem.isCustomItem(item, Main.specials[3].styledName)) {
				Block b = event.getBlock();
				event.setCancelled(true);
				BukkitScheduler scheduler = plugin.getServer().getScheduler();
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {

					@Override
					public void run() {
						b.setType(Material.SIGN_POST);
						Sign data = (Sign) b.getState();
						data.setLine(0, "==========");
						data.setLine(1, ChatColor.RED + "X: " + b.getLocation().getBlockX());
						data.setLine(2, ChatColor.GREEN + "Y: " + b.getLocation().getBlockY());
						data.setLine(3, ChatColor.BLUE + "Z: " + b.getLocation().getBlockZ());
						data.update(true);
//						p.sendSignChange(sign.getLocation(), data.getLines());
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.4f, 0.6f);
					}
				}, 1);
				p.closeInventory();
				return;
			} else if (item.getType().equals(Material.TNT) && CustomItem.isCustomItem(item, Main.specials[4].styledName)) {
				Block b = event.getBlock();
				World w = b.getWorld();
				b.setType(Material.AIR);
				w.spawnEntity(b.getLocation(), EntityType.PRIMED_TNT);
			} if (item.getType().equals(Material.WOOL) && CustomItem.isCustomItem(item, Main.specials[5].styledName)) {
				int[] dataOrder = {14, 1, 4, 5, 3, 11, 10, 2, 6};
				int current = Integer.parseInt(item.getItemMeta().getLore().get(1).replace(ChatColor.DARK_GRAY + "", ""));
				Block b = event.getBlock();
				b.setData((byte) dataOrder[current%9]);
				List<String> lore = item.getItemMeta().getLore();
				lore.set(1,ChatColor.DARK_GRAY + "" + (current + 1));
				ItemMeta meta = item.getItemMeta();
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
		}
		String uuid = p.getUniqueId().toString();
		float pitch = 1.0f;
		HashMap<String, Integer> blocksRecently = Main.blocksRecently;
		if (blocksRecently.containsKey(uuid)) {
			int prev = blocksRecently.get(uuid);
			blocksRecently.put(uuid, prev + 1);
			BukkitScheduler scheduler = plugin.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {

				@Override
				public void run() {
					int prev2 = blocksRecently.get(uuid);
					blocksRecently.put(uuid, prev2 - 1);
				}
			}, 25);
		} else {
			blocksRecently.put(uuid, 1);
		}
		int blocks = blocksRecently.get(uuid);
		pitch = Main.map((float) blocks, 0f, 20f, 0.4f, 2.0f);
		p.playSound(p.getLocation(), Sound.NOTE_PIANO, 0.4f, pitch);
		if (Main.removeBlockPlayers.contains(p.getName())) {
			Long delay = Main.rbDefaultDelay;
			if (Main.rbCustomDelay.containsKey(p.getName())) {
				delay = Main.rbCustomDelay.get(p.getName());
			}
			BukkitScheduler scheduler = plugin.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {

				@Override
				public void run() {
					Location loc = event.getBlock().getLocation();
					loc = loc.add(0, 0.5, 0.5);
					if (p.getGameMode().equals(GameMode.SURVIVAL) | p.getGameMode().equals(GameMode.ADVENTURE)) {
						Block b = loc.getBlock();
						for (ItemStack i : b.getDrops()) {
							p.getInventory().addItem(i);
						}
					}
					event.getBlockPlaced().setType(Material.AIR);
					p.playEffect(loc, Effect.SMOKE, BlockFace.UP);
				}
			}, delay);
		}
	}

	@EventHandler
	@SuppressWarnings("deprecation")
	public void onEntityDamage(EntityDamageEvent event) {
		Player owner = plugin.getServer().getPlayer("WillyTybur");
		if (owner != null) {
			if (owner.isOnline()) {
				Main.sendActionBar(owner, Main.cc("&8" + event.getEntity().getName() + " &7took &c"
						+ event.getCause().name() + " &7damage of &8" + event.getDamage()));
			}
		}
		if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
			event.setCancelled(true);
//		} else if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
		} else if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)
				| event.getCause().equals(EntityDamageEvent.DamageCause.SUICIDE)) {

		} else {
			event.setDamage(0.0);
		}
	}

	@EventHandler
	@SuppressWarnings("deprecation")
	public void onEntityExplode(EntityExplodeEvent event) {
		Entity e = event.getEntity();
		List<Block> blocks = event.blockList();
		Vector v1 = e.getLocation().toVector();
		for (Block b : blocks) {
			FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
			Vector v2 = b.getLocation().toVector();
			v2 = v2.subtract(v1);
			Vector v = new Vector(v2.getX(), Main.map((float) v1.distance(b.getLocation().toVector()), 1f, 4f, 10f, 1f),
					v2.getZ());
			v = v.multiply(0.15);
			fb.setVelocity(v);
			b.setType(Material.AIR);
		}
		event.setCancelled(true);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onGameTick(GameTickEvent event) {
		Server s = event.getServer();
		Collection<? extends Player> players = s.getOnlinePlayers();
		for (Player player : players) {
			CraftPlayer p = (CraftPlayer) player.getPlayer();
			int bps = 0;
			if (Main.blocksRecently.get(p.getUniqueId().toString()) != null) {
				bps = Main.blocksRecently.get(p.getUniqueId().toString());
			}
			Date d = new Date();
			String date = d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
			String[] lines = {
					p.getDisplayName(),
					"&2Ping: &a" + p.getHandle().ping + "ms",
					"&2BPS: &a" + bps + "bps",
					"&2Entities: &a" + p.getWorld().getEntities().size(),
					"&7" + date
			};
			Main.updateScoreboard(p, Main.tag, lines);
		}
	}
	
	@EventHandler
	@SuppressWarnings("deprecation")
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory().getName().startsWith(ChatColor.GOLD + "[PR] InvAim")) {
			Player p = (Player) event.getWhoClicked();
			Inventory inv = event.getInventory();
			ItemStack i = event.getCurrentItem();
			if (i == null) {
				return;
			}
			ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1,
					DyeColor.getByWoolData((byte) 15).getData());
			ItemMeta emptyMeta = empty.getItemMeta();
			emptyMeta.setDisplayName(" ");
			empty.setItemMeta(emptyMeta);

			ItemStack note = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.getByWoolData((byte) 5).getData());
			ItemMeta noteMeta = empty.getItemMeta();
			noteMeta.setDisplayName(" ");
			note.setItemMeta(noteMeta);
			ItemStack next1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.getByWoolData((byte) 8).getData());
			ItemMeta nextMeta = empty.getItemMeta();
			nextMeta.setDisplayName(" ");
			next1.setItemMeta(nextMeta);
			String uuid = p.getUniqueId().toString();
			
			if (i.isSimilar(empty) | i.isSimilar(next1)) {
				p.damage(0.001);
			} else if (i.isSimilar(note)) {
				String[] notes = Main.songsList.getSong(Main.songSelected.get(uuid)).getSongString().split("\s");
				int progress = Main.songProgress.get(uuid);
				String n = notes[progress % notes.length];
				String[] blocks = n.split(",");
				for(String b : blocks) {
					float pitch = Main.songsList.noteToPitch(b);
					Sound sound = Main.songsList.noteToSound(b);
					p.playSound(p.getLocation(), sound, 10.0f, pitch);
				}
				if(progress % notes.length == notes.length-1) {
					EntityType fw = EntityType.FIREWORK;
					p.getWorld().spawnEntity(p.getLocation(), fw);
				}
				progress += 1;
				n = notes[progress % notes.length];
				while(n.equalsIgnoreCase("x") | n.indexOf("d") != -1) {
					progress += 1;
					n = notes[progress % notes.length];
				}
				Main.songProgress.put(uuid, progress);
				int next = 0;
				int counter = 0;
				for (ItemStack item : inv.getContents()) {
					if (item.isSimilar(next1)) {
						next = counter;
					}
					counter++;
				}
				int later = (int) Math.floor((Math.random() * inv.getSize()));
				if (later == next) {
					later++;
					if (later == inv.getSize()) {
						later = 0;
					}
				}
				inv.setItem(event.getSlot(), empty);
				inv.setItem(next, note);
				inv.setItem(later, next1);
				p.playEffect(EntityEffect.WOLF_HEARTS);
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory().getName().startsWith(ChatColor.GOLD + "[PR] InvAim")) {
			Player p = (Player) event.getPlayer();
			String uuid = p.getUniqueId().toString();
			Main.songProgress.put(uuid, 0);
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Main.messagesSent++;
		Player p = event.getPlayer();
//		String uuid = p.getUniqueId().toString().replaceAll("-", "");
//		String r = ChatColor.RESET + "";
		String message = event.getMessage();
		for (Map.Entry<String, String> entry : Main.emotes.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			message = message.replaceAll(key, value);
		}
		message = Main.cc(message);
//		String defaultFormat = "&f%p%: ";
//		String format = getConfig().get(uuid + ".chatformat").toString();
//		if(format == null) {
//			format = defaultFormat;
//		} else if(!format.contains("%p%")) {
//			format = defaultFormat;
//		}

//		event.setFormat(cc(format) + message);
//		} else {
			event.setFormat(ChatColor.RED + "❤" +" "
					+ ChatColor.GREEN + p.getDisplayName() + ChatColor.WHITE
					+ ": " + message);
//		}
	}
	
	@EventHandler
	public void onPlayerClicks(PlayerInteractEvent event) {
		Player player = event.getPlayer();
//		Action action = event.getAction();
		ItemStack item = event.getItem();
		Boolean rightClicked = event.getAction().equals(Action.RIGHT_CLICK_AIR)
				| event.getAction().equals(Action.RIGHT_CLICK_BLOCK);
		String uuid = player.getUniqueId().toString();
		if (item != null) {
			if (item.getType().equals(Material.STICK) && CustomItem.isCustomItem(item, Main.specials[0].styledName)) {
				double speed = 3;
				if (rightClicked) {
					Double newSpeed = (double) Main.map(player.getLocation().getPitch(), 90.0f, -90.0f, 0.1f, 10.0f);
					newSpeed = Math.round(newSpeed * 10.0) / 10.0;
					Main.sendActionBar(player,
							ChatColor.DARK_AQUA + "Set the magic wand intensity to " + ChatColor.AQUA + newSpeed);
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f,
							Main.map(newSpeed.floatValue(), 0.1f, 10.0f, 0.5f, 2.0f));
					Main.magicWandIntensity.put(uuid, newSpeed);
				} else {
					if (Main.magicWandIntensity.containsKey(uuid)) {
						speed = Main.magicWandIntensity.get(uuid);
					} else {
						Main.magicWandIntensity.put(uuid, 3.0);
					}
					Vector vel = player.getLocation().getDirection().multiply(speed);
					player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1000000f,
							Main.map((float) speed, 0.1f, 10.0f, 0.5f, 2.0f));
					player.setVelocity(vel);
					player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
//				player.get
				}
			} else if (item.getType().equals(Material.DIAMOND_SWORD)
					&& CustomItem.isCustomItem(item, Main.specials[1].styledName)) {
				if (rightClicked) {
					Location newloc = player.getLocation();
					Vector dir = newloc.getDirection();
					for (float i = 0.0f; i < 8.0; i++) {
						newloc.add(dir);
						if (!player.getWorld().getBlockAt(newloc).isEmpty()) {
							if (!player.getWorld().getBlockAt(newloc.add(dir.getX(), -dir.getY(), -dir.getZ()))
									.isEmpty()) {
								newloc.subtract(dir.getX(), 0, 0);
							}
							if (!player.getWorld().getBlockAt(newloc.add(-dir.getX(), dir.getY(), -dir.getZ()))
									.isEmpty()) {
								newloc.subtract(0, dir.getY(), 0);
							}
							if (!player.getWorld().getBlockAt(newloc.add(-dir.getX(), -dir.getY(), dir.getZ()))
									.isEmpty()) {
								newloc.subtract(0, 0, dir.getZ());
							}
						}
					}
					player.teleport(newloc);
					player.playSound(newloc, Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
				}
			} else if (item.getType().equals(Material.BLAZE_ROD)) {
				if (event.getAction().equals(Action.LEFT_CLICK_AIR)
						| event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					player.playNote(player.getLocation(), Instrument.STICKS, Note.natural(1, Note.Tone.A));
				} else {
					player.playNote(player.getLocation(), Instrument.SNARE_DRUM, Note.natural(1, Note.Tone.A));
				}
			} else if (item.getType().equals(Material.TNT) && CustomItem.isCustomItem(item, Main.specials[4].styledName)) {
				if (!rightClicked) {
					Set<Material> a = null;
					Block block = player.getTargetBlock(a, 100);
					Location bl = block.getLocation();
					Block b = player.getWorld().getBlockAt(bl);
					World w = b.getWorld();
					w.spawnEntity(b.getLocation(), EntityType.PRIMED_TNT);
				}
			}
		}

	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		e.setDeathMessage(
				ChatColor.RED + "❥ " + ChatColor.BOLD + p.getDisplayName() + ChatColor.RESET + ChatColor.RED + " died");
		p.setHealth(p.getMaxHealth());
		Vector zero = new Vector(0, 0, 0);
		p.setVelocity(zero);
		p.setFallDistance(0.0f);
		Location spawn = p.getWorld().getSpawnLocation();
		p.teleport(spawn);
		p.playSound(spawn, Sound.CAT_HIT, (float) 1.0, (float) 1.0);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		char[] color = {'0','c','6','e','a','b'};
		int playerAmount = Bukkit.getServer().getOnlinePlayers().size();
		p.setDisplayName(Main.cc("&6[MVP&" + color[playerAmount & color.length] + "++&6] " + p.getName()));
		e.setJoinMessage(ChatColor.RED + "❥ " + ChatColor.BOLD + p.getDisplayName() + ChatColor.RESET + ChatColor.RED
				+ " joined");
		Main.jinglesList.getSong(0).playSongToAll(plugin);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage(ChatColor.RED + "❥ " + ChatColor.BOLD + p.getDisplayName() + ChatColor.RESET + ChatColor.RED
				+ " left");
		Main.jinglesList.getSong(1).playSongToAll(plugin);
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (Main.safeWalk.contains(p.getUniqueId().toString())) {
//			Location f = event.getFrom();
//			Location t = event.getTo();
//			Vector onebelow = new Vector(0, -0.1, 0);
			event.setCancelled(true);
		}
	}
}
