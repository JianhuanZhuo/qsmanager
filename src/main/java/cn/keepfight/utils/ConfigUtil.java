package cn.keepfight.utils;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 配置工具类
 * 和属性工具类很像，但这是为了分离出来，因为它的职责是读不会被打包的文件的
 * 也就是外部配置
 * Created by tom on 2017/6/9.
 */
public class ConfigUtil {
    private static final String PROPERTIES_RESOURCE_PATH = "./conf/";

    private static File getFile(String file) throws IOException {
        return new File(PROPERTIES_RESOURCE_PATH + file).getCanonicalFile();
    }

    public static Properties load(String file) {
        Properties resProperties = new Properties();

        //load properties file
        try (InputStream in = new FileInputStream(getFile(file))) {
            resProperties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resProperties;
    }

    public synchronized static void alter(String file, String key, String value) {

        Properties props = load(file);
        props.setProperty(key, value);

        try (FileOutputStream out = new FileOutputStream(getFile(file))) {
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
