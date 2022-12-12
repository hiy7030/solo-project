package com.example.solo_project.dto;

import com.example.solo_project.response.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MultiResponseDto<T>{
    private List<T> data;
    private PageInfo pageInfo;
}
