package Olive.Pets.BLE;

/**
 *  PetTrack과 통신하기 위한 Packet 구조
 *
 *  @author KMJ
 *  @author luncliff
 */
public class BeanPacket
{
    byte prefix;
    byte param;
    int value;

    public class Oper {
        public static final byte OP_Discon = -1;
        public static final byte OP_Report = 1;
        public static final byte OP_Train = 2;
        public static final byte OP_Sync = 3;
    }

    public final static int Postures = 8;

    public class Pos {
        public static final byte P_Unknown = 0;
        public static final byte P_Lie = 1;
        public static final byte P_LieSide = 2;
        public static final byte P_LieBack = 3;
        public static final byte P_Sit = 4;
        public static final byte P_Stand = 5;
        public static final byte P_Walk = 6;
        public static final byte P_Run = 7;
    }

    public class Attr {
        public static final byte A_Mean = 1;
        public static final byte A_Stdev = 2;
        public static final byte A_Time = 3;
    }

    private BeanPacket(byte _prefix, byte _param, int _value){
        this.prefix = _prefix;
        this.param = _param;
        this.value = _value;
    }

    public static BeanPacket Train(byte pos)
    {
        return new BeanPacket(Oper.OP_Report, Param(pos,(byte)0), 0);
    }

    public static BeanPacket Report(byte pos, byte attr)
    {
        return new BeanPacket(Oper.OP_Report, Param(pos,attr), 0);
    }

    public static BeanPacket Sync(byte pos, byte attr, int value)
    {
        return new BeanPacket(Oper.OP_Sync, Param(pos,attr), value);
    }

    public static BeanPacket Disconnect()
    {
        return new BeanPacket(Oper.OP_Discon, (byte)0, 0);
    }


    static public byte Param(byte pos, byte attr)
    {
        return (byte)((pos << 4) + attr);
    }

    static public byte Posture(byte param)
    {
        return (byte)(param >> 4);
    }

    static public byte Attribute(byte param)
    {
        return (byte)(param & 0x0F);
    }

    public byte[] toBytes()
    {
        byte[] data = null;
        switch(this.prefix){
            case Oper.OP_Discon:
                data =  new byte[1];
                data[0] = this.prefix;
                break;
            case Oper.OP_Report:
            case Oper.OP_Train:
                data =  new byte[2];
                data[0] = this.prefix;
                data[1] = this.param;
                break;
            case Oper.OP_Sync:
                data =  new byte[6];
                data[0] = this.prefix;
                data[1] = this.param;
                for(int i = 2; i< 6; i++){
                    // to littel-endian
                    data[i] = (byte)(this.value >> (i*8));
                }
                break;
        }
        return data;
    }

}
