package com.iw2fag.lab.lab.document;

import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.Header;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.service.ResponseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApiResponseMessage extends ResponseMessage {

    /**
     * Adaptive constructor for backward compatibility
     * @param code
     * @param message
     * @param responseModel
     */
    public ApiResponseMessage(int code, String message, ModelReference responseModel) {
        this(code, message, responseModel, new HashMap<String, Header>(), new ArrayList<VendorExtension>());
    }

    /**
     *
     * @param code
     * @param message
     * @param responseModel
     * @param headers
     * @param vendorExtensions
     */
    public ApiResponseMessage(int code, String message, ModelReference responseModel, Map<String, Header> headers, List<VendorExtension> vendorExtensions) {
        super(code, message, responseModel, headers, vendorExtensions);
    }
}
