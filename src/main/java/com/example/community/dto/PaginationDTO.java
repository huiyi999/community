package com.example.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showFirstPage;
    private boolean showPrevious;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;  // current page
    private List<Integer> pages = new ArrayList<>();   // page list
    private Integer totalPage;
    // private Integer totalCount;

    public void setPagination(Integer totalCount, Integer page, Integer size) {

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        this.page = page;

        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        // show Previous
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // show Next
        if (page.equals(totalPage)) {
            showNext = false;
        } else {
            showNext = true;
        }

        // showFirstPage
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // showEndPage
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
