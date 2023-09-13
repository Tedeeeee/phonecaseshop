package com.project.phonecaseshop.responseApi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {
    private boolean success;
    private String status;
    private String msg;
}
