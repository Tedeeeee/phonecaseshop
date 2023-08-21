package com.project.phonecaseshop.responseApi;

import lombok.AllArgsConstructor;
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

    public CommonResult getSuccessResult(int i) {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    public CommonResult getFailResult() {
        CommonResult result = new CommonResult();
        setFailResult(result);
        return result;
    }

    public CommonResult getFailResult(String code, String cause) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(cause);

        return result;
    }

    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(CommonResult result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }
}
