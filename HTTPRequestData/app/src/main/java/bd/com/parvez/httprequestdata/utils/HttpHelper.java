package bd.com.parvez.httprequestdata.utils;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper class for working with a remote server
 */
public class HttpHelper {

    /**
     * Returns text from a URL on a web server
     *
     * @param address
     * @return
     * @throws IOException
     */
    public static String downloadUrl(String address, String user, String password) throws IOException {

        InputStream is = null;
        byte[] loginByte = (user + ":" + password).getBytes();//for authorization
        Log.d("HttpHelper",loginByte.toString());
        StringBuilder sb = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginByte,Base64.DEFAULT));
        Log.d("HttpHelper",sb.toString());//for authorization
        try {

            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Authorization",sb.toString());//for authorization
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Got response code " + responseCode);
            }
            is = conn.getInputStream();
            return readStream(is);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("HttpHelper",e.getMessage());
        } finally {
            if (is != null) {
                is.close();
                Log.d("HttpHelper","Closed");
            }
        }
        return null;
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private static String readStream(InputStream stream) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BufferedOutputStream out = null;
        try {
            int length = 0;
            out = new BufferedOutputStream(byteArray);
            while ((length = stream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return byteArray.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
