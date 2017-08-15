package cn.keepfight.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class HttpUtils {

//	/**
//	 * 简单 Get 操作，仅传入 URL 和 Session Cookie，当然你可以在 URL 中添加 Get 参数
//	 *
//	 * @param url
//	 *            Get 方法的目标 URL
//	 * @return 简单 Get 的结果字符串
//	 * @throws Exception
//	 *             简单 Get 异常
//	 */
//	public static String simpleGet(String url) throws Exception {
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//		try {
//			System.out.println("simple get: "+url);
//			HttpPost post = new HttpPost(url);
//			CloseableHttpResponse resp = httpClient.execute(post);
//			try {
//				return EntityUtils.toString(resp.getEntity());
//			} finally {
//				resp.close();
//			}
//		} finally {
//			httpClient.close();
//		}
//	}
//
//	public static String simpleGetWithEncode(String url, List<Pair<String, String>> params) throws Exception {
//		String paramStr = "";
//		for (Pair<String, String> p : params) {
//			paramStr += "&"+p.getKey()+"="+URLEncoder.encode(p.getValue(), "utf-8");
//		}
//		System.out.println();
//		params.stream()
//				.map(p -> {
//					try {
//						return p.getKey()+"="+URLEncoder.encode(p.getValue(), "utf-8");
//					} catch (UnsupportedEncodingException e) {
//						e.printStackTrace();
//					}
//					return null;
//				})
//				.collect(Collectors.joining("&"));
//		return simpleGet(url+"?"+paramStr.substring(1));
//	}
//
//	public static String simplePost(String url, String param) throws Exception {
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//		try {
//			HttpPost post = new HttpPost(url);
//			// add Cookie JSESSIONID
//			// post.addHeader("Cookie", "JSESSIONID=" + jseSession);
//			StringEntity params =new StringEntity("yy="+param);
//			post.addHeader("content-type", "application/x-www-form-urlencoded");
//			post.setEntity(params);
//			CloseableHttpResponse resp = httpClient.execute(post);
//			try {
//				return EntityUtils.toString(resp.getEntity());
//			} finally {
//				resp.close();
//			}
//		} finally {
//			httpClient.close();
//		}
//	}
//
//	public static String simplePostJSONWithUTG8(String url, Pair<String, String> param) throws Exception {
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//		try {
//			HttpPost post = new HttpPost(url);
//			StringEntity params =new StringEntity(param.getKey()+"="+URLEncoder.encode(param.getValue(), "utf-8"));
//			post.addHeader("content-type", "application/x-www-form-urlencoded");
//			post.setEntity(params);
//			CloseableHttpResponse resp = httpClient.execute(post);
//			try {
//				return EntityUtils.toString(resp.getEntity());
//			} finally {
//				resp.close();
//			}
//		} finally {
//			httpClient.close();
//		}
//	}

//    public static void mainx(String[] args) throws IOException {
//
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//
//        List<NameValuePair> formparams = new ArrayList<>();
//        formparams.add(new BasicNameValuePair("param1", "value1"));
//        formparams.add(new BasicNameValuePair("param2", "value2"));
//        HttpPost httppost = new HttpPost("http://localhost/handler.do");
//        httppost.setEntity(new UrlEncodedFormEntity(formparams, Consts.UTF_8));
//
//        ResponseHandler<JSONObject> rh = httpResponse -> {
//            StatusLine statusLine = httpResponse.getStatusLine();
//            HttpEntity entity = httpResponse.getEntity();
//            if (statusLine.getStatusCode() >= 300) {
//                throw new HttpResponseException(
//                        statusLine.getStatusCode(),
//                        statusLine.getReasonPhrase());
//            }
//            if (entity == null) {
//                throw new ClientProtocolException("Response contains no content");
//            }
//            Charset charset = ContentType.getOrDefault(entity).getCharset();
//            return new JSONObject(new JSONTokener(new InputStreamReader(entity.getContent(), charset)));
//        };
//
//        HttpGet httpget = new HttpGet("http://localhost/");
//        JSONObject resultJSON = httpclient.execute(httpget, rh);
//
//        HttpContext context = null;
//        HttpClientContext clientContext = HttpClientContext.adapt(context);
//        HttpHost target = clientContext.getTargetHost();
//        HttpRequest request = clientContext.getRequest();
//        HttpResponse response = clientContext.getResponse();
//        RequestConfig config = clientContext.getRequestConfig();
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setSocketTimeout(1000)
//                .setConnectTimeout(1000)
//                .build();
//        HttpGet httpget1 = new HttpGet("http://localhost/1");
//        httpget1.setConfig(requestConfig);
//        JSONObject response1 = httpclient.execute(httpget1, rh, context);
//
//        CloseableHttpClient xx = HttpClients.custom()
//                // 添加拦截器
//                .addInterceptorLast((HttpRequestInterceptor) (request1, context1) -> {
//                    AtomicInteger count = (AtomicInteger) context1.getAttribute("count");
//                    request1.addHeader("Count", Integer.toString(count.getAndIncrement()));
//                })
//                .build();
//
//        File file = new File("");
//        HttpPost post = new HttpPost("http://echo.200please.com");
//        HttpClient client = new DefaultHttpClient();
//
//        MultipartEntity entity = new MultipartEntity();
//        entity.addPart("file", new FileBody(file));
//        post.setEntity(entity);
//
//        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
//        StringBody stringBody1 = new StringBody("Message 1", ContentType.MULTIPART_FORM_DATA);
//        StringBody stringBody2 = new StringBody("Message 2", ContentType.MULTIPART_FORM_DATA);
////
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        builder.addPart("upfile", fileBody);
//        builder.addPart("text1", stringBody1);
//        builder.addPart("text2", stringBody2);
//
//        post.setEntity(entity);
//    }

    /**
     * JSON 转换器，将返回结果为 JSON 的 API 请求转换为 JSON 对象
     * @return 结果 JSON 对象
     */
    public static ResponseHandler<JSONObject> jsonConvert(){
        return httpResponse -> {
            HttpEntity entity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() >= 300 || entity == null) {
                throw new IOException();
            }
            Charset charset = ContentType.getOrDefault(entity).getCharset();
            return new JSONObject(new JSONTokener(new InputStreamReader(entity.getContent(), charset)));
        };
    }

    public static void main(String[] args) throws IOException {


        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://10.10.18.96:8081/qs/api/image/upload");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addPart("image", new FileBody(new File("C:\\Users\\tom\\Pictures\\Image.bmp")));
        post.setEntity(builder.build());
        
        JSONObject resultJSON = httpclient.execute(post, jsonConvert());
        System.out.println(resultJSON);
    }
}

