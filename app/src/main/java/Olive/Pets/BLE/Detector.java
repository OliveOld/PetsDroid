package olive.Pets.BLE;

/**
 * Pets Device scanner
 * @author luncliff
 */
public interface Detector
{
    void begin();
    void end();
    Proxy next();
}
