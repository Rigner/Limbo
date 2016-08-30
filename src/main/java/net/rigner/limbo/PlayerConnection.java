package net.rigner.limbo;

import net.rigner.limbo.packets.AbstractProtocol;
import net.rigner.limbo.packets.Status;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * Created by Rigner on 29/08/16 for project Limbo.
 * All rights reserved.
 */
public class PlayerConnection
{
    private SocketChannel socket;
    private ByteBuffer byteBuffer;
    private AbstractProtocol protocol;
    private Status status;
    private String userName;
    private UUID uuid;
    private InetSocketAddress inetAddress;
    private int protocolId;
    private long lastKeepAlive;

    PlayerConnection(NetworkManager networkManager, SocketChannel socket) throws IOException
    {
        this.socket = socket;
        this.byteBuffer = ByteBuffer.allocate(1024);
        this.protocol = networkManager.getProtocolByVersionId(-1);
        this.status = Status.HANDSHAKE;
        this.userName = "";
        this.protocolId = -1;
        this.lastKeepAlive = System.currentTimeMillis();
        this.uuid = UUID.randomUUID();
        this.inetAddress = (InetSocketAddress)socket.getRemoteAddress();
    }

    SocketChannel getSocket()
    {
        return this.socket;
    }

    ByteBuffer getByteBuffer()
    {
        return byteBuffer;
    }

    public AbstractProtocol getProtocol()
    {
        return this.protocol;
    }

    public void setProtocol(AbstractProtocol protocol)
    {
        this.protocol = protocol;
    }

    Status getStatus()
    {
        return this.status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    void setByteBuffer(ByteBuffer byteBuffer)
    {
        this.byteBuffer = byteBuffer;
    }

    String getUserName()
    {
        return this.userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setProtocolId(int protocolId)
    {
        this.protocolId = protocolId;
    }

    public int getProtocolId()
    {
        return this.protocolId;
    }

    long getLastKeepAlive()
    {
        return this.lastKeepAlive;
    }

    public void keepAlive()
    {
        this.lastKeepAlive = System.currentTimeMillis();
    }

    public UUID getUuid()
    {
        return this.uuid;
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
    }

    public InetSocketAddress getInetAddress()
    {
        return this.inetAddress;
    }

    public void setInetAddress(InetSocketAddress inetAddress)
    {
        this.inetAddress = inetAddress;
    }
}
