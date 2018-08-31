package net.rigner.limbo;

import net.rigner.limbo.packets.AbstractProtocol;
import net.rigner.limbo.packets.PacketSerializer;
import net.rigner.limbo.packets.Status;
import net.rigner.limbo.packets.in.PacketIn;
import net.rigner.limbo.packets.out.PacketOut;
import net.rigner.limbo.packets.protocols.HandshakeProtocol;
import net.rigner.limbo.packets.protocols.Protocol107;
import net.rigner.limbo.packets.protocols.Protocol108;
import net.rigner.limbo.packets.protocols.Protocol110;
import net.rigner.limbo.packets.protocols.Protocol47;
import net.rigner.limbo.world.World;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Rigner on 29/08/16 for project Limbo.
 * All rights reserved.
 */
public class NetworkManager
{
    static final int MAX_BUFFER_SIZE = 4096;

    private LimboConfiguration limboConfiguration;
    private World world;
    private Selector selector;
    private ServerSocketChannel serverSocket;
    private List<PlayerConnection> playerConnections;
    private List<AbstractProtocol> protocols;
    private long lastKeepAlive;
    private int keepAliveId;

    NetworkManager(World world, LimboConfiguration limboConfiguration)
    {
        this.limboConfiguration = limboConfiguration;
        this.world = world;
        this.selector = null;
        this.serverSocket = null;
        this.playerConnections = new LinkedList<>();
        this.protocols = new ArrayList<>();
        this.lastKeepAlive = System.currentTimeMillis();
        this.keepAliveId = 0;

        this.protocols.add(new HandshakeProtocol(this));
        this.protocols.add(new Protocol47(this));
        this.protocols.add(new Protocol107(this));
        this.protocols.add(new Protocol108(this));
        this.protocols.add(new Protocol110(this));
    }

    void bind(String ip, short port) throws IOException
    {
        this.selector = Selector.open();
        this.serverSocket = ServerSocketChannel.open();
        this.serverSocket.bind(new InetSocketAddress(ip, port));
        this.serverSocket.configureBlocking(false);
        this.serverSocket.register(this.selector, this.serverSocket.validOps(), null);
    }

    void select() throws IOException
    {
        long time = System.currentTimeMillis();
        if (time >= this.lastKeepAlive + 10000L)
        {
            ++this.keepAliveId;
            this.lastKeepAlive = time;
            final long t = time - 30000L;
            List<PlayerConnection> connections = new ArrayList<>(this.playerConnections);
            connections.stream().filter(playerConnection -> playerConnection.getLastKeepAlive() < t).forEach(playerConnection -> this.disconnect(playerConnection, "Timed out"));
            connections.forEach(playerConnection -> playerConnection.getProtocol().sendKeepAlive(playerConnection, this.keepAliveId));
        }
        time += 10000L;
        time -= this.lastKeepAlive;
        int n = this.selector.select(time);

        if (n == 0)
            return;

        for (SelectionKey key : this.selector.selectedKeys())
        {
            if (!key.isValid())
                continue;

            if (key.isAcceptable())
            {
                SocketChannel channel = this.serverSocket.accept();
                if (channel != null)
                {
                    channel.configureBlocking(false);
                    PlayerConnection playerConnection = new PlayerConnection(this, channel);
                    this.playerConnections.add(playerConnection);
                    channel.register(this.selector, channel.validOps(), null);
                }
            }
            else if (key.isReadable())
                this.readPacket((SocketChannel) key.channel());
        }
        this.selector.selectedKeys().clear();
    }

