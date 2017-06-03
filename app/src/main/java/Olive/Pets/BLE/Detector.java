package Olive.Pets.BLE;

/**
 * Pets Device scanner
 * @author luncliff
 */
public interface Detector
{
    public void begin();
    public void end();
    public Proxy next();
}
