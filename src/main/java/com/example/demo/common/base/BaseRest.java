//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.demo.common.base;

import com.example.demo.common.base.CodeEnumInterface.CodeEnumEnum;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class BaseRest {
    @Autowired
    protected HttpServletRequest request;
    private static final String ERROR_MESSAGE = "操作失败";
    public static final String SUCCESS_MESSAGE = "操作成功";

    public BaseRest() {
    }

    public Result addSucResult() {
        return this.addResult(true, CodeEnumEnum.SUCCESS.getValue(), "操作成功", (Object)null);
    }

    public <T> Result addSucResult(T object) {
        if (!(object instanceof List) && !(object instanceof Set) && !(object instanceof LinkedList)) {
            return this.addResult(true, CodeEnumEnum.SUCCESS.getValue(), "操作成功", object);
        } else {
            Map<String, T> map = new HashMap();
            map.put("list", object);
            return this.addResult(true, CodeEnumEnum.SUCCESS.getValue(), "操作成功", map);
        }
    }

    public <T> Result addSucResult(String message, T object) {
        if (!(object instanceof List) && !(object instanceof Set) && !(object instanceof LinkedList)) {
            return this.addResult(true, CodeEnumEnum.SUCCESS.getValue(), message, object);
        } else {
            Map<String, T> map = new HashMap();
            map.put("list", object);
            return this.addResult(true, CodeEnumEnum.SUCCESS.getValue(), message, map);
        }
    }

    public <T> Result addSucResult(String code, String message, T object) {
        if (!(object instanceof List) && !(object instanceof Set) && !(object instanceof LinkedList)) {
            return this.addResult(true, code, message, object);
        } else {
            Map<String, T> map = new HashMap();
            map.put("list", object);
            return this.addResult(true, code, message, map);
        }
    }

    public Result addSucResult(String message) {
        return this.addResult(true, CodeEnumEnum.SUCCESS.getValue(), message, (Object)null);
    }

    public Result addErrResult() {
        return this.addResult(false, CodeEnumEnum.ERROR.getValue(), "操作失败", (Object)null);
    }

    public Result addErrResult(String errMsg) {
        return this.addResult(false, CodeEnumEnum.ERROR.getValue(), errMsg, (Object)null);
    }

    public Result addErrResult(String code, String errMsg) {
        return this.addResult(false, code, errMsg, (Object)null);
    }

    public <T> Result addResult(boolean result, String code, String message, T data) {
        Result<T> rs = new Result();
        rs.setResult(result);
        rs.setCode(code);
        rs.setMessage(message);
        rs.setData(data);
        return rs;
    }
}
