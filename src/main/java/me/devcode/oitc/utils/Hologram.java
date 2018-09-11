package me.devcode.oitc.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hologram {

    private static String version;
    private static Class<?> craftWorld, entityClass, nmsWorld, armorStand, entityLiving, spawnPacket, removePacket;

    static {
        version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

        try {
            craftWorld = Class.forName("org.bukkit.craftbukkit." + version + "CraftWorld");
            entityClass = Class.forName("net.minecraft.server." + version + "Entity");
            nmsWorld = Class.forName("net.minecraft.server." + version + "World");
            armorStand = Class.forName("net.minecraft.server." + version + "EntityArmorStand");
            entityLiving = Class.forName("net.minecraft.server." + version + "EntityLiving");
            spawnPacket = Class.forName("net.minecraft.server." + version + "PacketPlayOutSpawnEntityLiving");
            removePacket = Class.forName("net.minecraft.server." + version + "PacketPlayOutEntityDestroy");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Location location;
    private List<String> lines = new ArrayList<String>();
    private List<Integer> ids = new ArrayList<Integer>();
    private List<Object> entities = new ArrayList<Object>();
    private double offset = 0.23D;

    public Hologram(Location location, String... text) {
        this.location = location;
        addLine(text);
    }

    public Hologram(String... text) {
        this(null, text);
    }

    public Hologram(Location location) {
        this(location, null);
    }

    public Hologram() {
        this(null, null);
    }

    /**
     * Returns the CB/NMS version string. For example v1_10_R1
     *
     * @return - The CB/NMS version.
     */
    public static String getVersion() {
        return version;
    }

    /**
     * Add a line or multiple, character colours will be converted.
     *
     * @param text - The text to add.
     */
    public void addLine(String... text) {
        lines.addAll(Arrays.asList(text));
        update();
    }

    /**
     * Returns a List of the lines the hologram is displaying.
     *
     * @return
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Sets the hologram lines, removing any currently lines previously added.
     *
     * @param text
     */
    public void setLines(String... text) {
        lines = Arrays.asList(text);
        update();
    }

    /**
     * Return the current stored location of the Hologram.
     *
     * @return - The current hologram location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set the location of the hologram.
     *
     * @param location - The location to set.
     */
    public void setLocation(Location location) {
        this.location = location;
        update();
    }

    public void teleport(Location loc) {
        update();
    }

    /**
     * Display the hologram to a player or multiple
     *
     * @param players - The players to show the hologram to.
     */
    public void displayTo(Player... players) {
        Location current = location.clone().add(0, (offset * lines.size()) - 1.97D, 0);

        for (String str : lines) {
            Object[] packet = getCreatePacket(location, ChatColor.translateAlternateColorCodes('&', str));
            ids.add((Integer) packet[1]);

            for (Player player : players)
                sendPacket(player, packet[0]);

            current.subtract(0, offset, 0);
        }
    }

    /**
     * Delete a hologram from a player or multiple.
     *
     * @param players
     */
    public void removeFrom(Player... players) {
        Object packet = null;

        for (int id : ids)
            packet = getRemovePacket(id);

        for (Player player : players)
            if (packet != null)
                sendPacket(player, packet);
    }

    /**
     * Spawn the hologram for everyone to see.
     */
    public void spawn() {
        Location current = location.clone().add(0, (offset * lines.size()) - 1.97D, 0).add(0, offset, 0);

        for (String str : lines)
            spawnHologram(ChatColor.translateAlternateColorCodes('&', str), current.subtract(0, offset, 0));
    }

    /**
     * Spawns a hologram with -text- at -location-
     */
    private void spawnHologram(String text, Location location) {
        try {
            // The ArmorStand
            Object craftWorld = Hologram.craftWorld.cast(location.getWorld());
            Object entityObject = armorStand.getConstructor(nmsWorld).newInstance(Hologram.craftWorld.getMethod("getHandle").invoke(craftWorld));

            configureHologram(entityObject, text, location);

            Hologram.craftWorld.getMethod("addEntity", entityClass, CreatureSpawnEvent.SpawnReason.class).invoke(craftWorld, entityObject, CreatureSpawnEvent.SpawnReason.CUSTOM);

            entities.add(entityObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Delete the hologram from the world.
     */
    public void remove() {
        for (Object ent : entities)
            removeEntity(ent);
    }

    private void removeEntity(Object entity) {
        try {
            Object craftWorld = Hologram.craftWorld.cast(location.getWorld());

            nmsWorld.getMethod("removeEntity", entityClass).invoke(Hologram.craftWorld.getMethod("getHandle").invoke(craftWorld), entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get the packet for creating a new Hologram, using EntityArmorStands and PacketPlayOutSpawnEntityLiving
     *
     * @param location - The location for which to spawn the hologram.
     * @param text     - The text (entity name) of the hologram.
     * @return Object - The PacketPlayOutSpawnEntityLiving packet in the form of an Object (Because of reflection, duh ^^)
     */
    private Object[] getCreatePacket(Location location, String text) {
        try {
            // The ArmorStand
            Object entityObject = armorStand.getConstructor(nmsWorld).newInstance(craftWorld.getMethod("getHandle").invoke(craftWorld.cast(location.getWorld())));
            Object id = entityObject.getClass().getMethod("getId").invoke(entityObject);

            configureHologram(entityObject, text, location);

            // Return the packet, and the entity id so we can later remove it.
            return new Object[]{spawnPacket.getConstructor(entityLiving).newInstance(entityObject), id};
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Get the removal packet for the hologram.
     *
     * @param id - The entity ID to remove (ArmorStand)
     * @return The destroy packet object.
     */
    private Object getRemovePacket(int id) {
        try {
            Class<?> packet = Class.forName("net.minecraft.server." + version + "PacketPlayOutEntityDestroy");
            return packet.getConstructor(int[].class).newInstance(new int[]{id});
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the currently existant hologram.
     */
    public void update() {
        try {
            if (!entities.isEmpty()) { // spawned as an actual entity, moving is ezpz.

                for (int i = 0; i < entities.size(); i++) {
                    Object ent = entities.get(i);

                    if (i > lines.size() - 1) // 1 'hologram' per line
                        removeEntity(ent);
                }

                Location current = location.clone().add(0, (offset * lines.size()) - 1.97D, 0);

                for (int i = 0; i < lines.size(); i++) {
                    String text = ChatColor.translateAlternateColorCodes('&', lines.get(i));

                    if (i >= entities.size()) {
                        spawnHologram(text, current);
                    } else {
                        configureHologram(entities.get(i), text, current);
                    }

                    current.subtract(0, offset, 0);
                }

            } else { // TODO allow the user to update packet holograms

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Configures the hologram properties.
     *
     * @param entityObject - The EntityArmorStand object to modify.
     * @param text         - The text the hologram has.
     * @throws Exception
     */
    private void configureHologram(Object entityObject, String text, Location location) throws Exception {
        // Methods for modifying the properties
        Method setCustomName = entityObject.getClass().getMethod("setCustomName", String.class);
        Method setCustomNameVisible = entityObject.getClass().getMethod("setCustomNameVisible", boolean.class);
        Method setNoGravity = entityObject.getClass().getMethod("setNoGravity", boolean.class); // Previously setGravity(boolean) prior to 1.10
        Method setLocation = entityObject.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
        Method setInvisible = entityObject.getClass().getMethod("setInvisible", boolean.class);

        // Setting the properties
        setCustomName.invoke(entityObject, text);
        setCustomNameVisible.invoke(entityObject, true);
        setNoGravity.invoke(entityObject, true);
        setLocation.invoke(entityObject, location.getX(), location.getY(), location.getZ(), 0.0F, 0.0F);
        setInvisible.invoke(entityObject, true);
    }

    /**
     * Send a packet to a player.
     *
     * @param player
     * @param packet
     */
    private void sendPacket(Player player, Object packet) {
        try {
            if (packet == null)
                return;

            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            connection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + "Packet")).invoke(connection, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
