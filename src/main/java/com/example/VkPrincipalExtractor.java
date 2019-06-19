package com.example;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VkPrincipalExtractor implements PrincipalExtractor {
    private static final String PRINCIPAL_KEYS = "first_name";

    public VkPrincipalExtractor() {
    }

    public Object extractPrincipal(Map<String, Object> map) {
        String var2 = PRINCIPAL_KEYS;

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (entry.getKey() == "response") {
                ArrayList arrayList = (ArrayList)entry.getValue();
                Map<String, String> internalMap = (Map<String, String>) arrayList.get(0);
                for (Map.Entry<String, String> internalEntry : internalMap.entrySet()) {
                    if (internalEntry.getKey().equals(var2) ) {
                        System.out.println(internalEntry);
                        return internalEntry.getValue();
                    }
                }
            }

        }

        return null;
    }}