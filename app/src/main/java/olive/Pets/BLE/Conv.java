package olive.Pets.BLE;

import olive.Pets.BLE.Packet;

/**
 * Created by luncl on 6/5/2017.
 */

public class Conv
{
    public static byte[] toBytes(Packet pack)
    {
        byte[] data = null;
        switch(pack.prefix){
            case Packet.Oper.OP_Discon:
                data =  new byte[1];
                data[0] = pack.prefix;
                break;
            case Packet.Oper.OP_Report:
            case Packet.Oper.OP_Train:
                data =  new byte[2];
                data[0] = pack.prefix;
                data[1] = pack.param;
                break;
            case Packet.Oper.OP_Sync:
                data =  new byte[6];
                data[0] = pack.prefix;
                data[1] = pack.param;
                for(int i = 2; i< 6; i++){
                    // to littel-endian
                    data[i] = (byte)(pack.value >> (i*8));
                }
                break;
        }
        return data;
    }

    // @// TODO: 6/5/2017 bytes to packet
    public static Packet toPacket(byte[] bytes)
    {
        return null;
    }

}
