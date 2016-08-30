package net.rigner.limbo;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class LimboConfiguration
{
    private int maxSlots;
    private short port;
    private String ip;
    private String schematicFile;
    private byte dimension;
    private byte gameMode;
    private double spawnX;
    private double spawnY;
    private double spawnZ;
    private float spawnYaw;
    private float spawnPitch;
    private boolean reducedDebugInfo;

    private LimboConfiguration()
    {
    }

    static LimboConfiguration load()
    {
        try
        {
            File file = new File("server.properties");
            if (!file.exists())
                Files.copy(Limbo.class.getResourceAsStream("/server.properties"), Paths.get(file.toURI()));
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            LimboConfiguration limboConfiguration = new LimboConfiguration();
            limboConfiguration.maxSlots = LimboConfiguration.getInt(properties, "maxSlots", -1);
            limboConfiguration.port = (short)LimboConfiguration.getInt(properties, "server-port", 25565);
            limboConfiguration.ip = LimboConfiguration.getString(properties, "server-ip", "localhost");
            limboConfiguration.schematicFile = LimboConfiguration.getString(properties, "schematic-file", "world.schematic");
            limboConfiguration.dimension = (byte)LimboConfiguration.getInt(properties, "dimension", 1);
            limboConfiguration.gameMode = (byte)LimboConfiguration.getInt(properties, "gamemode", 2);
            limboConfiguration.reducedDebugInfo = Boolean.parseBoolean(LimboConfiguration.getString(properties, "reduceddebuginfo", "true"));

            String[] split = LimboConfiguration.getString(properties, "spawn", "0.5;64;0.5;0;0").split(";");
            try
            {
                limboConfiguration.spawnX = Double.parseDouble(split[0]);
                limboConfiguration.spawnY = Double.parseDouble(split[1]);
                limboConfiguration.spawnZ = Double.parseDouble(split[2]);
                limboConfiguration.spawnYaw = Float.parseFloat(split[3]);
                limboConfiguration.spawnPitch = Float.parseFloat(split[4]);
            }
            catch (NumberFormatException | IndexOutOfBoundsException ex)
            {
                throw new InvalidPropertiesFormatException("Invalid coordinate format");
            }
            return limboConfiguration;
        }
        catch (Exception ex)
        {
            Limbo.LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private static int getInt(Properties properties, String key, int def) throws InvalidPropertiesFormatException
    {
        String result = properties.getProperty(key);
        if (result == null)
            return def;
        try
        {
            return Integer.parseInt(result);
        }
        catch (NumberFormatException ex)
        {
            throw new InvalidPropertiesFormatException(key + " is not a number property");
        }
    }

    private static String getString(Properties properties, String key, String def) throws InvalidPropertiesFormatException
    {
        String result = properties.getProperty(key);
        return result == null ? def : result;
    }

    public byte getDimension()
    {
        return this.dimension;
    }

    public byte getGameMode()
    {
        return this.gameMode;
    }

    public double getSpawnX()
    {
        return this.spawnX;
    }

    public double getSpawnY()
    {
        return this.spawnY;
    }

    public int getMaxSlots()
    {
        return this.maxSlots;
    }

    public double getSpawnZ()
    {
        return this.spawnZ;
    }

    short getPort()
    {
        return this.port;
    }

    String getIp()
    {
        return this.ip;
    }

    String getSchematicFile()
    {
        return this.schematicFile;
    }

    public float getSpawnYaw()
    {
        return this.spawnYaw;
    }

    public float getSpawnPitch()
    {
        return this.spawnPitch;
    }

    public boolean isReducedDebugInfo()
    {
        return this.reducedDebugInfo;
    }
}
