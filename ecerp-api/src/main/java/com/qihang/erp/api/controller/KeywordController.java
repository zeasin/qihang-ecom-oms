package com.qihang.erp.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qihang.erp.api.common.ApiResult;
import com.qihang.erp.api.domain.Keyword;
import com.qihang.erp.api.req.KeyWordListReq;
import com.qihang.erp.api.service.IKeywordService;
import com.zhijian.common.annotation.Log;
import com.zhijian.common.annotation.RepeatSubmit;
import com.zhijian.common.core.domain.AjaxResult;
import com.zhijian.common.core.domain.entity.SysDictData;
import com.zhijian.common.enums.BusinessType;
import com.zhijian.common.utils.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keyword")
public class KeywordController {
    private final IKeywordService keywordService;

    public KeywordController(IKeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @GetMapping("/list")
    public Page<Keyword> list(KeyWordListReq req){
        System.out.println(req);
        Page<Keyword> listAll = keywordService.getPageList(req);
        System.out.println(listAll);
        return listAll;
    }

    @Log(title="市场洞察-热搜词",businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/save")
    public ApiResult<String> save(@RequestBody Keyword keyword){
        System.out.println(keyword);
        if(keyword.getCreateTime() == null){
            keyword.setCreateTime(null);
        }
//        if(StringUtils.isNotEmpty(keyword.getDate()) && keyword.getDate().length()>10){
//            keyword.setDate(keyword.getDate().substring(0,10));
//        }
        keywordService.add(keyword);
        return ApiResult.ok();
    }

    @DeleteMapping("/del/{id}")
    public ApiResult<String> del(@PathVariable ("id") String id){
        System.out.println(id);
        keywordService.delById(id);
        return ApiResult.ok();
    }

    @PutMapping("/edit")
    public ApiResult<String> edit(@RequestBody Keyword keyword)
    {
//        if(StringUtils.isNotEmpty(keyword.getDate()) && keyword.getDate().length()>10){
//            keyword.setDate(keyword.getDate().substring(0,10));
//        }
        System.out.println(keyword);
        keywordService.editById(keyword);
        return ApiResult.ok();
    }
}
