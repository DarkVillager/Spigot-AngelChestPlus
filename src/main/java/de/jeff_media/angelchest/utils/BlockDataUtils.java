package de.jeff_media.angelchest.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;

public final class BlockDataUtils {

    protected static BlockFace getBlockDirection(final Block b) {
        BlockFace dir;
        try {
            // check for player skull
            dir = ((Rotatable) b.getBlockData()).getRotation();
            dir = dir.getOppositeFace();
        } catch (final Exception e) {
            try {
                // check for chest
                dir = ((Directional) b.getBlockData()).getFacing();
            } catch (final Exception e2) {
                // Can't get block rotation, probably because it doesn't support it
                return BlockFace.NORTH;
            }
        }
        return dir;
    }

    public static BlockFace getChestFacingDirection(final String directionPlayerIsFacing) {
        // Set the relative direction of the block and offset the new chest location
        switch (directionPlayerIsFacing) {
            case "N":
            case "NE":
                return BlockFace.SOUTH;
            case "E":
            case "SE":
                return BlockFace.WEST;
            case "W":
            case "NW":
                return BlockFace.EAST;
            case "S":
            case "SW":
            default:
                return BlockFace.NORTH;
        }
    }

    public static Location getLocationInDirection(final Location loc, final String directionPlayerIsFacing) {
        final Location newLoc = loc.clone();
        switch (directionPlayerIsFacing) {
            case "N":
                newLoc.add(0, 0, -2);
                break;
            case "NE":
                newLoc.add(2, 0, -2);
                break;
            case "E":
                newLoc.add(2, 0, 0);
                break;
            case "SE":
                newLoc.add(2, 0, 2);
                break;
            case "S":
                newLoc.add(0, 0, 2);
                break;
            case "SW":
                newLoc.add(-2, 0, 2);
                break;
            case "W":
                newLoc.add(-2, 0, 0);
                break;
            case "NW":
                newLoc.add(-2, 0, -2);
                break;
            default:
                //main.getLogger().info("Unable to get block facing direction");
                break;
        }
        return newLoc;
    }

    public static void setBlockDirection(final Block b, final BlockFace dir) {
        try {
            // check for player skull
            final Rotatable blockData = ((Rotatable) b.getBlockData());
            blockData.setRotation(dir.getOppositeFace());
            b.setBlockData(blockData);
        } catch (final Exception e) {
            try {
                // check for chest
                final Directional blockData = ((Directional) b.getBlockData());
                blockData.setFacing(dir);
                b.setBlockData(blockData);
            } catch (final Exception e2) {
                // Can't set block rotation, probably because it doesn't support it
            }
        }
    }

}
