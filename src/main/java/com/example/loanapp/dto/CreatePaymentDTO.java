package com.example.loanapp.dto;

import com.nimbusds.jose.shaded.gson.annotations.SerializedName;

public class CreatePaymentDTO {
    static class CreatePaymentItem {
        @SerializedName("id")
        String id;

        public String getId() {
            return id;
        }
    }
    @SerializedName("items")
    CreatePaymentItem[] items;

    public CreatePaymentItem[] getItems() {
        return items;
    }
}
