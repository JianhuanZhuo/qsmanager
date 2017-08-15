package cn.keepfight.utils;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * QS 服务器 API
 * Created by tom on 2017/8/12.
 */
public class QSAPI {

    private static String server = "http://10.10.18.96:8081";
    private static String project = "/qs";

    /**
     * 图片上传接口 URI
     */
    private static String API_IMAGE_UPLOAD = server+project+"/api/image/upload";

    /**
     * 图片查看 API
     */
    private static String API_IMAGE_TAKE = server+project+"/img/";

    /**
     * 图片上传接口
     * @param httpClient 使用的客户端
     * @param imageFile 图片文件
     * @return JSON 结果对象
     */
    public static JSONObject uploadImage(CloseableHttpClient httpClient, File imageFile) throws IOException {
        HttpPost post = new HttpPost(API_IMAGE_UPLOAD);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addPart("image", new FileBody(imageFile));
        post.setEntity(builder.build());

        return httpClient.execute(post, HttpUtils.jsonConvert());
    }

    /**
     * 获得指定图片名的完整 HTTP 路径<br/>
     * 示例：
     * <pre>
     * 123.png => http://10.10.18.96:8081/qs/img/123.png
     * </pre>
     * @param imageName 图片名
     * @return 完整 HTTP 路径
     */
    public static String imagePath(String imageName){
        return API_IMAGE_TAKE+imageName;
    }
}
