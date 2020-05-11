//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.demo.common.base;

public interface CodeEnumInterface {
    public static enum CodeEnumEnum implements CodeEnumInterface {
        SUCCESS("200", "操作成功."),
        LOGIN_WITHOUT("401", "未登录."),
        LOGIN_TIMEOUT("40101", "登录超时."),
        LOGIN_CONFLICT("40102", "登录冲突."),
        PERMISSION_WITHOUT("403", "无权限."),
        ERROR("500", "系统未知错误."),
        ERROR_INPUT_CHECK("500001", "参数验证错误."),
        ERROR_USER_INSERT("5001001", "用户新增异常"),
        ERROR_USER_UPDATE("5001002", "用户更新异常"),
        ERROR_USER_DELETE("5001003", "用户删除异常"),
        ERROR_USER_EXPECTED("5001004", "用户数据不符合预期"),
        ERROR_USER_SELECT("5001005", "用户数据未找到"),
        ERROR_ROLE_INSERT("5002001", "角色新增异常"),
        ERROR_ROLE_UPDATE("5002002", "角色更新异常"),
        ERROR_ROLE_DELETE("5002003", "角色删除异常"),
        ERROR_ROLE_EXPECTED("5002004", "角色数据不符合预期"),
        ERROR_ROLE_SELECT("5002005", "角色数据未找到"),
        ERROR_ORG_INSERT("5003001", "组织机构新增异常"),
        ERROR_ORG_UPDATE("5003002", "组织机构更新异常"),
        ERROR_ORG_DELETE("5003003", "组织机构删除异常"),
        ERROR_ORG_EXPECTED("5003004", "组织机构数据不符合预期"),
        ERROR_ORG_SELECT("5003005", "组织机构数据未找到"),
        ERROR_DIC_INSERT("5004001", "字典新增异常"),
        ERROR_DIC_UPDATE("5004002", "字典更新异常"),
        ERROR_DIC_DELETE("5004003", "字典删除异常"),
        ERROR_DIC_EXPECTED("5004004", "字典数据不符合预期"),
        ERROR_DIC_SELECT("5004005", "字典数据未找到");

        private final String value;
        private final String label;

        private CodeEnumEnum(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }
    }
}