    private void readPacket(SocketChannel socket)
    {
        PlayerConnection playerConnection = this.playerConnections.stream().filter(connection -> connection.getSocket().equals(socket)).findFirst().orElse(null);
        if (playerConnection == null)
        {
            Limbo.LOGGER.severe("Received data on channel not registered. (" + socket.socket().getInetAddress().getHostAddress() + ").");
            return;
        }
        try
        {
            ByteBuffer byteBuffer = playerConnection.getByteBuffer();
            if (socket.read(byteBuffer) == -1)
            {
                this.disconnect(playerConnection, null);
                return;
            }
            PacketSerializer packetSerializer = new PacketSerializer(new ByteArrayInputStream(byteBuffer.array()));
            byte[] data;
            int size = packetSerializer.available();
            try
            {
                int s = packetSerializer.readVarInt();
                if (s > NetworkManager.MAX_BUFFER_SIZE)
                    this.disconnect(playerConnection, "Invalid packet, too big");
                data = new byte[s];
                for (int i = 0; i < s; i++)
                    data[i] = packetSerializer.readByte();
                if (data.length == 0)
                    return;
            }
            catch (EOFException ignored)
            {
                return;
            }
            size -= packetSerializer.available();
            ByteBuffer tmp = ByteBuffer.allocate(NetworkManager.MAX_BUFFER_SIZE);
            for (int i = size; i < byteBuffer.position(); i++)
                tmp.put(byteBuffer.get(i));
            playerConnection.setByteBuffer(tmp);

            packetSerializer = new PacketSerializer(new ByteArrayInputStream(data));
            int packetId = packetSerializer.readVarInt();

            Class<? extends PacketIn> packetInClass = playerConnection.getProtocol().getPacketInById(packetId, playerConnection.getStatus());
            if (packetInClass == null)
            {
                Limbo.LOGGER.warning("Received unknown packet " + packetId + " (0x" + (char) (packetId / 16 > 9 ? packetId / 16 + 'A' - 10 : packetId / 16 + '0') + "" + (char) (packetId % 16 > 9 ? packetId % 16 + 'A' - 10 : packetId % 16 + '0') + ").");
                this.disconnect(playerConnection, "Unknown packet " + packetId);
                return;
            }

            PacketIn packetIn = packetInClass.newInstance();
            packetIn.readPacket(packetSerializer);
            packetIn.handlePacket(this, playerConnection);

            this.readPacket(socket);
        }
        catch (Exception ex)
        {
            Limbo.LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            this.disconnect(playerConnection, null);
        }
    }

    public void sendPacket(PlayerConnection playerConnection, PacketOut packetOut)
    {
        try
        {
            int id = playerConnection.getProtocol().getPacketOutByClass(packetOut.getClass(), playerConnection.getStatus());
            if (id == -1)
            {
                Limbo.LOGGER.warning("Sending unknown packet " + packetOut.getClass().getSimpleName());
                return;
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PacketSerializer packetSerializer = new PacketSerializer(stream);
            packetSerializer.writeVarInt(id);
            packetOut.write(packetSerializer);

            byte[] data = stream.toByteArray();
            stream.close();
            stream = new ByteArrayOutputStream();
            packetSerializer = new PacketSerializer(stream);
            packetSerializer.writeByteArray(data);
            playerConnection.getSocket().write(ByteBuffer.wrap(stream.toByteArray()));
            stream.close();
        }
        catch (ClosedChannelException ignored)
        {
        }
        catch (Exception ex)
        {
            Limbo.LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    void stop()
    {
        new ArrayList<>(this.playerConnections).forEach(playerConnection -> this.disconnect(playerConnection, "Server closed"));
        try
        {
            this.serverSocket.close();
        }
        catch (IOException ex)
        {
            Limbo.LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public AbstractProtocol getProtocolByVersionId(int version)
    {
        for (AbstractProtocol protocol : this.protocols)
            for (int id : protocol.getVersions())
                if (id == version)
                    return protocol;
        return null;
    }

    public int getCurrentKeepAliveId()
    {
        return this.keepAliveId;
    }

    public void disconnect(PlayerConnection playerConnection, String message)
    {
        if (playerConnection.getStatus() != Status.STATUS && playerConnection.getStatus() != Status.HANDSHAKE)
            Limbo.LOGGER.info(playerConnection.getInetAddress().getHostString() + (playerConnection.getUserName().isEmpty() ? "" : " (" + playerConnection.getUserName() + ")") + " disconnected" + (message == null ? "" : " : " + message));
        this.playerConnections.remove(playerConnection);
        if (message != null)
            playerConnection.getProtocol().disconnect(playerConnection, message);

        try
        {
            playerConnection.getSocket().close();
        }
        catch (IOException ex)
        {
            Limbo.LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public World getWorld()
    {
        return this.world;
    }

    public LimboConfiguration getLimboConfiguration()
    {
        return this.limboConfiguration;
    }

    public long getConnectedPlayers()
    {
        return this.playerConnections.stream().filter(playerConnection -> playerConnection.getStatus() == Status.LOGIN || playerConnection.getStatus() == Status.PLAY).count();
    }
}
