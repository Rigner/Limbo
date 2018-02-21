package net.rigner.limbo;

import net.rigner.limbo.world.Schematic;
import net.rigner.limbo.world.World;
import net.rigner.limbo.world.nbt.NBTTag;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by Rigner on 28/08/16 for project for project Limbo.
 * All rights reserved.
 */
public class Limbo
{
    public static final Logger LOGGER = Logger.getLogger("Limbo");

    private NetworkManager networkManager;
    private LimboConfiguration limboConfiguration;
    private World world;
    private boolean run;

    private Limbo(LimboConfiguration limboConfiguration)
    {
        this.limboConfiguration = limboConfiguration;
    }

    private void loadWorld(String fileName)
    {
        Limbo.LOGGER.info("Loading schematic from " + fileName);
        try (FileInputStream fileInputStream = new FileInputStream(fileName))
        {
            this.world = Schematic.toWorld(NBTTag.readTag(new GZIPInputStream(fileInputStream)).toCompoundTag());
            fileInputStream.close();
        }
        catch (Exception ex)
        {
            Limbo.LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            Limbo.LOGGER.info("World loading failed");
            this.world = null;
            return ;
        }
        Limbo.LOGGER.info("World loaded");
    }

    private void run()
    {
        this.networkManager = new NetworkManager(this.world, this.limboConfiguration);
        this.run = true;

        try
        {
            this.networkManager.bind(this.limboConfiguration.getIp(), this.limboConfiguration.getPort());
            Limbo.LOGGER.info("Listening on port " + this.limboConfiguration.getPort());
            while (this.run)
                this.networkManager.select();
        }
        catch (Exception ex)
        {
            Limbo.LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void stop()
    {
        this.run = false;
        this.networkManager.stop();
    }

    public static void main(String[] args)
    {
        Limbo.LOGGER.setUseParentHandlers(false);
        Limbo.LOGGER.addHandler(new ConsoleHandler());
        Limbo.LOGGER.getHandlers()[0].setFormatter(new LogFormatter());

        LimboConfiguration limboConfiguration = LimboConfiguration.load();
        if (limboConfiguration == null)
            return ;
        Limbo limbo = new Limbo(limboConfiguration);
        limbo.loadWorld(limboConfiguration.getSchematicFile());
        if (limbo.world == null)
            return ;
        Runtime.getRuntime().addShutdownHook(new Thread(limbo::stop));
        limbo.run();
        System.exit(0);
    }

    private static class LogFormatter extends Formatter
    {
        private static final String FORMAT = "[%1$tT][%3$s] %4$s : %5$s%6$s%n";
        private Date date = new Date();

        @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
        @Override
        public String format(LogRecord record)
        {
            this.date.setTime(record.getMillis());
            String source;
            if (record.getSourceClassName() != null)
            {
                source = record.getSourceClassName();
                if (record.getSourceMethodName() != null)
                    source += " " + record.getSourceMethodName();
            }
            else
                source = record.getLoggerName();
            String message = formatMessage(record);
            String throwable = "";
            if (record.getThrown() != null)
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.println();
                record.getThrown().printStackTrace(pw);
                pw.close();
                throwable = sw.toString();
            }
            return String.format(LogFormatter.FORMAT,
                    this.date,
                    source,
                    record.getLoggerName(),
                    record.getLevel().getLocalizedName(),
                    message,
                    throwable);
        }
    }
}
