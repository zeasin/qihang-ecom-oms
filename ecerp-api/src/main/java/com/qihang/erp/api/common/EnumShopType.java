package com.qihang.erp.api.common;

/**
 * 描述：
 * 店铺类型Enum
 *
 * @author qlp
 * @date 2019-09-18 19:44
 */
public enum EnumShopType {
    Ali("阿里1688", 1),
    YouZan("有赞", 2),
    TaoBao("淘宝", 3),
    Tmall("淘系", 4),
    Pdd("拼多多", 5),
    DouYin("抖音", 6),
    KuaiShou("快手", 7),
    ERP("ERP系统", 8),
    YunGou("华衣云购", 9),
    QiTa("其他", 10),
    ZhiBo("直播机构", 11),
    MoGuJie("蘑菇街", 12),
    Kwai("快手", 13),
    Sys("内部系统", 99);

    private String name;
    private int index;

    // 构造方法
    private EnumShopType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (EnumShopType c : EnumShopType.values()) {
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
