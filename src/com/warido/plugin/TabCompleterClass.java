package com.warido.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class TabCompleterClass implements TabCompleter {
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
//	        Player p = ((Player) sender).getPlayer();
		    if(command.getName().contentEquals("removeblock") | command.getName().contentEquals("rb")) {
		    	if (args.length == 1) {
		            commands.add("add");
		            commands.add("remove");
		            commands.add("list");
		        }
		    } else if(command.getName().contentEquals("killall") | command.getName().contentEquals("eraseall")) {
		    	for(EntityType e : EntityType.values()) {
		    		commands.add(e.name().toLowerCase());
		    	}
		    } else if(command.getName().contentEquals("special") | command.getName().contentEquals("spc")) {
		    	for(Special s : Main.specials) {
		    		commands.add(s.name);
		    	}
		    } else {
		    	Collection<? extends Player> online = sender.getServer().getOnlinePlayers();
			    for(Player player : online) {
			    	commands.add(player.getName());
			    }
		    }
        }
        StringUtil.copyPartialMatches(args[args.length - 1], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}
