package cn.qihangerp.enums;

/**
 * 描述：发货类型
 *
 *
 * @author qlp
 * @date 2019-09-18 19:44
 */
public enum EnumUserType {
    SUPPER("总部管理员", 0),
    WAREHOUSE("仓库", 10),
    MERCHANT("商户", 20),
    VENDOR("供应商", 30),
    STORE("店铺", 40)
    ;
    private String name;
    private int index;

    // 构造方法
    private EnumUserType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (EnumUserType c : EnumUserType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
