package com.henry.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtil {
	protected static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	private static final String DEFAULT_CHARSET_UTF8 = "UTF-8";
	private static final String APPLICATION_JSON = "application/json";

	public static String get(String url) {
		return get(url, null, null);
	}

	public static String get(String url, Map<String, Object> queryParas) {
		return get(url, queryParas, null);
	}

	private static String processRequest(HttpUriRequest request) {
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(final HttpResponse response) throws IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();

						return entity != null ? EntityUtils.toString(entity, DEFAULT_CHARSET_UTF8) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			result = httpClient.execute(request, responseHandler);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 发送GET请求
	 */
	public static String get(String url, Map<String, Object> queryParas, Map<String, String> headers) {
		HttpGet httpGet = new HttpGet(buildUrlWithQueryString(url, queryParas));
		setHeadersByJson(httpGet, headers);
		return processRequest(httpGet);
	}

	public static String post(String url) {
		return post(url, null, new HashMap<String, String>());
	}

	public static String post(String url, Map<String, Object> params) {
		return post(url, params, new HashMap<String, String>());
	}

	public static String post(String url, String params) {
		return postJson(url, params, new HashMap<String, String>());
	}

	public static String post(String url, Map<String, Object> queryParas, Map<String, String> headers) {
		HttpPost httpPost = new HttpPost(url);
		setHeadersByJson(httpPost, headers);
		setEntity(httpPost, queryParas);
		return processRequest(httpPost);
	}

	public static void asyncPost(String url, Map<String, Object> params) {
		asyncPost(url, params, new HashMap<String, String>());
	}

	private static void processCloseHttp(Closeable httpclient) {
		if (httpclient != null) {
			try {
				httpclient.close();
			} catch (IOException ex) {
			}
		}
	}

	public static void asyncPost(String url, Map<String, Object> queryParas, Map<String, String> headers) {
		HttpPost httpPost = new HttpPost(url);
		setHeadersByJson(httpPost, headers);
		setEntity(httpPost, queryParas);

		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		httpclient.start();
		logger.debug("caller thread id is : " + Thread.currentThread().getId());

		httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {
			@Override
			public void completed(final HttpResponse response) {
				logger.debug("StatusCode" + response.getStatusLine().getStatusCode());
				processCloseHttp(httpclient);
			}

			@Override
			public void failed(final Exception ex) {
				logger.debug("failed:" + ex.getMessage());
				processCloseHttp(httpclient);
			}

			@Override
			public void cancelled() {
				logger.debug("cancelled:");
				processCloseHttp(httpclient);
			}
		});

	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param queryParas
	 * @param headers
	 * @return
	 */
	public static String postJson(String url, String queryParas, Map<String, String> headers) {
		HttpPost httpPost = new HttpPost(url);
		setHeadersByJson(httpPost, headers);

		StringEntity entity = new StringEntity(queryParas, DEFAULT_CHARSET_UTF8);
		entity.setContentType(APPLICATION_JSON);
		entity.setContentEncoding(DEFAULT_CHARSET_UTF8);
		httpPost.setEntity(entity);

		return processRequest(httpPost);
	}

	private static ByteArrayOutputStream processByteArrayOutputStream(HttpUriRequest request) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse result = null;
		try {
			result = httpClient.execute(request);
			int status = result.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				return FileUtil.getByteArrayOutputStream(result.getEntity().getContent());
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (IOException e) {
			// log.error("请求出错,url = " + httGet.getURI(), e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// HttpClientUtils.closeQuietly(response);
			// log.debug("请求接口，url = " + httGet.getURI() + "\n" + "参数：" +
			// headers.toString());
		}
		return null;
	}

	public static ByteArrayOutputStream postByByteArrayOutputStream(String url, Map<String, Object> queryParas,
			Map<String, String> headers) {
		HttpPost httpPost = new HttpPost(url);
		setHeadersByJson(httpPost, headers);
		setEntity(httpPost, queryParas);
		return processByteArrayOutputStream(httpPost);
	}

	public static ByteArrayOutputStream getByByteArrayOutputStream(String url, Map<String, String> headers) {
		HttpGet httGet = new HttpGet(url);
		setHeadersByJson(httGet, headers);
		return processByteArrayOutputStream(httGet);
	}

	private static void setHeaders(HttpRequestBase httpRequestBase, Map<String, String> headers) {
		// 设置传入的Header
		if (MapUtils.isNotEmpty(headers)) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpRequestBase.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	private static void setHeadersByJson(HttpRequestBase httpRequestBase, Map<String, String> headers) {
		httpRequestBase.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
		setHeaders(httpRequestBase, headers);
	}

	private static void setHeadersByFile(HttpRequestBase httpRequestBase, Map<String, String> headers) {
		setHeaders(httpRequestBase, headers);
	}

	/**
	 * 构建URL的查询参数
	 */
	private static String buildUrlWithQueryString(String url, Map<String, Object> queryParas) {
		if (queryParas == null || queryParas.isEmpty()) {
			return url;
		}

		StringBuilder sb = new StringBuilder(url);
		boolean isFirst;
		if (url.indexOf("?") == -1) {
			isFirst = true;
			sb.append("?");
		} else {
			isFirst = false;
		}

		for (Entry<String, Object> entry : queryParas.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append("&");
			}

			String key = entry.getKey();
			String value = "";
			if (entry.getValue() != null) {
				value = entry.getValue().toString();
			}
			if (StringUtils.isNotBlank(value)) {
				try {
					value = URLEncoder.encode(value, DEFAULT_CHARSET_UTF8);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
			sb.append(key).append("=").append(value);
		}
		return sb.toString();
	}

	private static void setEntity(HttpPost httpPost, Map<String, Object> queryParas) {
		JSONObject json = new JSONObject();
		if (MapUtils.isNotEmpty(queryParas)) {
			json.putAll(queryParas);
		}

		StringEntity entity = new StringEntity(json.toString(), DEFAULT_CHARSET_UTF8);
		entity.setContentType(APPLICATION_JSON);
		entity.setContentEncoding(DEFAULT_CHARSET_UTF8);
		httpPost.setEntity(entity);
	}

	public static String postFile(MultipartFile files, String url, Map<String, String> headers)
			throws IOException, URISyntaxException {
		return postFile(files, url, headers, "file_upload");
	}

	public static String postFile(MultipartFile files, String url, Map<String, String> headers, String uploadName)
			throws IOException, URISyntaxException {
		HttpPost httpPost = new HttpPost(url);
		setHeadersByFile(httpPost, headers);

		InputStream inputStream = files.getInputStream();
		String fileName = files.getOriginalFilename();

		ByteArrayOutputStream swapStream = FileUtil.getByteArrayOutputStream(inputStream);
		try {
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.addPart(uploadName, new ByteArrayBody(swapStream.toByteArray(), fileName)).build();

			httpPost.setEntity(reqEntity);
			return processRequest(httpPost);
		} finally {
			swapStream.close();
		}
	}

	public static String postFileMulti(List<MultipartFile> fileList, String url, Map<String, String> headers,
                                       String uploadName) throws IOException, URISyntaxException {

		HttpPost httpPost = new HttpPost(url);
		setHeadersByFile(httpPost, headers);

		MultipartEntityBuilder multBuilderObj = MultipartEntityBuilder.create();
		for (int i = 0; i < fileList.size(); ++i) {
			MultipartFile file = fileList.get(i);
			String fileName = file.getOriginalFilename();
			InputStream inputStream = file.getInputStream();
			ByteArrayOutputStream swapStream = FileUtil.getByteArrayOutputStream(inputStream);
			try {
				multBuilderObj.addPart(uploadName, new ByteArrayBody(swapStream.toByteArray(), fileName));
			} finally {
				swapStream.close();
			}
		}
		HttpEntity reqEntity = multBuilderObj.build();
		httpPost.setEntity(reqEntity);
		return processRequest(httpPost);
	}

	/**
	 * 检测url是否有效
	 */
	public static boolean checkURLConnect(String urlStr) {
		return checkURLConnect(urlStr, 1);
	}

	public static synchronized boolean checkURLConnect(String urlStr, int retryNum) {
		int counts = 0;
		if (!checkHttpUrl(urlStr)) {
			return false;
		}
		while (counts < retryNum) {
			try {
				URL url = new URL(urlStr);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				int state = con.getResponseCode();
				if (state == 200) {
					return true;
				}
			} catch (Exception ex) {
				counts++;
				continue;
			}
		}
		return false;
	}

	/**
	 * 检测url是否合法
	 */
	public static boolean checkUrl(String url) {
		if ((null == url) || (url.trim().length() < 1)) {
			return false;
		}
		return url.matches("^((https|http|ftp|rtsp|mms)?://)"
				+ "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" + "|"
				+ "([0-9a-z_!~*'()-]+\\.)*" + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." + "[a-z]{2,6})" + "(:[0-9]{1,4})?"
				+ "((/?)|" + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$");
	}

	public static boolean checkHttpUrl(String url) {
		if ((null == url) || (url.trim().length() < 1)) {
			return false;
		}
		url = url.toLowerCase();
		if ((url.startsWith("https://")) || (url.startsWith("http://"))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 提取主域名
	 */
	private static final Pattern PATTERN_TOPDOMAIN = Pattern
			.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);

	public static String getTopUrl(String url) {
		Matcher matcher = PATTERN_TOPDOMAIN.matcher(url);
		matcher.find();
		// System.out.println(matcher.group());
		return matcher.group();
	}

	public static String getMimeType(String fileUrl) {
		String type = null;
		URL u = null;
		try {
			u = new URL(fileUrl);
		} catch (MalformedURLException e) {
			return "";
		}
		URLConnection uc = null;
		try {
			if (u != null) {
				uc = u.openConnection();
			}
		} catch (IOException e) {
			return "";
		}
		if (uc != null) {
			type = uc.getContentType();
		}
		return type;
	}

	public static boolean isImageType(String fileUrl) {
		String str = getMimeType(fileUrl);
		if ((null == str) || (str.trim().length() < 1)) {
			return false;
		}
		str = str.trim().toLowerCase();
		return str.contains("image/");
	}

}
