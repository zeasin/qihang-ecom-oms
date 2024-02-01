package com.qihang.erp.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihang.erp.api.domain.Keyword;
import com.qihang.erp.api.mapper.KeywordMapper;
import com.qihang.erp.api.req.KeyWordListReq;
import com.qihang.erp.api.service.IKeywordService;
import com.zhijian.common.core.page.PageDomain;
import com.zhijian.common.utils.StringUtils;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Log
@Service
public class KeywordServiceImpl extends ServiceImpl<KeywordMapper, Keyword> implements IKeywordService {
    @Override
    public void add(Keyword keyword) {
        if(StringUtils.isNotEmpty(keyword.getWord())) {
            String[] words = keyword.getWord().split(";");
            for (String w : words) {
                if(StringUtils.isNotEmpty(w)) {
                    boolean exists = this.baseMapper.exists(new LambdaQueryWrapper<Keyword>().eq(Keyword::getWord, w).eq(Keyword::getDate, keyword.getDate()).eq(Keyword::getSource, keyword.getSource()));
                    if(!exists) {
                        Keyword k = new Keyword();
//                        k.setCreateTime(System.currentTimeMillis() + "");
                        k.setCreateTime(new Date());
                        k.setDate(keyword.getDate());
                        k.setWord(w);
                        k.setSource(keyword.getSource());
                        k.setRemark(keyword.getRemark());
                        this.save(k);
                        log.info(w+" 添加成功！");
                    }else{
                        log.info(w+" 已存在！");
                    }
                }
            }

        }
    }

    @Override
    public void delById(String id) {
        this.baseMapper.deleteById(id);
    }

    @Override
    public void editById(Keyword keyword) {
        keyword.setCreateTime(null);
        this.baseMapper.updateById(keyword);
    }

    @Override
    public Page<Keyword> getPageList(KeyWordListReq req) {
        Page<Keyword> page = new Page<>(req.getCurrentPage(), req.getPageSize());
        Page<Keyword> keywordPage = this.baseMapper.selectPage(page,
                new LambdaQueryWrapper<Keyword>().orderByDesc(Keyword::getId)
                        .eq(StringUtils.isNotEmpty(req.getWord()),Keyword::getWord,req.getWord())
                        .eq(StringUtils.isNotEmpty(req.getSource()),Keyword::getSource,req.getSource())
                        .eq(StringUtils.isNotEmpty(req.getDate()),Keyword::getDate,req.getDate())

        );
        return keywordPage;
//        return this.list();
    }
}
