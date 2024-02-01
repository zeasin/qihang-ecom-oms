package com.qihang.erp.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qihang.erp.api.domain.Keyword;
import com.qihang.erp.api.req.KeyWordListReq;

import java.util.List;

public interface IKeywordService {
    void add(Keyword keyword);
    void delById(String id);
    void editById(Keyword keyword);
    Page<Keyword> getPageList(KeyWordListReq req);
}
