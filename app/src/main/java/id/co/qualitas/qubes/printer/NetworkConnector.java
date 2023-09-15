package id.co.qualitas.qubes.printer;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * This class implements a network connector to remote host.
 */
public class NetworkConnector extends AbstractConnector {

    // remote host address
    private String mHost;

    // remote port
    private int mPort;

    // socket instance associate with this object
    private Socket mSocket;

    // cached input steam associate with the socket
    private InputStream mInputStream;

    // cached output steam associate with the socket
    private OutputStream mOutputStream;

    // cached hashCode
    private volatile int mHashCode;

    /**
     * Creates a <code>NetworkConnector</code> from a hostname and a port number.
     *
     * @param context the context of network connector.
     * @param host    the host name.
     * @param port    the port address.
     */
    public NetworkConnector(Context context, String host, int port) {
        super(context);
        mHost = host;
        mPort = port;
    }

    /**
     * Gets the host name.
     *
     * @return the host name.
     */
    public String getHost() {
        return mHost;
    }

    /**
     * Gets the port number.
     *
     * @return the port number.
     */
    public int getPort() {
        return mPort;
    }

    /**
     * Connects this connector to the specified remote network host.
     *
     * @throws IOException if socket is already connected or an error occurs while connecting.
     */
    @Override
    public void connect() throws IOException {
        if (mSocket != null) {
            throw new IOException("Socket is already connected");
        }
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(mHost, mPort));
        socket.setSoTimeout(0);
        socket.setTcpNoDelay(true);
        mInputStream = socket.getInputStream();
        mOutputStream = socket.getOutputStream();
        mSocket = socket;
    }

    /**
     * Closes this network connector and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        if (mSocket != null) {
            mSocket.shutdownInput();
            mSocket.shutdownOutput();
            mSocket.close();
            mSocket = null;

            if (mInputStream != null) {
                mInputStream.close();
                mInputStream = null;
            }

            if (mOutputStream != null) {
                mOutputStream.close();
                mInputStream = null;
            }
        }
    }

    /**
     * Return an input stream to read data from this connector.
     *
     * @return the byte-oriented input stream.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return mInputStream;
    }

    /**
     * Return an output stream to write data into this connector.
     *
     * @return the byte-oriented output stream.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        return mOutputStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof NetworkConnector))
            return false;

        NetworkConnector connector = (NetworkConnector) o;
        return mHost.equals(connector.mHost)
                && mPort == connector.mPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = mHashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + mHost.hashCode();
            result = 31 * result + mPort;
            mHashCode = result;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "NetworkConnector{" +
                "host='" + mHost + '\'' +
                ", port=" + mPort +
                '}';
    }
}
