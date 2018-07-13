package spring.springboot;

import com.alibaba.fastjson.JSON;
import com.sun.media.jfxmedia.track.Track;
import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.springboot.config.RedisUtils;
import org.spring.springboot.domain.City;
import org.spring.springboot.service.impl.CityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.*;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

//import org.spring.springboot.service.CityService;

/**
 * Created by bysocket on 05/06/2017.
 */

public class ApplicationTests2 {

//    private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);

    @Test
    public void test() throws IOException {

        String sql="75 4D 87 79 7E 3E 75 88 75 10 0D 0A 58 79 74 74 75 7E 4D 41 10 0D 0A 5C 7F 73 71 84 79 7F 7E 4D 53 4A 6C 67 79 7E 7E 84 6C 63 89 83 84 75 7D 43 42 6C 10 0D 0A";
        String[] split = sql.split(" ");
        for (String s : split) {


            byte[] baKeyword = new byte[s.length() / 2];
            for (int i = 0; i < baKeyword.length; i++) {
                try {
                    baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                            i * 2, i * 2 + 2), 16));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
//                s = new String(baKeyword, "ASCII");
//                s = new String(baKeyword, "utf-8");
//                s = new String(baKeyword, "gbk");
                s = new String(baKeyword, "US-ASCII");

            } catch (Exception e1) {
                e1.printStackTrace();
            }
            final BASE64Decoder decoder = new BASE64Decoder();
            //解码
//            System.out.print(new String(decoder.decodeBuffer(s), "UTF-8"));
            System.out.print(s);
        }
    }
    public static String x16toString(String x16 , String CharsetName) throws UnsupportedEncodingException{
        if(x16==null || "".equals(x16.trim())){
            return "";
        }
        String tempStr = "";
        byte[] b = new byte[x16.length()/2];
        for(int i=0; i<x16.length()/2; i++){
            tempStr = x16.substring(i*2, i*2+2);
            int temp = Integer.parseInt(tempStr, 16);
            b[i] = (byte) temp;
        }
        String restr = new String(b,CharsetName);
        return restr;
    }
    @Test
    public void testDea() throws IOException {

        String sql="754D87797E3E758875100D0A58797474757E4D41100D0A5C7F737184797F7E4D534A6C67797E7E846C63898384757D43426C100D0A";
        System.out.println(x16toString(sql,"US-ASCII"));

    }


    @Test
    public void test1() throws IOException {
        final BASE64Encoder encoder = new BASE64Encoder();
        final BASE64Decoder decoder = new BASE64Decoder();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
        //编码
        final String encodedText = encoder.encode(textByte);
        System.out.println(encodedText);
        //解码
        System.out.println(new String(decoder.decodeBuffer(encodedText), "UTF-8"));


//        String fileName = "C:\\Users\\ztrk\\Desktop\\ODBC.ini";
//        try {
//            Wini ini = new Wini(new File(fileName));
//            for (Map.Entry<String, Profile.Section> stringSectionEntry : ini.entrySet()) {
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void testRedis() {
        // 读取一般的属性文件
        FileInputStream fin = null; // 打开文件
        try {
            fin = new FileInputStream("C:\\Users\\ztrk\\Desktop\\ODBC.ini");
            Properties props = new Properties();                 // 建立属性类
            props.load(fin);                                   // 读入文件
            fin.close();                                       // 关闭文件
            System.out.println(JSON.toJSONString(props));
            for (Map.Entry<Object, Object> objectObjectEntry : props.entrySet()) {
                System.out.println(objectObjectEntry.getKey());

                System.out.println(objectObjectEntry.getValue());
                System.out.println(unicodeToCn((String) objectObjectEntry.getValue()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testRedis2() throws IOException {
        // 读取一般的属性文件
     Properties props =readFileByReader("C:\\Users\\ztrk\\Desktop\\ODBC.ini");
//        for (String s : props.stringPropertyNames()) {
//            final BASE64Decoder decoder = new BASE64Decoder();
//            //解码
//            System.out.println(new String(decoder.decodeBuffer(s),"GB2312"));
//          //  System.out.println(s);
////            String s1 = unicodeToCn(s);
////            System.out.println(s1);
//        }
//        JSON.toJSONString(props);
//     System.out.println(JSON.toJSONString(props));
    }

    static final BASE64Decoder decoder = new BASE64Decoder();

    /**
     * 读取配置文件
     *
     * @param filePath文件的路径
     */
    public static Properties readFileByReader(String filePath) throws IOException {
/**定义变量*/
        InputStream inputStream =null;
        try {
            inputStream = new FileInputStream("C:\\Users\\ztrk\\Desktop\\ODBC.ini");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Properties proper = new Properties();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            String s=null;
            while((s=bf.readLine()) != null){
                //解码
                System.out.println(new String(decoder.decodeBuffer(s),"GBK"));
//                System.out.println(bf.readLine());
            }
            proper.load(bf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return proper;
    }



    /**
     * 读取配置文件
     * @param filePath配置文件的路径 这个方法用的是字节流来读取文件，中文会乱码；
     */
    public static Properties readFileByStream(String filePath){
/**定义变量*/
        FileInputStream fileInput = null;//读
        Properties proper = new Properties();
        try {
            fileInput = new FileInputStream(filePath);
            proper.load(fileInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proper;

    }


    private static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }

    private static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
            returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }
}