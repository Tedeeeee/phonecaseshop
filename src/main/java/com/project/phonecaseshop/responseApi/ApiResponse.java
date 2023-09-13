package com.project.phonecaseshop.responseApi;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ApiResponse {

    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setData(list);
        setSuccessResult(result);
        return result;
    }

    public <T> PageResult<T> getPageResult(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setData(page);
        setSuccessResult(result);
        return result;
    }

    public <T> SliceResult<T> getSliceResult(Slice<T> slice) {
        SliceResult<T> result = new SliceResult<>();
        result.setData(slice);
        setSuccessResult(result);
        return result;
    }

    public CommonResult getSuccessResult(int i) {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    public CommonResult getFailResult(String status, String cause) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setStatus(status);
        result.setMsg(cause);

        return result;
    }

    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setStatus(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(CommonResult result) {
        result.setSuccess(false);
        result.setStatus(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }
}
