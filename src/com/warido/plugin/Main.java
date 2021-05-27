package com.warido.plugin;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.standard.ColorSupported;

import org.apache.commons.lang.StringUtils;
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
import org.bukkit.Note.Tone;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.warido.plugin.JSONMessage.HoverAction;
import com.warido.plugin.Liberaries.ConsoleColor;
import com.warido.plugin.Liberaries.CustomItem;
import com.warido.plugin.Liberaries.ParticleEffect;
import com.warido.plugin.Liberaries.TableGenerator;
import com.warido.plugin.Liberaries.ParticleEffect.NoteColor;
import com.warido.plugin.Liberaries.ParticleEffect.ParticleColor;
import com.warido.plugin.Liberaries.TableGenerator.Alignment;
import com.warido.plugin.Liberaries.TableGenerator.Receiver;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.MobSpawnerAbstract.a;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;

@SuppressWarnings("unused")
public class Main extends JavaPlugin implements Listener {
	FileConfiguration config = this.getConfig();
	public Plugin plugin;
	static String tag = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "WARIDO" + ChatColor.DARK_GREEN + "] "
			+ ChatColor.WHITE;
	private String ctag = ConsoleColor.GREEN + "[" + ConsoleColor.GREEN_BRIGHT + "WARIDO" + ConsoleColor.GREEN + "] "
			+ ConsoleColor.WHITE;
	public String[][] chatColors = { { "Black", "0" }, { "Blue", "1" }, { "Green", "2" }, { "Dark_Aqua", "3" },
			{ "Dark_Red", "4" }, { "Dark_Purple", "5" }, { "Gold", "6" }, { "Gray", "7" }, { "Dark_Gray", "8" },
			{ "Blue", "9" }, { "Green", "a" }, { "Aqua", "b" }, { "Red", "c" }, { "Light_Purple", "d" },
			{ "Yellow", "e" }, { "White", "f" }, { "Magic", "k" }, { "Bold", "l" }, { "Strike", "m" },
			{ "Underline", "n" }, { "Italic", "o" }, { "Reset", "r" } };
	public static Long rbDefaultDelay = 30L;
	public static ArrayList<String> removeBlockPlayers = new ArrayList<String>();
	public static ArrayList<String> safeWalk = new ArrayList<String>();
	public static HashMap<String, String> emotes = new HashMap<String, String>();
	public static HashMap<String, Long> rbCustomDelay = new HashMap<String, Long>();
	public static HashMap<String, Integer> blocksRecently = new HashMap<String, Integer>();
	public static HashMap<String, Double> magicWandIntensity = new HashMap<String, Double>();
	public static HashMap<String, String> hurtAllow = new HashMap<String, String>();
	public static HashMap<String, Integer> songSelected = new HashMap<String, Integer>();
	public static HashMap<String, Integer> songProgress = new HashMap<String, Integer>();
	public static Songs songsList = new Songs();
	public static Songs jinglesList = new Jingle();
	public static Special[] specials = new Special[0];

	public static int commandsRan = 0;
	public static int messagesSent = 0;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new Listeners(Bukkit.getPluginManager().getPlugin("Warido")),
				this);
//		config.addDefault("youAreAwesome", true);
//		Object specialsList = config.get("specials");
//		if(specialsList != null) {
////			for(Object special : specialsList) {
////			log(specialsList + "");
//		     log(new GsonBuilder().setPrettyPrinting().create().toJson(specialsList));
////			}
//		} else {
//			log("SPECIALS IS NULL");
//		}
		Emotes emoteLoader = new Emotes();
		emoteLoader.prepareEmotes(emotes);
		config.options().copyDefaults(true);
		saveConfig();

		songsList.loadSongs(this);
		jinglesList.loadSongs(this);

		this.getCommand("killall").setTabCompleter(new TabCompleterClass());
		this.getCommand("removeblock").setTabCompleter(new TabCompleterClass());
		this.getCommand("special").setTabCompleter(new TabCompleterClass());
