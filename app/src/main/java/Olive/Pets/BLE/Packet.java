package olive.Pets.BLE;

/**
 *  PetTrack과 통신하기 위한 Packet 구조
 *
 *  @author KMJ
 *  @author luncliff
 */
public class Packet
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

    private Packet(byte _prefix, byte _param, int _value){
        this.prefix = _prefix;
        this.param = _param;
        this.value = _value;
    }

    public static Packet Train(byte pos)
    {
        return new Packet(Oper.OP_Report, Param(pos,(byte)0), 0);
    }

    public static Packet Report(byte pos, byte attr)
    {
        return new Packet(Oper.OP_Report, Param(pos,attr), 0);
    }

    public static Packet Sync(byte pos, byte attr, int value)
    {
        return new Packet(Oper.OP_Sync, Param(pos,attr), value);
    }

    public static Packet Disconnect()
    {
        return new Packet(Oper.OP_Discon, (byte)0, 0);
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



}
