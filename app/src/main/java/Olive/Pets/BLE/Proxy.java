package Olive.Pets.BLE;

/**
 * bytes to packet && packet to bytes
 * @author luncliff
 */
public interface Proxy
{
    public Packet recv();
    public boolean send(Packet pkt);

    public boolean isConnected();
    public void close();

}
