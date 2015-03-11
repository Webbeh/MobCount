/**
 * This file has been created by Nicolas Glassey (weby@raidstone.net) on 11 November 2014, at 14h04.
 * <p>
 * The aforementioned creator holds the copyright of this protected code, registered under the Swiss Federal Institute of Intellectual Property.
 * <p>
 * Unless a written and duly signed permission has been explicitly granted to you and/or your company by the author himself, you are not allowed to :
 * - Use this code or any of its updates
 * - Base your work on this code or any of its updates
 * - Edit this code or any of its updates
 * - Delete or alter this license for any on any of the files
 * <p>
 * You are specifically allowed to :
 * - Use this code in the compiled form in the way the author intended
 * <p>
 * You are pleased to report any infringement of any of those conditions to the author himself directly.
 * <p>
 * Any proven infringement of this license will have the author reported to the Swiss Federal Authorities.
 */

package net.raidstone.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Count extends JavaPlugin implements Listener
{
    public void onEnable()
    {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void onDisable()
    {
        //Nothing
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("countmobs"))
        {
            if (!sender.hasPermission("raidstone.countmobs"))
            {
                sender.sendMessage(ChatColor.RED + "Let cindy use this, would you ?");
                return true;
            }
            if (sender instanceof ConsoleCommandSender)
            {
                sender.sendMessage("Please use it as a player.");
                return true;
            }

            HashMap<EntityType, Set<Entity>> entities = new HashMap<EntityType, Set<Entity>>();

            Player player = (Player) sender;
            int radius = 50;
            if (args.length < 1)
            {
                player.sendMessage("No radius defined, assuming " + radius + " (squared).");
            } else
            {
                radius = Integer.valueOf(args[0]);
            }
            int squared = radius * radius;
            for (Entity entity : player.getNearbyEntities(squared, 256, squared))
            {
                EntityType type = entity.getType();
                entities.putIfAbsent(type, new HashSet<Entity>());

                Set<Entity> entityset = entities.get(type);
                entityset.add(entity);
                entities.put(type, entityset);
            }

            player.sendMessage("Analyzing entities. Here are the types with the amount of naturally spawned entities.");
            for (EntityType type : entities.keySet())
            {
                Set<Entity> entityset = entities.get(type);
                int count = entityset.size();
                player.sendMessage(type.toString() + ": " + count);
            }
        } else if (cmd.getName().equalsIgnoreCase("entityremove"))
        {
            if (!sender.hasPermission("raidstone.entityremove"))
            {
                sender.sendMessage(ChatColor.RED + "No.");
                return true;
            }

            if (sender instanceof ConsoleCommandSender)
            {
                sender.sendMessage("Can't use in console.");
                return true;
            }

            Player p = (Player) sender;
            if (args.length < 1)
            {
                p.sendMessage("Give us a type.");
                return true;
            }

            String t = args[0];
            try
            {
                int radius = 50;
                if (args.length == 2)
                {
                    radius = Integer.valueOf(args[1]);
                }
                int squared = radius * radius;
                int counter = 0;

                for (Entity e : p.getNearbyEntities(squared, 256, squared))
                {
                    if (e.getType().name().equalsIgnoreCase(t))
                    {
                        counter++;
                        e.remove();
                    }
                }
                p.sendMessage("Removed " + counter + " entities of type " + t);
            } catch (Exception e)
            {
                p.sendMessage("Entity type not recognized.");
            }
        }
        return true;
    }
}