//		this.getCommand("world").setTabCompleter(new TabCompleterClass());

		GameTickEvent gametickevent = new GameTickEvent(getServer(), this);
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
//				Bukkit.getPluginManager().callEvent(gametickevent);
			}
		}, 20L);
		specials = this.addSpecial(specials,
				new Special("magicwand", "mw", ChatColor.BOLD + "" + ChatColor.GOLD + "Magic Wand", 280,
						ChatColor.GRAY + "Launch yourself in the direction",
						ChatColor.RESET + "" + ChatColor.GRAY + "you're looking at. " + ChatColor.DARK_GRAY
								+ "(Left Click)",
						"", ChatColor.RESET + "" + ChatColor.GRAY + "Change your launch-intensity depending",
						ChatColor.RESET + "" + ChatColor.GRAY + "on how high you're looking " + ChatColor.DARK_GRAY
								+ "(Right Click)"));
		specials = this.addSpecial(specials, new Special("aspectoftheend", "aote", ChatColor.BLUE + "Aspect of the End",
				276, ChatColor.GRAY + "Teleport " + ChatColor.GREEN + "8 blocks" + ChatColor.GRAY + " ahead of you."));
		specials = this.addSpecial(specials,
				new Special("fatpickaxe", "fatpick", ChatColor.BOLD + "" + ChatColor.YELLOW + "FAT Pickaxe", 285,
						ChatColor.GRAY + "Everytime you mine a block with it,",
						ChatColor.GRAY + "you break some blocks around", ChatColor.GRAY + "it or cause an explosion."));
		specials = this.addSpecial(specials, new Special("coordinatesign", "coordssign",
				ChatColor.YELLOW + "Coordinate Sign", 323, ChatColor.GRAY + "Prints location when you place it"));
		specials = this.addSpecial(specials, new Special("primedtnt", "ptnt",
				ChatColor.RESET + "" + ChatColor.BOLD + "Primed TNT", 46, ChatColor.GRAY + "Is ignited upon placing"));
		specials = this.addSpecial(specials,
				new Special("rainbowwool", "rbwool", ChatColor.RESET + cc("&cR&6a&ei&an&bb&1o&dw &fWool"), 35,
						cc("&c&oR&6&oa&e&oi&a&on&b&ob&1&oo&5&ow&d&os!"), ChatColor.DARK_GRAY + "0"));
		log("--E-N-A-B-L-E-D--");
	}

	@Override
	public void onDisable() {
		System.out.println(ctag + ConsoleColor.GREEN + "TOTAL MSGS: " + ConsoleColor.GREEN_BRIGHT + messagesSent);
		System.out.println(ctag + ConsoleColor.GREEN + "TOTAL CMDS: " + ConsoleColor.GREEN_BRIGHT + commandsRan);
	}

	@SuppressWarnings({ "deprecation" })
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		commandsRan++;
		// Check if an admin command is executed since admin
		Player p = Bukkit.getPlayer(sender.getName());
		String cmd = command.getName().toLowerCase();
		if (cmd.equals("warido")) {
			if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
				line(sender);
				sender.sendMessage(tag + "Commands");
				sender.sendMessage(
						ChatColor.DARK_AQUA + "/colors: " + ChatColor.WHITE + "List all chat color prefixes");
				sender.sendMessage(
						ChatColor.DARK_AQUA + "/removeblock: " + ChatColor.WHITE + "List all the RemoveBlock commands");
				sender.sendMessage(ChatColor.DARK_AQUA + "/removeblock " + ChatColor.AQUA + "add: " + ChatColor.WHITE
						+ "Adds you to remove-block list");
				sender.sendMessage(ChatColor.DARK_AQUA + "/removeblock " + ChatColor.AQUA + "remove: " + ChatColor.WHITE
						+ "Removes you from remove-block list");
				sender.sendMessage(ChatColor.DARK_AQUA + "/removeblock " + ChatColor.AQUA + "list: " + ChatColor.WHITE
						+ "View remove-block list");
				sender.sendMessage(ChatColor.DARK_AQUA + "/special " + ChatColor.WHITE + "Obtain a special item");
				line(sender);
			}
			return true;
		} else if (cmd.equals("colors")) {
			for (int i = 0; i < chatColors.length; i += 2) {
				String message = "";
				String[] colorData = chatColors[i];
				message += "&" + colorData[1] + ": " + ChatColor.getByChar(colorData[1]) + colorData[0];
				String spaces = StringUtils.repeat(" ", 20 - message.replaceAll("§", "").length());
				String[] colorData2 = chatColors[i + 1];
				if (colorData2 != null) {
					message += ChatColor.RESET + spaces + "&" + colorData2[1] + ": "
							+ ChatColor.getByChar(colorData2[1]) + colorData2[0];
				}
				sender.sendMessage(message);
			}
			return true;
		} else if (cmd.equals("hat")) {
			if (args.length == 0) {
				p.sendMessage(tag + cc("&c/hat <Player Name>"));
				return true;
			} else {
				String c = "give " + p.getName() + " minecraft:skull 1 3 {SkullOwner:\"" + args[0] + "\"}";
				p.sendMessage(c);
				getServer().dispatchCommand(sender, c);
				return true;
			}
		} else if (cmd.equals("special") || cmd.equals("spc")) {
			if (args.length != 0) {
				for (int i = 0; i < specials.length; i++) {
					Special spc = specials[i];
					if (args[0].equals(spc.name) || args[0].equals(spc.alias)) {
						List<String> a = spc.lore;
						ItemStack wand = CustomItem.customItem(new ItemStack(spc.itemId), spc.styledName, a);
						ItemFlag hideenchant = ItemFlag.HIDE_ENCHANTS;
						ItemFlag unbreakable = ItemFlag.HIDE_UNBREAKABLE;
						ItemMeta meta = wand.getItemMeta();
						meta.addItemFlags(hideenchant);
						meta.addItemFlags(unbreakable);
						wand.setItemMeta(meta);
						wand.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
						p.getInventory().addItem(wand);
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
						sender.sendMessage(tag + ChatColor.GREEN + "Gave you " + spc.styledName);
						return true;
					}
				}
				p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 0.77f);
				sender.sendMessage(tag + ChatColor.RED + "Invalid item");
			} else {
				line(sender);
				for (int i = 0; i < specials.length; i++) {
					String alias = "";
					if (specials[i].alias != null) {
						alias = ChatColor.DARK_GRAY + " [" + specials[i].alias + "]";
					}
					sender.sendMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + (i + 1) + ChatColor.DARK_GREEN
							+ "] " + specials[i].styledName + alias);
				}
				line(sender);
				return true;
			}
		} else if (cmd.equals("removeblock") | cmd.equals("rb")) {
			boolean help = false;
			if (args.length >= 1) {
				if (args[0].equals("add")) {
					if (!removeBlockPlayers.contains(p.getName())) {
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
						sender.sendMessage(ChatColor.GREEN + "Added you to " + ChatColor.GOLD + "remove-block"
								+ ChatColor.GREEN + " list");
						removeBlockPlayers.add(p.getName());
						return true;
					} else {
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 0.5f);
						sender.sendMessage(ChatColor.RED + "You are already in the fucking list dumbface");
						return true;
					}
				} else if (args[0].equals("remove")) {
					if (removeBlockPlayers.contains(p.getName())) {
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
						sender.sendMessage(ChatColor.GREEN + "Removed you from " + ChatColor.GOLD + "remove-block"
								+ ChatColor.GREEN + " list");
						removeBlockPlayers.remove(p.getName());
						return true;
					} else {
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 0.5f);
						sender.sendMessage(ChatColor.RED + "You are not in the list...");
						return true;
					}
				} else if (args[0].equals("list")) {
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.125f);
					sender.sendMessage(
							ChatColor.GREEN + "Remove-block List: " + ChatColor.GRAY + removeBlockPlayers.toString());
					return true;

				} else if (args[0].equals("delay")) {
					if (args.length == 1) {
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.125f);
						Long delay = rbDefaultDelay;
						if (rbCustomDelay.containsKey(p.getName())) {
							delay = rbCustomDelay.get(p.getName());
						}
						sender.sendMessage(ChatColor.GREEN + "Your Delay: " + delay + " ticks");
					} else if (args.length == 2) {
						Long delay = Long.parseLong(args[1]);
						rbCustomDelay.put(p.getName(), delay);
						sender.sendMessage(ChatColor.GREEN + "Set your delay to " + ChatColor.GOLD + delay
								+ ChatColor.GREEN + " ticks");
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.125f);
						return true;
