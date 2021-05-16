package com.warido.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class GameTickEvent extends Event{
	private Server server;
	public GameTickEvent(Server server, Plugin plugin) {
		this.server = server;
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){
            @Override
            public void run(){
            	GameTickEvent gametickevent = new GameTickEvent(getServer(), plugin); 
                Bukkit.getPluginManager().callEvent(gametickevent);
            }
        }, 2L);
	}
	
	public Server getServer() {
		return this.server;
	}
	
    private static final HandlerList handlers = new HandlerList();
 
    @Override
    public HandlerList getHandlers(){
        return GameTickEvent.handlers;
    }
 
    public static HandlerList getHandlerList(){
        return GameTickEvent.handlers;
    }
}
