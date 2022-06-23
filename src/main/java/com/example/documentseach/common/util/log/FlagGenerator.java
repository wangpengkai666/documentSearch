package com.example.documentseach.common.util.log;

import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangpengkai
 */
public class FlagGenerator {
    private static AtomicInteger NEXT_INC = new AtomicInteger((new Random()).nextInt());
    private static final int GEN_MACHINE;
    private final int curTime = (int)(System.currentTimeMillis() / 1000L);
    private final int curMachine;
    private final int curInc;

    public static FlagGenerator get() {
        return new FlagGenerator();
    }

    private static boolean isValid(String s) {
        if (s == null) {
            return false;
        } else {
            int len = s.length();
            if (len != 24) {
                return false;
            } else {
                for(int i = 0; i < len; ++i) {
                    char c = s.charAt(i);
                    if ((c < '0' || c > '9') && (c < 'a' || c > 'f') && (c < 'A' || c > 'F')) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private FlagGenerator() {
        this.curMachine = GEN_MACHINE;
        this.curInc = NEXT_INC.getAndIncrement();
    }

    public String toStringBabble() {
        return babbleToMongod(this.toStringMongod());
    }

    private String toStringMongod() {
        byte[] b = this.toByteArray();
        StringBuilder buf = new StringBuilder(24);

        for(int i = 0; i < b.length; ++i) {
            int x = b[i] & 255;
            String s = Integer.toHexString(x);
            if (s.length() == 1) {
                buf.append("0");
            }

            buf.append(s);
        }

        return buf.toString();
    }

    private byte[] toByteArray() {
        byte[] b = new byte[12];
        ByteBuffer bb = ByteBuffer.wrap(b);
        bb.putInt(this.curTime);
        bb.putInt(this.curMachine);
        bb.putInt(this.curInc);
        return b;
    }

    private static String _pos(String s, int p) {
        return s.substring(p * 2, p * 2 + 2);
    }

    private static String babbleToMongod(String b) {
        if (!isValid(b)) {
            throw new IllegalArgumentException("invalid object id: " + b);
        } else {
            StringBuilder buf = new StringBuilder(24);

            int i;
            for(i = 7; i >= 0; --i) {
                buf.append(_pos(b, i));
            }

            for(i = 11; i >= 8; --i) {
                buf.append(_pos(b, i));
            }

            return buf.toString();
        }
    }

    @Override
    public String toString() {
        return this.toStringMongod();
    }

    static {
        try {
            int machinePiece;
            try {
                StringBuilder sb = new StringBuilder();
                Enumeration e = NetworkInterface.getNetworkInterfaces();

                while(e.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface)e.nextElement();
                    sb.append(ni.toString());
                }

                machinePiece = sb.toString().hashCode() << 16;
            } catch (Throwable var7) {
                machinePiece = (new Random()).nextInt() << 16;
            }

            int processId = (new Random()).nextInt();

            try {
                processId = ManagementFactory.getRuntimeMXBean().getName().hashCode();
            } catch (Throwable var6) {
            }

            ClassLoader loader = FlagGenerator.class.getClassLoader();
            int loaderId = loader != null ? System.identityHashCode(loader) : 0;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toHexString(processId));
            sb.append(Integer.toHexString(loaderId));
            int processPiece = sb.toString().hashCode() & '\uffff';
            GEN_MACHINE = machinePiece | processPiece;
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }
    }
}
