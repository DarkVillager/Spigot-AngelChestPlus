package de.jeff_media.AngelChestPlus.listeners;

import de.jeff_media.AngelChestPlus.data.AngelChest;
import de.jeff_media.AngelChestPlus.Main;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.io.File;

/**
 * Listens to block related events, e.g. messing with the actual Block where an AngelChest is located
 */
public class BlockListener implements Listener {

    final Main main;

    public BlockListener() {
        this.main = Main.getInstance();
    }

    /**
     * Called when a bucket is emptied inside the block of an AngelChest
     * @param event PlayerBucketEmptyEvent
     */
    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if(main.isAngelChest(event.getBlock())) {
            event.setCancelled(true);
        } else {
            return;
        }

        // The client thinks the player was removed anyway, so it will show up as a "regular" head.
        // Gotta reload the AngelChest to fix this
        AngelChest ac = main.getAngelChest(event.getBlock());
        if(ac==null) return;
        File file = ac.saveToFile(true);
        main.angelChests.put(event.getBlock(),new AngelChest(file));
    }

    /**
     * Called when an AngelChest's block is being broken.
     * Handles protecting the chest, or dropping it's content if it was not protected or if the user
     * was allowed to break the chest.
     * @param event BlockBreakEvent
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!main.isAngelChest(event.getBlock()))
            return;
        AngelChest angelChest = main.getAngelChest(event.getBlock());
        if (!angelChest.owner.equals(event.getPlayer().getUniqueId())
                && !event.getPlayer().hasPermission("angelchest.protect.ignore") && angelChest.isProtected) {
            event.getPlayer().sendMessage(main.messages.MSG_NOT_ALLOWED_TO_BREAK_OTHER_ANGELCHESTS);
            event.setCancelled(true);
            return;
        }
        if(!angelChest.hasPaidForOpening(event.getPlayer())) {
            return;
        }
        angelChest.destroy(false);
        angelChest.remove();
    }

    /**
     * Prevent liquids and dragon eggs from destroying AngelChest blocks
     * @param event BlockFromToEvent
     */
    @EventHandler
    public void onLiquidDestroysChest(BlockFromToEvent event) {
        // Despite the name, this event only fires when liquid or a teleporting dragon egg changes a block
        if (main.isAngelChest(event.getToBlock())) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents the AngelChest block from being broken by breaking the block below it
     * @param event BlockBreakEvent
     */
    @EventHandler
    public void onBreakingBlockThatThisIsAttachedTo(BlockBreakEvent event) {
        if (!main.isAngelChest(event.getBlock().getRelative(BlockFace.UP))) return;
        if(event.getBlock().getRelative(BlockFace.UP).getPistonMoveReaction()!= PistonMoveReaction.BREAK) return;

            event.setCancelled(true);
            main.debug("Preventing BlockBreakEvent because it interferes with AngelChest.");

    }

    /**
     * Prevents all entity explosions from destroying AngelChest blocks
     * @param event EntityExplodeEvent
     */
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(main::isAngelChest);
    }

    // TODO: Why is this commented out?
	/*@EventHandler
	public void onRightClickHologram(PlayerInteractAtEntityEvent e) {
		if(!(e.getRightClicked() instanceof ArmorStand)) {
			return;
		}

		ArmorStand as = (ArmorStand) e.getRightClicked();
		plugin.blockArmorStandCombinations.forEach((combination) -> {
			if(combination.armorStand.equals(as)) {
				plugin.getAngelChest(combination.block)

			}
		});

	}*/

    /**
     * Prevent all block explosions from destroying AngelChest blocks
     * @param event BlockExplodeEvent
     */
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(main::isAngelChest);
    }

}