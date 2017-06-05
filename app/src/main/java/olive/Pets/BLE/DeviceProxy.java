package olive.Pets.BLE;

/**
 * bytes to packet && packet to bytes
 * @author luncliff
 */
public interface DeviceProxy
{
    Packet recv();
    boolean send(Packet pkt);

    boolean isConnected();
    void close();

}
