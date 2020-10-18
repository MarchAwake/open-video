package cn.marchawake.server.enums;

public enum FileUseEnum {

    COURSE("C", "课程"),
    TEACHER("T", "讲师");

    private String code;

    private String desc;

    FileUseEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * <h2>将枚举代码号与所有枚举进行匹配</h2>
     * @param code
     * @return
     */
    public static FileUseEnum getByCode(String code){
        for(FileUseEnum e: FileUseEnum.values()){
            if(code.equals(e.getCode())){
                return e;
            }
        }
        return  null;
    }
}
