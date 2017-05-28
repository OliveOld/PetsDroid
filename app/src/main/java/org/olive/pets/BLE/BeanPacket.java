package org.olive.pets.BLE;

import static org.olive.pets.BLE.BeanPacket.Oper.OP_Discon;
import static org.olive.pets.BLE.BeanPacket.Oper.OP_Report;
import static org.olive.pets.BLE.BeanPacket.Oper.OP_Sync;

/**
 * Created by KMJ on 2017-05-24.
 */

public class BeanPacket {
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

    public byte param(byte pos, byte attr) { return (byte)((pos << 4) + attr); }

    public byte pos(byte param)
    {
        return (byte)(param >> 4);
    }

    public byte attr(byte param)
    {
        return (byte)(param & 0x0F);
    }

    // 자세의 report를 요청하는 바이트 배열을 생성한다
    public byte[] makeReportngBytes(byte pos, byte att) {
        byte[] array_request = {0, 0};
        array_request[0] = OP_Report;
        array_request[1] = param(pos, att);
        return array_request;
    }
    public byte[] makeSynchronizeBytes(byte pos, byte att) {
        byte[] array_request = {0,};
        array_request[0] = OP_Sync;
        array_request[1] = param(pos, att);
        // 무슨 value인지 몰라서 일단 0으로 놔둠
        array_request[2] =0;
        array_request[3] =0;
        array_request[4] =0;
        array_request[5] =0;
        return array_request;
    }
    public byte[] makeTrainingBytes(byte pos) {
        byte[] array_request = {0, };
        array_request[0] = OP_Report;
        array_request[1] = pos;
        return array_request;
    }
    public byte[] makeDisconnBytes() {
        byte[] array_request={};
        array_request[0] = OP_Discon;
        return array_request;
    }
}
