package id.co.qualitas.qubes.helper;

import android.content.Context;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class NetworkHelper {
    /*GET*/
    public static Object getWebservice(String url, Class<?> responseType) {
        int flag = 0;

        HttpEntity<?> response = null;
//        while (flag == 0) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        String token = (String) Helper.getItemParam(Constants.TOKEN);
        String bearerToken = Constants.BEARER.concat(token);
        requestHeaders.set("Authorization", bearerToken);

        HttpEntity<?> entity = new HttpEntity<>(requestHeaders);
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        restTemplate.getMessageConverters().add(gsonHttpMessageConverter);
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());//403
            if (e.getMessage().equals("401 Unauthorized")) {
            } else if (e.getMessage().contains("ENETUNREACH")) {
                Helper.setItemParam(Constants.NO_CONNECTION, "1");
            }

        }
//        }
        return Objects.requireNonNull(response).getBody();
    }

    public static Object getWebserviceWoToken(String url, Class<?> responseType) {
        int flag = 0;

        HttpEntity<?> response = null;
//        while (flag == 0) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        HttpEntity<?> entity = new HttpEntity<>(requestHeaders);
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        restTemplate.getMessageConverters().add(gsonHttpMessageConverter);
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());//403
            if (e.getMessage().equals("401 Unauthorized")) {
            } else if (e.getMessage().contains("ENETUNREACH")) {
                Helper.setItemParam(Constants.NO_CONNECTION, "1");
            }

        }
//        }
        return Objects.requireNonNull(response).getBody();
    }

    /*POST*/
    public static Object postWebserviceLogin(String url, Class<?> responseType, Object body) {
        int flag = 0;
        ResponseEntity<?> responseEntity = null;
        while (flag == 0) {
            flag = 1;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            requestHeaders.set("Authorization", Constants.AUTHORIZATION_LOGIN);
            requestHeaders.set("Content-Type", Constants.HTTP_HEADER_CONTENT_TYPE);
            HttpEntity<?> requestEntity = new HttpEntity<>(body, requestHeaders);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
            } catch (Exception e) {
                Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());//403
                if (e.getMessage().contains("400")) {
                    Helper.setItemParam(Constants.ERROR_LOGIN, "1");
                } else if (e.getMessage().contains("ENETUNREACH")) {
                    Helper.setItemParam(Constants.NO_CONNECTION, "1");
                }
                e.getMessage();
            }

        }
        return Objects.requireNonNull(responseEntity).getBody();
    }

    public static Object postWebserviceWithBody(String url, Class<?> responseType, Object body) {
        int flag = 0;
        ResponseEntity<?> responseEntity = null;
        while (flag == 0) {
            flag = 1;
            String token = (String) Helper.getItemParam(Constants.TOKEN);
            String bearerToken = Constants.BEARER.concat(token);

            RestTemplate restTemplate = new RestTemplate();

//            HttpComponentsClientHttpRequestFactory fac = new HttpComponentsClientHttpRequestFactory();
//            fac.setConnectTimeout(10000);
//
//            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//            factory.setConnectTimeout(1000);

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            requestHeaders.set("Authorization", bearerToken);

            HttpEntity<?> requestEntity = new HttpEntity<>(body, requestHeaders);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
//            restTemplate.setRequestFactory(fac);

            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
            } catch (Exception e) {
                Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());//403
                if (e.getMessage().equals("401 Unauthorized")) {
                    flag = 0;
                } else {
                    if (e.getMessage().contains("ENETUNREACH")) {
                        Helper.setItemParam(Constants.NO_CONNECTION, "1");
                        break;
                    }
                }
            }
        }
        return Objects.requireNonNull(responseEntity).getBody();
    }

    public static Object postWebserviceWithBodyWithoutHeaders(String url, Class<?> responseType, Object body) {
        int flag = 0;
        ResponseEntity<?> responseEntity = null;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        HttpEntity<?> requestEntity = new HttpEntity<>(body, requestHeaders);
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());//403
            e.getMessage();
            if (e.getMessage().contains("ENETUNREACH")) {
                Helper.setItemParam(Constants.NO_CONNECTION, "1");
            }
        }

        return responseEntity != null ? responseEntity.getBody() : null;
    }

    /*PUT*/
    public static Object putWebserviceWithBody(String url, Class<?> responseType, Object body) {
        int flag = 0;
        ResponseEntity<?> responseEntity = null;
        while (flag == 0) {
            flag = 1;

            String token = (String) Helper.getItemParam(Constants.TOKEN);
            String bearerToken = Constants.BEARER.concat(token);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            requestHeaders.set("Authorization", bearerToken);
            HttpEntity<?> requestEntity = new HttpEntity<>(body, requestHeaders);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType);
            } catch (Exception e) {
                Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());//403
                if (e.getMessage().equals("401 Unauthorized")) {
                    flag = 0;
                } else if (e.getMessage().contains("ENETUNREACH")) {
                    Helper.setItemParam(Constants.NO_CONNECTION, "1");
                    break;
                }
            }
        }
        return Objects.requireNonNull(responseEntity).getBody();
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static Object postWebserviceWithBodyMultiPart(String url, Class<?> responseType, Object body) {
        int flag = 0;
        ResponseEntity<?> responseEntity = null;
//        while (flag == 0) {
//            flag = 1;

//            String token = (String) Helper.getItemParam(Constants.TOKEN);
        String token = SessionManagerQubes.getToken();
        String bearerToken = Constants.BEARER.concat(token);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        requestHeaders.set("Authorization", bearerToken);

        HttpEntity<?> requestEntity = new HttpEntity<>(body, requestHeaders);
//        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
        } catch (Exception e) {
            if (e.getMessage() != null) {
                Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());//403
                Log.e(url, e.getMessage());
                if (e.getMessage().contains("401")) {
//                    flag = 0;
                    Helper.setItemParam(Constants.UNAUTHORIZED, "1");
//                    break;
                } else {
                    if (e.getMessage().contains("ENETUNREACH")) {
                        Helper.setItemParam(Constants.NO_CONNECTION, "1");
//                        break;
                    }
                }
            }

        }
        return Objects.requireNonNull(responseEntity).getBody();
    }
}
