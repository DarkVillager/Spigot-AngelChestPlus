package de.jeff_media.AngelChestPlus.utils;

import de.jeff_media.AngelChestPlus.config.Config;
import de.jeff_media.AngelChestPlus.Main;
import de.jeff_media.AngelChestPlus.data.Group;
import de.jeff_media.AngelChestPlus.enums.EconomyStatus;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class GroupUtils {

    final Main main;
    YamlConfiguration yaml;
    LinkedHashMap<String,Group> groups;
    public GroupUtils(File yamlFile) {
        this.main=Main.getInstance();
        if(!yamlFile.exists()) {
            main.getLogger().info("groups.yml does not exist, skipping custom group settings.");
            return;
        }
        this.yaml=YamlConfiguration.loadConfiguration(yamlFile);
        groups = new LinkedHashMap<>();

        for(String groupName : yaml.getKeys(false)) {
            int angelchestDuration = yaml.getInt(groupName+".angelchest-duration",-1);
            int chestsPerPlayer = yaml.getInt(groupName+".max-allowed-angelchests",-1);
            String priceSpawn = yaml.getString(groupName+".price-spawn","-1");
            String priceOpen = yaml.getString(groupName+".price-open","-1");
            String priceFetch = yaml.getString(groupName+".price-fetch","-1");
            String priceTeleport = yaml.getString(groupName+".price-teleport","-1");
            double xpPercentage = yaml.getDouble(groupName+".xp-percentage",-2);
            int unlockDuration = yaml.getInt(groupName+".unlock-duration",-2);
            double spawnChance = yaml.getDouble(groupName+".spawn-chance",1.0);

            main.debug("Registering group "+groupName);
            Group group = new Group(angelchestDuration,chestsPerPlayer,priceSpawn,priceOpen,priceTeleport,priceFetch, xpPercentage, unlockDuration, spawnChance);

            groups.put(groupName, group);

                  }
    }

    public double getXPPercentagePerPlayer(Player p) {
        if(yaml==null) return main.getConfig().getDouble(Config.XP_PERCENTAGE);
        Iterator<String> it = groups.keySet().iterator();
        Double bestValueFound = null;
        while(it.hasNext()) {
            String group = it.next();
            if(!p.hasPermission("angelchest.group."+group)) continue;
            double valuePerPlayer = groups.get(group).xpPercentage;
            if(valuePerPlayer==-2) {
                continue;
            }
            bestValueFound = bestValueFound == null ? valuePerPlayer : Math.max(valuePerPlayer, bestValueFound);
        }
        if(bestValueFound!=null) {
            return bestValueFound;
        } else {
            return main.getConfig().getDouble(Config.XP_PERCENTAGE);
        }
    }

    public int getDurationPerPlayer(Player p) {
        if(yaml==null) return main.getConfig().getInt(Config.ANGELCHEST_DURATION);
        Iterator<String> it = groups.keySet().iterator();
        Integer bestValueFound = null;
        while(it.hasNext()) {
            String group = it.next();
            if(!p.hasPermission("angelchest.group."+group)) continue;
            int valuePerPlayer = groups.get(group).duration;
            if(valuePerPlayer==-1) {
                continue;
            }
            bestValueFound = bestValueFound == null ? valuePerPlayer : Math.max(valuePerPlayer, bestValueFound);
        }
        if(bestValueFound!=null) {
            return bestValueFound;
        } else {
            return main.getConfig().getInt(Config.ANGELCHEST_DURATION);
        }
    }

    public int getUnlockDurationPerPlayer(Player p) {
        if(yaml==null) {
            if(main.getConfig().getInt(Config.UNLOCK_DURATION)==0) return -1;
            return main.getConfig().getInt(Config.UNLOCK_DURATION);
        }
        Iterator<String> it = groups.keySet().iterator();
        Integer bestValueFound = null;
        while(it.hasNext()) {
            String group = it.next();
            if(!p.hasPermission("angelchest.group."+group)) continue;
            int valuePerPlayer = groups.get(group).unlockDuration;
            if(valuePerPlayer==-2) {
                continue;
            }
            if(valuePerPlayer==-1) return -1; // Important! This is different from the other methods!
            bestValueFound = bestValueFound == null ? valuePerPlayer : Math.max(valuePerPlayer, bestValueFound);
        }
        if(bestValueFound!=null) {
            return bestValueFound;
        } else {
            return main.getConfig().getInt(Config.UNLOCK_DURATION);
        }
    }

    public int getChestsPerPlayer(Player p) {
        if(yaml==null) return main.getConfig().getInt(Config.MAX_ALLOWED_ANGELCHESTS);
        Iterator<String> it = groups.keySet().iterator();
        Integer bestValueFound = null;
        while(it.hasNext()) {
            String group = it.next();
            if(!p.hasPermission("angelchest.group."+group)) continue;
            int valuePerPlayer = groups.get(group).maxChests;
            if(valuePerPlayer==-1) {
                continue;
            }
            bestValueFound = bestValueFound == null ? valuePerPlayer : Math.max(valuePerPlayer, bestValueFound);
        }
        if(bestValueFound!=null) {
            return bestValueFound;
        } else {
            return main.getConfig().getInt(Config.MAX_ALLOWED_ANGELCHESTS);
        }
    }

    public double getSpawnPricePerPlayer(Player p) {
        if(yaml==null) return getPercantagePrice(p,main.getConfig().getString(Config.PRICE));
        Iterator<String> it = groups.keySet().iterator();
        Double bestValueFound = null;
        while(it.hasNext()) {
            String group = it.next();
            if(!p.hasPermission("angelchest.group."+group)) continue;
            String pricePerPlayer = groups.get(group).priceSpawn;
            if(pricePerPlayer.equals("-1")) {
                continue;
            }
            bestValueFound = bestValueFound == null ? getPercantagePrice(p,pricePerPlayer) : Math.min(getPercantagePrice(p,pricePerPlayer), bestValueFound);
        }
        if(bestValueFound!=null) {
            return bestValueFound;
        } else {
            return getPercantagePrice(p,main.getConfig().getString(Config.PRICE));
        }
    }

    public double getOpenPricePerPlayer(Player p) {
        if(yaml==null) return getPercantagePrice(p,main.getConfig().getString(Config.PRICE_OPEN));
        Iterator<String> it = groups.keySet().iterator();
        Double bestValueFound = null;
        while(it.hasNext()) {
            String group = it.next();
            if(!p.hasPermission("angelchest.group."+group)) continue;
            String pricePerPlayer = groups.get(group).priceOpen;
            if(pricePerPlayer.equals("-1")) {
                continue;
            }
            bestValueFound = bestValueFound == null ? getPercantagePrice(p,pricePerPlayer) : Math.min(getPercantagePrice(p,pricePerPlayer), bestValueFound);
        }
        if(bestValueFound!=null) {
            return bestValueFound;
        } else {
            return getPercantagePrice(p,main.getConfig().getString(Config.PRICE_OPEN));
        }
    }

    public double getFetchPricePerPlayer(Player p) {
        if(yaml==null) return getPercantagePrice(p,main.getConfig().getString(Config.PRICE_FETCH));
        Iterator<String> it = groups.keySet().iterator();
        Double bestValueFound = null;
        while(it.hasNext()) {
            String group = it.next();
            if(!p.hasPermission("angelchest.group."+group)) continue;
            String pricePerPlayer = groups.get(group).priceFetch;
            if(pricePerPlayer.equals("-1")) {
                continue;
            }
            bestValueFound = bestValueFound == null ? getPercantagePrice(p,pricePerPlayer) : Math.min(getPercantagePrice(p,pricePerPlayer), bestValueFound);
        }
        if(bestValueFound!=null) {
            return bestValueFound;
        } else {
            return getPercantagePrice(p,main.getConfig().getString(Config.PRICE_FETCH));
        }
    }

    public double getTeleportPricePerPlayer(Player p) {
        if(yaml==null) return getPercantagePrice(p,main.getConfig().getString(Config.PRICE_TELEPORT));
        Iterator<String> it = groups.keySet().iterator();
        Double bestValueFound = null;
        while(it.hasNext()) {
            String group = it.next();
            if(!p.hasPermission("angelchest.group."+group)) continue;
            String pricePerPlayer = groups.get(group).priceTeleport;
            if(pricePerPlayer.equals("-1")) {
                continue;
            }
            bestValueFound = bestValueFound == null ? getPercantagePrice(p,pricePerPlayer) : Math.min(getPercantagePrice(p,pricePerPlayer), bestValueFound);
        }
        if(bestValueFound!=null) {
            return bestValueFound;
        } else {
            return getPercantagePrice(p,main.getConfig().getString(Config.PRICE_TELEPORT));
        }
    }

    public static double getPercantagePrice(Player p, String value) {
        Main main = Main.getInstance();
        if(value.endsWith("p")) {
            double percentage = Double.parseDouble(value.substring(0,value.length()-1));
            if(main.economyStatus != EconomyStatus.ACTIVE) {
                return 0;
            }
            if(percentage<=0) {
                return 0;
            }
            double result = main.econ.getBalance(p)*percentage;
            main.debug(value+" contains a p, getting percentage for player "+p.getName()+": "+result);
            return result;
        } else {
            return Double.parseDouble(value);
        }
    }

    public double getSpawnChancePerPlayer(Player p) {
        if(yaml==null) return main.getConfig().getDouble(Config.SPAWN_CHANCE);
        Iterator<String> it = groups.keySet().iterator();
        Double bestValueFound = null;
        while(it.hasNext()) {
            String group = it.next();
            if(!p.hasPermission("angelchest.group."+group)) continue;
            double spawnChancePlayer = groups.get(group).spawnChance;
            if(spawnChancePlayer>=1) return 1;
            if(spawnChancePlayer==-1) {
                continue;
            }
            bestValueFound = bestValueFound == null ? spawnChancePlayer : Math.max(spawnChancePlayer, bestValueFound);
        }
        if(bestValueFound!=null) {
            return bestValueFound;
        } else {
            return main.getConfig().getDouble(Config.SPAWN_CHANCE);
        }
    }
}
