package cn.keepfight.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * properties utility class
 *
 * @author Tom
 */
public class PropertieUtil {

    public static final String PROPERTIES_RESOURCE_PATH = "conf/";

    /**
     * load a properties file by specifying a file name under {@link #PROPERTIES_RESOURCE_PATH}
     *
     * @param file the file name which will be loaded
     * @return Properties object
     */
    public static Properties loadProperties(String file) throws RuntimeException {
        Properties resProperties = new Properties();

        // construct URL
        String filePath = PROPERTIES_RESOURCE_PATH + file;

        //XXX 这里可以是更简单的配置方式
        //load properties file
        try {
            resProperties.load(PropertieUtil.class.getClassLoader().getResourceAsStream(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resProperties;
    }


    /**
     * load a properties file by specifying a file name under {@link #PROPERTIES_RESOURCE_PATH}
     * <br/>
     * optionally, it could be passed another parameter defaultFile to try to read another file
     * when exception occur.
     *
     * @param file        the file name which will be loaded
     * @param defaultFile default properties file which may be could contain configuration
     * @return Properties object
     */
    public static Properties loadProperties(String file, String defaultFile) throws RuntimeException {
        try {
            return loadProperties(file);
        } catch (Exception e) {
            return loadProperties(defaultFile);
        }
    }
}
