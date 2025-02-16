package com.tiger.job.common.dto;

import com.tiger.job.common.entity.Classify;
import com.tiger.job.common.entity.Organization;
import com.tiger.job.common.util.tree.TreeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 描述：任务分类DTO
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class ClassifyDto extends Classify implements TreeNode<Long> {
    private List<ClassifyDto> children;

    @Override
    public void setChildren(List<? extends TreeNode<?>> children) {
        this.children = (List<ClassifyDto>)children;
    }

    @Override
    public Object clone() {
        ClassifyDto organization;
        try {
            organization = (ClassifyDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassifyDto that = (ClassifyDto) o;
        return Objects.equals(id, that.id);
    }
}
