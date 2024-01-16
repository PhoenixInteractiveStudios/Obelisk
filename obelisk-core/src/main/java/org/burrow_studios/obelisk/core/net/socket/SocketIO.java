package org.burrow_studios.obelisk.core.net.socket;

import org.burrow_studios.obelisk.core.net.socket.crypto.EncryptionException;
import org.burrow_studios.obelisk.core.net.socket.crypto.EncryptionHandler;
import org.burrow_studios.obelisk.util.function.ExceptionalConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

class SocketIO extends Thread {
    private static final Logger LOG = Logger.getLogger(SocketIO.class.getSimpleName());
    private final SocketAddress address;
    private final Socket socket;
    private DataInputStream  in;
    private DataOutputStream out;
    private @NotNull EncryptionHandler crypto;
    private volatile boolean listen = true;
    /** @see #receive() */
    private boolean running = true;
    private @NotNull ExceptionalConsumer<Throwable, ? extends Exception> onShutdown = t -> { };
    private @NotNull ExceptionalConsumer<byte[], ? extends Exception> onReceive = b -> { };
    private int packetsSent     = 0;
    private int packetsReceived = 0;

    public SocketIO(@NotNull SocketAddress address) {
        this(address, new Socket());
    }

    public SocketIO(@NotNull Socket socket) {
        this(socket.getRemoteSocketAddress(), socket);
    }

    private SocketIO(@NotNull SocketAddress address, @NotNull Socket socket) {
        this.address = address;
        this.socket = socket;

        this.crypto = EncryptionHandler.NONE;

        this.setDaemon(true);
    }

    public void setCrypto(@NotNull EncryptionHandler crypto) {
        this.crypto = crypto;
    }

    public void setListen(boolean b) {
        this.listen = b;
        if (b)
            this.notifyAll();
    }

    public void onShutdown(@NotNull ExceptionalConsumer<Throwable, ? extends Exception> onShutdown) {
        this.onShutdown = onShutdown;
    }

    public void onReceive(@NotNull ExceptionalConsumer<byte[], ? extends Exception> onReceive) {
        this.onReceive = onReceive;
    }

    public void onReceiveString(@NotNull Consumer<String> onReceive) {
        this.onReceive = bytes -> onReceive.accept(new String(bytes));
    }

    void shutdown(@Nullable Throwable cause) {
        this.listen = false;
        this.running = false; // see #receive()

        this.interrupt();

        if (this.socket.isConnected()) {
            try {
                this.socket.close();
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Could not properly close socket connection to " + address, e);
            }
        }

        try {
            this.onShutdown.accept(cause);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Encountered an unexpected exception when attempting to consume shutdown cause.", e);
        }
    }

    @Override
    public void run() {
        this.ensureConnection();
        // check if successful
        if (!this.socket.isConnected())
            return; // errors would have been logged already

        while (!this.isInterrupted()) {
            // wait until notified; see #setListening()
            if (!listen) {
                try {
                    this.wait();
                } catch (InterruptedException ignored) { }

                // test if awakened spuriously
                if (!listen) continue;
            }

            if (listen) {
                this.receive();
            }
        }
    }

    private synchronized void receive() {
        try {
            if (this.socket.isClosed()) {
                this.shutdown(null);
                return;
            }

            final int length = this.in.readInt();
            if (length < 1) return;

            final byte[] data = new byte[length];
            this.in.readFully(data);

            final byte[] decrypted = this.crypto.decrypt(data);

            try {
                this.onReceive.accept(decrypted);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Encountered an unexpected exception when attempting to consume received packet.", e);
            }

            this.packetsReceived++;
        } catch (EOFException e) {
            LOG.log(Level.WARNING, "Unexpected EOFException. This usually happens when a connection is abruptly closed", e);
            this.shutdown(e);
        } catch (SocketException e) {
            if (!running) {
                // in.readInt() threw an exception because the socket closed as intended.
                // Meaning everything is ok and the exception can safely be ignored.
                return;
            }

            LOG.log(Level.WARNING, "Unexpected SocketException", e);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Could not read input", e);
        } catch (EncryptionException e) {
            LOG.log(Level.WARNING, "Could not decrypt packet", e);
        } catch (Throwable t) {
            LOG.log(Level.WARNING, "Unexpected internal error when attempting to receive data", t);
        }
    }

    public void send(@NotNull String str) throws NetworkException {
        this.send(str.getBytes());
    }

    public synchronized void send(byte[] data) throws NetworkException {
        this.ensureConnection();

        try {
            final byte[] encrypted = this.crypto.encrypt(data);

            this.out.writeInt(encrypted.length);
            this.out.write(encrypted);

            this.packetsSent++;
        } catch (EncryptionException e) {
            LOG.log(Level.WARNING, "Encountered an exception when attempting to encrypt a packet");
            throw new NetworkException("Internal encryption error", e);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Encountered an exception when attempting to send a packet", e);
            throw new NetworkException("Internal IO error", e);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Encountered an unknown exception when attempting to send a packet", e);
            throw new NetworkException("Internal error", e);
        }
    }

    private void ensureConnection() {
        if (this.socket.isConnected()) return;

        try {
            this.socket.connect(this.address);
            this.in  = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Could not establish connection to " + address + " due to an IOException", e);
            this.shutdown(e);
        } catch (Throwable t) {
            LOG.log(Level.WARNING, "Internal error occurred when attempting to establish connection to " + address, t);
            this.shutdown(t);
        }
    }

    public boolean isListening() {
        return listen;
    }

    public boolean isRunning() {
        return running;
    }

    public int getPacketsSent() {
        return packetsSent;
    }

    public int getPacketsReceived() {
        return packetsReceived;
    }
}
