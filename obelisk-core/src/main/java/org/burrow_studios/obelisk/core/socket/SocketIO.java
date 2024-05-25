package org.burrow_studios.obelisk.core.socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.util.crypto.Encryption;
import org.burrow_studios.obelisk.util.crypto.EncryptionException;
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
    private Encryption encryption = Encryption.NONE;
    private Receiver receiver = data -> { };

    public SocketIO(@NotNull Socket socket, @Nullable Receiver receiver) throws IOException {
        this.socket = socket;
        this.setReceiver(receiver);
        this.in  = new DataInputStream(this.socket.getInputStream());
        this.out = new DataOutputStream(this.socket.getOutputStream());
    }

    public void setEncryption(@Nullable Encryption encryption) {
        this.encryption = encryption == null ? Encryption.NONE : encryption;
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
        byte[] encrypted;
        try {
            encrypted = encryption.encrypt(data);
        } catch (EncryptionException e) {
            LOG.warn("Encountered an exception when attempting to encrypt", e);
            return;
        }

        this.sendLock.lock();
        try {
            this.out.writeInt(encrypted.length);
            this.out.write(encrypted);
        } catch (IOException e) {
            LOG.warn("Encountered an exception when attempting to send", e);
        } finally {
            this.sendLock.unlock();
        }
    }

    public void receive() {
        byte[] data;
        byte[] decrypted;

        this.receiveLock.lock();
        try {
            int length = this.in.readInt();

            data = new byte[length];
            this.in.readFully(data);
        } catch (IOException e) {
            LOG.warn("Encountered an exception when attempting to receive", e);
            return;
        } finally {
            this.receiveLock.unlock();
        }

        try {
            decrypted = this.encryption.decrypt(data);
        } catch (EncryptionException e) {
            LOG.warn("Encountered an exception when attempting to decrypt", e);
            return;
        }

        this.receiver.receive(decrypted);
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
