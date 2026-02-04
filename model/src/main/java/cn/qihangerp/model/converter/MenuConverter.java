//package cn.qihangerp.model.converter;
//
//import cn.qihangerp.model.entity.Menu;
//import cn.qihangerp.model.form.MenuForm;
//import cn.qihangerp.model.vo.MenuVO;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
///**
// * 菜单对象转换器
// *
// * @author Ray Hao
// * @since 2024/5/26
// */
//@Mapper(componentModel = "spring")
//public interface MenuConverter {
//
//    MenuVO toVo(Menu entity);
//
//    @Mapping(target = "params", ignore = true)
//    MenuForm toForm(Menu entity);
//
//    @Mapping(target = "params", ignore = true)
//    Menu toEntity(MenuForm menuForm);
//
//}