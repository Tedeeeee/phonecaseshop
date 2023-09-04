package com.project.phonecaseshop.responseApi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
public class SliceResult<T> extends CommonResult{
    private Slice<T> data;
}
