package org.burrow_studios.obelisk.core.socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

final class SocketIO implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(SocketIO.class);

    static final Gson GSON = new GsonBuilder().serializeNulls().create();

    private final Socket socket;
    private final DataInputStream  in;
    private final DataOutputStream out;
    private final ReentrantLock    sendLock = new ReentrantLock();
    private final ReentrantLock receiveLock = new ReentrantLock();
    private Receiver receiver;

    public SocketIO(@NotNull Socket socket, @Nullable Receiver receiver) throws IOException {
        this.socket = socket;
        this.setReceiver(receiver);
        this.in  = new DataInputStream(this.socket.getInputStream());
        this.out = new DataOutputStream(this.socket.getOutputStream());
    }

    public void setReceiver(@Nullable Receiver receiver) {
        this.receiver = receiver == null ? data -> { } : receiver;
    }

    public void send(@NotNull JsonElement data) {
        this.send(GSON.toJson(data));
    }

    public void send(@NotNull String data) {
        this.send(data.getBytes());
    }

    public void send(byte[] data) {
        this.sendLock.lock();
        try {
            this.out.writeInt(data.length);
            this.out.write(data);
        } catch (IOException e) {
            LOG.warn("Encountered an exception when attempting to send", e);
        } finally {
            this.sendLock.unlock();
        }
    }

    public void receive() {
        this.receiveLock.lock();
        try {
            int length = this.in.readInt();

            byte[] data = new byte[length];
            this.in.readFully(data);
        } catch (IOException e) {
            LOG.warn("Encountered an exception when attempting to receive", e);
        } finally {
            this.receiveLock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        this.sendLock.lock();
        this.receiveLock.lock();
        try {
            this.socket.close();
        } finally {
            this.sendLock.unlock();
            this.receiveLock.unlock();
        }
    }
}
