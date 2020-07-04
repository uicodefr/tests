package test.uicode.mongoperf;

import java.util.List;

import test.uicode.mongoperf.entity.BaseEntity;

public class ResultDto {

    private Integer totalPages;

    private Long totalElements;

    private List<BaseEntity> data;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public List<BaseEntity> getData() {
        return data;
    }

    public void setData(List<BaseEntity> data) {
        this.data = data;
    }
}
