package io.metty.seriliazble;

import java.io.*;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-05-27 11:31 上午
 */
public class Coder {

        public static byte[] objectToByteArray(Object obj) throws IOException {
            ByteArrayOutputStream bis = null;
            ObjectOutputStream os = null;
            try {
                bis = new ByteArrayOutputStream(1024);
                os = new ObjectOutputStream(bis);
                os.writeObject(obj);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (os != null) {
                    os.close();
                }
            }
            return bis.toByteArray();
        }

        public static Object byteArrayToObject(byte[] src) throws IOException,
                ClassNotFoundException {
            ObjectInputStream ois = null;
            ByteArrayInputStream bos = null;
            try {
                bos = new ByteArrayInputStream(src);
                ois = new ObjectInputStream(bos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    bos.close();
                }
                if (ois != null) {
                    ois.close();
                }
            }
            return ois.readObject();

        }
}