//						if(delay != -1L) {
//						} else {
//							p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 0.65f);
//							sender.sendMessage(ChatColor.RED + "Invalid Number");
//						}
					}
					return true;

				} else {
					help = true;
				}
			} else if (args.length == 0) {
				help = true;
			}
			return true;
		} else if (cmd.equals("soundtest")) {
			float pitch = 1.0f;
			float volume = 1.0f;
			Sound sound = Sound.NOTE_PIANO;
			if (args.length == 1) {
				if (args[0].getClass() == String.class) {
					Sound s = Sound.valueOf(args[0].toUpperCase());
					if (s != null) {
						sound = s;
					} else {
						p.sendMessage(tag + ChatColor.RED + "Sound " + ChatColor.DARK_RED + args[0].toUpperCase()
								+ ChatColor.RED + " not found");
					}
				}
			}
			p.playSound(p.getLocation(), sound, volume, pitch);
			sender.sendMessage(tag + ChatColor.translateAlternateColorCodes('&',
					"&aPlayed &2" + sound.name() + " &aat &2" + volume + " &avolume and &2" + pitch + " &apitch"));
			return true;
		} else if (cmd.equals("spawn")) {
			Location spawn = p.getWorld().getSpawnLocation();
			p.teleport(spawn);
			p.playSound(spawn, Sound.NOTE_PLING, 1.0f, 1.0f);
			return true;
		} else if (command.getName().toLowerCase().startsWith("gm")) {
			GameMode gm = GameMode.CREATIVE;
			String gms = "CREATIVE";
			if (cmd.equals("gms")) {
				gm = GameMode.SURVIVAL;
				gms = "SURVIVAL";
			} else if (cmd.equals("gma")) {
				gm = GameMode.ADVENTURE;
				gms = "ADVENTURE";
			} else if (cmd.equals("gmsp")) {
				gm = GameMode.SPECTATOR;
				gms = "SPECTATOR";
			} else if (!cmd.equals("gmc")) {
				return true;
			}
			Player target = p;
			if (args.length != 0) {
				for (String arg : args) {
					Player player = getServer().getPlayer(arg);
					if (player != null) {
						player.setGameMode(gm);
						p.sendMessage(tag + ChatColor.GREEN + arg + " is in " + gms + " mode now");
					} else {
						p.sendMessage(tag + ChatColor.RED + "\"" + arg + "\" not found");
					}
				}
			} else {
				target.setGameMode(gm);
				p.sendMessage(tag + ChatColor.GREEN + "You are in " + gms + " mode now");
			}
			return true;
		} else if (cmd.equals("feed")) {
			Player target = p;
			if (args.length != 0) {
				for (String arg : args) {
					Player player = getServer().getPlayer(arg);
					if (player != null) {
						player.setFoodLevel(20);
						player.setSaturation(20);
						player.sendMessage(tag + ChatColor.AQUA + p.getDisplayName() + ChatColor.WHITE + " fed you");
					} else {
						p.sendMessage(tag + ChatColor.RED + "\"" + arg + "\" not found");
					}
				}
			} else {
				target.setFoodLevel(20);
				target.setSaturation(20);
				p.sendMessage(tag + ChatColor.WHITE + "You are fed now");
			}
			return true;
		} else if (cmd.equals("heal")) {
			if (args.length != 0) {
				for (String arg : args) {
					Player player = getServer().getPlayer(arg);
					if (player != null) {
						player.sendMessage(tag + ChatColor.AQUA + p.getDisplayName() + ChatColor.WHITE + " healed you");
						player.setHealth(player.getMaxHealth());
					} else {
						p.sendMessage(tag + ChatColor.RED + "\"" + arg + "\" not found");
					}
				}
			} else {
				p.setHealth(p.getMaxHealth());
				p.sendMessage(tag + ChatColor.WHITE + "Healed you");
			}
			return true;
		} else if (cmd.equals("broadcast") | cmd.equals("bc")) {
			if (args.length == 0) {
				p.sendMessage(tag + ChatColor.RED + "/broadcast <message> you fucking -5 iq fleshhead");
			} else {
				String msg = "";
				for (String arg : args) {
					msg += arg + " ";
				}
				char a = '&';
				msg = ChatColor.translateAlternateColorCodes(a, msg);
				getServer().broadcastMessage(msg);
			}
			return true;
		} else if (cmd.equals("look")) {
			if (args.length == 2) {
				Location l = p.getLocation();
				Float pitch = Float.parseFloat(args[1]);
				Float yaw = Float.parseFloat(args[0]);
				if (pitch == null | yaw == null) {
					p.sendMessage(tag + ChatColor.RED + "You need to enter numbers");
					return true;
				}
				l.setPitch(pitch);
				l.setYaw(yaw);
				p.teleport(l);
			} else if (args.length == 3) {
				Player target = getServer().getPlayer(args[0]);
				if (target == null) {
					p.sendMessage(tag + ChatColor.RED + "Player not found");
					return true;
				}
				Location l = p.getLocation();
				Float pitch = Float.parseFloat(args[2]);
				Float yaw = Float.parseFloat(args[1]);
				if (pitch == null | yaw == null) {
					p.sendMessage(tag + ChatColor.RED + "You need to enter numbers");
					return true;
				}
				l.setPitch(pitch);
				l.setYaw(yaw);
				p.teleport(l);
			} else {
				p.sendMessage(tag + ChatColor.RED + "/look [player] <yaw> <pitch>");
			}
			return true;
		} else if (cmd.equals("ip")) {
			p.sendMessage(tag + p.getAddress());
			return true;
		} else if (cmd.equals("ping")) {
			CraftPlayer cp = (CraftPlayer) p;
			p.sendMessage(tag + ChatColor.GREEN + cp.getHandle().ping + "ms");
			return true;
		} else if (cmd.equals("safewalk") | cmd.equals("sw")) {
			if (safeWalk.indexOf(p.getUniqueId().toString()) == -1) {
				p.sendMessage(tag + ChatColor.WHITE + "Safewalk " + ChatColor.GREEN + "on");
				safeWalk.add(p.getUniqueId().toString());
			} else {
				p.sendMessage(tag + ChatColor.WHITE + "Safewalk " + ChatColor.RED + "off");
				safeWalk.remove(safeWalk.indexOf(p.getUniqueId().toString()));
			}
			return true;
		} else if (cmd.equals("world") | cmd.equals("w")) {
			if (args.length == 0) {
				line(sender);
				sender.sendMessage(tag + "World Commands");
				sender.sendMessage(cc("&2/world &alist&e: Lists all the worlds"));
				sender.sendMessage(cc("&2/world &acreate&e: Create a new world"));
				sender.sendMessage(cc("&2/world &ateleport&e: Teleport between worlds"));
				line(sender);
			} else if (args[0].equalsIgnoreCase("list")) {
				List<World> worlds = getServer().getWorlds();
				for (World world : worlds) {
					sender.sendMessage(cc("&a" + world.getName() + " &2[" + world.getEnvironment().name() + "]"));
				}
			} else if (args[0].equalsIgnoreCase("create")) {
				if (args.length == 1) {
					sender.sendMessage(tag + ChatColor.RED
							+ "fucking moron you need to name it something youre so lame god please delete this game");
				} else {
					String name = "";
					for (int i = 1; i < args.length; i++) {
						name += args[i];
					}
					name = name.replaceAll("\\s", "\\_");
					sender.sendMessage(tag + ChatColor.WHITE + "Creating new world");
					getServer()
							.broadcastMessage(tag + ChatColor.GRAY + "Creating world: " + ChatColor.DARK_GRAY + name);
					WorldCreator wc = new WorldCreator(name);
					getServer().createWorld(wc);
					getServer().broadcastMessage(tag + ChatColor.GRAY + "Done!");
				}
			} else if (args[0].equalsIgnoreCase("teleport") | args[0].equalsIgnoreCase("tp")) {
				if (args.length == 1) {
					sender.sendMessage(tag + ChatColor.RED + "Do " + ChatColor.DARK_RED + "/world list" + ChatColor.RED
							+ " to see which worlds you can go to");
				} else {
					String name = "";
					for (int i = 1; i < args.length; i++) {
						name += args[i];
					}
					name = name.replaceAll("\\s", "\\_");
					List<World> worlds = getServer().getWorlds();
					for (World w : worlds) {
						if (w.getName().equals(name)) {
							sender.sendMessage(
									tag + ChatColor.WHITE + "Teleporting you to " + ChatColor.YELLOW + w.getName());
							p.teleport(w.getSpawnLocation());
							return true;
						}
					}
					sender.sendMessage(tag + ChatColor.RED + "World not found");
				}
			}
			return true;
		} else if (cmd.equals("killall") | cmd.equals("eraseall")) {
			if (args.length == 0) {
				int counter = 0;
				for (Entity entity : p.getWorld().getEntities()) {
					if (!(entity instanceof Player)) {
						counter++;
						entity.remove();
					}
				}
				sender.sendMessage(tag + cc("&akilled &e" + counter + " &aentities"));
//				sender.sendMessage(tag + ChatColor.RED + "not killing literally everyone, fuck you");
				return true;
			} else {
				for (String arg : args) {
					EntityType e = null;
					for (EntityType type : EntityType.values()) {
						if (type.name().equalsIgnoreCase(arg)) {
							e = type;
						}
					}
					if (e == null) {
						sender.sendMessage(tag + arg + ChatColor.RED + " is not a valid entity-type");
					} else {
						int counter = 0;
						for (Entity entity : p.getWorld().getEntities()) {
							if (entity.getType() == e) {
								counter++;
								entity.remove();
							}
						}
						sender.sendMessage(tag + cc("&akilled &e" + counter + " &aof &e" + e.name().toLowerCase()));
					}
				}
				return true;
			}
		} else if (cmd.equals("names")) {
			Player target = p;
			if (args.length != 0) {
				Player check = getServer().getPlayer(args[0]);
				if(check != null) {
					target = check;
				}
			}
			line(sender);
			TableGenerator tg = new TableGenerator(Alignment.LEFT, Alignment.LEFT);
			tg.addRow("&2Username", target.getName());
			tg.addRow("&2Display Name", target.getDisplayName());
			tg.addRow("&2Custom Name", target.getCustomName());
			tg.addRow("&2Player List Name", target.getPlayerListName());
			for (String line : tg.generate(Receiver.CLIENT, true, true)) {
				p.sendMessage(cc(line));
			}
			line(sender);
		}else if (cmd.equals("name")) {
			Player target = p;
			if (args.length != 0) {
				Player check = getServer().getPlayer(args[0]);
				if(check != null) {
					target = check;
				}
			}
			line(sender);
			TableGenerator tg = new TableGenerator(Alignment.LEFT, Alignment.LEFT);
			tg.addRow("&2Username", target.getName());
			tg.addRow("&2Display Name", target.getDisplayName());
			tg.addRow("&2Custom Name", target.getCustomName());
			tg.addRow("&2Player List Name", target.getPlayerListName());
			for (String line : tg.generate(Receiver.CLIENT, true, true)) {
				p.sendMessage(cc(line));
			}
			line(sender);
		} else if (cmd.equals("fly")) {
			if (args.length == 0) {
				if (p.getAllowFlight()) {
					p.setFlying(false);
					p.setAllowFlight(false);
					sender.sendMessage(tag + "fly disabled");
				} else {
					p.setAllowFlight(true);
					sender.sendMessage(tag + "fly enabled");
				}
			}
			return true;
		} else if (cmd.equals("speed")) {
			if (args.length == 0) {
				line(p);
				sender.sendMessage(cc("Your walk speed: &a" + map(p.getWalkSpeed(), 0, 0.2f, 0, 100) + "%"));
				sender.sendMessage(cc("Your fly speed:  &a" + map(p.getFlySpeed(), 0, 0.1f, 0, 100) + "%"));
				line(p);
			} else {
				String newSpeed = args[0];
				float speed = Float.parseFloat(args[0]) / 500;
				if (speed > 1) {
					newSpeed = "500";
					speed = 1;
				}
				if (speed < 0) {
					newSpeed = "0";
					speed = 0;
				}
				p.setWalkSpeed(speed);
				sender.sendMessage(cc("Set your walk speed to &a" + args[0] + "%"));
			}
			return true;
		} else if (cmd.equals("practice") | cmd.equals("pr")) {
			if (args.length == 0) {
				line(sender);
				sender.sendMessage(tag + "Practice Commands");
				JSONMessage msg = new JSONMessage();
				msg.addText("/pr ", ChatColor.DARK_GREEN);
				msg.addText("inventory", ChatColor.GREEN);
				msg.addHoverEvent(HoverAction.TEXT, "list, id");
				msg.addText(": Starts an inventory aim trainer",ChatColor.YELLOW);
				msg.addText("");
				sendJSONMessage(p, msg);
//				sender.sendMessage(cc("&2/pr &ainventory&e: Starts an inventory aim trainer"));
				line(sender);
				return true;
			} else {
				if (args[0].equalsIgnoreCase("inventory") | args[0].equalsIgnoreCase("inv")) {
					String uuid = p.getUniqueId().toString();
					int songID = 0;
					songProgress.put(uuid, 0);
					if (args.length == 1) {
						if (songSelected.get(uuid) == null) {
							songSelected.put(uuid, 0);
						} else {
							songID = songsList.rotate(songSelected.get(uuid));
							songSelected.put(uuid, songID);
						}
					} else {
						if (args[1].equalsIgnoreCase("list")) {
							line(sender);
							sender.sendMessage(tag + "Inventory Practice Songs");
							for (int i = 0; i < songsList.length(); i++) {
								Song s = songsList.getSong(i);
								sender.sendMessage(cc("&2[" + i + "] &a" + s.toString() + " &7("
										+ ticksToTimeString(s.getSongLength()) + ")"));
							}
							line(sender);
						} else {
							int actualSongID = Integer.parseInt(args[1]);
							if (songsList.getSong() != null) {
								songID = actualSongID;
								songSelected.put(uuid, songID);
							} else {
								if (songSelected.get(uuid) == null) {
									songSelected.put(uuid, 0);
								} else {
									songID = songsList.rotate(songSelected.get(uuid));
									songSelected.put(uuid, songID);
								}
							}
						}
					}
					String title = ChatColor.GOLD + "[PR] InvAim: " + ChatColor.GRAY + songsList.getSongName(songID);
					if (title.length() > 32) {
						title = title.substring(0, 29) + "...";
					}
					Inventory inv = Bukkit.createInventory(p, 9 * 6, title);
					int notePlace = (int) Math.floor((Math.random() * inv.getSize()));
					int next = (int) Math.floor((Math.random() * inv.getSize()));
					if (notePlace == next) {
						next = notePlace + 1;
						if (next <= inv.getSize()) {
							next = 0;
						}
					}
					ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1,
							DyeColor.getByWoolData((byte) 15).getData());
					ItemMeta emptyMeta = empty.getItemMeta();
					emptyMeta.setDisplayName(" ");
					empty.setItemMeta(emptyMeta);
					ItemStack note = new ItemStack(Material.STAINED_GLASS_PANE, 1,
							DyeColor.getByWoolData((byte) 5).getData());
					ItemMeta noteMeta = empty.getItemMeta();
					noteMeta.setDisplayName(" ");
					note.setItemMeta(noteMeta);
					ItemStack next1 = new ItemStack(Material.STAINED_GLASS_PANE, 1,
							DyeColor.getByWoolData((byte) 8).getData());
					ItemMeta nextMeta = empty.getItemMeta();
					nextMeta.setDisplayName(" ");
					next1.setItemMeta(nextMeta);
					for (int i = 0; i < inv.getSize(); i++) {
						if (i == notePlace) {
							inv.setItem(i, note);
						} else if (i == next) {
							inv.setItem(i, next1);
						} else {
							inv.setItem(i, empty);
						}
					}
					p.openInventory(inv);
				}
			}
			return true;
		}else if(cmd.equals("note")) {
			if(args.length == 0) {
				
			}else {
				Location loc = p.getLocation().add(new Vector(0, 1, 0));
				float size = 70f;
		        float dist = 2;
		        float dir = (float) Math.toRadians(p.getLocation().getYaw() + 90);
				float x = (float) (loc.getX() + (Math.cos(dir) * dist));
		        float y = (float) loc.getY();
		        float z = (float) (loc.getZ() + (Math.sin(dir) * dist));
		        int col = Integer.parseInt(args[0]);
		        NoteColor color = new NoteColor(col);
		        Location locFinal = new Location(p.getWorld(), x, y, z);
		        ParticleEffect.NOTE.display(color, locFinal, 32);
			}
		} else if (cmd.equals("song") | cmd.equals("jingle")) {
			String keyword = "Song";
			Songs list = songsList;
			if(cmd.equals("jingle")) {
				keyword = "Jingle";
				list = jinglesList;
			}
			if (args.length == 0) {
				line(sender);
				sender.sendMessage(tag + keyword + " Commands");
				sender.sendMessage(cc("&2/"+keyword.toLowerCase()+" &alist&e: List all "+keyword.toLowerCase()+"s"));
				sender.sendMessage(cc("&2/"+keyword.toLowerCase()+" &aplay <id>&e: Play a " + keyword.toLowerCase()));
				line(sender);
			} else {
				if (args[0].equalsIgnoreCase("list")) {
					line(sender);
					sender.sendMessage(tag + keyword + " list");
					for (int i = 0; i < list.length(); i++) {
						Song s = list.getSong(i);
						sender.sendMessage(cc("&2[" + i + "] &a" + s.toString() + " &7("
								+ ticksToTimeString(s.getSongLength()) + ")"));
					}
					line(sender);
				} else if (args[0].equalsIgnoreCase("play")) {
					if (args.length == 1) {
						p.sendMessage(tag + ChatColor.RED + "/"+keyword.toLowerCase()+" play <id>");
					} else {
						int i = 0;
						if (args[1].equalsIgnoreCase("random")) {
							i = (int) Math.floor(Math.random() * list.length());
						} else {
							i = Integer.parseInt(args[1]);
							if (i >= list.length() | i < 0) {
								p.sendMessage(tag + ChatColor.RED + "Invalid ID");
								return true;
							}
							
							String uuid = p.getUniqueId().toString();
							Song s = list.getSong(i);
							
							if(s.isLayered()) {
								for(Layer l : s.getLayers()) {
									announce(l.getName() + ": " + l.getStart());
								}
							}
							
							songSelected.put(uuid, i);
							songProgress.put(uuid, 0);
							Songs liste = list;
							p.sendMessage(tag + "Playing " + ChatColor.ITALIC + s.toString());
							
							BukkitScheduler scheduler = this.getServer().getScheduler();
							for(int i1 = 0; i1 < s.getLength(); i1++) {
								scheduler.scheduleSyncDelayedTask(this, new Runnable() {
	
									@Override
									public void run() {
										int progress = songProgress.get(uuid);
										String[] notes = liste.getSong(songSelected.get(uuid)).getSongString().split("\s");
										String n = notes[progress % notes.length];
										songProgress.put(uuid, progress + 1);
										String extra = "";
										if(!n.equalsIgnoreCase("x")) {
											String[] blocks = n.split(",");
											int d = blocks.length;
											for(String b : blocks) {
												float pitch = liste.noteToPitch(b);
												Sound sound = liste.noteToSound(b);
												String prefix = liste.noteToSoundString(b);
												com.warido.plugin.Instrument instrument = liste.noteToInstrument(b);
												p.playSound(p.getLocation(), sound, 1.0f, pitch);
												Location loc = p.getLocation().add(new Vector(0, 1, 0));
												float size = 70f;
										        float offset = map(pitch,0.5f,2f,-size,size);
										        float dist = 2;
										        float dir = (float) Math.toRadians(p.getLocation().getYaw() + offset + 90);
										        float yoff = 0;
										        int col = 10;
										        float yfac = 2;
										        if(instrument.toString().equalsIgnoreCase(com.warido.plugin.Instrument.PIANO.toString())) {
										        	col = 22;
										        	yoff = 0.5f;
										        }else if(instrument.toString().equalsIgnoreCase(com.warido.plugin.Instrument.BASS.toString())) {
										        	col = 11;
										        	yoff = 0f;
										        }else if(instrument.toString().equalsIgnoreCase(com.warido.plugin.Instrument.BASS_DRUM.toString())) {
										        	col = 14;
										        	yoff = -1f;
										        }else if(instrument.toString().equalsIgnoreCase(com.warido.plugin.Instrument.SNARE.toString())) {
										        	col = 7;
										        	yoff = -0.5f;
										        }
												float x = (float) (loc.getX() + (Math.cos(dir) * dist));
										        float y = (float) loc.getY() + yoff * yfac;
										        float z = (float) (loc.getZ() + (Math.sin(dir) * dist));
										        NoteColor color = new NoteColor(col);
										        Location locFinal = new Location(p.getWorld(), x, y, z);
										        ParticleEffect.NOTE.display(color, locFinal, 32);
//										        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
//										        		net.minecraft.server.v1_8_R3.EnumParticle.NOTE, true, x, y, z, rd, gr, bl, 0.05f, 0);
//										        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
												extra = pitch + " - " + sound.name(); 
											}
										}
										sendActionBar(p, cc("&3" + ticksToTimeString((s.getLength() - progress) * s.getSpeed(), false)));
//										sendActionBar(p, cc("&2" + progress + ": &a" + n + " &7(" + extra + ")"));
									}
								}, s.getSpeed() * i1);
							}
						}
					}
				}else if (args[0].equalsIgnoreCase("read")) {
					if (args.length == 1) {
						p.sendMessage(tag + ChatColor.RED + "/song read <id>");
					} else {
						int i = 0;
						i = Integer.parseInt(args[1]);
						if (i >= list.length() | i < 0) {
							p.sendMessage(tag + ChatColor.RED + "Invalid ID");
							return true;
						}
						Song s = list.getSong(i);
						line(p);
						int linesize = 16;
						ArrayList<Alignment> a = new ArrayList<Alignment>();
						for(int i1 = 0;i1 < linesize;i1++) {
							a.add(Alignment.LEFT);
						}
						TableGenerator tg = new TableGenerator(a.toArray(new Alignment[0]));
						int left = 0;
						while(left != s.getSongLength()) {
							ArrayList<String> tableItems = new ArrayList<String>();
							for(int i1 = 0;i1 < linesize;i1++) {
								String n = s.getNote(left);
								if(n.equalsIgnoreCase("x")) {
									tableItems.add("&8x");	
								}else {
									String[] notes = n.split(",");
									String str = "";
									for(String note : notes) {
										com.warido.plugin.Instrument instrument = com.warido.plugin.Instrument.getInstrument(note.replaceAll("([A-G](|#)\\d)", "").trim());
										String prefix = instrument.toString();
										note = note.replaceAll("\\d", "").replace('#', '\'');
										if(prefix == "") {
											str += " " + instrument.getColor() + note;
										}else {
											str += " " + note.replace(prefix, instrument.getColor() + "");
										}
									}
									tableItems.add(str.trim());	
								}
								left++;
								if(left == s.getSongLength()) {
									break;
								}
							}
							tg.addRow(tableItems.toArray(new String[0]));
						}
						p.sendMessage(tag + "Reading " + ChatColor.ITALIC + s.toString());
						JSONMessage msg = new JSONMessage();
						msg.addText("[Play]");
						msg.addClickEvent(JSONMessage.ClickAction.COMMAND, "/"+keyword.toLowerCase()+" play " + args[1]);
						Main.sendJSONMessage(p, msg);
						for (String line : tg.generate(Receiver.CLIENT, true, true)) {
							if(line.trim() != "") {
								p.sendMessage(cc(line));
							}
						}
						line(p);
					}
				}
			}
			return true;
		}else if (cmd.equals("clearsb") | cmd.equals("clearscoreboard")) {
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			return true;
		}
		p.sendMessage(tag + ChatColor.RED + "Command not handled: " + ChatColor.DARK_RED + "tell warido he sucks");
		return false;

	}

	public Special[] addSpecial(Special arr[], Special x) {
		int i;
		int n = arr.length;
		Special newarr[] = new Special[n + 1];
		for (i = 0; i < n; i++)
			newarr[i] = arr[i];

		newarr[n] = x;

		return newarr;
	}

	public static float map(float n, float start1, float stop1, float start2, float stop2) {
		return ((n - start1) / (stop1 - start1)) * (stop2 - start2) + start2;
	};

	public static void sendActionBar(Player player, String message) {
		message = message.replaceAll("%player%", player.getDisplayName());
		message = ChatColor.translateAlternateColorCodes('&', message);
		CraftPlayer p = (CraftPlayer) player;
		IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		p.getHandle().playerConnection.sendPacket(ppoc);
	}
	
	public static void updateScoreboard(Player p, String name, String[] lines) {
		Scoreboard sb = p.getScoreboard();
		for(Team t : sb.getTeams()) {
			t.unregister();
		}
		Objective board = sb.getObjective("scoreboard");
		if(board == null) {
//			old.unregister();
			board = sb.registerNewObjective("scoreboard", "dummy");
		}
		board.setDisplayName(name);
		board.setDisplaySlot(DisplaySlot.SIDEBAR);
		int i = lines.length;
		for(String line : lines) {
			Team s = sb.getTeam(i+"");
			if(s == null) {
				s = sb.registerNewTeam(i+"");
			}
			for(String e : s.getEntries()) {				
				s.removeEntry(e);
			}
			String l = cc(line);
//			s.addEntry(l);
			s.setPrefix("");
			s.setSuffix("");
//			board.getScore(l).setScore(i);
			i--;
		}
		p.setScoreboard(sb);
	}


	public static void log(String... msgs) {
		for (String msg : msgs) {
			System.out.println(msg);
		}
	}

	public static String cc(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static void line(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "------------------------------------");
	}

	public static String ticksToTimeString(int ticks, Boolean round) {
		String output = "";
		int hours = 0;
		int minutes = 0;
		float seconds = ticks * 0.05f;
		if (seconds >= 60) {
			minutes = (int) (seconds / 60);
			seconds = seconds % 60;
		}
		String zeroSec = "";
		if (seconds < 10) {
			zeroSec = "0";
		}
		if(round) {
			output = minutes + ":" + zeroSec + ((int) seconds);
		}else {
			output = minutes + ":" + zeroSec + (((double) Math.round(seconds * 10)) / 10);
		}
		return output;
	}
	
	public static String ticksToTimeString(int ticks) {
		return ticksToTimeString(ticks, true);
	}

	public static void announce(String msg) {
		Bukkit.getServer().broadcastMessage(msg);
	}

	public static void setScoreboardValue(Objective o, String key, int value) {
		o.setDisplayName(cc("&2" + key + ": &a" + value));
	}
	
	public static void sendJSONMessage(Player p, JSONMessage msg) {
		String msgString = msg.toString();
		log(msgString);
		IChatBaseComponent comp = ChatSerializer.a(msgString);
		PacketPlayOutChat chat = new PacketPlayOutChat(comp);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
	}
}