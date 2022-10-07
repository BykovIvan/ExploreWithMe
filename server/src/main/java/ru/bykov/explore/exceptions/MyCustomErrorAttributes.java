package ru.bykov.explore.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import ru.bykov.explore.exceptions.model.ApiError;

import java.time.format.DateTimeFormatter;
import java.util.Map;


public class MyCustomErrorAttributes extends DefaultErrorAttributes {

//    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    private final String currentApiVersion;
//    private final String sendReportUri;
//
//    public MyCustomErrorAttributes(final String currentApiVersion, final String sendReportUri) {
//        this.currentApiVersion = currentApiVersion;
//        this.sendReportUri = sendReportUri;
//    }

//    @Override
//    public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final boolean includeStackTrace) {
//        final Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, ErrorAttributeOptions options);

//        final ApiError apiError = ApiError.fromDefaultAttributeMap(
//                currentApiVersion, defaultErrorAttributes, sendReportUri
//        );
//        return null;
    }

//    @Override
//    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
//            Map<String, Object> errorAttributes =
//                    super.getErrorAttributes(webRequest, options);
////            errorAttributes.remove("error");
////        errorAttributes.remove("path");
////            errorAttributes.put("errors", "errors");
//
//
//
//
//            return errorAttributes;
//    }

    //    @Override
//    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options, String stackTrace) {
//        Map<String, Object> errorAttributes =
//                super.getErrorAttributes(webRequest, options);
//        errorAttributes.remove("error");
//        errorAttributes.remove("path");
//        errorAttributes.put("errors", stackTrace);
//
//        return errorAttributes;
//    }
//}
